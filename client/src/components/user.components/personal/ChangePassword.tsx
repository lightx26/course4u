import { useForm } from "react-hook-form";
import { changePasswordSchema } from "../../../schemas/change-password-schema";
import { zodResolver } from "@hookform/resolvers/zod";
import { Form, FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from "../../ui/form";
import { Input } from "../../ui/input";
import { Button } from "../../ui/button";
import { z } from "zod";

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
        <div className="flex items-center w-1/2 p-2 grow">
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="p-[20px] bg-white shadow-sm rounded-3xl flex flex-col gap-8 items-end">
                    <h3 className="w-full text-xl font-semibold text-left">Change password</h3>
                    <div className="w-full">
                        <FormField
                            control={form.control}
                            name="oldPassword"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>Old password</FormLabel>
                                    <FormControl>
                                        <Input className="w-full" placeholder="Enter your old password" {...field} />
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
                                        <Input placeholder="Enter you new password" {...field} />
                                    </FormControl>
                                    <FormDescription className="text-[12px]">
                                        Password must include at least one number, one lowercase letter, one uppercase letter, and one special character
                                    </FormDescription>
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
                                        <Input placeholder="Confirm your new password" {...field} />
                                    </FormControl>
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
