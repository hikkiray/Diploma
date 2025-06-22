export const tg = window.Telegram.WebApp;
tg.ready();

export function getUserId() {
    return tg.initDataUnsafe?.user?.id;
}

export function getHeaders() {
    return {
        'Content-Type': 'application/json',
        'X-Telegram-Init-Data': tg.initData,
        'X-Telegram-User-ID': getUserId()
    };
}
