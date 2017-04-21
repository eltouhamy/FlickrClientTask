package com.example.android.flickrclient;

/**
 * Created by mohamed on 20/04/17.
 */

public class Photo {
    private String id;
    private String secret;
    private String server;
    private String farm;

    public Photo(String secret, String id, String server, String farm) {
        this.secret = secret;
        this.id = id;
        this.server = server;
        this.farm = farm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }
}
