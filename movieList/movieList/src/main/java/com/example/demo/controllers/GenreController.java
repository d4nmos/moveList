package com.example.demo.controllers;

import com.example.demo.models.Genre;
import com.example.demo.models.UserProfile;
import com.example.demo.repository.GenreRepository;
import com.example.demo.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/genre")
public class GenreController {
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;

    @GetMapping("/add")
    public String addGenre(Model model){
        Genre genre = new Genre();
        model.addAttribute("genre", genre);

        return "/genreViews/genreAdd";
    }

    @PostMapping("/save")
    public String saveGenre(@ModelAttribute("genre") Genre genre, Model model){
        if(genreRepository.findByName(genre.getName()) != null){
            model.addAttribute("errorMessage", "Genre already exists.");
            return "/genreViews/genreAdd";
        }

        genreRepository.save(genre);

        List<UserProfile> userProfiles = userProfileRepository.findAll();
        for(UserProfile userProfile : userProfiles){
            userProfile.getGenreWeights().put(genre.getId(), 40);
            userProfileRepository.save(userProfile);
        }

        return "redirect:/admin/genre/add";
    }
}
