package com.teste.twitterlike.api.controller;

import com.teste.twitterlike.api.dao.UserRepository;
import com.teste.twitterlike.api.dto.TweetDTO;
import com.teste.twitterlike.api.model.Tweet;
import com.teste.twitterlike.api.model.User;
import com.teste.twitterlike.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ruhandosreis on 23/12/17.
 */
@RestController
@RequestMapping(value = "/api/v1")
public class TweetControllerV1 {

    private static String API_DATE_PATTERN = "yyyy-mm-dd hh:mm:ss";

    @Autowired
    UserService userDetailsService;

    @GetMapping(value = "/tweets")
    public ResponseEntity getTweets(Principal principal ) {
        final String username = principal.getName();
        final User user = userDetailsService.findByUsername(username);

        final List<Tweet> tweets = user.getTweets();
        tweets.sort( Comparator.comparing( o -> o.getDate() ) );

        final DateFormat dateFormat = new SimpleDateFormat( API_DATE_PATTERN );

        final List<Object> tweetsList = new ArrayList<>();

        for( final Tweet tweet : tweets ) {
            final TweetDTO tweetDTO = new TweetDTO();
            tweetDTO.setTweet( tweet.getTweet() );

            String dateStr = dateFormat.format(tweet.getDate());
            tweetDTO.setDate( dateStr );

            tweetsList.add( tweetDTO );
        }

        return new ResponseEntity(tweetsList, HttpStatus.OK);
    }

    @PostMapping(value = "/tweet")
    public ResponseEntity tweet(@RequestBody String tweet, Principal principal) {
       return new ResponseEntity(HttpStatus.MOVED_PERMANENTLY);
    }
}
