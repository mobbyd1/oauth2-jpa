package com.teste.twitterlike.api.service;

import com.teste.twitterlike.api.dao.UserRepository;
import com.teste.twitterlike.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by ruhandosreis on 23/12/17.
 */
@Service("userDetailsService")
public class UserService {

    @Autowired
    private UserRepository repo;

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public User findByUsername( String username ) {
        return repo.findByUsername( username );
    }

    public void save(User user){
        user.setPassword(getPasswordEncoder().encode(user.getPassword()));
        repo.save(user);
    }

    public void update(User user) {
        repo.save( user );
    }
}
