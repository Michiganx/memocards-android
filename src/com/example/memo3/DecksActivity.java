package com.example.memo3;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Eric on 30.05.2015.
 */
public class DecksActivity extends ListActivity {
    private ListView dlv = null;

    String url = "https://warm-castle-7655.herokuapp.com/decks";
    //    String url = "http://127.0.0.1:3000/decks";
    String result = "";


    private class GetDecksTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            URL obj = null;
            try {
                obj = new URL(urls[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                //add request header
                con.setRequestProperty("Accept", "application/json");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

//        protected void onProgressUpdate(Integer... progress) {
//            setProgressPercent(progress[0]);
//        }
//
//        protected void onPostExecute(Long result) {
//            showDialog("Downloaded " + result + " bytes");
//        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decks);


        String json_decks = null;
        try {
            json_decks = (new GetDecksTask()).execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ArrayList<Deck> decks_array = new ArrayList<Deck>();

        try {
            JSONArray jarr = new JSONArray(json_decks);
            int len = jarr.length();
            for (int i = 0; i < len; i++) {
                JSONObject deck_object = new JSONObject(jarr.get(i).toString());
                decks_array.add(new Deck(Integer.parseInt(  deck_object.get("id").toString()),
                                                            deck_object.get("name").toString(),
                                                            deck_object.get("description").toString()));
            }

            dlv =  getListView();

            ArrayAdapter<Deck> ad = new ArrayAdapter<Deck>(this, android.R.layout.simple_list_item_1, decks_array);
            dlv.setAdapter(ad);

            dlv.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
                {
                    Deck d = (Deck)adapter.getItemAtPosition(position);
                    // assuming string and if you want to get the value on click of list item
                    // do what you intend to do on click of listview row
                    Intent i = new Intent(v.getContext(), CardsActivity.class);
                    i.putExtra("CURRENT_DECK_ID", d.get_id().toString());
                    startActivity(i);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
