package com.example.demo.services;

import com.example.demo.exception.UserAlreadyExistException;
import com.example.demo.models.User;

public interface UserService {
    void register(User user, String roleName) throws UserAlreadyExistException;
    boolean checkIfUserExist(String username);
}
