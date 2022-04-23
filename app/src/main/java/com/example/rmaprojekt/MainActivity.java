package com.example.rmaprojekt;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static class summonerComparator implements Comparator<JSONObject> {

       @Override
        public int compare(JSONObject summoner1, JSONObject summoner2) {
            try {
                if(summoner1.getInt("leaguePoints") > summoner2.getInt("leaguePoints")) {
                    return 1;
                } else if (summoner1.getInt("leaguePoints") < summoner2.getInt("leaguePoints")) {
                    return -1;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        final float TEXT_SIZE = 20;
        Context context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScrollView scrollview = findViewById(R.id.home_scroll);
        LinearLayout scrollChild = new LinearLayout(this);

        scrollChild.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        scrollChild.setOrientation(LinearLayout.VERTICAL);

        Map<String, String> params = new HashMap<>();
        params.put("queue", getResources().getString(R.string.queue_type_1));
        params.put("server", getResources().getString(R.string.server_1));
        params.put("api", getResources().getString(R.string.api_1));

        JSONObject challengerRequest = SingletonRequestSender.createJsonRequest(params);

        SingletonRequestSender.sendRequest(challengerRequest, getResources().getString(R.string.request_url)
                ,new SingletonRequestSender.RequestResult() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(JSONObject result) {
                Log.d("Done", "success");

                JSONArray entries;
                ArrayList<JSONObject> summoners = new ArrayList<>();

                try {
                    entries = result.getJSONArray("entries");

                    for(int i = 0; i < entries.length(); i++) {
                        summoners.add(entries.getJSONObject(i));
                    }

                    Collections.sort(summoners, new summonerComparator().reversed());

                    int position = 1;
                    for(JSONObject obj:summoners) {
                        RelativeLayout summoner = new RelativeLayout(context);
                        TextView rank = new TextView(context);
                        TextView summonerName = new TextView(context);
                        TextView points = new TextView(context);

                        summoner.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150));

                        RelativeLayout.LayoutParams rankParams = new RelativeLayout.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT);
                        rankParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                        rank.setLayoutParams(rankParams);
                        rank.setTextSize(TEXT_SIZE);
                        rank.setId(View.generateViewId());
                        rank.setTextColor(getResources().getColor(R.color.red));
                        rank.setText(String.valueOf(position));
                        rank.setPadding(20,0,0,0);

                        RelativeLayout.LayoutParams summonerNameParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        summonerNameParams.addRule(RelativeLayout.END_OF, rank.getId());
                        summonerNameParams.addRule(RelativeLayout.START_OF, points.getId());
                        summonerName.setLayoutParams(summonerNameParams);
                        summonerName.setTextSize(TEXT_SIZE);
                        summonerName.setText(obj.get("summonerName").toString());
                        summonerName.setId(View.generateViewId());

                        RelativeLayout.LayoutParams pointsParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        pointsParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
                        points.setLayoutParams(pointsParams);
                        points.setTextSize(TEXT_SIZE);
                        points.setText(obj.get("leaguePoints").toString());
                        points.setId(View.generateViewId());
                        points.setPadding(0,0,50,0);

                        summoner.addView(rank);
                        summoner.addView(summonerName);
                        summoner.addView(points);
                        scrollChild.addView(summoner);

                        position++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError error) {
                //TODO display error
                Log.d("Done", error.getMessage());
            }
        });
        scrollview.addView(scrollChild);
    }
}