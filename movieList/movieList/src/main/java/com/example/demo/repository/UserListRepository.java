package com.example.demo.repository;

import com.example.demo.models.ListOfLists;
import com.example.demo.models.User;
import com.example.demo.models.UserList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserListRepository extends JpaRepository<UserList, Long> {
    List<UserList> findByListOfLists(ListOfLists listOfLists);
    UserList findByName(String name);
}
