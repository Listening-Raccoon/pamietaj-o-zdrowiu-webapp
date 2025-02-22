package com.listeningraccoon.pamietaj_o_zdrowiu.backend.controllers;

import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.User;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/id")
    public ResponseEntity<Optional<User>> getUserById(@RequestParam ObjectId id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/email")
    public ResponseEntity<Optional<User>> getUserByEmail(@RequestParam String email) {
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<User>> filterUser(@RequestParam String filter) {
        return new ResponseEntity<>(userService.getUsersByFilter(filter), HttpStatus.OK);
    }
}
