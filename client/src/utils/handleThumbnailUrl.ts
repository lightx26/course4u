import { defaultThumbnailUrl } from "../constant/index";

export default function handleThumbnailUrl(url?: string) {
    try {
        if (!url || url === defaultThumbnailUrl) {
            return defaultThumbnailUrl;
        }
        if (url.startsWith("http")) {
            return url;
        }
        const prefix = `${import.meta.env.VITE_BACKEND_URL}/thumbnail/`;
        if (url.startsWith(prefix)) {
            const regex = new RegExp(`^(${prefix})+`);
            url = url.replace(regex, "");
        }
        return `${prefix}${url}`.replace("//", "/");
    } catch (error) {
        return undefined;
    }
}
