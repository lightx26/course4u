import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { registrationSchema } from "../../schemas/registration-schema";
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "../ui/form";
import { Input } from "../ui/input";
import { CourseForm } from "./course-form";
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "../ui/select";

import { useEffect } from "react";
import { RegistrationsProps } from "../user.components/registrations";
import { RegistrationButton } from "../user.components/registration-button";

type RegistrationsFormProps = RegistrationsProps & {
    isEdit: boolean;
    setIsEdit: (isEdit: boolean) => void;
};

export const RegistrationsForm = ({
    id,
    duration,
    durationUnit,
    status,
    course,
    isEdit,
    setIsEdit,
}: RegistrationsFormProps) => {
    const form = useForm<z.infer<typeof registrationSchema>>({
        resolver: zodResolver(registrationSchema),
        mode: "onBlur",
        defaultValues: {
            name: "",
            teacherName: "",
            link: "",
            level: "BEGINNER",
            platform: "",
            category: [],
            duration: 1,
            durationUnit: "DAY",
            thumbnailUrl: "",
        },
    });
    useEffect(() => {
        if (id) {
            form.setValue("duration", duration!);
            form.setValue("durationUnit", durationUnit || "DAY");
            form.setValue("platform", course?.platform || "");
            form.setValue("name", course?.name || "");
            form.setValue("teacherName", course?.teacherName || "");
            form.setValue("link", course?.link || "");
            form.setValue("thumbnailUrl", course?.thumbnailUrl || "");
        }
    }, [course, duration, durationUnit, form, id]);
    function onSubmit(values: z.infer<typeof registrationSchema>) {
        console.log(values);
    }

    return (
        <Form {...form}>
            <form
                onSubmit={form.handleSubmit(onSubmit)}
                className='space-y-8 w-full '
            >
                <CourseForm
                    //eslint-disable-next-line
                    // @ts-ignore
                    form={form}
                    course={course}
                    isEdit={isEdit}
                />
                <div className='flex w-[60%] pr-4 gap-2'>
                    <FormField
                        control={form.control}
                        name='duration'
                        render={({ field }) => (
                            <FormItem className='flex-1'>
                                <FormLabel>
                                    Duration{" "}
                                    <span className='text-red-500'>*</span>
                                </FormLabel>
                                <FormControl>
                                    <Input
                                        type='number'
                                        placeholder='Duration'
                                        {...field}
                                        onChange={(event) =>
                                            field.onChange(+event.target.value)
                                        }
                                        className=''
                                        disabled={!isEdit}
                                    />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name='durationUnit'
                        render={({ field }) => (
                            <FormItem className='w-[100px]'>
                                <FormLabel>Unit</FormLabel>
                                <Select
                                    onValueChange={field.onChange}
                                    defaultValue={field.value}
                                    disabled={!isEdit}
                                >
                                    <FormControl>
                                        <SelectTrigger>
                                            <SelectValue placeholder='Select a level for this course' />
                                        </SelectTrigger>
                                    </FormControl>
                                    <SelectContent>
                                        <SelectItem value='DAY'>
                                            Days
                                        </SelectItem>
                                        <SelectItem value='WEEK'>
                                            Weeks
                                        </SelectItem>
                                        <SelectItem value='MONTH'>
                                            Months
                                        </SelectItem>
                                    </SelectContent>
                                </Select>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                </div>
                <RegistrationButton
                    status={status!}
                    setIsEdit={setIsEdit}
                    isEdit={isEdit}
                />
            </form>
        </Form>
    );
};
