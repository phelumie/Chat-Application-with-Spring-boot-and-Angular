package com.ajisegiri.chatapp.repo;

import com.ajisegiri.chatapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface UserRepository extends MongoRepository<User,String> {
    boolean existsByUsername(String username);
    User findByUsername(String username);
    void  deleteByUsername(String username);

}
