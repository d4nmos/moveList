package com.example.demo.repository;

import com.example.demo.models.ListOfLists;
import com.example.demo.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByListOfLists(ListOfLists listOfLists);
}
