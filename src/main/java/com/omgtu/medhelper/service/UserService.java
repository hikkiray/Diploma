package com.omgtu.medhelper.service;

import com.omgtu.medhelper.model.User;
import com.omgtu.medhelper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String telegramId, String name) {
        User user = new User();
        user.setTelegramId(telegramId);
        user.setName(name);
        return userRepository.save(user);
    }

    public Optional<User> getUserByTelegramId(String telegramId) {
        return userRepository.findByTelegramId(telegramId);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public boolean existsByTelegramId(String telegramId) {
        return userRepository.existsByTelegramId(telegramId);
    }
} 