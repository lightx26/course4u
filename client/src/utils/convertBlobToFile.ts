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

export async function _blobToFile(blob: Blob, fileName: string) {
    try {
        const file = new File([blob], fileName, { type: blob.type });
        return file;
    } catch (error) {
        console.error("Error converting blob to file:", error);
        return null;
    }
}

export const base64ToBlob = (base64: string): Blob => {
    const byteString = atob(base64.split(",")[1]);
    const mimeString = base64.split(",")[0].split(":")[1].split(";")[0];
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);
    for (let i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
    }
    return new Blob([ab], { type: mimeString });
};

export default blobToFile;
