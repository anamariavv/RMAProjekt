package com.example.rmaprojekt;

public class LeaderboardRank {
    private String number;
    private String summoner;
    private String rank;
    private String wins;
    private String losses;
    private String lp;
    private boolean hostreak;

    public LeaderboardRank(String number, String summoner, String rank, String wins, String losses, String lp, boolean hostreak) {
        this.number = number;
        this.summoner = summoner;
        this.rank = rank;
        this.wins = wins;
        this.losses = losses;
        this.lp = lp;
        this.hostreak = hostreak;
    }

    public String getNumber() {
        return number;
    }

    public String getSummoner() {
        return summoner;
    }

    public String getRank() {
        return rank;
    }

    public String getWins() {
        return wins;
    }

    public String getLosses() {
        return losses;
    }

    public String getLp() {
        return lp;
    }

    public boolean isHostreak() {
        return hostreak;
    }
}
