package com.teste.twitterlike.api.model;

import javax.persistence.*;

/**
 * Created by ruhandosreis on 23/12/17.
 */
@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column
    private String post;


}
