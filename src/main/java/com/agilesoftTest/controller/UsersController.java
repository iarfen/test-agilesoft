package com.agilesoftTest.controller;

import com.agilesoftTest.dao.UsersDAO;
import com.agilesoftTest.model.User;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

@RestController
public class UsersController {

    @Autowired
    private UsersDAO usersDAO;

    /**
     * Returns the data of the current user
     * @param httpSession
     * @return
     * @throws ResponseStatusException
     */
    @RolesAllowed("USER")
    @GetMapping("/current-user")
    public User readCurrentUser(HttpSession httpSession) throws ResponseStatusException {
        return usersDAO.findById((Long) httpSession.getAttribute("userId")).orElseThrow(() -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"); } );
    }

    /**
     * Creates a new user
     * @param user
     */
    @PostMapping("/users")
    void createUser(@RequestBody User user) {
        int strength = 10;
        BCryptPasswordEncoder bCryptPasswordEncoder =
                new BCryptPasswordEncoder(strength, new SecureRandom());
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        usersDAO.save(user);
    }
}
