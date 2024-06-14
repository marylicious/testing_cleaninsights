package com.example.myapplication3

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import org.cleaninsights.sdk.Campaign
import org.cleaninsights.sdk.CleanInsights
import org.cleaninsights.sdk.Configuration
import org.cleaninsights.sdk.Feature
import java.net.URL
import java.util.Calendar


class MainActivity : ComponentActivity() {

    private var ci: CleanInsights? = null

    private fun initMeasurement()  {
        try {
            // with file
            val inputStream = this.resources.openRawResource(R.raw.cleaninsights);
            val configString = String(inputStream.readBytes()) // Read bytes and convert to String
            ci = CleanInsights(configString, filesDir);

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun makeMeasurement() { //RFC336 UTC
        //send measure
        try {
            ci?.measureVisit(listOf("Prueba coop"), "test")
            ci?.measureEvent("prueba-coop", "sdlasjd", "test", "hello 2", 1.0)

        } catch (e: Exception) {
            println("Exception"+ e);
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        /////// A SIMPLE TEXTVIEW ///////////////////
        val textView = TextView(this)
        textView.text = "hi, this is a test app for clean insights"
        val layout = LinearLayout(this)
        layout.addView(textView)
        setContentView(layout)

        //////// CLEAN INSIGHTS STUFF //////////////

        // initialization
        initMeasurement();

        // test server connection
        ci?.testServer {
            if (it != null) {
                Log.e("Clean Insights Server Test", "An exception was raised")
                it.printStackTrace()
            } else {
                Log.i("Clean Insights Server Test", "No exception - works!")
            }
        }

        // request consent
        val ui = ConsentUI(this);

        //val check0 = ci?.consent("test")?.granted


        ci?.requestConsent("test", ui) { granted ->
            if (!granted) return@requestConsent
            makeMeasurement();
        }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        ci?.persist()

    }

    override fun onPause() {
        super.onPause()
        ci?.persist()
    }

    override fun onDestroy() {
        super.onDestroy()
        ci?.persist()
    }


}

