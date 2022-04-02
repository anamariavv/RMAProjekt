package com.example.rmaprojekt;
//creates json objects for SingletonRequestSender class
public class MyRequest {
    public org.json.JSONObject requestObject;
    private String[] params;
    private String server;

    //TODO make custom constructors for requests for each necessary api

    public MyRequest(String server, String...params) {
        this.server = server;
        this.params = params;
    }

}
