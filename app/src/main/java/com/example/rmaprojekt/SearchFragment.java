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
                        Log.d("summoner:", result.toString());
                      //  createView(result);
                        try {
                            for(int i = 0; i < 10; i++) {
                                JSONObject matchObject = result.getJSONObject(String.valueOf(i));
                                 SummonerMatch match = new SummonerMatch(1,1,1,1,1,1,1,
                                         1,1,1, true, "0/0/0", "Normal", "1.6.2022.","25 min");
                                 matches.add(match);
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

    private void createView(JSONObject data) {
      /*  summonerProfileScrollView = profileFragmentView.findViewById(R.id.summoner_profile_scrollview);
        summonerProfileScrollView.removeAllViews();

        LinearLayout scrollChild = new LinearLayout(getContext());
        scrollChild.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        scrollChild.setOrientation(LinearLayout.VERTICAL);
        scrollChild.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

        RelativeLayout headerLayout = new RelativeLayout(getContext());
        TextView summonerName = new TextView(getContext());
        TextView summonerLevel = new TextView(getContext());
        ImageView summonerIcon = new ImageView(getContext());

        headerLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        RelativeLayout.LayoutParams summonerIconParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        summonerIconParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        summonerIcon.setLayoutParams(summonerIconParams);
        summonerIcon.setId(View.generateViewId());
        summonerIcon.setImageResource(R.drawable.ic_profile);

        RelativeLayout.LayoutParams summonerNameParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        summonerNameParams.addRule(RelativeLayout.BELOW, summonerIcon.getId());
        summonerName.setLayoutParams(summonerNameParams);
        summonerName.setId(View.generateViewId());

        RelativeLayout.LayoutParams summonerLevelParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        summonerLevelParams.addRule(RelativeLayout.BELOW, summonerName.getId());
        summonerLevel.setLayoutParams(summonerLevelParams);
        summonerLevel.setId(View.generateViewId());

        headerLayout.addView(summonerIcon);
        headerLayout.addView(summonerName);
        headerLayout.addView(summonerLevel);
        scrollChild.addView(headerLayout);

        //for (int i = 0; i < 10; i++) {
            RelativeLayout matchLayout = new RelativeLayout(getContext());
            matchLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));


            ImageView champPicture = new ImageView(getContext());
            ImageView rune1 = new ImageView(getContext());
            ImageView rune2 = new ImageView(getContext());
            ImageView spell1 = new ImageView(getContext());
            ImageView spell2 = new ImageView(getContext());
            ImageView item1 = new ImageView(getContext());
            ImageView item2 = new ImageView(getContext());
            ImageView item3 = new ImageView(getContext());
            ImageView item4 = new ImageView(getContext());
            ImageView item5 = new ImageView(getContext());
            TextView outcome = new TextView(getContext());
            TextView kda = new TextView(getContext());
            TextView mode = new TextView(getContext());
            TextView duration = new TextView(getContext());

            RelativeLayout.LayoutParams champParams = new RelativeLayout.LayoutParams(150, 150);
            champParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
            champPicture.setLayoutParams(champParams);
            champPicture.setId(View.generateViewId());
            champPicture.setImageResource(R.drawable.ic_loading);

            RelativeLayout.LayoutParams rune1Params = new RelativeLayout.LayoutParams(75, 75);
            rune1Params.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
            rune1Params.addRule(RelativeLayout.BELOW, champPicture.getId());
            rune1.setLayoutParams(rune1Params);
            rune1.setId(View.generateViewId());
            rune1.setImageResource(R.drawable.ic_loading);

            RelativeLayout.LayoutParams rune2Params = new RelativeLayout.LayoutParams(75,75);
            rune2Params.addRule(RelativeLayout.END_OF, rune1.getId());
            rune2Params.addRule(RelativeLayout.BELOW, champPicture.getId());
            rune2.setLayoutParams(rune2Params);
            rune2.setId(View.generateViewId());
            rune2.setImageResource(R.drawable.ic_loading);

            RelativeLayout.LayoutParams outcomeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            outcomeParams.addRule(RelativeLayout.END_OF, champPicture.getId());
            outcome.setLayoutParams(outcomeParams);
            outcome.setId(View.generateViewId());
            outcome.setText("VICTORY");

            RelativeLayout.LayoutParams outcomeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            outcomeParams.addRule(RelativeLayout.END_OF, champPicture.getId());
            outcome.setLayoutParams(outcomeParams);
            outcome.setId(View.generateViewId());
            outcome.setText("VICTORY");

            matchLayout.addView(champPicture);
            matchLayout.addView(rune1);
            matchLayout.addView(rune2);
            matchLayout.addView(spell1);
            matchLayout.addView(spell2);
            matchLayout.addView(item1);
            matchLayout.addView(item2);
            matchLayout.addView(item3);
            matchLayout.addView(item4);
            matchLayout.addView(item5);
            matchLayout.addView(outcome);
            matchLayout.addView(kda);
            matchLayout.addView(mode);
            matchLayout.addView(duration);

            scrollChild.addView(matchLayout);
       // }

        try {
            summonerLevel.setText(data.getString("summonerLevel"));
            summonerName.setText(data.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        summonerProfileScrollView.addView(scrollChild);*/
    }
}