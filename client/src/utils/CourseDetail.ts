import instance from "./customizeAxios";

export default async function fetchOpenGraphData(url:string) {
    try {
        const response = await instance.get(`/opengraph?url=${encodeURIComponent(url)}`);
        return response.data; 
    } catch (error) {
        console.error("Error fetching Open Graph data:", error);
        return null;
    }
}
