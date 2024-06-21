package com.example.demo.repository;

import com.example.demo.models.ListOfLists;
import com.example.demo.models.Movie;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListOfListsRepository extends JpaRepository<ListOfLists, Long> {
    ListOfLists findByUser(User user);
}
