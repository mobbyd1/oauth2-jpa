package com.teste.twitterlike.api.controller;

import com.teste.twitterlike.api.model.Tweet;
import com.teste.twitterlike.api.model.User;
import com.teste.twitterlike.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

/**
 * Created by ruhandosreis on 26/12/17.
 */
@RestController
@RequestMapping(value = "/api/v2")
public class TweetControllerV2 {

    @Autowired
    UserService userService;

    @Autowired
    TweetControllerV1 v1;

    @GetMapping(value = "/tweets")
    public ResponseEntity getTweets(Principal principal ) {
        return v1.getTweets( principal );
    }

    @PostMapping(value = "/tweet")
    public ResponseEntity tweet(@RequestBody Map<String, String> body, Principal principal) {
       final  String tweet = body.get("tweet");

        if( tweet.length() > 140  ) {
            return new ResponseEntity("O tamanho do tweet não pode ser maior do que 140 caracteres", HttpStatus.BAD_REQUEST);
        }

        final String username = principal.getName();
        final User user = userService.findByUsername(username);

        final Tweet newTweet = new Tweet(tweet);
        user.addTweet( newTweet );

        userService.update( user );

        return new ResponseEntity( HttpStatus.OK );
    }
}
