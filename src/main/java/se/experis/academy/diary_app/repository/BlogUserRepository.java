package se.experis.academy.diary_app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.experis.academy.diary_app.model.BlogUser;

import java.util.Optional;

@Repository
public interface BlogUserRepository extends JpaRepository<BlogUser, Long> { // JPA repo to store users

    Optional<BlogUser> findByUsername(String username); //function to find a username returns model class

}
