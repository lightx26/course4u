export default function handleThumbnailUrl(url?: string) {
  const defaultThumbnailUrl = `${import.meta.env.BASE_URL
    }/course/Default Course thumnail 1.svg`.replace("//", "/");
  try {
    if (!url) {
      return defaultThumbnailUrl;
    }
    if (url.startsWith("http")) {
      return url;
    }
    const prefix = `${import.meta.env.VITE_BACKEND_URL}/thumbnail/`;
    if (url.startsWith(prefix)) {
      const regex = new RegExp(`^(${prefix})+`);
      url = url.replace(regex, '');
    }
    return `${prefix}${url}`;
  } catch (error) {
    return undefined;
  }
}
