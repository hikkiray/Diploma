import { tg } from './telegram.js';
import { updateTheme } from './theme.js';
import { fetchSuggestions, fetchMedicines } from './api.js';
import { createMedicineCard, displaySuggestions } from './ui.js';
import { initReminders } from './reminder.js';

updateTheme();
tg.onEvent('themeChanged', updateTheme);

const searchInput = document.getElementById('searchInput');
const searchButton = document.getElementById('searchButton');
const searchResults = document.getElementById('searchResults');
const suggestionsContainer = document.getElementById('suggestions');
const medicineDetails = document.getElementById('medicineDetails');

let currentMedicine = {};
let lastSearchResults = [];

initReminders({ medicine: currentMedicine });

searchInput.addEventListener('input', async () => {
    const query = searchInput.value.trim();
    if (query.length < 2) {
        suggestionsContainer.classList.add('hidden');
        return;
    }

    suggestionsContainer.innerHTML = '<div class="suggestion-loading">Поиск...</div>';
    const suggestions = await fetchSuggestions(query);
    displaySuggestions(suggestions, suggestionsContainer, query, (text) => {
        searchInput.value = text;
        suggestionsContainer.classList.add('hidden');
        searchButton.click();
    });
});

searchButton.addEventListener('click', async () => {
    const query = searchInput.value.trim();
    const medicines = await fetchMedicines(query);
    lastSearchResults = medicines;
    searchResults.innerHTML = '';
    medicineDetails.classList.add('hidden');

    medicines.forEach(medicine => {
        const card = createMedicineCard(medicine, showMedicineDetails);
        searchResults.appendChild(card);
    });
});

function showMedicineDetails(medicine) {
    currentMedicine.medicine = medicine;
    document.getElementById('medicineName').textContent = medicine.name;
    document.getElementById('medicineDescription').textContent = medicine.description || 'Описание отсутствует';
    document.getElementById('medicineDosage').textContent = medicine.dosage || 'Информация о дозировке отсутствует';
    document.getElementById('medicineInteractions').textContent = medicine.interactions || 'Информация о взаимодействии отсутствует';

    searchResults.innerHTML = '';
    medicineDetails.classList.remove('hidden');
}
