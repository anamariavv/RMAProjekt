package com.example.rmaprojekt;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
        String spell2Url = currentMatch.getSpell2Url();
        String item1Url = currentMatch.getItem1Url();
        String item2Url = currentMatch.getItem2Url();
        String item3Url = currentMatch.getItem3Url();
        String item4Url = currentMatch.getItem4Url();
        String item5Url = currentMatch.getItem5Url();
        String item6Url = currentMatch.getItem6Url();
        String item7Url = currentMatch.getItem7Url();
        String victory = currentMatch.getVictory();
        String kda = currentMatch.getKda();
        String mode = currentMatch.getMode();
        String date = currentMatch.getDate();
        String duration = currentMatch.getDuration();

        setMatchBackgroundColor(holder, victory);

        holder.outcome.setText(victory);
        holder.kda.setText(kda);
        holder.mode.setText(mode);
        holder.date.setText(date);
        holder.duration.setText(duration);
        Picasso.get().load(champIconUrl)
                .into(holder.champPicture);
        Picasso.get().load(item1Url)
                .into(holder.item1);
        Picasso.get().load(item2Url)
                .into(holder.item2);
        Picasso.get().load(item3Url)
                .into(holder.item3);
        Picasso.get().load(item4Url)
                .into(holder.item4);
        Picasso.get().load(item5Url)
                .into(holder.item5);
        Picasso.get().load(item6Url)
                .into(holder.item6);
        Picasso.get().load(item7Url)
                .into(holder.item7);
        Picasso.get().load(spell1Url)
                .into(holder.spell1);
        Picasso.get().load(spell2Url)
                .into(holder.spell2);
        Picasso.get().load(rune1Url)
                .into(holder.rune1);
        Picasso.get().load(rune2Url)
                .into(holder.rune2);
    }

    private void setMatchBackgroundColor( MatchViewHolder holder, String outcome) {
        if(outcome.equals("VICTORY")) {
            holder.matchParent.setBackgroundColor(matchContext.getResources().getColor(R.color.outcome_victory));
        } else {
            holder.matchParent.setBackgroundColor(matchContext.getResources().getColor(R.color.outcome_defeat));
        }
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
        public ImageView item6;
        public ImageView item7;
        public TextView outcome;
        public TextView kda;
        public TextView mode;
        public TextView date;
        public TextView duration;
        public RelativeLayout matchParent;

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
            item6 = itemView.findViewById(R.id.item6);
            item7 = itemView.findViewById(R.id.item7);
            outcome = itemView.findViewById(R.id.outcome);
            kda = itemView.findViewById(R.id.kda);
            mode = itemView.findViewById(R.id.mode);
            date = itemView.findViewById(R.id.date);
            duration = itemView.findViewById(R.id.duration);
            matchParent = itemView.findViewById(R.id.match_parent);
        }
    }

}
