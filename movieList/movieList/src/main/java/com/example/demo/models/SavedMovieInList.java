package com.example.demo.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SavedMovieInList {
    private Long selectedList;
    private List<UserList> allUserLists;
}
