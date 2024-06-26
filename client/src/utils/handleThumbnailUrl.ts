export default function handleThumbnailUrl(url?:string) {
    try {
       if (url == undefined || url == "" ){
            return "/course/default_course_thumbnail.png"
       } 
       if (url.startsWith("http")){
            return url;
       }
         return `${import.meta.env.VITE_BACKEND_URL}/thumbnail/${url}`;
    } catch (error) {
        return undefined;
    }
}
