import { useForm } from "react-hook-form";
import { changePasswordSchema } from "../../../schemas/change-password-schema";
import { zodResolver } from "@hookform/resolvers/zod";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "../../ui/form";
import { Button } from "../../ui/button";
import { z } from "zod";
import PasswordInput from "./InputPassword";

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
        console.log(values);
    }

    return (
        <div className="flex items-center h-[500px] justify-center w-1/2 p-[20px] bg-white shadow-sm rounded-3xl grow">
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="flex flex-col items-end justify-between w-full h-full">
                    <h3 className="w-full text-xl font-semibold text-left">Change password</h3>
                    <div className="w-full">
                        <FormField
                            control={form.control}
                            name="oldPassword"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>Old password</FormLabel>
                                    <FormControl>
                                        <PasswordInput className="w-full" placeholder="Enter your old password" {...field} />
                                    </FormControl>
                                    <FormMessage className="text-[12px]" />
                                </FormItem>
                            )}
                        />
                    </div>
                    <div className="w-full">
                        <FormField
                            control={form.control}
                            name="newPassword"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>New password</FormLabel>
                                    <FormControl>
                                        <PasswordInput placeholder="Enter you new password" {...field} />
                                    </FormControl>
                                    <FormMessage className="text-[12px]" />
                                </FormItem>
                            )}
                        />
                    </div>
                    <div className="w-full">
                        <FormField
                            control={form.control}
                            name="confirmPassword"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>Confirm password</FormLabel>
                                    <FormControl>
                                        <PasswordInput placeholder="Confirm your new password" {...field} />
                                    </FormControl>
                                    <FormMessage className="text-[12px]" />
                                </FormItem>
                            )}
                        />
                    </div>
                    <Button className="text-white min-w-20 bg-purple" type="submit">Submit</Button>
                </form>
            </Form>
        </div>
    )
}
