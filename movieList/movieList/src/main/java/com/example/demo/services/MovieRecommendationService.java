package com.example.demo.services;

import com.example.demo.models.*;
import com.example.demo.models.UserProfile;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.UserListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieRecommendationService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserListRepository userListRepository;

    public List<Movie> getTopRecommendation(UserProfile userProfile, int count){
        List<Movie> movies = movieRepository.findAll();
        Map<Movie, Integer> moviesWeights = new HashMap<>();
        List<UserList> userLists = userListRepository.findByListOfLists(userProfile.getListOfLists());

        for(UserList userList : userLists){
            movies.removeAll(userList.getMovies());
        }

        Map<Long, Integer> genresWeights = userProfile.getGenreWeights();
        Map<Long, Integer> directorsWeights = userProfile.getDirectorWeights();

        for(Movie movie : movies){
            Long directorId = movie.getDirector().getId();
            Long genreId = movie.getGenre().getId();

            int directorWeight = directorsWeights.getOrDefault(directorId, 0);
            int genreWeight = genresWeights.getOrDefault(genreId, 0);

            moviesWeights.put(movie, directorWeight + genreWeight);
        }

        int limit = Math.min(count, moviesWeights.size());

        List<Movie> topMovies = moviesWeights.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return topMovies;
    }


}
