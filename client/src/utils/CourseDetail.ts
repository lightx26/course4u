import axios from "axios";
import cheerio from "cheerio";
export default async function fetchOpenGraphData(url: string) {
    try {
        const response = await axios.get(url);
        const html = response.data;
        const $ = cheerio.load(html);

        const ogData: { [key: string]: string } = {};
        $('meta[property^="og:"]').each((_, meta) => {
            const property = $(meta).attr("property");
            const content = $(meta).attr("content");
            if (property && content) {
                const key = property.replace("og:", "");
                ogData[key] = content;
            }
        });

        return ogData;
    } catch (error) {
        console.error("Error fetching Open Graph data:", error);
        return null;
    }
}
