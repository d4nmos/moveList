package com.example.demo.controllers;

import com.example.demo.models.ListOfLists;
import com.example.demo.models.Movie;
import com.example.demo.models.User;
import com.example.demo.models.UserList;
import com.example.demo.repository.ListOfListsRepository;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.UserListRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/userList")
public class UserListController {
    @Autowired
    private UserListRepository userListRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ListOfListsRepository listOfListsRepository;
    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/{id}")
    public String getList(@PathVariable("id") Long id, Model model){
        Optional<UserList> userList = userListRepository.findById(id);
        List<Movie> movies = userList.get().getMovies();

        model.addAttribute("userList", userList);
        model.addAttribute("movies", movies);
        return "userList/showUserList";
    }

    @PostMapping("/deleteMovieFromList")
    public String deleteMovieFromList(@RequestParam("userListId") Long userListId,
                                      @RequestParam("movieId") Long movieId,
                                      Model model)
    {
        Optional<UserList> userListOp = userListRepository.findById(userListId);
        Optional<Movie> movieOp = movieRepository.findById(movieId);

        if(userListOp.isPresent() && movieOp.isPresent()){
            UserList userList = userListOp.get();
            Movie movie = movieOp.get();

            userList.getMovies().remove(movie);
            userListRepository.save(userList);
        }
        else {
            System.out.println("Неверные данные");
        }

        return "redirect:/userList/" + userListId;
    }

}
