package com.omgtu.medhelper.repository;

import com.omgtu.medhelper.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByTelegramId(String telegramId);
    boolean existsByTelegramId(String telegramId);
} 