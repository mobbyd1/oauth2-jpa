package com.teste.twitterlike.api.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ruhandosreis on 23/12/17.
 */
@Entity
@Table(name = "tweet")
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column
    private String tweet;

    @Column
    private Date date;

    public Tweet() {
        super();
    }

    public Tweet( String tweet ) {
        this.tweet = tweet;
        this.date = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }
}
