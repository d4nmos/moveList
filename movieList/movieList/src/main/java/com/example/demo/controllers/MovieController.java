package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.models.UserProfile;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/movie")
public class MovieController {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ListOfListsRepository listOfListsRepository;
    @Autowired
    private UserListRepository userListRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private DirectorRepository directorRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;

    @GetMapping("/admin/add")
    public String movieAdd(Model model){
        Movie savedMovie = new Movie();
        List<Director> directors = directorRepository.findAll();
        List<Genre> genres = genreRepository.findAll();
        model.addAttribute("genres", genres);
        model.addAttribute("directors", directors);
        model.addAttribute("savedMovie", savedMovie);

        return "movieViews/movieAdd";
    }

    @PostMapping("/save")
    public String saveMovie(@ModelAttribute("savedMovie") Movie movie, Model model){
//        if(movieRepository.findByName(movie.getName()) != null){
//            model.addAttribute("errorMessage", "Movie already exists.");
//            return "movieViews/movieAdd";
//        }
        Director director = movie.getDirector();
        Genre genre = movie.getGenre();

        genre.getMovies().add(movie);
        director.getMovies().add(movie);

        movieRepository.save(movie);

        return "redirect:/movie/list";
    }

    @GetMapping("/{id}")
    public String getMovie( @PathVariable("id") Long id, Model model){

        Optional<Movie> movieOptional = movieRepository.findById(id);

        if(movieOptional.isPresent()){
            Movie movie = movieOptional.get();
            model.addAttribute("movie", movie);
        } else {
            System.out.println("Фильм не найден");
        }

        return "movieViews/movie";
    }

    @GetMapping("/list")
    public String movieList(Model model){
        Authentication auth = (Authentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username);
        ListOfLists listOfLists = listOfListsRepository.findByUser(user);
        SavedMovieInList savedMovieInList = new SavedMovieInList();

        savedMovieInList.setAllUserLists(userListRepository.findByListOfLists(listOfLists));

        List<Movie> movies = movieRepository.findAll();

        model.addAttribute("movies", movies);
        model.addAttribute("savedMovieInList", savedMovieInList);
        return "movieViews/movieList";
    }

    @PostMapping("/addMovieTolist")
    public String addMovieToList(@ModelAttribute("savedMovieInList") SavedMovieInList savedMovieInList,
                                 @RequestParam("movieId") Long movieId, Model model)
    {
        UserList userList = userListRepository.findById(savedMovieInList.getSelectedList()).get();
        Optional<Movie> movieOp = movieRepository.findById(movieId);

        UserProfile userProfile = userProfileRepository.findByListOfLists(userList.getListOfLists());


        if(movieOp.isPresent()) {
            Movie movie = movieOp.get();

            Long genreId = movie.getGenre().getId();
            Long directorId = movie.getDirector().getId();

            Integer genreWeight = userProfile.getGenreWeights().get(genreId);
            Integer directorWeight = userProfile.getDirectorWeights().get(directorId);

            userProfile.getGenreWeights().put(genreId, genreWeight + 2);

            userProfile.getDirectorWeights().put(directorId, directorWeight + 4);

            movieRepository.save(movie);
            userList.getMovies().add(movie);
            userListRepository.save(userList);
            userProfileRepository.save(userProfile);
        }
        else {
            System.out.println("Неверный фильм");
        }

        return "redirect:/userList/" + userList.getId();
    }

    @GetMapping("/search")
    public String searchMovies(Model model){
        String movieName = new String();

        model.addAttribute("movieName", movieName);
        return "movieViews/search";
    }

    @PostMapping("/searchResult")
    public String getSearchResult(@ModelAttribute("movieName") String movieName, Model model){
        List<Movie> movies = movieRepository.findByNameContaining(movieName);
        Authentication auth = (Authentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username);
        ListOfLists listOfLists = listOfListsRepository.findByUser(user);
        SavedMovieInList savedMovieInList = new SavedMovieInList();

        savedMovieInList.setAllUserLists(userListRepository.findByListOfLists(listOfLists));


        model.addAttribute("searchResult", movies);
        model.addAttribute("savedMovieInList", savedMovieInList);
        return "movieViews/searchResult";
    }


}
