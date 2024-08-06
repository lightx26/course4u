import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "sonner";

import { changePasswordSchema } from "../../schemas/change-password-schema";
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "../ui/form";
import { Button } from "../ui/button";
import PasswordInput from "../shared/input-password";

import { handleChangePassword } from "../../service/user";

export default function ChangePassword() {
    const form = useForm<z.infer<typeof changePasswordSchema>>({
        resolver: zodResolver(changePasswordSchema),
        defaultValues: {
            oldPassword: "",
            newPassword: "",
            confirmPassword: "",
        },
    });

    function onSubmit(values: z.infer<typeof changePasswordSchema>) {
        handleChangePassword(values.oldPassword, values.newPassword)
            .then((status: number | undefined) => {
                if (status === 200) {
                    toast.success("Change password successfully!", {
                        style: {
                            color: "green",
                        },
                    });
                    form.reset();
                } else if (status === 406) {
                    // HTTP 406 Not Acceptable
                    toast.error(
                        "New password cannot be the same as the old password",
                        {
                            style: {
                                color: "red",
                            },
                        }
                    );
                } else if (status === 400) {
                    // HTTP 400 Bad Request
                    toast.error("Old password is incorrect!!!", {
                        style: {
                            color: "red",
                        },
                    });
                    form.setError("oldPassword", {
                        type: "manual",
                        message: "Old password is incorrect!",
                    });
                } else {
                    toast.error("An unexpected error occurred.", {
                        style: {
                            color: "red",
                        },
                    });
                }
            })
            //@ts-ignore
            .catch((error) => {
                toast.error(
                    `Error occurred while changing password ${error.status}`,
                    {
                        style: {
                            color: "red",
                        },
                    }
                );
            });
    }

    return (
        <div className='flex items-center h-[500px] w-[500px] justify-center p-[20px] bg-white shadow-sm rounded-3xl grow'>
            <Form {...form}>
                <form
                    onSubmit={form.handleSubmit(onSubmit)}
                    className='flex flex-col items-end justify-between w-full h-full select-none'
                >
                    <h3 className='w-full text-xl font-semibold text-left'>
                        Change password
                    </h3>
                    {/* Visually Hidden Username Field */}
                    <div className='w-full'>
                        <FormField
                            control={form.control}
                            name='oldPassword'
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>Old password</FormLabel>
                                    <FormControl>
                                        <PasswordInput
                                            placeholder='Enter your old password'
                                            {...field}
                                            name='old-password'
                                        />
                                    </FormControl>
                                    <FormMessage className='text-[12px]' />
                                </FormItem>
                            )}
                        />
                    </div>
                    <div className='w-full'>
                        <FormField
                            control={form.control}
                            name='newPassword'
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>New password</FormLabel>
                                    <FormControl>
                                        <PasswordInput
                                            placeholder='Enter your new password'
                                            {...field}
                                            name='new-password'
                                        />
                                    </FormControl>
                                    <FormMessage className='text-[12px]' />
                                </FormItem>
                            )}
                        />
                    </div>
                    <div className='w-full'>
                        <FormField
                            control={form.control}
                            name='confirmPassword'
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>Confirm password</FormLabel>
                                    <FormControl>
                                        <PasswordInput
                                            placeholder='Confirm your new password'
                                            {...field}
                                            name='confirm-password'
                                        />
                                    </FormControl>
                                    <FormMessage className='text-[12px]' />
                                </FormItem>
                            )}
                        />
                    </div>
                    <Button
                        className='text-white min-w-20 bg-purple'
                        type='submit'
                    >
                        Submit
                    </Button>
                </form>
            </Form>
        </div>
    );
}
