package com.example.Covid19App;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
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

    TextView counryNameSearch;
    Button searchButton;

    public void search(View view) {
        counryNameSearch = findViewById(R.id.counryNameSearch);
        searchButton = findViewById(R.id.searchButton);
        Covid19 covid19 = new Covid19();
        String content;
        try {
            content = covid19.execute("https://api.covid19api.com/summary").get();

            Log.i("Content", content);
            //get data as JsonObject
            JSONObject jsonObject = new JSONObject(content);
            String global = jsonObject.getString("Global");
            Log.i("GlobalData", global);
            //country data are in array
            String country = jsonObject.getString("Countries");
            JSONArray jsonArray = new JSONArray(country);

            String countryName = "";
            String newConfirmed = "";
            String totalConfirmed = "";
            String newDeaths = "";
            String totalDeaths = "";
            String newRecovered = "";
            String totalRecovered = "";
            String date = "";

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject part = jsonArray.getJSONObject(i);
                countryName = part.getString("Country");
                newConfirmed = part.getString("NewConfirmed");
                totalConfirmed = part.getString("TotalConfirmed");
                newDeaths = part.getString("NewDeaths");
                totalDeaths = part.getString("TotalDeaths");
                newRecovered = part.getString("NewRecovered");
                totalRecovered = part.getString("TotalRecovered");
                date = part.getString("Date");
            }

            Log.i("CountryName", countryName);
            Log.i("newConfirmedData", newConfirmed);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
