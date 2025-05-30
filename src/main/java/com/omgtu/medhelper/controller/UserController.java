package com.omgtu.medhelper.controller;

import com.omgtu.medhelper.model.User;
import com.omgtu.medhelper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile/{telegramId}")
    public ResponseEntity<User> getUserProfile(@PathVariable String telegramId) {
        return userService.getUserByTelegramId(telegramId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/profile")
    public ResponseEntity<User> createUserProfile(@RequestParam String telegramId, @RequestParam String name) {
        if (userService.existsByTelegramId(telegramId)) {
            return ResponseEntity.badRequest().build();
        }
        User user = userService.createUser(telegramId, name);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile/{telegramId}")
    public ResponseEntity<User> updateUserProfile(@PathVariable String telegramId, @RequestBody User user) {
        return userService.getUserByTelegramId(telegramId)
                .map(existingUser -> {
                    user.setId(existingUser.getId());
                    user.setTelegramId(telegramId);
                    return ResponseEntity.ok(userService.updateUser(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 