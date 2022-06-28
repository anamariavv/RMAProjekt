package com.example.rmaprojekt;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class HomepageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private RecyclerView rankRecyclerView;
    private RankAdapter rankAdapter;
    private ArrayList<LeaderboardRank> rankings;

    public HomepageFragment() {
    }

    public static HomepageFragment newInstance(String param1, String param2) {
        HomepageFragment fragment = new HomepageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static class summonerComparator implements Comparator<JSONObject> {

        @Override
        public int compare(JSONObject summoner1, JSONObject summoner2) {
            try {
                if (summoner1.getInt("leaguePoints") > summoner2.getInt("leaguePoints")) {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        rankRecyclerView = view.findViewById(R.id.homepage_recycler_view);
        rankRecyclerView.setHasFixedSize(true);
        rankRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        rankings = new ArrayList<>();
        rankRecyclerView.setAdapter(new RankAdapter(getContext(), rankings));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Map<String, String> params = new HashMap<>();
        params.put("queue", getResources().getString(R.string.queue_type_1));
        params.put("server", getResources().getString(R.string.server_1));
        params.put("api", getResources().getString(R.string.api_1));

        JSONObject challengerRequest = SingletonRequestSender.createJsonRequest(params);

        SingletonRequestSender.sendRequest(challengerRequest, getResources().getString(R.string.request_url)
                , new SingletonRequestSender.RequestResult() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(JSONObject result) {
                        JSONArray entries;
                        ArrayList<JSONObject> summonerObjects = new ArrayList<>();

                        try {
                            entries = result.getJSONArray("entries");
                            for (int i = 0; i < entries.length(); i++) {
                                summonerObjects.add(entries.getJSONObject(i));
                            }
                            Collections.sort(summonerObjects, new summonerComparator().reversed());

                            int position = 1;
                            for (JSONObject summonerObject : summonerObjects) {
                                LeaderboardRank rank = createRank(summonerObject, position);
                                rankings.add(rank);

                                position++;
                            }

                            rankAdapter = new RankAdapter(getContext(), rankings);
                            rankRecyclerView.setAdapter(rankAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Toast toast = Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
    }

    private LeaderboardRank createRank(JSONObject summonerObject, int position) throws JSONException {
        String number = String.valueOf(position);
        String summoner = summonerObject.getString("summonerName");
        String rank = summonerObject.getString("rank");
        String wins = summonerObject.getString("wins");
        String losses = summonerObject.getString("losses");
        String lp = summonerObject.getString("leaguePoints");
        boolean hotstreak = (boolean) summonerObject.get("hotStreak");

        return new LeaderboardRank(number, summoner, rank, wins, losses, lp, hotstreak);
    }

}