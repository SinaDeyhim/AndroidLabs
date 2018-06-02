package com.example.sdeyh.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast  extends Activity {
    protected static final String ACTIVITY_NAME = "WeatherActivity";
    private TextView currentTextView,lowTextView,highTextView;
    private ImageView weatherImageView;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.getProgressDrawable().setColorFilter(
                Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);

        currentTextView = (TextView) findViewById(R.id.currentT);
        lowTextView = (TextView) findViewById(R.id.minT);
        highTextView = (TextView) findViewById(R.id.maxT);
        weatherImageView = (ImageView) findViewById(R.id.imageWeather);

        new ForecastQuery().execute(null, null, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "onResume()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "onStart()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "onDestroy()");
    }

    public class ForecastQuery extends AsyncTask<String, Integer, String> {
        private String minT,maxT,currentT, iconName;
        private Bitmap weatherPicture;


        protected String doInBackground(String... arg) {
            InputStream stream = null;

            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                stream = new BufferedInputStream(conn.getInputStream());
                XmlPullParser xml = Xml.newPullParser();
                xml.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xml.setInput(stream, null);

                int parserEvent = xml.getEventType();
                boolean set = false;

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    if (parserEvent == XmlPullParser.START_TAG) {
                        if (xml.getName().equalsIgnoreCase("current")) {
                            set = true;
                        } else if (xml.getName().equalsIgnoreCase("temperature") && set) {
                            currentT = xml.getAttributeValue(null, "value");
                            publishProgress(25);
                            minT = xml.getAttributeValue(null, "min");
                            publishProgress(50);
                            maxT = xml.getAttributeValue(null, "max");
                            publishProgress(75);
                        }  else if (xml.getName().equalsIgnoreCase("weather") && set) {
                            iconName = xml.getAttributeValue(null, "icon") + ".png";
                            File file = getBaseContext().getFileStreamPath(iconName);
                            if (!file.exists()) {
                                weatherPicture  = HttpUtils.getImage("http://openweathermap.org/img/w/" + iconName);
                                FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                                weatherPicture.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();

                            } else {
                                Log.i(ACTIVITY_NAME, "Saved icon, " + iconName + " is displayed.");
                                try {
                                    FileInputStream fis = new FileInputStream(file);
                                    weatherPicture = BitmapFactory.decodeStream(fis);
                                } catch (FileNotFoundException e) {
                                    Log.i(ACTIVITY_NAME, "Saved icon, " + iconName + " not found.");
                                }
                            }
                            publishProgress(100);

                        }
                    } else if (parserEvent == XmlPullParser.END_TAG) {
                        if (xml.getName().equalsIgnoreCase("current"))
                            set = false;
                    }
                    parserEvent = xml.next();
                }

            } catch (IOException e) {
                Log.i(ACTIVITY_NAME, "IOException: " + e.getMessage());
            } catch (XmlPullParserException e) {
                Log.i(ACTIVITY_NAME, "XmlPullParserException: " + e.getMessage());
            } finally {
                if (stream != null)
                    try {
                        stream.close();
                    } catch (IOException e) {
                        Log.i(ACTIVITY_NAME, "IOException: " + e.getMessage());
                    }
                return null;
            }
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            currentTextView.setText("Current: "+currentT+" \u2103");
            lowTextView.setText("Low: "+minT + " \u2103");
            highTextView.setText("High: "+maxT + " \u2103");
            weatherImageView.setImageBitmap(weatherPicture);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
}
