package com.omgtu.medhelper.service;

import com.omgtu.medhelper.model.Medicine;
import com.omgtu.medhelper.model.Reminder;
import com.omgtu.medhelper.repository.MedicineRepository;
import com.omgtu.medhelper.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final MedicineRepository medicineRepository;

    @Autowired
    public ReminderService(ReminderRepository reminderRepository, MedicineRepository medicineRepository) {
        this.reminderRepository = reminderRepository;
        this.medicineRepository = medicineRepository;
    }

    public Reminder createReminder(Reminder reminder) {
        if (reminder.getMedicine() == null || reminder.getMedicine().getId() == null) {
            throw new IllegalArgumentException("Medicine ID не указан");
        }
        Medicine medicine = medicineRepository.findById(reminder.getMedicine().getId())
                .orElseThrow(()-> new IllegalArgumentException("Лекарство не найдено"));
        reminder.setMedicine(medicine);
        return reminderRepository.save(reminder);
    }

    public List<Reminder> getUserReminders(Long userId) {
        return reminderRepository.findByUserId(userId);
    }

    public void deleteReminder(Long id) {
        reminderRepository.deleteById(id);
    }
} 