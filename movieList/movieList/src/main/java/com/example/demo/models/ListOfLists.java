package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lists_of_lists")
@Getter
@Setter
@NoArgsConstructor
public class ListOfLists {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "listOfLists", cascade = CascadeType.REMOVE)
    private List<UserList> userLists = new ArrayList<>();
}
