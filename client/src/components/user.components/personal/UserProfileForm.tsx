import { useForm } from "react-hook-form"
import { editUserSchema } from "../../../schemas/edit-user-schema.ts"
import { zodResolver } from "@hookform/resolvers/zod"
import { z } from "zod"
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "../../ui/form.tsx"
import { Input } from "../../ui/input.tsx"
import { Popover, PopoverContent, PopoverTrigger } from "../../ui/popover.tsx"
import { format } from "date-fns"
import { CalendarIcon, Upload, XIcon } from "lucide-react"
import { Calendar } from "../../ui/calendar.tsx"
import { Button } from "../../ui/button.tsx"
import { cn } from "../../../utils.ts"
import { CropThumbnail } from "../crop-thumbnail.tsx"
import { useCallback, useEffect, useState } from "react"
import { useDropzone } from "react-dropzone";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../../ui/select.tsx"
import { useSelector } from "react-redux";
import { RootState } from "../../../redux/store/store.ts";

type Avatar = {
    imageUrl: string | undefined;
    croppedImageUrl: string | undefined;
    crop?: { x: number; y: number };
    zoom?: number;
    aspect?: { value: number; text: string };
}

export default function UserProfileForm() {
    const [isEditing, setIsEditing] = useState(false);
    const userInfor = useSelector((state: RootState) => state.user.user);
    const [avatar, setAvatar] = useState<Avatar>({ imageUrl: userInfor.avatarUrl, croppedImageUrl: undefined });
    const [isOpen, setIsOpen] = useState(false);

    const form = useForm<z.infer<typeof editUserSchema>>({
        resolver: zodResolver(editUserSchema),
        defaultValues: {
            name: "",
            username: "",
            avatar: "",
            phoneNumber: "",
            dateOfBirth: undefined,
            email: ""
        },
    })


    useEffect(() => {
        form.setValue("name", userInfor.fullName ?? "")
        form.setValue("username", userInfor.username ?? "")
        form.setValue("phoneNumber", userInfor.telephone ?? "")
        form.setValue("email", userInfor.email ?? "")
        form.setValue("dateOfBirth", userInfor.dateOfBirth ? new Date(userInfor.dateOfBirth) : undefined)
        form.setValue("avatar", userInfor.avatarUrl ?? "")
        setAvatar({ ...avatar, imageUrl: userInfor.avatarUrl })
    }, [userInfor])
    function onSubmit(values: z.infer<typeof editUserSchema>) {
        console.log(values);
        setIsEditing(!isEditing)
    }

    const onDrop = useCallback((acceptedFiles: FileList) => {
        const file = new FileReader();
        file.onload = () => {
            setAvatar({ ...avatar, imageUrl: file.result as string });
            form!.setValue("avatar", file.result as string);
            setIsOpen(true);
        };

        file.readAsDataURL(acceptedFiles[0]);
    }, []);

    //eslint-disable-next-line
    //@ts-expect-error
    const setCroppedImageFor = (crop, zoom, aspect, croppedImageUrl) => {
        const newAvatar = {
            ...avatar,
            crop,
            zoom,
            aspect,
            croppedImageUrl,
        };
        setAvatar(newAvatar);
        form!.setValue("avatar", croppedImageUrl);
        setIsOpen(false);
    };
    //eslint-disable-next-line
    //@ts-ignore
    const { acceptedFiles, getRootProps, getInputProps, isDragActive } =
        useDropzone({
            //@ts-expect-error
            onDrop,
            accept: {
                "image/jpeg": [".jpg", ".jpeg"],
                "image/png": [".png"],
            },
            maxSize: 10 * 1024 * 1024,
        });

    const onDeleteImage = () => {
        setAvatar({ ...avatar, imageUrl: undefined });
        form!.setValue("avatar", "");
    };


    return (
        <div className="flex items-center justify-center w-1/2 p-2 grow">
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="bg-white w-full shadow-sm rounded-3xl space-y-8 p-[20px] flex flex-col items-end">
                    <h3 className="w-full text-xl font-semibold text-left">Account setting</h3>
                    <div className={cn("flex w-full gap-6", { 'pointer-events-none opacity-80': !isEditing })}>
                        <div className="w-[70%] grow justify-between flex flex-col">
                            <FormField
                                control={form.control}
                                name="name"
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>Full name</FormLabel>
                                        <FormControl>
                                            <Input className="w-full" readOnly={!isEditing} placeholder="Enter your full name." {...field} />
                                        </FormControl>
                                        <FormMessage className="text-[12px]" />
                                    </FormItem>
                                )}
                            />
                            <FormField
                                control={form.control}
                                name="username"
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>Username</FormLabel>
                                        <FormControl>
                                            <Input className="border-gray-200 pointer-events-none opacity-80 focus:select-none focus:border focus:outline focus:outline-0" readOnly placeholder="Enter you username" {...field} />
                                        </FormControl>
                                        <FormMessage className="text-[12px]" />
                                    </FormItem>
                                )}
                            />
                            <FormField
                                control={form.control}
                                name="phoneNumber"
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>Phone number</FormLabel>
                                        <FormControl>
                                            <Input readOnly={!isEditing} placeholder="Enter you phone number" {...field} />
                                        </FormControl>
                                        <FormMessage className="text-[12px]" />
                                    </FormItem>
                                )}
                            />
                            <FormField
                                control={form.control}
                                name="email"
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>Email</FormLabel>
                                        <FormControl>
                                            <Input readOnly className="pointer-events-none opacity-80" placeholder="Enter you email" {...field} />
                                        </FormControl>
                                        <FormMessage className="text-[12px]" />
                                    </FormItem>
                                )}
                            />
                        </div>
                        <div className="flex w-[30%]  grow flex-col gap-4">
                            <div>
                                <div className='relative flex justify-center gap-4 text-base'>
                                    {avatar.imageUrl ? (
                                        <CropThumbnail
                                            imageUrl={avatar.imageUrl}
                                            cropInit={avatar.crop!}
                                            zoomInit={avatar.zoom!}
                                            aspectInit={avatar.aspect!}
                                            isOpen={isOpen}
                                            setIsOpen={setIsOpen}
                                            setCroppedImageFor={setCroppedImageFor}
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
                                                        className='block object-cover w-full h-full rounded-[50%] mt-[8px]'
                                                    />
                                                    <div>
                                                        <Button
                                                            className='text-[#861FA2] w-10 h-10 rounded-full absolute bottom-2 right-2 hover:bg-violet-600 hover:text-white bg-violet-100 z-50 p-2'
                                                            disabled={!avatar.imageUrl}
                                                            type='button'
                                                            onClick={onDeleteImage}
                                                            asChild
                                                            aria-readonly={!isEditing}
                                                        >
                                                            <XIcon width={100} height={100} />
                                                        </Button>
                                                    </div>
                                                </div>
                                            </div>
                                        </CropThumbnail>
                                    ) : (
                                        <FormField
                                            control={form!.control}
                                            name='avatar'
                                            render={({ field }) => (
                                                <FormItem>
                                                    <FormLabel>Avatar</FormLabel>
                                                    <FormControl>
                                                        <div
                                                            className='flex flex-col justify-center items-center w-[160px] h-[160px] border-dashed border-[#D9D9D9] hover:border-[#c1e2ff] border-2 rounded-full cursor-pointer group relative'
                                                            {...getRootProps()}
                                                        >
                                                            <Input
                                                                readOnly={!isEditing}
                                                                {...field}
                                                                className='bg-transparent'
                                                                type='file'
                                                                value={undefined}
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
                                                    <FormMessage className="text-[12px]" />
                                                </FormItem>
                                            )}
                                        />
                                    )}
                                </div>
                            </div>
                            <FormField
                                control={form.control}
                                name="dateOfBirth"
                                render={({ field }) => (
                                    <FormItem className="flex flex-col">
                                        <FormLabel>Date of birth</FormLabel>
                                        <Popover>
                                            <PopoverTrigger asChild>
                                                <FormControl>
                                                    <Button
                                                        variant={"outline"}
                                                        className={cn(
                                                            "border border-gray-200 border-solid pl-3 text-left font-normal",
                                                            !field.value && "text-muted-foreground"
                                                        )}
                                                    >
                                                        {field.value ? (
                                                            format(field.value, "PPP")
                                                        ) : (
                                                            <span>Pick a date</span>
                                                        )}
                                                        <CalendarIcon className="w-4 h-4 ml-auto opacity-50" />
                                                    </Button>
                                                </FormControl>
                                            </PopoverTrigger>
                                            <PopoverContent className="w-auto p-0" align="start">
                                                <Calendar
                                                    mode="single"
                                                    //@ts-ignore
                                                    selected={field.value}
                                                    onSelect={field.onChange}
                                                    disabled={(date) =>
                                                        date > new Date() || date < new Date("1900-01-01")
                                                    }
                                                    initialFocus
                                                />
                                            </PopoverContent>
                                        </Popover>
                                        <FormMessage className="text-[12px]" />
                                    </FormItem>
                                )}
                            />

                            <FormField
                                control={form.control}
                                name="gender"
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>Gender</FormLabel>
                                        <Select onValueChange={field.onChange} defaultValue={field.value}>
                                            <FormControl>
                                                <SelectTrigger className="border border-gray-200 border-solid">
                                                    <SelectValue placeholder="Select your gender" />
                                                </SelectTrigger>
                                            </FormControl>
                                            <SelectContent>
                                                <SelectItem value="Male">Male</SelectItem>
                                                <SelectItem value="Female">Female</SelectItem>
                                            </SelectContent>
                                        </Select>
                                        <FormMessage className="text-[12px]" />
                                    </FormItem>
                                )}
                            />
                        </div>
                    </div>
                    {
                        isEditing
                            ? <Button className="text-white min-w-20 bg-purple" type="submit"> Save </Button>
                            : <Button type="button" className="text-white bg-black min-w-20" onClick={(event) => { event.preventDefault(); setIsEditing(true) }}>Edit</Button>
                    }
                </form>
            </Form>
        </div>
    )
}