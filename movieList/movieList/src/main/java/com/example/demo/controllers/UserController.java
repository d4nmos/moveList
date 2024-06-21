package com.example.demo.controllers;

import com.example.demo.exception.UserAlreadyExistException;
import com.example.demo.models.*;
import com.example.demo.models.UserProfile;
import com.example.demo.repository.*;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ListOfListsRepository listOfListsRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private DirectorRepository directorRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/registration")
    public String goRegistration(Model model){
        User savedUser = new User();
        model.addAttribute("savedUser", savedUser);
        return "registration";
    }

    @PostMapping("/registration")
    public String saveUser(@ModelAttribute("savedUser") User user, Model model) throws UserAlreadyExistException {

        if(userRepository.findByUsername(user.getUsername()) != null){
            model.addAttribute("errorMessage", "User already exists.");
            return "registration";
        }

        ListOfLists listOfLists = new ListOfLists();
        UserProfile userProfile = new UserProfile();
        List<Genre> genres = genreRepository.findAll();
        List<Director> directors = directorRepository.findAll();

        for(Genre genre : genres){
            userProfile.getGenreWeights().put(genre.getId(), 50);
        }

        for(Director director : directors){
            userProfile.getDirectorWeights().put(director.getId(), 50);
        }

        listOfLists.setUser(user);
        userProfile.setListOfLists(listOfLists);

        userService.register(user, "USER");
        listOfListsRepository.save(listOfLists);
        userProfileRepository.save(userProfile);
        return "home";
    }

    @GetMapping("/admin/registrationAdmin")
    public String registrationAdmin(Model model){
        User savedUser = new User();
        model.addAttribute("savedUser", savedUser);
        return "registrationAdmin";
    }

    @PostMapping("/registrationAdmin")
    public String saveAdmin(@ModelAttribute("savedUser") User user, Model model) throws UserAlreadyExistException {
        if(userRepository.findByUsername(user.getUsername()) != null){
            model.addAttribute("errorMessage", "Admin already exists.");
            return "registrationAdmin";
        }

        ListOfLists listOfLists = new ListOfLists();
        UserProfile userProfile = new UserProfile();
        List<Genre> genres = genreRepository.findAll();
        List<Director> directors = directorRepository.findAll();

        for(Genre genre : genres){
            userProfile.getGenreWeights().put(genre.getId(), 50);
        }

        for(Director director : directors){
            userProfile.getDirectorWeights().put(director.getId(), 50);
        }

        listOfLists.setUser(user);
        userProfile.setListOfLists(listOfLists);

        userService.register(user, "ADMIN");
        listOfListsRepository.save(listOfLists);
        userProfileRepository.save(userProfile);
        return "home";
    }


}
