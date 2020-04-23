package com.example.Covid19App;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    class Covid19 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                //establish connection
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                //input data from url
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                //return the retrieved data as String
                int data = inputStreamReader.read();
                String content = "";
                char c;
                while (data != -1) {
                    c = (char) data;
                    //fetch the new data to the old data string
                    content = content + c;
                    //load another data
                    data = inputStreamReader.read();
                }
                return content;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Covid19 covid19 = new Covid19();
        String content;
        try {
            content = covid19.execute("https://api.covid19api.com/summary").get();

            Log.i("Content", content);
            //get data as JsonObject
            JSONObject jsonObject = new JSONObject(content);
            String global = jsonObject.getString("global");
            Log.i("GlobalData", global);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
