package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Entity
@Table(name = "users_lists")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "lists_of_lists_id")
    @ToString.Exclude
    private ListOfLists listOfLists;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "users_lists_movies",
            joinColumns = @JoinColumn(name = "user_list_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    private List<Movie> movies;
}
