export function createMedicineCard(medicine, onClick) {
    const card = document.createElement('div');
    card.className = 'medicine-card';
    card.innerHTML = `<h3>${medicine.name}</h3><p>${medicine.shortDescription || ''}</p>`;
    card.addEventListener('click', () => onClick(medicine));
    return card;
}

export function displaySuggestions(suggestions, container, query, onSelect) {
    if (suggestions.length > 0) {
        container.innerHTML = suggestions.map(suggestion => {
            const highlighted = suggestion.replace(
                new RegExp(query, 'gi'),
                match => `<span class="highlight">${match}</span>`
            );
            return `<div class="suggestion-item">${highlighted}</div>`;
        }).join('');
    } else {
        container.innerHTML = '<div class="suggestion-item">Ничего не найдено</div>';
    }
    container.classList.remove('hidden');

    container.querySelectorAll('.suggestion-item').forEach(item => {
        item.addEventListener('click', () => {
            onSelect(item.textContent);
        });
    });
}
