package com.omgtu.medhelper.controller;

import com.omgtu.medhelper.model.Reminder;
import com.omgtu.medhelper.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    @Autowired
    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

//    @PostMapping
//    public ResponseEntity<Reminder> createReminder(@RequestBody Reminder reminder) {
//        Reminder savedReminder = reminderService.createReminder(reminder);
//        return ResponseEntity.ok(savedReminder);
//    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserReminders(@PathVariable Long userId) {
        return ResponseEntity.ok(reminderService.getUserReminders(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReminder(@PathVariable Long id) {
        reminderService.deleteReminder(id);
        return ResponseEntity.ok().build();
    }
}