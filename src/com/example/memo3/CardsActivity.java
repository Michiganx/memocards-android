package com.example.memo3;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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
 * Created by Eric on 31.05.2015.
 */
public class CardsActivity extends Activity {

    ArrayList<Pair<String,String>> cards_arr_list = new ArrayList<Pair<String,String>>();
    Integer current_card_inex =0, total_cards = 0;


    String cur_deck_id =  "" ;

    String url = "https://warm-castle-7655.herokuapp.com/decks/" + cur_deck_id;

    public void onCardClick(View view)
    {
        flipCard();
    }

    private void flipCard()
    {
        View rootLayout = (View) findViewById(R.id.main_activity_root);
        View cardFace = (View) findViewById(R.id.main_activity_card_face);
        View cardBack = (View) findViewById(R.id.main_activity_card_back);

        FlipAnimation flipAnimation = new FlipAnimation(cardFace, cardBack);

        if (cardFace.getVisibility() == View.GONE)
        {
            flipAnimation.reverse();
        }
        rootLayout.startAnimation(flipAnimation);
    }



    private class GetCardsTask extends AsyncTask<String, Void, String> {
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
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cards);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        cur_deck_id = extras  != null ? extras.getString("CURRENT_DECK_ID") : "" ;
        url+= cur_deck_id;

        String json_deck = null;
        try {
            json_deck = (new GetCardsTask()).execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            JSONObject deck = new JSONObject(json_deck);
            JSONArray cards_arr = (JSONArray) deck.get("cards");
             total_cards = cards_arr.length();
            for (int i = 0; i < total_cards; i++) {
                JSONObject card_object = new JSONObject(cards_arr.get(i).toString());
                cards_arr_list.add(new Pair<String, String>(
                        card_object.getString("sidea"),
                        card_object.getString("sideb")
                        ));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView cardFaceText = (TextView) findViewById(R.id.textView3);
        TextView cardBackText = (TextView) findViewById(R.id.textView4);


        if(total_cards > 0){
            cardFaceText.setText(cards_arr_list.get(0).getLeft());
            cardBackText.setText(cards_arr_list.get(0).getRight());
        }
    }

    public void onRightClick(View view){
        if (current_card_inex< total_cards - 1 && total_cards>1){
            current_card_inex++;
        }

        View cardFace = (View) findViewById(R.id.main_activity_card_face);
        if (cardFace.getVisibility() == View.GONE)
        {

            flipCard();
        }

        TextView cardFaceText = (TextView) findViewById(R.id.textView3);
        TextView cardBackText = (TextView) findViewById(R.id.textView4);
        cardFaceText.setText(cards_arr_list.get(current_card_inex).getLeft());
        cardBackText.setText(cards_arr_list.get(current_card_inex).getRight());


    }

    public void onLeftClick(View view){
        if (current_card_inex > 0 && total_cards>1){
            current_card_inex--;
        }

        View cardFace = (View) findViewById(R.id.main_activity_card_face);
        if (cardFace.getVisibility() == View.GONE)
        {

            flipCard();
        }

        TextView cardFaceText = (TextView) findViewById(R.id.textView3);
        TextView cardBackText = (TextView) findViewById(R.id.textView4);
        cardFaceText.setText(cards_arr_list.get(current_card_inex).getLeft());
        cardBackText.setText(cards_arr_list.get(current_card_inex).getRight());
    }
}
