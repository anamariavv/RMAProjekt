package com.example.rmaprojekt;

public class SummonerMatch {
    private static final String CHAMPION_URL_CONST = "http://ddragon.leagueoflegends.com/cdn/12.11.1/img/champion/";
    private static final String ITEM_URL_CONST = "http://ddragon.leagueoflegends.com/cdn/12.11.1/img/item/";
    private static final String SPELL_URL_CONST = "http://ddragon.leagueoflegends.com/cdn/12.11.1/img/spell/";
    private static final String RUNE_URL_CONST = "http://ddragon.leagueoflegends.com/cdn/img/";
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
    private String item6Url;
    private String item7Url;
    private String victory;
    private String kda;
    private String mode;
    private String date;
    private String duration;

    public SummonerMatch(String champIconUrl, String rune1Url, String rune2Url, String spell1Url, String spell2Url, String item1Url, String item2Url, String item3Url, String item4Url, String item5Url, String item6Url, String item7Url, String victory, String kda, String mode, String date, String duration) {
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
        this.item6Url = item6Url;
        this.item7Url = item7Url;
        this.victory = victory;
        this.kda = kda;
        this.mode = mode;
        this.date = date;
        this.duration = duration;
    }

    public static String formatDuration(int duration, boolean hasGameEndTimestamp) {
        int minutes;
        int seconds;

        if (hasGameEndTimestamp) {
            minutes = (duration / 1000) / 60;
            seconds = (duration / 1000) % 60;
        } else {
            minutes = duration / 60;
            seconds = duration % 60;
        }

        return minutes + ":" + seconds;
    }

    public static String createChampionUrl(String championName) {
        return CHAMPION_URL_CONST + championName + ".png";
    }

    public static String createItemUrl(String itemNumber) {
        return ITEM_URL_CONST + itemNumber + ".png";
    }

    public static String createSpellUrl(String spellName) {
        return SPELL_URL_CONST + spellName + ".png";
    }

    public static String createRuneUrl(String runePath) {
        return RUNE_URL_CONST + runePath;
    }

    public static String returnOutcome(boolean win) {
        if (win) return "VICTORY";

        return "DEFEAT";
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

    public String getItem6Url() {
        return item6Url;
    }

    public String getItem7Url() {
        return item7Url;
    }

    public String getVictory() {
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
