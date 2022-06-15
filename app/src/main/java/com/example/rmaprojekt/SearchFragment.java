package com.example.rmaprojekt;

import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class SearchFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View profileFragmentView;
    private ScrollView summonerProfileScrollView;
    private String mParam1;
    private String mParam2;

    public SearchFragment() {}

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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileFragmentView = view;

        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction().add(R.id.search_fragment_container, SearchResultFragment.class, null)
                    .setReorderingAllowed(true)
                    .commit();
        }

        SearchView summonerSearch = view.findViewById(R.id.summoner_search);
        summonerSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("query", query);
                Map<String, String> requestInfo = new HashMap<>();
                requestInfo.put("name", query);
                requestInfo.put("server", getResources().getString(R.string.server_1));
                requestInfo.put("api", "summoner_match_history");

                JSONObject requestBody = SingletonRequestSender.createJsonRequest(requestInfo);

                SingletonRequestSender.sendRequest(requestBody, getResources().getString(R.string.request_url), new SingletonRequestSender.RequestResult() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Log.d("summoner:", result.toString());
                        createView(result);
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
        summonerProfileScrollView = profileFragmentView.findViewById(R.id.summoner_profile_scrollview);
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

        for(int i = 0; i < 10; i++) {
            RelativeLayout matchLayout = new RelativeLayout(getContext());
            matchLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView matchId = new TextView(getContext());

            RelativeLayout.LayoutParams matchIdParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            matchIdParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
            matchId.setLayoutParams(summonerNameParams);
            matchId.setId(View.generateViewId());

            try {
                matchId.setText(data.getString(String.valueOf(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            matchLayout.addView(matchId);
            scrollChild.addView(matchLayout);
        }


        try {
            summonerLevel.setText(data.getString("summonerLevel"));
            summonerName.setText(data.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        summonerProfileScrollView.addView(scrollChild);
    }
}