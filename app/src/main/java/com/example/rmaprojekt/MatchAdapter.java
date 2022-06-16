package com.example.rmaprojekt;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {
    private Context matchContext;
    private ArrayList<SummonerMatch> matches;

    public MatchAdapter(Context matchContext, ArrayList<SummonerMatch> matches) {
        this.matchContext = matchContext;
        this.matches = matches;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(matchContext).inflate(R.layout.summoner_match, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        SummonerMatch currentMatch = matches.get(position);

        String champIconUrl = currentMatch.getChampIconUrl();
        String rune1Url = currentMatch.getRune1Url();
        String rune2Url = currentMatch.getRune2Url();
        String spell1Url = currentMatch.getSpell1Url();
        String spell2Url = currentMatch.getItem2Url();
        String item1Url = currentMatch.getItem1Url();
        String item2Url = currentMatch.getItem2Url();
        String item3Url = currentMatch.getItem3Url();
        String item4Url = currentMatch.getItem4Url();
        String item5Url = currentMatch.getItem5Url();
        Boolean victory = currentMatch.getVictory();
        String kda = currentMatch.getKda();
        String mode = currentMatch.getMode();
        String date = currentMatch.getDate();
        String duration = currentMatch.getDuration();

        //TODO set images

        if(!victory) {
            holder.outcome.setText("VICTORY");
        } else {
            holder.outcome.setText("DEFEAT");
        }
        holder.kda.setText(kda);
        holder.mode.setText(mode);
        holder.date.setText(date);
        holder.duration.setText(duration);

        Picasso.get().load("http://ddragon.leagueoflegends.com/cdn/12.11.1/img/champion/Ahri.png")
                .into(holder.champPicture);

    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public class MatchViewHolder extends RecyclerView.ViewHolder {
        public ImageView champPicture;
        public ImageView rune1;
        public ImageView rune2;
        public ImageView spell1;
        public ImageView spell2;
        public ImageView item1;
        public ImageView item2;
        public ImageView item3;
        public ImageView item4;
        public ImageView item5;
        public TextView outcome;
        public TextView kda;
        public TextView mode;
        public TextView date;
        public TextView duration;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);

            champPicture = itemView.findViewById(R.id.champ);
            rune1 = itemView.findViewById(R.id.rune1);
            rune2 = itemView.findViewById(R.id.rune2);
            spell1 = itemView.findViewById(R.id.spell1);
            spell2 = itemView.findViewById(R.id.spell2);
            item1 = itemView.findViewById(R.id.item1);
            item2 = itemView.findViewById(R.id.item2);
            item3 = itemView.findViewById(R.id.item3);
            item4 = itemView.findViewById(R.id.item4);
            item5 = itemView.findViewById(R.id.item5);
            outcome = itemView.findViewById(R.id.outcome);
            kda = itemView.findViewById(R.id.kda);
            mode = itemView.findViewById(R.id.mode);
            date = itemView.findViewById(R.id.date);
            duration = itemView.findViewById(R.id.duration);
        }
    }

}
