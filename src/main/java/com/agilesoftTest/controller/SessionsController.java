package com.agilesoftTest.controller;

import com.agilesoftTest.dao.UsersDAO;
import com.agilesoftTest.dto.SessionDTO;
import com.agilesoftTest.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SessionsController {

    @Autowired
    private UsersDAO usersDAO;

    /**
     * Login with the given credentials if those credentials math some existing user
     * @param session
     * @param sessionDTO
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public void login(HttpSession session, @RequestBody SessionDTO sessionDTO) {
        List<User> users = (List<User>) usersDAO.findAll();
        Long userId = 0L;
        for(User user : users) {
            if (user.getUsername().equals(sessionDTO.getUsername()) && user.getPassword().equals(sessionDTO.getPassword())) {
                userId = user.getId();
                break;
            }
        }
        if (userId != 0L) {
            session.setAttribute("userId",userId);
        }
    }
}