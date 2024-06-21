package com.example.demo.services;

import com.example.demo.exception.UserAlreadyExistException;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service("userService")
public class DefaultUserService implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void register(User user, String roleName) throws UserAlreadyExistException {
        if (checkIfUserExist(user.getUsername())) {
            throw new UserAlreadyExistException("User already exists for this email");
        }

        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            throw new RuntimeException("Role not found");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList(role));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfUserExist(String username) {
        return userRepository.findByUsername(username) != null;
    }
}
