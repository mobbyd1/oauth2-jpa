package com.teste.twitterlike.api.dto;

import java.util.Date;

/**
 * Created by ruhandosreis on 26/12/17.
 */
public class TweetDTO {

    private String tweet;
    private String date;

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
