import { ReactElement, useState } from "react";
import Cropper, { Point } from "react-easy-crop";
import { Button } from "../ui/button";
import getCroppedImg from "../../utils/cropImage";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "../ui/dialog";
type Props = {
  children?: ReactElement;
  imageUrl: string | null;
  cropInit?: { x: number; y: number } | null;
  zoomInit: number | null;
  aspectInit: { value: number; text: string } | null;
  isOpen: boolean;
  setIsOpen: (isOpen: boolean) => void;
  setCroppedImageFor: (
    crop: { x: number; y: number },
    zoom: number,
    aspect: { value: number; text: string },
    croppedImageUrl: string
  ) => void;
  isOval: boolean;
  isEdit: boolean;
};

export const CropThumbnail = ({
  children,
  imageUrl,
  cropInit,
  zoomInit,
  aspectInit,
  isOpen,
  setIsOpen,
  setCroppedImageFor,
  isOval = false,
  isEdit,
}: Props) => {
  if (zoomInit == null) zoomInit = 1;
  if (cropInit == null) cropInit = { x: 0, y: 0 };
  if (aspectInit == null) aspectInit = { value: 1 / 1, text: "1/1" };
  const [zoom, setZoom] = useState<number>(zoomInit);
  const [crop, setCrop] = useState<Point>(cropInit);
  //@ts-ignore
  const [aspect, setAspect] = useState<{ value: number; text: string }>(
    aspectInit
  );
  const [croppedAreaPixels, setCroppedAreaPixels] = useState(null);
  const onCropChange = (crop: Point) => {
    setCrop(crop);
  };
  const onOpen = () => {
    setIsOpen(!isOpen);
  };
  const onZoomChange = (zoom: number) => {
    setZoom(zoom);
  };

  //@ts-ignore
  const onCropComplete = (croppedArea, croppedAreaPixels) => {
    setCroppedAreaPixels(croppedAreaPixels);
  };
  const onCrop = async () => {
    const croppedImageUrl = await getCroppedImg(imageUrl!, croppedAreaPixels!);
    // @ts-ignore
    setCroppedImageFor(crop, zoom, aspect, croppedImageUrl);
  };
  const onResetImage = async () => {
    setCroppedImageFor(cropInit, zoomInit, aspectInit, imageUrl!);
  };
  return (
    <Dialog open={isOpen} onOpenChange={onOpen}>
      {isEdit ? (
        <DialogTrigger asChild onClick={() => setIsOpen(true)}>
          {children}
        </DialogTrigger>
      ) : (
        <div className="pointer-events-none opacity-60">{children}</div>
      )}

      <DialogContent className="md:max-w-[1000px] h-[500px]">
        <DialogHeader>
          <DialogTitle>Crop Thumbnail</DialogTitle>
          <DialogDescription>
            Crop thumbnail to {aspect.text} aspect ratio
          </DialogDescription>
        </DialogHeader>
        <div className="fixed top-[80px] left-0 right-0 bottom-[80px]">
          <Cropper
            image={imageUrl!}
            crop={crop}
            zoom={zoom}
            objectFit="contain"
            aspect={aspect.value}
            onCropChange={onCropChange}
            onZoomChange={onZoomChange}
            onCropComplete={onCropComplete}
            style={{
              cropAreaStyle: {
                borderRadius: isOval ? "50%" : "",
              },
            }}
          />
        </div>
        <DialogFooter className="fixed -bottom-5 right-4 w-full h-[80px]">
          <Button
            type="button"
            size="sm"
            variant="outline"
            onClick={onResetImage}
          >
            Reset
          </Button>
          <Button type="button" size="sm" onClick={onCrop}>
            Save changes
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};
