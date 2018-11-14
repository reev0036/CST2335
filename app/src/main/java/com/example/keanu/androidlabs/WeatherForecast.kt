package com.example.keanu.androidlabs
import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import org.w3c.dom.Text
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.HttpURLConnection
import java.net.URL
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.net.MalformedURLException

class WeatherForecast : Activity() {

    var windValue: String = ""
    var tempValue: String = ""
    var tempMax: String = ""
    var tempMin: String = ""
    var weatherIcon: String = ""

    lateinit var minTemp: TextView
    lateinit var maxTemp: TextView
    lateinit var currentTemp: TextView
    lateinit var windSpeed: TextView
    lateinit var currentWeather: ImageView
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_forecast)

        currentWeather = findViewById(R.id.currentWeather)
        currentTemp = findViewById(R.id.currentTemp)
        minTemp = findViewById(R.id.minTemp)
        maxTemp = findViewById(R.id.maxTemp)
        windSpeed = findViewById(R.id.windSpeed)
        progressBar = findViewById(R.id.progressBar)

        progressBar.visibility = View.VISIBLE

        var myQuery = forecastQuery()
        myQuery.execute()

    }

    inner class forecastQuery : AsyncTask<String, Integer, String>(){

        var progress = 0
        var weatherImage : Bitmap? = null

        override fun doInBackground(vararg params: String?): String {

            try{
                val url = URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric")
                val urlConnection = url.openConnection() as HttpURLConnection
                val stream = urlConnection.getInputStream()
                val factory = XmlPullParserFactory.newInstance()
                factory.setNamespaceAware(false)
                val xpp = factory.newPullParser()
                xpp.setInput(stream, "UTF-8")

                while (xpp.eventType != XmlPullParser.END_DOCUMENT) {
                    when(xpp.eventType){
                        XmlPullParser.START_TAG -> {
                            if(xpp.name.equals("speed")) {
                                windValue = xpp.getAttributeValue(null, "value")
                                progress += 20
                            }

                            if(xpp.name.equals("temperature")) {
                                tempValue = xpp.getAttributeValue(null, "value")
                                progress += 20
                                tempMin = xpp.getAttributeValue(null, "min")
                                progress += 20
                                tempMax = xpp.getAttributeValue(null, "max")
                                progress += 20
                            }

                            if(xpp.name.equals("weather")) {
                                weatherIcon = xpp.getAttributeValue(null, "icon")
                                var weatherURL = "http://openweathermap.org/img/w/$weatherIcon.png"

                                if (fileExistance("$weatherIcon.png")){
                                    var fis: FileInputStream? = null
                                    try {    fis = openFileInput("$weatherIcon.png")   }
                                    catch (e: FileNotFoundException) {    e.printStackTrace()  }
                                    weatherImage = BitmapFactory.decodeStream(fis)
                                }else{
                                    weatherImage = getImage(weatherURL)
                                    val outputStream = openFileOutput( "$weatherIcon.png", Context.MODE_PRIVATE);
                                    weatherImage?.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    outputStream.flush();
                                    outputStream.close();
                                }

                                progress += 20
                            }
                        }
                        XmlPullParser.TEXT -> { }

                    }

                    publishProgress()
                    xpp.next()
                }
            }
            catch(e : Exception){
            }

            return "Done"
        }

        override fun onProgressUpdate(vararg values: Integer?) {
            progressBar.setProgress(progress)
            currentTemp.setText(tempValue)
            minTemp.setText(tempMin)
            maxTemp.setText(tempMax)
            windSpeed.setText(windValue)

        }

        override fun onPostExecute(result: String?) {
           currentWeather.setImageBitmap(weatherImage)
            progressBar.visibility = View.INVISIBLE
        }

        fun getImage(url: URL): Bitmap? {
            var connection: HttpURLConnection? = null
            try {
                connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val responseCode = connection.responseCode
                return if (responseCode == 200) {
                    BitmapFactory.decodeStream(connection.inputStream)
                } else
                    null
            } catch (e: Exception) {
                return null
            } finally {
                connection?.disconnect()
            }
        }

        fun getImage(urlString: String): Bitmap? {
            try {
                val url = URL(urlString)
                return getImage(url)
            } catch (e: MalformedURLException) {
                return null
            }

        }

        fun fileExistance(fname : String):Boolean{
            val file = getBaseContext().getFileStreamPath(fname)
            return file.exists()   }
    }
}
