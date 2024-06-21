package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.models.UserProfile;
import com.example.demo.repository.*;
import com.example.demo.services.MovieRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MovieRecommendationController {
    @Autowired
    private MovieRecommendationService movieRecommendationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ListOfListsRepository listOfListsRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserListRepository userListRepository;

    @GetMapping("/recommendation")
    public String getRecommendedMovies(Model model){
        Authentication auth = (Authentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username);
        ListOfLists listOfLists = listOfListsRepository.findByUser(user);
        UserProfile userProfile = userProfileRepository.findByListOfLists(listOfLists);
        SavedMovieInList savedMovieInList = new SavedMovieInList();

        savedMovieInList.setAllUserLists(userListRepository.findByListOfLists(listOfLists));

        List<Movie> recommendedMovies = movieRecommendationService.getTopRecommendation(userProfile, 20);

        model.addAttribute("recommendedMovies", recommendedMovies);
        model.addAttribute("userProfile", userProfile);
        model.addAttribute("savedMovieInList", savedMovieInList);

        return "recommendationViews/showRecommendation";
    }

    @PostMapping("/deleteFromRecommendation")
    public String deleteFromRecommendation(@RequestParam("movieId") Long movieId,
                                           @RequestParam("userProfileId") Long userProfileId, Model model){
        UserProfile userProfile = userProfileRepository.findById(userProfileId).get();
        Movie movie = movieRepository.findById(movieId).get();

        Long genreId = movie.getGenre().getId();
        Long directorId = movie.getDirector().getId();

        Integer genreWeight = userProfile.getGenreWeights().get(genreId);
        Integer directorWeight = userProfile.getDirectorWeights().get(directorId);

        if(genreWeight > 0){
            userProfile.getGenreWeights().put(genreId, genreWeight - 5);
        }

        if(directorWeight > 0){
            userProfile.getDirectorWeights().put(directorId, directorWeight - 10);
        }

        userProfileRepository.save(userProfile);

        return "redirect:/recommendation";
    }

    @PostMapping("/addToList")
    public String addToList(@RequestParam("movieId") Long movieId,
                            @RequestParam("userProfileId") Long userProfileId,
                            @ModelAttribute("savedMovieInList") SavedMovieInList savedMovieInList,Model model){
        UserProfile userProfile = userProfileRepository.findById(userProfileId).get();
        Movie movie = movieRepository.findById(movieId).get();

        Long genreId = movie.getGenre().getId();
        Long directorId = movie.getDirector().getId();

        Integer genreWeight = userProfile.getGenreWeights().get(genreId);
        Integer directorWeight = userProfile.getDirectorWeights().get(directorId);

        userProfile.getGenreWeights().put(genreId, genreWeight + 2);

        userProfile.getDirectorWeights().put(directorId, directorWeight + 4);

        UserList userList = userListRepository.findById(savedMovieInList.getSelectedList()).get();

        movieRepository.save(movie);
        userList.getMovies().add(movie);
        userListRepository.save(userList);
        userProfileRepository.save(userProfile);

        return "redirect:/userList/" + userList.getId();
    }
}
