export default function handleThumbnailUrl(url?: string) {
     try {
          if (!url) {
               return `${import.meta.env.BASE_URL}course/Default Course thumnail 1.svg`
          }
          if (url!.startsWith("http")) {
               return url;
          }
          return `${import.meta.env.VITE_BACKEND_URL}/thumbnail/${url}`;
     } catch (error) {
          return undefined;
     }
}
