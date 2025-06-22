import { createReminder } from './api.js';
import { getUserId } from './telegram.js';

export function initReminders(currentMedicineRef) {
    const addReminderButton = document.getElementById('addReminder');
    const reminderModal = document.getElementById('reminderModal');
    const saveReminderButton = document.getElementById('saveReminder');
    const cancelReminderButton = document.getElementById('cancelReminder');

    addReminderButton.addEventListener('click', () => {
        reminderModal.classList.remove('hidden');
    });

    cancelReminderButton.addEventListener('click', () => {
        reminderModal.classList.add('hidden');
    });

    saveReminderButton.addEventListener('click', async () => {
        const time = document.getElementById('reminderTime').value;
        const frequency = document.getElementById('reminderFrequency').value;

        if (!time) {
            alert('Пожалуйста, выберите время приема');
            return;
        }

        try {
            const response = await createReminder({
                medicineId: currentMedicineRef.medicine.id,
                time: time,
                frequency: frequency,
                userId: getUserId()
            });

            if (response.ok) {
                reminderModal.classList.add('hidden');
                alert('Напоминание успешно добавлено');
            } else {
                throw new Error('Ошибка при сохранении напоминания');
            }
        } catch (error) {
            alert('Произошла ошибка при сохранении напоминания');
        }
    });

    reminderModal.addEventListener('click', (e) => {
        if (e.target === reminderModal) {
            reminderModal.classList.add('hidden');
        }
    });
}
