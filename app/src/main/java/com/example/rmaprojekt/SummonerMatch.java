package com.example.rmaprojekt;

import java.time.LocalDate;

public class SummonerMatch {
    //TODO change fields
    private String champIconUrl;
    private String rune1Url;
    private String rune2Url;
    private String spell1Url;
    private String spell2Url;
    private String item1Url;
    private String item2Url;
    private String item3Url;
    private String item4Url;
    private String item5Url;
    private Boolean victory;
    private String kda;
    private String mode;
    private String date;
    private String duration;

    public SummonerMatch(String champIconUrl, String rune1Url, String rune2Url, String spell1Url, String spell2Url, String item1Url, String item2Url, String item3Url, String item4Url, String item5Url, Boolean victory, String kda, String mode, String date, String duration) {
        this.champIconUrl = champIconUrl;
        this.rune1Url = rune1Url;
        this.rune2Url = rune2Url;
        this.spell1Url = spell1Url;
        this.spell2Url = spell2Url;
        this.item1Url = item1Url;
        this.item2Url = item2Url;
        this.item3Url = item3Url;
        this.item4Url = item4Url;
        this.item5Url = item5Url;
        this.victory = victory;
        this.kda = kda;
        this.mode = mode;
        this.date = date;
        this.duration = duration;
    }

    public String getChampIconUrl() {
        return champIconUrl;
    }

    public String getRune1Url() {
        return rune1Url;
    }

    public String getRune2Url() {
        return rune2Url;
    }

    public String getSpell1Url() {
        return spell1Url;
    }

    public String getSpell2Url() {
        return spell2Url;
    }

    public String getItem1Url() {
        return item1Url;
    }

    public String getItem2Url() {
        return item2Url;
    }

    public String getItem3Url() {
        return item3Url;
    }

    public String getItem4Url() {
        return item4Url;
    }

    public String getItem5Url() {
        return item5Url;
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
