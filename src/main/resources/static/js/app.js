// Инициализация Telegram WebApp
const tg = window.Telegram.WebApp;
tg.ready();

// Настройка темы Telegram
function updateTheme() {
    const theme = tg.themeParams;
    document.documentElement.style.setProperty('--primary-color', theme.button_color || '#2AABEE');
    document.documentElement.style.setProperty('--secondary-color', theme.button_color || '#229ED9');
    document.documentElement.style.setProperty('--background-color', theme.bg_color || '#ffffff');
    document.documentElement.style.setProperty('--text-color', theme.text_color || '#333333');
    document.documentElement.style.setProperty('--border-color', theme.hint_color || '#e0e0e0');
}

// Применяем тему при загрузке
updateTheme();

// Слушаем изменения темы
tg.onEvent('themeChanged', updateTheme);

// Элементы DOM
const searchInput = document.getElementById('searchInput');
const searchButton = document.getElementById('searchButton');
const searchResults = document.getElementById('searchResults');
const medicineDetails = document.getElementById('medicineDetails');
const medicineName = document.getElementById('medicineName');
const medicineDescription = document.getElementById('medicineDescription');
const medicineDosage = document.getElementById('medicineDosage');
const medicineInteractions = document.getElementById('medicineInteractions');
const addReminderButton = document.getElementById('addReminder');
const reminderModal = document.getElementById('reminderModal');
const saveReminderButton = document.getElementById('saveReminder');
const cancelReminderButton = document.getElementById('cancelReminder');
const suggestionsContainer = document.getElementById('suggestions');

// Текущее выбранное лекарство
let currentMedicine = null;

// Функция для создания заголовков запроса
function getHeaders() {
    return {
        'Content-Type': 'application/json',
        'X-Telegram-Init-Data': tg.initData,
        'X-Telegram-User-ID': tg.initDataUnsafe?.user?.id
    };
}

// Обработчик ввода в поле поиска
let debounceTimer;
searchInput.addEventListener('input', async (e) => {
    const query = e.target.value.trim();
    
    // Очищаем предыдущий таймер
    clearTimeout(debounceTimer);
    
    if (query.length < 2) {
        suggestionsContainer.classList.add('hidden');
        return;
    }
    
    // Показываем индикатор загрузки
    suggestionsContainer.innerHTML = '<div class="suggestion-loading">Поиск...</div>';
    suggestionsContainer.classList.remove('hidden');
    
    // Устанавливаем новый таймер с меньшей задержкой
    debounceTimer = setTimeout(async () => {
        try {
            const response = await fetch(`/api/medicines/suggestions?query=${encodeURIComponent(query)}`, {
                headers: getHeaders()
            });
            const suggestions = await response.json();
            
            if (suggestions.length > 0) {
                suggestionsContainer.innerHTML = suggestions
                    .map(suggestion => {
                        // Подсветка совпадающего текста
                        const highlightedSuggestion = suggestion.replace(
                            new RegExp(query, 'gi'),
                            match => `<span class="highlight">${match}</span>`
                        );
                        return `<div class="suggestion-item">${highlightedSuggestion}</div>`;
                    })
                    .join('');
                suggestionsContainer.classList.remove('hidden');
            } else {
                suggestionsContainer.innerHTML = '<div class="suggestion-item">Ничего не найдено</div>';
            }
        } catch (error) {
            console.error('Ошибка при получении подсказок:', error);
            suggestionsContainer.innerHTML = '<div class="suggestion-item error">Ошибка при поиске</div>';
        }
    }, 150);
});

// Обработчик клика по подсказке
suggestionsContainer.addEventListener('click', (e) => {
    if (e.target.classList.contains('suggestion-item')) {
        // Получаем текст без HTML-тегов
        const text = e.target.textContent;
        searchInput.value = text;
        suggestionsContainer.classList.add('hidden');
        searchButton.click(); // Автоматически запускаем поиск
    }
});

// Закрытие подсказок при клике вне поля поиска
document.addEventListener('click', (e) => {
    if (!searchInput.contains(e.target) && !suggestionsContainer.contains(e.target)) {
        suggestionsContainer.classList.add('hidden');
    }
});

// Обработчик фокуса на поле ввода
searchInput.addEventListener('focus', () => {
    const query = searchInput.value.trim();
    if (query.length >= 2) {
        // Повторно запускаем поиск подсказок при фокусе
        const event = new Event('input');
        searchInput.dispatchEvent(event);
    }
});

// Обработчик поиска
searchButton.addEventListener('click', async () => {
    const query = searchInput.value.trim();
    if (!query) return;

    try {
        const response = await fetch(`/api/medicines/search?query=${encodeURIComponent(query)}`, {
            headers: getHeaders()
        });
        const medicines = await response.json();
        displaySearchResults(medicines);
    } catch (error) {
        console.error('Ошибка при поиске:', error);
        searchResults.innerHTML = '<p class="error">Произошла ошибка при поиске. Попробуйте позже.</p>';
    }
});

// Отображение результатов поиска
function displaySearchResults(medicines) {
    searchResults.innerHTML = '';
    medicineDetails.classList.add('hidden');

    if (medicines.length === 0) {
        searchResults.innerHTML = '<p>Лекарство не найдено</p>';
        return;
    }

    medicines.forEach(medicine => {
        const card = document.createElement('div');
        card.className = 'medicine-card';
        card.innerHTML = `
            <h3>${medicine.name}</h3>
            <p>${medicine.shortDescription || ''}</p>
        `;
        card.addEventListener('click', () => showMedicineDetails(medicine));
        searchResults.appendChild(card);
    });

}


// Обработчики модального окна напоминания
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
        const response = await fetch('/api/reminders', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                medicine: {id : currentMedicine.id},
                time: time,
                frequency: frequency,
                userId: tg.initDataUnsafe?.user?.id
            })
        });

        if (response.ok) {
            reminderModal.classList.add('hidden');
            alert('Напоминание успешно добавлено');
        } else {
            throw new Error('Ошибка при сохранении напоминания');
        }
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Произошла ошибка при сохранении напоминания');
    }
});

// Закрытие модального окна при клике вне его
reminderModal.addEventListener('click', (e) => {
    if (e.target === reminderModal) {
        reminderModal.classList.add('hidden');
    }
}); 