package net.engineeringdigest.journalapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.engineeringdigest.journalapp.entity.User;
import net.engineeringdigest.journalapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@Tag(name = "Public APIs", description = "Read, Update & Delete User")
public class PublicController {

    @Autowired
    private UserService userService;

    @PostMapping("/create-user")
    public void createUser(@RequestBody User user) {
        userService.saveNewUserEntry(user);
    }
}
