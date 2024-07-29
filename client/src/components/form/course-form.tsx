import { useCallback, useEffect, useState } from "react";
import { useDropzone } from "react-dropzone";
import {
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "../ui/form";
import { Input } from "../ui/input";
import MultipleSelector, { Option } from "../ui/multi-selected";
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "../ui/select";
import { Button } from "../ui/button";
import { CropThumbnail } from "../user.components/crop-thumbnail";
import { z } from "zod";
import { UseFormReturn } from "react-hook-form";
import { registrationSchema } from "../../schemas/registration-schema";
import fetchOpenGraphData from "../../utils/CourseDetail";
import instance from "../../utils/customizeAxios";
import { ArrowRightToLine, Upload } from "lucide-react";
import { Skeleton } from "../ui/skeleton";
import { toast } from "sonner";
import { platform } from "../../utils/index";
import { cn } from "../../utils";

const courseSchema = registrationSchema.omit({
    duration: true,
    durationUnit: true,
});

type Props = {
    form: UseFormReturn<z.infer<typeof courseSchema>> | undefined;
    course?: z.infer<typeof courseSchema>;
    isEdit: boolean;
    registrationStatus?: string;
    isBlockedModifiedCourse?: boolean;
};

type Thumbnail = {
    imageUrl: string | null;
    croppedImageUrl: string | null;
    crop?: { x: number; y: number };
    zoom?: number;
    aspect?: { value: number; text: string };
};

const initData: Thumbnail = {
    imageUrl: null,
    croppedImageUrl: null,
    aspect: { value: 4 / 3, text: "4:3" },
};

export const CourseForm = ({
    form,
    course,
    isEdit,
    isBlockedModifiedCourse = false,
}: Props) => {
    const [thumbnail, setThumbnail] = useState<Thumbnail>(initData);
    const [isOpen, setIsOpen] = useState(false);
    const [categories, setCategories] = useState<Option[]>([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (course) {
            setThumbnail({
                imageUrl: course.thumbnailUrl,
                croppedImageUrl: null,
                crop: { x: 0, y: 0 },
                zoom: 1,
                aspect: { value: 4 / 3, text: "4:3" },
            });
            form?.setValue("link", course.link);
            form?.setValue("level", course.level);
            form?.setValue("teacherName", course.teacherName);
            const categoriesData = course?.categories?.map((category) => ({
                label: category.name,
                value: category.name!,
            }));
            form?.setValue("categories", categoriesData || []);
            form?.setValue("platform", course.platform.toUpperCase());
            form?.setValue("name", course.name);
        }
    }, [course]);

    useEffect(() => {
        const getCategories = async () => {
            try {
                const response = await instance.get("/categories/available");
                const categories = response.data
                    .map((category: Option) => ({
                        label: category.name,
                        value: category.id + "",
                    }))
                    .sort((a: { label: string }, b: { label: any }) =>
                        a.label.localeCompare(b.label)
                    );
                setCategories(categories);
            } catch (error) {
                toast.error("Oops something went wrong...", {
                    description: "Please refresh the page and try again!",
                    style: {
                        color: "red",
                        fontWeight: "bold",
                        textAlign: "center",
                    },
                });
            }
        };
        getCategories();
    }, []);

    const onDrop = useCallback(
        (acceptedFiles: File[], rejectedFiles: File[]) => {
            const file = new FileReader();
            if (rejectedFiles.length > 0) {
                toast.error("Unable to process your file request", {
                    description:
                        "The thumbnail size is too large or the format is incorrect. Please edit and upload again.",
                    style: {
                        color: "red",
                        fontWeight: "bold",
                        textAlign: "center",
                    },
                });
            }
            file.onload = () => {
                setThumbnail({ ...thumbnail, imageUrl: file.result as string });
                form!.setValue("thumbnailUrl", file.result as string);
                setIsOpen(true);
            };

            file.readAsDataURL(acceptedFiles[0]);
        },
        [form, thumbnail]
    );

    const { getRootProps, getInputProps, isDragActive } = useDropzone({
        //@ts-ignore
        onDrop,
        accept: {
            "image/jpeg": [".jpg", ".jpeg"],
            "image/png": [".png"],
        },
        maxSize: 10 * 1024 * 1024,
    });

    const onDeleteImage = () => {
        setThumbnail({ ...thumbnail, imageUrl: null, croppedImageUrl: null });
        form!.setValue("thumbnailUrl", "");
    };

    const setCroppedImageFor = (
        crop: { x: number; y: number },
        zoom: number,
        aspect: { value: number; text: string },
        croppedImageUrl: string
    ) => {
        const newThumbnail = {
            ...thumbnail,
            crop,
            zoom,
            aspect,
            croppedImageUrl,
            isOval: false,
        };
        setThumbnail(newThumbnail);
        form!.setValue("thumbnailUrl", croppedImageUrl);
        setIsOpen(false);
    };
    const standardizeUrl = (url: string) => {
        try {
            const urlObj = new URL(url);
            return `${urlObj.protocol}//${urlObj.hostname}${urlObj.pathname}`;
        } catch (error) {
            return url;
        }
    };

    const handleCourseLink = async () => {
        setLoading(true);
        const courseLink = form?.watch("link");
        try {
            const data = await fetchOpenGraphData(courseLink!);
            if (data) {
                if (data.title != null) {
                    form?.setValue("name", data.title);
                    form?.trigger("name");
                }
                if (data.url != null && data.url != "")
                    form?.setValue("link", standardizeUrl(data.url));
                if (data.site_name != null) {
                    form?.setValue("platform", data.site_name.toUpperCase());
                    form?.trigger("platform");
                }
                if (data.image) {
                    setThumbnail({
                        ...thumbnail,
                        imageUrl: data.image,
                        croppedImageUrl: data.image,
                    });
                    form?.setValue("thumbnailUrl", data.image);
                }
            }
        } catch (error) {
            toast.error("Error fetching Open Graph data: ", {
                style: { color: "red" },
                description:
                    "Failed to fetch Open Graph data from the Open Graph",
            });
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return <CourseSkeleton />;
    }
    return (
        <div
            aria-readonly={isBlockedModifiedCourse}
            className={
                isBlockedModifiedCourse || !isEdit
                    ? "select-none cursor-not-allowed"
                    : "" + "flex flex-col gap-6 "
            }
        >
            <div className='space-y-4'>
                <FormField
                    control={form!.control}
                    name='link'
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>
                                Link <span className='text-red-500'>*</span>
                            </FormLabel>
                            <FormControl>
                                <div className='relative w-full'>
                                    <Input
                                        placeholder='Course Link'
                                        {...field}
                                        className='flex-1 w-full'
                                        disabled={
                                            isBlockedModifiedCourse || !isEdit
                                        }
                                    />
                                    <Button
                                        type='button'
                                        onClick={handleCourseLink}
                                        size='sm'
                                        variant='default'
                                        className='absolute right-0 transform -translate-y-1/2 cursor-pointer top-1/2 text-violet-600 bg-violet-200 hover:bg-violet-600 hover:text-white'
                                        disabled={
                                            isBlockedModifiedCourse || !isEdit
                                        }
                                    >
                                        <ArrowRightToLine
                                            width={20}
                                            height={20}
                                        />
                                    </Button>
                                </div>
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                <FormField
                    control={form!.control}
                    name='name'
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>
                                Name <span className='text-red-500'>*</span>
                            </FormLabel>
                            <FormControl>
                                <Input
                                    placeholder='Course Name'
                                    {...field}
                                    className='w-full'
                                    disabled={
                                        isBlockedModifiedCourse || !isEdit
                                    }
                                />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                <FormField
                    control={form!.control}
                    name='teacherName'
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>
                                Teacher Name{""}
                                <span className='text-red-500'>*</span>
                            </FormLabel>
                            <FormControl>
                                <Input
                                    placeholder='Teacher Name'
                                    {...field}
                                    className='w-full'
                                    disabled={
                                        isBlockedModifiedCourse || !isEdit
                                    }
                                />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />
            </div>
            <div className='flex items-stretch gap-6'>
                <div className='w-[60%] gap-6 flex justify-between flex-col'>
                    <div className='flex gap-4'>
                        <FormField
                            control={form!.control}
                            name='platform'
                            render={({ field }) => (
                                <FormItem className='w-[50%]'>
                                    <FormLabel>
                                        Platform{""}
                                        <span className='text-red-500'>*</span>
                                    </FormLabel>
                                    <Select
                                        onValueChange={field.onChange}
                                        value={field.value.toUpperCase()}
                                        disabled={
                                            isBlockedModifiedCourse || !isEdit
                                        }
                                    >
                                        <FormControl>
                                            <SelectTrigger>
                                                <SelectValue placeholder='Select a platform for this course' />
                                            </SelectTrigger>
                                        </FormControl>
                                        <SelectContent>
                                            {platform.map(
                                                (item: {
                                                    value: string;
                                                    content: string;
                                                }) => (
                                                    <SelectItem
                                                        value={item.value}
                                                    >
                                                        {item.content}
                                                    </SelectItem>
                                                )
                                            )}
                                        </SelectContent>
                                    </Select>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form!.control}
                            name='level'
                            render={({ field }) => (
                                <FormItem className='w-[50%]'>
                                    <FormLabel>
                                        Level{""}
                                        <span className='text-red-500'>*</span>
                                    </FormLabel>
                                    <Select
                                        onValueChange={field.onChange}
                                        defaultValue={""}
                                        value={field.value.toUpperCase()}
                                        disabled={
                                            isBlockedModifiedCourse || !isEdit
                                        }
                                    >
                                        <FormControl>
                                            <SelectTrigger className='border-gray-300'>
                                                <SelectValue placeholder='Select a level for this course' />
                                            </SelectTrigger>
                                        </FormControl>
                                        <SelectContent>
                                            <SelectItem value='BEGINNER'>
                                                Beginner
                                            </SelectItem>
                                            <SelectItem value='INTERMEDIATE'>
                                                Intermediate
                                            </SelectItem>
                                            <SelectItem value='ADVANCED'>
                                                Advanced
                                            </SelectItem>
                                        </SelectContent>
                                    </Select>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                    </div>
                    <FormField
                        control={form!.control}
                        name='categories'
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>
                                    Category{""}
                                    <span className='text-red-500'>*</span>
                                </FormLabel>
                                <FormControl>
                                    <MultipleSelector
                                        {...field}
                                        defaultOptions={categories}
                                        options={categories}
                                        value={field.value || []}
                                        placeholder='Select category...'
                                        creatable={true}
                                        disabled={
                                            isBlockedModifiedCourse || !isEdit
                                        }
                                        className={
                                            !isEdit ? "cursor-not-allowed" : ""
                                        }
                                        form={form}
                                    />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                </div>
                <div className='w-[40%] flex gap-4 items-center'>
                    {thumbnail.imageUrl ? (
                        <CropThumbnail
                            imageUrl={thumbnail.imageUrl}
                            cropInit={thumbnail.crop!}
                            zoomInit={thumbnail.zoom!}
                            aspectInit={thumbnail.aspect!}
                            isOpen={isOpen}
                            setIsOpen={setIsOpen}
                            setCroppedImageFor={setCroppedImageFor}
                            isEdit={
                                !isBlockedModifiedCourse &&
                                isEdit &&
                                !thumbnail.imageUrl.startsWith("http")
                            }
                            isOval={false}
                        >
                            <div className='w-[228px] h-[192px] cursor-pointer'>
                                <label className='text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70 h-[19px]'>
                                    Thumbnail
                                </label>
                                <img
                                    src={
                                        thumbnail.croppedImageUrl ||
                                        thumbnail.imageUrl
                                    }
                                    alt='thumbnail'
                                    className='object-cover max-w-[228px] w-[228px] h-[160px] rounded-xl mt-[8px]'
                                />
                            </div>
                        </CropThumbnail>
                    ) : (
                        <FormField
                            control={form!.control}
                            name='thumbnailUrl'
                            render={({ field }) => (
                                <FormItem className='w-full h-full'>
                                    <FormLabel>Thumbnail</FormLabel>
                                    <FormControl>
                                        <div
                                            className={cn(
                                                "flex flex-col justify-center items-center w-[228px] h-[160px] px-[52px] py-[18px] border-dashed border-[#D9D9D9] hover:border-[#c1e2ff] border-2 rounded-xl cursor-pointer group",
                                                !isEdit &&
                                                    "cursor-not-allowed hover:border-[D9D9D9] select-none"
                                            )}
                                            {...(isEdit ? getRootProps() : {})}
                                        >
                                            <Input
                                                {...field}
                                                className='bg-transparent'
                                                type='file'
                                                value={undefined}
                                                disabled={
                                                    isBlockedModifiedCourse ||
                                                    !isEdit
                                                }
                                                {...getInputProps()}
                                            />

                                            <div
                                                className={cn(
                                                    "flex justify-center items-center p-3 border border-black rounded-full w-fit group-hover:border-[#96ceff] group-hover:text-[#4fadff] select-none",
                                                    !isEdit &&
                                                        "group-hover:border-black group-hover:text-black select-none"
                                                )}
                                            >
                                                <Upload
                                                    width={16}
                                                    height={16}
                                                />
                                            </div>
                                            <h4
                                                className={cn(
                                                    "text-base font-semibold group-hover:text-[#4fadff] text-center",
                                                    !isEdit &&
                                                        "select-none group-hover:text-inherit"
                                                )}
                                            >
                                                {isDragActive
                                                    ? "Drop it here..."
                                                    : "Choose a file or drag it here"}
                                            </h4>
                                        </div>
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                    )}
                    <div className='flex flex-col gap-5 select-none'>
                        <p className='text-[#6E7485] text-sm'>
                            Upload your course Thumbnail here. Important
                            <b> guidelines</b>: 1200x800 pixels or 12:8 Ratio.
                            Supported format: <b>.jpg, .jpeg, or .png</b>
                        </p>
                        <Button
                            className='text-white bg-[#861FA2] translate-y-6'
                            disabled={
                                isBlockedModifiedCourse ||
                                !thumbnail.imageUrl ||
                                !isEdit
                            }
                            type='button'
                            onClick={onDeleteImage}
                        >
                            Delete Image
                        </Button>
                    </div>
                </div>
            </div>
        </div>
    );
};

const CourseSkeleton = () => {
    return (
        <div className='max-w-full h-[464px] overflow-hidden bg-white rounded-3xlg'>
            <div className='space-y-3'>
                <div className='h-[20px] bg-gray-200 w-20 rounded'></div>
                <Skeleton className='w-full h-12 mb-4 skeleton-input'></Skeleton>
                <div className='h-[20px] bg-gray-200 w-20 rounded'></div>
                <Skeleton className='w-full h-12 mb-4 skeleton-input'></Skeleton>
                <div className='h-[20px] bg-gray-200 w-20 rounded'></div>
                <Skeleton className='w-full h-12 mb-4 skeleton-input'></Skeleton>
            </div>
            <div className='flex items-stretch gap-6 mt-4'>
                <div className='w-[60%] flex flex-col gap-6'>
                    <div className='flex gap-4'>
                        <div className='flex flex-col w-full gap-4 grow'>
                            <div className='h-[20px] bg-gray-200 w-20 rounded'></div>
                            <Skeleton className='w-full h-12 mb-4 skeleton-select'></Skeleton>
                        </div>
                        <div className='flex flex-col w-full gap-4 grow'>
                            <div className='h-[20px] bg-gray-200 w-20 rounded'></div>
                            <Skeleton className='w-full h-12 mb-4 skeleton-select'></Skeleton>
                        </div>
                    </div>
                    <Skeleton className='w-full h-12 skeleton-input'></Skeleton>
                </div>
                <div className='w-[40%] flex gap-4 items-center'>
                    <Skeleton className='skeleton-thumbnail w-[228px] h-[192px] rounded-xl'></Skeleton>
                    <div className='flex flex-col w-full gap-5'>
                        <Skeleton className='h-16 skeleton-text'></Skeleton>
                        <Skeleton className='h-10 skeleton-button w-28'></Skeleton>
                    </div>
                </div>
            </div>
        </div>
    );
};
