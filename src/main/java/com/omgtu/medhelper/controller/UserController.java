package com.omgtu.medhelper.controller;

import com.omgtu.medhelper.model.User;
import com.omgtu.medhelper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"https://web.telegram.org", "http://localhost:8080", "http://127.0.0.1:8080"}, 
             allowCredentials = "true",
             allowedHeaders = "*",
             exposedHeaders = {"X-Telegram-Init-Data", "X-Telegram-User-ID"})
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile/{telegramId}")
    public ResponseEntity<User> getProfile(@PathVariable String telegramId) {
        logger.info("Getting profile for telegramId: {}", telegramId);
        return userService.getUserByTelegramId(telegramId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user, HttpServletRequest request) {
        try {
            logger.info("Получен запрос на регистрацию пользователя: {}", user);
            logger.info("Request headers: {}", Collections.list(request.getHeaderNames())
                    .stream()
                    .collect(Collectors.toMap(
                            headerName -> headerName,
                            request::getHeader
                    )));
            
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                logger.error("Имя пользователя не может быть пустым");
                return ResponseEntity.badRequest().body("Имя пользователя обязательно");
            }
            
            if (user.getName() == null || user.getName().trim().isEmpty()) {
                logger.error("Имя не может быть пустым");
                return ResponseEntity.badRequest().body("Имя обязательно");
            }

            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                logger.error("Email не может быть пустым");
                return ResponseEntity.badRequest().body("Email обязателен");
            }

            if (user.getTelegramId() == null || user.getTelegramId().trim().isEmpty()) {
                logger.error("Telegram ID не указан");
                return ResponseEntity.badRequest().body("Telegram ID обязателен");
            }

            if (userService.existsByTelegramId(user.getTelegramId())) {
                logger.error("Пользователь с telegramId {} уже существует", user.getTelegramId());
                return ResponseEntity.badRequest().body("Пользователь уже существует");
            }

            User createdUser = userService.createUser(
                user.getTelegramId(),
                user.getUsername(),
                user.getName(),
                user.getEmail()
            );
            
            logger.info("Пользователь успешно создан: {}", createdUser);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            logger.error("Ошибка при создании пользователя", e);
            return ResponseEntity.badRequest().body("Ошибка при создании пользователя: " + e.getMessage());
        }
    }

    @PutMapping("/profile/{telegramId}")
    public ResponseEntity<?> updateUserProfile(@PathVariable String telegramId, @RequestBody User user) {
        try {
            logger.info("Получен запрос на обновление профиля: {}", user);
            
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                logger.error("Имя пользователя не может быть пустым");
                return ResponseEntity.badRequest().body("Имя пользователя обязательно");
            }
            
            if (user.getName() == null || user.getName().trim().isEmpty()) {
                logger.error("Имя не может быть пустым");
                return ResponseEntity.badRequest().body("Имя обязательно");
            }

            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                logger.error("Email не может быть пустым");
                return ResponseEntity.badRequest().body("Email обязателен");
            }

            return userService.getUserByTelegramId(telegramId)
                    .map(existingUser -> {
                        user.setId(existingUser.getId());
                        user.setTelegramId(telegramId);
                        User updatedUser = userService.updateUser(user);
                        logger.info("Профиль успешно обновлен: {}", updatedUser);
                        return ResponseEntity.ok(updatedUser);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Ошибка при обновлении профиля", e);
            return ResponseEntity.badRequest().body("Ошибка при обновлении профиля: " + e.getMessage());
        }
    }
} 