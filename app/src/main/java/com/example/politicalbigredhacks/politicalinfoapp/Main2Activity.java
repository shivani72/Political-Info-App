package com.example.politicalbigredhacks.politicalinfoapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
ListView listView;
    ArrayAdapter<String> c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.listView);
        String address = getIntent().getStringExtra(Intent.EXTRA_TEXT); //stores the input of the user
        FetchData d = new FetchData();
        c = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                new ArrayList<String>()); //displays the string array that the async task returns

        d.execute(address); //calls the async task, which is responsible for
        listView.setAdapter(c);

    }


    public class FetchData extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchData.class.getSimpleName();

        //race function parses the data that the API call returns, and returns a string array of "name" and "party", 2 relevant items
        //to the app
        String[] race(String djsonstr) throws JSONException {

            //retrieves the array of the list of candidates in the first race
            JSONObject j = new JSONObject(djsonstr);
            JSONArray r = j.getJSONArray("contests").getJSONObject(0).getJSONArray("candidates");

            //the list of candidates within that zipcode/state
            String[] parsedCandidates = new String [r.length()];

            //pulls out the name and party
            for (int i = 0; i < r.length(); i++) {
                JSONObject person = r.getJSONObject(i);
                parsedCandidates[i] = person.getString("name")+", "+person.getString("party");
                Log.d("fdksf",parsedCandidates[i]);
            }

            //list of candidates returned on the second screen of the app
            return parsedCandidates;
        }


        @Override
        protected String[] doInBackground(String... address) {
            if (address.length == 0);

            HttpURLConnection urlConnection=null;
            BufferedReader reader=null;
            String djsonstr=null;

            //URI - function call to the API
            try {

                final String u = "https://www.googleapis.com/civicinfo/v2/voterinfo?address="+address[0]+"&electionId=2000&officialOnly=false&returnAllAvailableData=true&fields=contests(candidates(name%2Cparty)%2Coffice)%2Cstate&key=AIzaSyDZ6k_vSaqtWu3KaFRLqmskprc5rlZBQnc";
                URL url = new URL(u);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //processing the input stream and transforming it into a string
                InputStream inputstream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputstream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputstream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                //holds the json object in the form a string
                djsonstr = buffer.toString();

            }

            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return race(djsonstr);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }




        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            for (String l:strings){
                c.add(l);

            }


        }
    }

}
