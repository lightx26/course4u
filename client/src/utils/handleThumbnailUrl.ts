export default function handleThumbnailUrl(url?:string) {
    try {
       if (url == undefined || url == "" ){
            return "/course/Default Course thumnail 1.svg"
       } 
       if (url.startsWith("http")){
            return url;
       }
         return `${import.meta.env.VITE_BACKEND_URL}/thumbnail/${url}`;
    } catch (error) {
        return undefined;
    }
}
