import { useDropzone } from "react-dropzone";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useCallback, useEffect, useState } from "react";
import { format } from "date-fns";

import { editUserSchema } from "../../schemas/edit-user-schema.ts";
import { z } from "zod";
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "../ui/form.tsx";
import { Input } from "../ui/input.tsx";
import { Popover, PopoverContent, PopoverTrigger } from "../ui/popover.tsx";
import { CalendarIcon, Upload, XIcon } from "lucide-react";
import { CustomCalendar } from "../ui/calendar.tsx";
import { Button } from "../ui/button.tsx";
import { cn } from "../../utils.ts";
import { CropThumbnail } from "../shared/crop-thumbnail.tsx";
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "../ui/select.tsx";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../redux/store/store.ts";
import { updateUser } from "../../redux/slice/user.slice.ts";
import { toast } from "sonner";
import blobToFile, {
    _blobToFile,
    base64ToBlob,
} from "../../utils/convertBlobToFile.ts";
import { handleEditProfile } from "../../service/user.ts";

type Avatar = {
    imageUrl: string | null | undefined;
    croppedImageUrl: string | null | undefined;
    crop?: { x: number; y: number };
    zoom?: number;
    aspect?: { value: number; text: string };
};

export default function UserProfileForm() {
    const [isEditing, setIsEditing] = useState(false);
    const userInfor = useSelector((state: RootState) => state.user.user);
    const [avatar, setAvatar] = useState<Avatar>({
        imageUrl: userInfor.avatarUrl,
        croppedImageUrl: undefined,
    });
    const [isOpen, setIsOpen] = useState(false);

    const dispatch = useDispatch();

    const form = useForm<z.infer<typeof editUserSchema>>({
        resolver: zodResolver(editUserSchema),
        defaultValues: {
            fullName: "",
            username: "",
            avatarUrl: "",
            telephone: "",
            dateOfBirth: undefined,
            email: "",
            gender: "",
        },
    });

    useEffect(() => {
        form.setValue("fullName", userInfor.fullName ?? "");
        form.setValue("username", userInfor.username ?? "");
        form.setValue("telephone", userInfor.telephone ?? "");
        form.setValue("email", userInfor.email ?? "");
        form.setValue(
            "dateOfBirth",
            userInfor.dateOfBirth ? new Date(userInfor.dateOfBirth) : undefined
        );
        form.setValue("avatarUrl", userInfor.avatarUrl ?? "");
        form.setValue("gender", userInfor.gender ?? "");
        setAvatar({
            ...avatar,
            imageUrl:
                userInfor.avatarUrl.startsWith("http") ||
                userInfor.avatarUrl.startsWith("data:image")
                    ? userInfor.avatarUrl
                    : `${import.meta.env.VITE_BACKEND_URL}${
                          userInfor.avatarUrl
                      }`,
        });
    }, [userInfor]);

    async function onSubmit(values: z.infer<typeof editUserSchema>) {
        const formData = new FormData();
        if (values.fullName) formData.append("fullName", values.fullName);
        if (values.telephone) formData.append("telephone", values.telephone);
        if (values.dateOfBirth) {
            const dateWithTime = new Date(values.dateOfBirth);
            dateWithTime.setHours(12);
            formData.append(
                "dateOfBirth",
                dateWithTime.toISOString().split("T")[0]
            );
        }
        if (values.gender) formData.append("gender", values.gender);
        if (values.avatarUrl && values.avatarUrl.length > 0) {
            let file;
            let blob;
            if (values.avatarUrl.startsWith("data:")) {
                blob = base64ToBlob(values.avatarUrl);
                file = await _blobToFile(blob, "avatar.jpg");
            } else if (values.avatarUrl != userInfor.avatarUrl) {
                file = await blobToFile(values.avatarUrl, "avatar.jpg"); // Await the file
            } else {
                file = await blobToFile(
                    `${import.meta.env.VITE_BACKEND_URL}${values.avatarUrl}`,
                    "avatar.jpg"
                ); // Await the file
            }
            if (file) {
                // Ensure file is not null before appending
                formData.append("imageFile", file);
            }
        }
        const response = await handleEditProfile(formData);
        if (response.status === 200) {
            toast.success("Update user information successfully");
            dispatch(updateUser(response.data));
        } else {
            toast.error("An error occurred while updating user information");
        }
        dispatch(updateUser(response.data));
        setIsEditing(!isEditing);
    }

    const onDrop = useCallback(
        (acceptedFiles: File[]) => {
            if (acceptedFiles.length === 0) {
                toast.error("File size exceeds 3MB", {
                    description:
                        "please select a smaller file & must be jpeg, jpg, png.",
                });
            } else {
                const file = new FileReader();
                file.onload = () => {
                    setAvatar({ ...avatar, imageUrl: file.result as string });
                    form!.setValue("avatarUrl", file.result as string);
                    setIsOpen(true);
                };
                file.readAsDataURL(acceptedFiles[0]);
            }
        },
        [avatar]
    );

    const setCroppedImageFor = (
        crop: { x: number; y: number },
        zoom: number,
        aspect: { value: number; text: string },
        croppedImageUrl: string
    ) => {
        const newAvatar = {
            ...avatar,
            crop,
            zoom,
            aspect,
            croppedImageUrl,
            isOval: true,
        };
        setAvatar(newAvatar);
        form!.setValue("avatarUrl", croppedImageUrl);
        setIsOpen(false);
    };

    const { getRootProps, getInputProps, isDragActive } = useDropzone({
        onDrop,
        accept: {
            "image/jpeg": [".jpg", ".jpeg"],
            "image/png": [".png"],
        },
        maxSize: 3 * 1024 * 1024,
    });

    const onDeleteImage = () => {
        setAvatar({
            ...avatar,
            imageUrl: undefined,
            croppedImageUrl: undefined,
        });
        form!.setValue("avatarUrl", "");
    };

    return (
        <div className='flex items-center h-[500px] justify-center p-2 bg-white shadow-sm rounded-3xl w-[500px] grow select-none'>
            <Form {...form}>
                <form
                    onSubmit={form.handleSubmit(onSubmit)}
                    className='w-full h-full justify-between p-[20px] gap-3 flex flex-col items-end'
                >
                    <h3 className='w-full text-xl font-semibold text-left'>
                        Account setting
                    </h3>
                    <div
                        className={cn("flex w-full gap-6", {
                            "pointer-events-none opacity-80": !isEditing,
                        })}
                    >
                        <div className='w-[70%] grow justify-between flex flex-col'>
                            <FormField
                                control={form.control}
                                name='fullName'
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>Full name</FormLabel>
                                        <FormControl>
                                            <Input
                                                className='w-full'
                                                readOnly={!isEditing}
                                                placeholder='Enter your full name.'
                                                {...field}
                                            />
                                        </FormControl>
                                        <FormMessage className='text-[12px]' />
                                    </FormItem>
                                )}
                            />
                            <FormField
                                control={form.control}
                                name='username'
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>Username</FormLabel>
                                        <FormControl>
                                            <Input
                                                className='border-gray-200 pointer-events-none opacity-80 focus:select-none focus:border focus:outline focus:outline-0'
                                                readOnly
                                                placeholder='Enter you username'
                                                {...field}
                                            />
                                        </FormControl>
                                        <FormMessage className='text-[12px]' />
                                    </FormItem>
                                )}
                            />
                            <FormField
                                control={form.control}
                                name='telephone'
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>Phone number</FormLabel>
                                        <FormControl>
                                            <Input
                                                type='tel'
                                                readOnly={!isEditing}
                                                placeholder='Enter your phone number'
                                                {...field}
                                                onChange={(e) => {
                                                    // Chỉ cho phép nhập số, dấu cách, và dấu gạch nối
                                                    const value =
                                                        e.target.value.replace(
                                                            /[^\d]+/,
                                                            ""
                                                        );
                                                    field.onChange(value); // Cập nhật giá trị vào form
                                                }}
                                            />
                                        </FormControl>
                                        <FormMessage className='text-[12px]' />
                                    </FormItem>
                                )}
                            />
                            <FormField
                                control={form.control}
                                name='email'
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>Email</FormLabel>
                                        <FormControl>
                                            <Input
                                                readOnly
                                                className='pointer-events-none opacity-80'
                                                placeholder='Enter you email'
                                                {...field}
                                            />
                                        </FormControl>
                                        <FormMessage className='text-[12px]' />
                                    </FormItem>
                                )}
                            />
                        </div>
                        <div className='flex w-[30%]  grow flex-col gap-4'>
                            <div>
                                <div className='relative flex justify-center gap-4 text-base'>
                                    {avatar.imageUrl &&
                                    avatar.imageUrl !== "" &&
                                    avatar.imageUrl !=
                                        `${
                                            import.meta.env.VITE_BACKEND_URL
                                        }` ? (
                                        <CropThumbnail
                                            imageUrl={avatar.imageUrl}
                                            cropInit={avatar.crop!}
                                            zoomInit={avatar.zoom!}
                                            aspectInit={avatar.aspect!}
                                            isOpen={isOpen}
                                            setIsOpen={setIsOpen}
                                            setCroppedImageFor={
                                                setCroppedImageFor
                                            }
                                            isOval={true}
                                            isEdit={isEditing}
                                        >
                                            <div className='cursor-pointer'>
                                                <label className='text-base font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70 h-[19px]'>
                                                    Avatar
                                                </label>
                                                <div className='w-[160px] h-[160px] relative cursor-pointer'>
                                                    <img
                                                        src={
                                                            avatar.croppedImageUrl ||
                                                            avatar.imageUrl
                                                        }
                                                        alt='avatar'
                                                        className='block object-cover bg-white w-full h-full rounded-[50%] mt-[8px]'
                                                    />
                                                    <div>
                                                        {isEditing && (
                                                            <Button
                                                                className='text-[#861FA2] w-10 h-10 rounded-full absolute bottom-2 right-2 hover:bg-violet-600 hover:text-white bg-violet-100 z-50 p-2'
                                                                disabled={
                                                                    !avatar.imageUrl
                                                                }
                                                                type='button'
                                                                onClick={
                                                                    onDeleteImage
                                                                }
                                                                asChild
                                                                aria-readonly={
                                                                    !isEditing
                                                                }
                                                            >
                                                                <XIcon
                                                                    width={100}
                                                                    height={100}
                                                                />
                                                            </Button>
                                                        )}
                                                    </div>
                                                </div>
                                            </div>
                                        </CropThumbnail>
                                    ) : (
                                        <FormField
                                            control={form.control}
                                            name='avatarUrl'
                                            render={({ field }) => (
                                                <FormItem>
                                                    <FormLabel>
                                                        Avatar
                                                    </FormLabel>
                                                    <FormControl>
                                                        <div
                                                            className='flex flex-col justify-center items-center w-[160px] h-[160px] border-dashed border-[#D9D9D9] hover:border-[#c1e2ff] border-2 rounded-full cursor-pointer group relative shadow-sm'
                                                            {...getRootProps()}
                                                        >
                                                            <Input
                                                                readOnly={
                                                                    !isEditing
                                                                }
                                                                {...field}
                                                                className='bg-transparent'
                                                                type='file'
                                                                onChange={(
                                                                    event
                                                                ) => {
                                                                    field.onChange(
                                                                        event
                                                                    );
                                                                }}
                                                                value={
                                                                    undefined
                                                                }
                                                                {...getInputProps()}
                                                            />

                                                            <div className='flex justify-center items-center p-3 border border-black rounded-full w-fit group-hover:border-[#96ceff] group-hover:text-[#4fadff]'>
                                                                <Upload
                                                                    width={16}
                                                                    height={16}
                                                                />
                                                            </div>
                                                            <h4 className='text-sm p-3 font-semibold group-hover:text-[#4fadff] text-center'>
                                                                {isDragActive
                                                                    ? "Drop it here..."
                                                                    : "Choose a file or drag it here"}
                                                            </h4>
                                                        </div>
                                                    </FormControl>
                                                </FormItem>
                                            )}
                                        />
                                    )}
                                </div>
                            </div>
                            <FormField
                                control={form.control}
                                name='dateOfBirth'
                                render={({ field }) => (
                                    <FormItem className='flex flex-col'>
                                        <FormLabel>Date of birth</FormLabel>
                                        <Popover>
                                            <PopoverTrigger asChild>
                                                <FormControl>
                                                    <Button
                                                        variant={"outline"}
                                                        className={cn(
                                                            "border border-gray-200 border-solid pl-3 text-left font-normal",
                                                            !field.value &&
                                                                "text-muted-foreground"
                                                        )}
                                                    >
                                                        {field.value ? (
                                                            format(
                                                                field.value,
                                                                "MM/dd/yyyy"
                                                            )
                                                        ) : (
                                                            <span>
                                                                Pick a date
                                                            </span>
                                                        )}
                                                        <CalendarIcon className='w-4 h-4 ml-auto opacity-50' />
                                                    </Button>
                                                </FormControl>
                                            </PopoverTrigger>
                                            <PopoverContent
                                                className='w-auto p-0'
                                                align='start'
                                            >
                                                <CustomCalendar
                                                    mode='single'
                                                    selected={field.value}
                                                    //@ts-ignore
                                                    onSelect={field.onChange}
                                                    fromYear={1901}
                                                    toYear={new Date().getFullYear()}
                                                    disabled={(date) =>
                                                        date > new Date() ||
                                                        date <
                                                            new Date(
                                                                "1900-01-01"
                                                            )
                                                    }
                                                    initialFocus
                                                />
                                            </PopoverContent>
                                        </Popover>
                                        <FormMessage className='text-[12px]' />
                                    </FormItem>
                                )}
                            />

                            <FormField
                                control={form.control}
                                name='gender'
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>Gender</FormLabel>
                                        <Select
                                            value={field.value?.toUpperCase()}
                                            onValueChange={field.onChange}
                                            defaultValue={field.value}
                                        >
                                            <FormControl>
                                                <SelectTrigger className='border border-gray-200 border-solid'>
                                                    <SelectValue placeholder='Gender' />
                                                </SelectTrigger>
                                            </FormControl>
                                            <SelectContent>
                                                <SelectItem value='MALE'>
                                                    Male
                                                </SelectItem>
                                                <SelectItem value='FEMALE'>
                                                    Female
                                                </SelectItem>
                                            </SelectContent>
                                        </Select>
                                        <FormMessage className='text-[12px]' />
                                    </FormItem>
                                )}
                            />
                        </div>
                    </div>
                    {isEditing ? (
                        <Button
                            className='text-white min-w-20 bg-purple'
                            type='submit'
                        >
                            {" "}
                            Save{" "}
                        </Button>
                    ) : (
                        <Button
                            type='button'
                            className='text-white bg-black min-w-20'
                            onClick={(event) => {
                                event.preventDefault();
                                setIsEditing(true);
                            }}
                        >
                            Edit
                        </Button>
                    )}
                </form>
            </Form>
        </div>
    );
}
