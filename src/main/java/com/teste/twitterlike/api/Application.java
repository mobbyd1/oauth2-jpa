package com.teste.twitterlike.api;

import com.teste.twitterlike.api.auth.CustomUserDetails;
import com.teste.twitterlike.api.dao.UserRepository;
import com.teste.twitterlike.api.model.Role;
import com.teste.twitterlike.api.model.User;
import com.teste.twitterlike.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ruhandosreis on 23/12/17.
 */
@SpringBootApplication
public class Application {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository repository, UserService service) throws Exception {

        if ( repository.count() == 0) {
            final List<Role> roles = Arrays.asList(new Role("USER"), new Role("ACTUATOR"));
            final User user = new User("user", "user", roles);

            service.save(user);
        }

        builder.userDetailsService(userDetailsService(repository)).passwordEncoder(passwordEncoder);
    }

    private UserDetailsService userDetailsService(final UserRepository repository) {
        return username -> new CustomUserDetails(repository.findByUsername(username));
    }
}
