package com.omgtu.medhelper.service;

import com.omgtu.medhelper.model.Reminder;
import com.omgtu.medhelper.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;

    @Autowired
    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    public Reminder createReminder(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    public List<Reminder> getUserReminders(Long userId) {
        return reminderRepository.findByUserId(userId);
    }

    public void deleteReminder(Long id) {
        reminderRepository.deleteById(id);
    }
} 