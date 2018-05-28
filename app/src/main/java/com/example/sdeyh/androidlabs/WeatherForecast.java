package com.example.sdeyh.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
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
    private TextView wind;
    private TextView currentTextView;
    private TextView lowTextView;
    private TextView highTextView;
    //    private TextView targetLocation;
    private ImageView weatherImageView;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);

        weatherImageView = (ImageView) findViewById(R.id.imageWeather);


        currentTextView = (TextView) findViewById(R.id.currentT);

        lowTextView = (TextView) findViewById(R.id.minT);
        highTextView = (TextView) findViewById(R.id.maxT);

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
        private String windSpeed;
        private String minTemperature;
        private String maxTemperature;
        private String currentTemperature;
        private Bitmap weatherPicture;
        private String weatherIconName;

        protected String doInBackground(String... arg) {
            InputStream in = null;

            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                in = new BufferedInputStream(conn.getInputStream());
                XmlPullParser xpp = Xml.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(in, null);

                int parserEvent = xpp.getEventType();
                boolean set = false;

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    if (parserEvent == XmlPullParser.START_TAG) {
                        if (xpp.getName().equalsIgnoreCase("current")) {
                            set = true;
                        } else if (xpp.getName().equalsIgnoreCase("temperature") && set) {
                            currentTemperature = xpp.getAttributeValue(null, "value");
                            publishProgress(25);
                            minTemperature = xpp.getAttributeValue(null, "min");
                            publishProgress(50);
                            maxTemperature = xpp.getAttributeValue(null, "max");
                            publishProgress(75);
                        } else if (xpp.getName().equalsIgnoreCase("speed") && set) {
                            windSpeed = xpp.getAttributeValue(null, "value");
                            publishProgress(100);

                        } else if (xpp.getName().equalsIgnoreCase("weather") && set) {
                            weatherIconName = xpp.getAttributeValue(null, "icon") + ".png";
                            File file = getBaseContext().getFileStreamPath(weatherIconName);
                            if (!file.exists()) {
                                saveImage(weatherIconName);
                            } else {
                                Log.i(ACTIVITY_NAME, "Saved icon, " + weatherIconName + " is displayed.");
                                try {
                                    FileInputStream inF = new FileInputStream(file);
                                    weatherPicture = BitmapFactory.decodeStream(in);
                                } catch (FileNotFoundException e) {
                                    Log.i(ACTIVITY_NAME, "Saved icon, " + weatherIconName + " not found.");
                                }
                            }
                            publishProgress(100);

                        }
                    } else if (parserEvent == XmlPullParser.END_TAG) {
                        if (xpp.getName().equalsIgnoreCase("current"))
                            set = false;
                    }
                    parserEvent = xpp.next();
                }

            } catch (IOException e) {
                Log.i(ACTIVITY_NAME, "IOException: " + e.getMessage());
            } catch (XmlPullParserException e) {
                Log.i(ACTIVITY_NAME, "XmlPullParserException: " + e.getMessage());
            } finally {
                if (in != null)
                    try {
                        in.close();
                    } catch (IOException e) {
                        Log.i(ACTIVITY_NAME, "IOException: " + e.getMessage());
                    }
                return null;
            }
        }

        private void saveImage(String fname) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL("http://openweathermap.org/img/w/" + fname);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    weatherPicture = BitmapFactory.decodeStream(connection.getInputStream());
                    FileOutputStream outputStream = openFileOutput(fname, Context.MODE_PRIVATE);
                    weatherPicture.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Log.i(ACTIVITY_NAME, "Weather icon, " + fname + " is downloaded and displayed.");
                } else
                    Log.i(ACTIVITY_NAME, "Can't connect to the weather icon for downloading.");
            } catch (Exception e) {
                Log.i(ACTIVITY_NAME, "weather icon download error: " + e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
            if (values[0] == 100) {

            }
        }

        @Override
        protected void onPostExecute(String result) {

            currentTextView.setText("Current : "+currentTemperature+" \u2103");
            lowTextView.setText("Lowe : "+minTemperature + " \u2103");
            highTextView.setText("High : "+maxTemperature + " \u2103");
            weatherImageView.setImageBitmap(weatherPicture);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
