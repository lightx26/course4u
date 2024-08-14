export type Thumbnail = {
    imageUrl: string | null;
    croppedImageUrl: string | null;
    crop?: { x: number; y: number };
    zoom?: number;
    aspect?: { value: number; text: string };
};