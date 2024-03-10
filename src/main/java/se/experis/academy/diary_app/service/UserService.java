package se.experis.academy.diary_app.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import se.experis.academy.diary_app.model.BlogUser;

import javax.management.relation.RoleNotFoundException;
import java.util.Optional;

public interface UserService extends UserDetailsService { //implementation for User service

    Optional<BlogUser> findByUsername(String username); //function to find a username

    BlogUser saveNewUser(BlogUser blogUser) throws RoleNotFoundException; //function to save a user into the DB.

}
