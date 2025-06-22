import { tg } from './telegram.js';

export function updateTheme() {
    const theme = tg.themeParams;
    document.documentElement.style.setProperty('--primary-color', theme.button_color || '#2AABEE');
    document.documentElement.style.setProperty('--secondary-color', theme.button_color || '#229ED9');
    document.documentElement.style.setProperty('--background-color', theme.bg_color || '#ffffff');
    document.documentElement.style.setProperty('--text-color', theme.text_color || '#333333');
    document.documentElement.style.setProperty('--border-color', theme.hint_color || '#e0e0e0');
}
