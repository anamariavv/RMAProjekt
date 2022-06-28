package com.example.rmaprojekt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankViewHolder> {
    private Context context;
    private ArrayList<LeaderboardRank> rankings;

    public RankAdapter(Context context, ArrayList<LeaderboardRank> rankings) {
        this.context = context;
        this.rankings = rankings;
    }

    @NonNull
    @Override
    public RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.leaderboard_rank, parent, false);
        return new RankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankViewHolder holder, int position) {
        LeaderboardRank currentRank = rankings.get(position);

        String number = currentRank.getNumber();
        String summoner = currentRank.getSummoner();
        String rank = currentRank.getRank();
        String wins = currentRank.getWins();
        String losses = currentRank.getLosses();
        String lp = currentRank.getLp();
        boolean hotstreak = currentRank.isHotstreak();

        holder.leaderboardNumber.setText(number);
        holder.leaderboardSummoner.setText(summoner);
        holder.leaderboardRank.setText(rank);
        holder.leaderboardWins.setText(wins);
        holder.leaderboardLosses.setText(losses);
        holder.leaderboardLp.setText(lp);

        if(hotstreak) {
            Picasso.get()
                    .load(R.drawable.hotstream_small)
                    .into(holder.leaderboardHotstreak);
        }

    }

    @Override
    public int getItemCount() {
        return rankings.size();
    }

    public class RankViewHolder extends RecyclerView.ViewHolder {
        public TextView leaderboardNumber;
        public TextView leaderboardSummoner;
        public TextView leaderboardRank;
        public TextView leaderboardWins;
        public TextView leaderboardLosses;
        public TextView leaderboardLp;
        public ImageView leaderboardHotstreak;

        public RankViewHolder(@NonNull View itemView) {
            super(itemView);

            leaderboardNumber = itemView.findViewById(R.id.leaderboard_number);
            leaderboardSummoner = itemView.findViewById(R.id.leaderboard_summoner);
            leaderboardRank = itemView.findViewById(R.id.leaderboard_rank);
            leaderboardWins = itemView.findViewById(R.id.leaderboard_wins);
            leaderboardLosses = itemView.findViewById(R.id.leaderboard_losses);
            leaderboardLp = itemView.findViewById(R.id.leaderboard_lp);
            leaderboardHotstreak = itemView.findViewById(R.id.leaderboard_hotstreak);
        }
    }

}
