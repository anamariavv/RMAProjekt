package com.example.rmaprojekt;

import java.time.LocalDate;

public class SummonerMatch {
    //TODO change fields
    private int champIconId;
    private int rune1Id;
    private int rune2Id;
    private int spell1Id;
    private int spell2Id;
    private int item1Id;
    private int item2Id;
    private int item3Id;
    private int item4Id;
    private int item5Id;
    private Boolean victory;
    private String kda;
    private String mode;
    private String date;
    private String duration;

    public SummonerMatch(int champIconId, int rune1Id, int rune2Id, int spell1Id, int spell2Id, int item1Id, int item2Id, int item3Id, int item4Id, int item5Id, Boolean victory, String kda, String mode, String date, String duration) {
        this.champIconId = champIconId;
        this.rune1Id = rune1Id;
        this.rune2Id = rune2Id;
        this.spell1Id = spell1Id;
        this.spell2Id = spell2Id;
        this.item1Id = item1Id;
        this.item2Id = item2Id;
        this.item3Id = item3Id;
        this.item4Id = item4Id;
        this.item5Id = item5Id;
        this.victory = victory;
        this.kda = kda;
        this.mode = mode;
        this.date = date;
        this.duration = duration;
    }

    public int getChampIconId() {
        return champIconId;
    }

    public int getRune1Id() {
        return rune1Id;
    }

    public int getRune2Id() {
        return rune2Id;
    }

    public int getSpell1Id() {
        return spell1Id;
    }

    public int getSpell2Id() {
        return spell2Id;
    }

    public int getItem1Id() {
        return item1Id;
    }

    public int getItem2Id() {
        return item2Id;
    }

    public int getItem3Id() {
        return item3Id;
    }

    public int getItem4Id() {
        return item4Id;
    }

    public int getItem5Id() {
        return item5Id;
    }

    public Boolean getVictory() {
        return victory;
    }

    public String getKda() {
        return kda;
    }

    public String getMode() {
        return mode;
    }

    public String getDate() {
        return date;
    }

    public String getDuration() {
        return duration;
    }
}
