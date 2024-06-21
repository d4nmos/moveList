package com.example.demo.controllers;

import com.example.demo.models.ListOfLists;
import com.example.demo.models.User;
import com.example.demo.models.UserList;
import com.example.demo.repository.ListOfListsRepository;
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
@RequestMapping("/listOfLists")
public class ListOfListsController {
    @Autowired
    private ListOfListsRepository listOfListsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserListRepository userListRepository;

    @GetMapping("/show")
    public String showListOfLists(Model model){
        Authentication auth = (Authentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username);
        ListOfLists listOfLists = listOfListsRepository.findByUser(user);
        List<UserList> userLists = userListRepository.findByListOfLists(listOfLists);

        model.addAttribute("userLists", userLists);
        return "listOfListsViews/listOfListsShow";
    }

    @GetMapping("/addList")
    public String addNewList(Model model){
        UserList userList = new UserList();
        model.addAttribute("savedUserList", userList);

        return "listOfListsViews/addList";
    }

    @PostMapping("/save")
    public String saveList(@ModelAttribute("savedUserList") UserList userList,Model model){
        Authentication auth = (Authentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username);
        ListOfLists listOfLists = listOfListsRepository.findByUser(user);

        userList.setListOfLists(listOfLists);
        userListRepository.save(userList);

//        List<UserList> userLists = userListRepository.findByListOfLists(listOfLists);
//        model.addAttribute("listOfLists", listOfLists);
//        model.addAttribute("userLists", userLists);

        return "redirect:/userList/" + userList.getId();
    }


    @PostMapping("/deleteList")
    public String deleteList(@RequestParam("userListId") Long userListId, Model model)
    {
        Optional<UserList> userListOp = userListRepository.findById(userListId);
        Authentication auth = (Authentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username);
        ListOfLists listOfLists = listOfListsRepository.findByUser(user);

        if(userListOp.isPresent()){
            UserList userList = userListOp.get();

            listOfLists.getUserLists().remove(userList);
            userListRepository.delete(userList);
            listOfListsRepository.save(listOfLists);

        }

        else {
            System.out.println("Неверные данные");
        }

        return "redirect:/listOfLists/show";
    }
}
