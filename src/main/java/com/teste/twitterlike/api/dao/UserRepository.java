package com.teste.twitterlike.api.dao;

import com.teste.twitterlike.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ruhandosreis on 23/12/17.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
