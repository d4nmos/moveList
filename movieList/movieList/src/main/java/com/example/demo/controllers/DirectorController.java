package com.example.demo.controllers;

import com.example.demo.models.Director;
import com.example.demo.models.UserProfile;
import com.example.demo.repository.DirectorRepository;
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
@RequestMapping("/admin/director")
public class DirectorController {
    @Autowired
    private DirectorRepository directorRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;

    @GetMapping("/add")
    public String addDirector(Model model){
        Director director = new Director();
        model.addAttribute("director", director);

        return "/directorViews/directorAdd";
    }

    @PostMapping("/save")
    public String saveDirector(@ModelAttribute("director") Director director, Model model){
        if(directorRepository.findByName(director.getName()) != null){
            model.addAttribute("errorMessage", "Director already exists.");
            return "/directorViews/directorAdd";
        }

        directorRepository.save(director);

        List<UserProfile> userProfiles = userProfileRepository.findAll();
        for(UserProfile userProfile : userProfiles){
            userProfile.getDirectorWeights().put(director.getId(), 40);
            userProfileRepository.save(userProfile);
        }
        return "redirect:/admin/director/add";
    }
}
