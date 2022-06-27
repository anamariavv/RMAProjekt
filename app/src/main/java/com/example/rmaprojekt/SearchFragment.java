package com.example.rmaprojekt;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchFragment extends Fragment {
    private static final int NUM_MATCHES = 10;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View profileFragmentView;
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
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.empty_divider));


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
                     //   Log.d("infois:", result.toString());
                        try {
                            if(!result.isNull("status") && result.getJSONObject("status").getString("message").equals("Data not found - summoner not found")) {
                               errorOrNull("Summoner doesn't exist");
                            } else {
                                fillSummonerHeader(result);

                                matches.clear();
                                for(int i = 0; i < NUM_MATCHES; i++) {
                                    SummonerMatch newMatch = createMatch(result.getJSONObject(String.valueOf(i)));
                                    matches.add(newMatch);
                                }
                                matchAdapter = new MatchAdapter(getContext(), matches);
                                matchRecyclerView.setAdapter(matchAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                       errorOrNull("An error occurred");
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

    private void errorOrNull(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void fillSummonerHeader(JSONObject summonerData) throws JSONException {
        ImageView summonerIconHolder = profileFragmentView.findViewById(R.id.summoner_search_icon);
        TextView summonerNameHolder = profileFragmentView.findViewById(R.id.summoner_search_name);
        TextView summonerLevelHolder = profileFragmentView.findViewById(R.id.summoner_search_level);
        TextView matchHistoryTitle = profileFragmentView.findViewById(R.id.summoner_search_match_history);
        RelativeLayout header = profileFragmentView.findViewById(R.id.summoner_search_header);

        String iconUrl = "http://ddragon.leagueoflegends.com/cdn/12.12.1/img/profileicon/"
                + summonerData.getString("profileIconId")
                + ".png";
        String summonerName = summonerData.getString("name");
        String summonerLevel = summonerData.getString("summonerLevel");

        Picasso.get().load(iconUrl).into(summonerIconHolder);
        summonerNameHolder.setText(summonerName);
        summonerLevelHolder.setText("Level: " + summonerLevel);

        matchHistoryTitle.setVisibility(View.VISIBLE);
        header.setBackgroundColor(getResources().getColor(R.color.primaryLightColor));
    }

    private SummonerMatch createMatch(JSONObject matchObject) throws JSONException {
        JSONObject summoner = matchObject.getJSONObject("info").getJSONArray("participants").getJSONObject(0);
        String victory = SummonerMatch.returnOutcome((boolean) summoner.get("win"));
        String assists = summoner.get("assists").toString();
        String deaths = summoner.get("deaths").toString();
        String kills = summoner.get("kills").toString();
        String kda = String.format("%s/%s/%s", kills, deaths, assists);
        String mode = matchObject.getJSONObject("info").get("gameMode").toString();
        String date = matchObject.getJSONObject("info").get("gameCreation").toString();
        String championUrl = SummonerMatch.createChampionUrl(summoner.getString("championName"));
        String item1Url = SummonerMatch.createItemUrl(summoner.getString("item0"));
        String item2Url = SummonerMatch.createItemUrl(summoner.getString("item1"));
        String item3Url = SummonerMatch.createItemUrl(summoner.getString("item2"));
        String item4Url = SummonerMatch.createItemUrl(summoner.getString("item3"));
        String item5Url = SummonerMatch.createItemUrl(summoner.getString("item4"));
        String item6Url = SummonerMatch.createItemUrl(summoner.getString("item5"));
        String item7Url = SummonerMatch.createItemUrl(summoner.getString("item6"));
        String spell1Url = SummonerMatch.createSpellUrl(summoner.getString("summoner1Id"));
        String spell2Url = SummonerMatch.createSpellUrl(summoner.getString("summoner2Id"));
        String rune1Url = SummonerMatch.createRuneUrl(summoner.getJSONObject("perks").getString("primary"));
        String rune2Url = SummonerMatch.createRuneUrl(summoner.getJSONObject("perks").getString("secondary"));

        boolean hasGameEndTimeStamp = matchObject.getJSONObject("info").isNull("gameEndTimestamp");
        String durationString = SummonerMatch.formatDuration((int)matchObject.getJSONObject("info").get("gameDuration"), hasGameEndTimeStamp);

        return new SummonerMatch(championUrl,rune1Url,rune2Url,spell1Url,spell2Url,
                item1Url,item2Url,item3Url,item4Url,item5Url, item6Url, item7Url, victory,kda, mode, date, durationString);
    }
}