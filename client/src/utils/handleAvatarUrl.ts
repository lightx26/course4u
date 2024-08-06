export function handleAvatarUrl(url: string | null | undefined): string {
    if (!url) {
        return `${import.meta.env.BASE_URL}/avatar/default-avatar.svg`.replace(
            "//",
            "/"
        );
    } else {
        return url.startsWith("http") || url.startsWith("data")
            ? url
            : `${import.meta.env.VITE_BACKEND_URL}${url}`;
    }
}
