let lastSearchResults = [];

function displaySearchResults(medicines) {
    lastSearchResults = medicines; // Сохраняем
    searchResults.innerHTML = '';
    medicineDetails.classList.add('hidden');
    backToResults.classList.add('hidden');

    medicines.forEach(medicine => {
        const card = document.createElement('div');
        card.className = 'medicine-card';
        card.innerHTML = `<h3>${medicine.name}</h3><p>${medicine.shortDescription}</p>`;
        card.addEventListener('click', () => showMedicineDetails(medicine));
        searchResults.appendChild(card);
    });
}

function showMedicineDetails(medicine) {
    currentMedicine = medicine;
    medicineName.textContent = medicine.name;
    medicineDescription.textContent = medicine.description || 'Описание отсутствует';
    medicineDosage.textContent = medicine.dosage || 'Информация о дозировке отсутствует';
    medicineInteractions.textContent = medicine.interactions || 'Информация о взаимодействии отсутствует';
    medicineDetails.classList.remove('hidden');
    searchResults.classList.add('hidden');
    backToResults.classList.remove('hidden');
}

backToResults.addEventListener('click', () => {
    displaySearchResults(lastSearchResults);
    medicineDetails.classList.add('hidden');
    searchResults.classList.remove('hidden');
    backToResults.classList.add('hidden');
});
