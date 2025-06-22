import { getHeaders } from './telegram.js';

export async function fetchSuggestions(query) {
    const response = await fetch(`/api/medicines/suggestions?query=${encodeURIComponent(query)}`, {
        headers: getHeaders()
    });
    return response.json();
}

export async function fetchMedicines(query) {
    const response = await fetch(`/api/medicines/search?query=${encodeURIComponent(query)}`, {
        headers: getHeaders()
    });
    return response.json();
}

export async function createReminder(data) {
    return fetch('/api/reminders', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
}
