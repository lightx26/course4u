async function blobToFile(blobUrl: string, fileName: string) {
    try {
        const response = await fetch(blobUrl);
        const blob = await response.blob();

        const file = new File([blob], fileName, { type: blob.type });

        return file;
    } catch (error) {
        console.error("Error converting blob to file:", error);
        return null;
    }
}
export default blobToFile;
