package com.example.demo.models;

import com.example.demo.models.Director;
import com.example.demo.models.Genre;
import com.example.demo.models.ListOfLists;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "list_of_lists_id")
    private ListOfLists listOfLists;
    @ElementCollection
    @CollectionTable(name = "user_profile_directors_weights",
            joinColumns = @JoinColumn(name = "user_profile_id"))
    @MapKeyColumn(name = "director_id")
    @Column(name = "weight")
    private Map<Long, Integer> directorWeights = new HashMap<>();
    @ElementCollection
    @CollectionTable(name = "user_profile_genres_weights",
            joinColumns = @JoinColumn(name = "user_profile_id"))
    @MapKeyColumn(name = "genre_id")
    @Column(name = "weight")
    private Map<Long, Integer> genreWeights = new HashMap<>();
}
