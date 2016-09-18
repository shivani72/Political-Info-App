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
        listView=(ListView) findViewById(R.id.listView);
        String address=getIntent().getStringExtra(Intent.EXTRA_TEXT);
        FetchData d= new FetchData();
        c=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new ArrayList<String>());
        d.execute(address);
        listView.setAdapter(c);



    }


    public class FetchData extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchData.class.getSimpleName();

        String[] race(String djsonstr) throws JSONException {

            JSONObject j = new JSONObject(djsonstr);
            JSONArray r=j.getJSONArray("contests").getJSONObject(0).getJSONArray("candidates");
            // JSONArray candidates = r.getJSONArray(0)["contests"][0]["candidates"];
            String[] parsedCandidates = new String [r.length()];

            for (int i = 0; i < r.length(); i++) {
                JSONObject person = r.getJSONObject(i);
                parsedCandidates[i] = person.getString("name")+", "+person.getString("party");
                Log.d("fdksf",parsedCandidates[i]);
            }
       /* String [] r = new String[j["contests"][0].length()];

        Iterator<String> iter = j["contests"][0].keys();
        for (int i = 0; iter.hasNext();) {
            r[]
            iter.next();
        }*/

            return parsedCandidates;
        }


        @Override
        protected String[] doInBackground(String... address) {
            if(address.length==0);

            HttpURLConnection urlConnection=null;
            BufferedReader reader=null;
            String djsonstr=null;
            try{
                //        final String baseurl=c+address[0]+"&electionId=2000&returnAllAvailableData=true&fields=contests(candidates(candidateUrl%2Cname%2Cparty)%2Cdistrict%2Cid%2Clevel%2Coffice%2CprimaryParty)%2Celection%2CotherElections%2Cstate&key={YOUR_API_KEY}";
                final String u = "https://www.googleapis.com/civicinfo/v2/voterinfo?address="+address[0]+"&electionId=2000&officialOnly=false&returnAllAvailableData=true&fields=contests(candidates(name%2Cparty)%2Coffice)%2Cstate&key=AIzaSyDZ6k_vSaqtWu3KaFRLqmskprc5rlZBQnc";
                URL url = new URL(u);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

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

                djsonstr = buffer.toString();
                // Log.v(LOG_TAG, "response string: " + djsonstr);
                //Log.d("tag42",djsonstr);

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
