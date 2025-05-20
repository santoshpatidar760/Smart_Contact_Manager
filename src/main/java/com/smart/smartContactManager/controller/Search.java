package com.smart.smartContactManager.controller;


import com.smart.smartContactManager.entity.Contact;
import com.smart.smartContactManager.entity.User;
import com.smart.smartContactManager.repo.ContactRepository;
import com.smart.smartContactManager.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class Search {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;
    //search handler
    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal)
    {
        System.out.println(query);
        User user= this.userRepository.getUserByUsername(principal.getName());
        List<Contact> contacts= this.contactRepository.findByNameContainingAndUser(query,user);
        return ResponseEntity.ok(contacts);
    }
}
