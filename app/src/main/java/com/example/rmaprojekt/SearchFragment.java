package com.example.rmaprojekt;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View profileFragmentView;
    private ScrollView summonerProfileScrollView;
    private String mParam1;
    private String mParam2;
    private RecyclerView matchRecyclerView;
    private MatchAdapter matchAdapter;
    private ArrayList<SummonerMatch> matches;
    public SearchFragment() {
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search, container, false);

        matchRecyclerView = view.findViewById(R.id.summoner_profile_recycler);
        matchRecyclerView.setHasFixedSize(true);
        matchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        matches = new ArrayList<>();
        matchRecyclerView.setAdapter( new MatchAdapter(getContext(), matches));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileFragmentView = view;

        SearchView summonerSearch = view.findViewById(R.id.summoner_search);
        summonerSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Map<String, String> requestInfo = new HashMap<>();
                requestInfo.put("name", query);
                requestInfo.put("server", getResources().getString(R.string.server_1));
                requestInfo.put("api", "summoner_match_history");

                JSONObject requestBody = SingletonRequestSender.createJsonRequest(requestInfo);

                SingletonRequestSender.sendRequest(requestBody, getResources().getString(R.string.request_url), new SingletonRequestSender.RequestResult() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        matches.clear();

                        try {
                            for(int i = 0; i < 10; i++) {
                                SummonerMatch newMatch = createMatch(result.getJSONObject(String.valueOf(i)));
                                matches.add(newMatch);
                            }
                            matchAdapter = new MatchAdapter(getContext(), matches);
                            matchRecyclerView.setAdapter(matchAdapter);
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
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private SummonerMatch createMatch(JSONObject matchObject) throws JSONException {
        JSONObject summoner = matchObject.getJSONObject("info").getJSONArray("participants").getJSONObject(0);
        Boolean victory = (Boolean) summoner.get("win");
        String assists = summoner.get("assists").toString();
        String deaths = summoner.get("deaths").toString();
        String kills = summoner.get("kills").toString();
        String kda = kills+"/"+deaths+"/"+assists;

        String mode = matchObject.getJSONObject("info").get("gameMode").toString();
        String date = matchObject.getJSONObject("info").get("gameCreation").toString();
        String duration = matchObject.getJSONObject("info").get("gameDuration").toString();

        return new SummonerMatch("a","a","a","a","a",
                "a","a","a","a","a", victory,kda, mode, date, duration);
    }
}