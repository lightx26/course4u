import { useForm } from "react-hook-form";
import { feedbackSchema } from "../../../schemas/feedback-schema";
import { zodResolver } from "@hookform/resolvers/zod";
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "../../ui/form";
import { z } from "zod";
import { Textarea } from "../../ui/textarea";
import { RegistrationButtonAdmin } from "./registration-button-admin";
import { useSelector } from "react-redux";
import { RootState } from "../../../redux/store/store";
import { Status } from "../../../utils/index";
import { useRegistrationModal } from "../../../hooks/use-registration-modal";
import { declineRegistration } from "../../../apiService/Registration.service";

type Props = {
    status?: Status;
};
const RegistrationAdminSection = ({ status }: Props) => {
    const user = useSelector((state: RootState) => state.user.user);
    const { id, close } = useRegistrationModal((store) => store);
    const form = useForm<z.infer<typeof feedbackSchema>>({
        resolver: zodResolver(feedbackSchema),
        mode: "onBlur",
        defaultValues: {
            comment: "",
        },
    });
    async function onSubmit(values: z.infer<typeof feedbackSchema>) {
        await declineRegistration(id!, values.comment, +user.id, close);
    }
    return (
        <Form {...form}>
            <form
                onSubmit={form.handleSubmit(onSubmit)}
                className='w-full space-y-8 mt-10'
            >
                {status === "SUBMITTED" && (
                    <FormField
                        control={form.control}
                        name='comment'
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel className='flex items-center'>
                                    <img
                                        src={user.avatarUrl}
                                        alt='avatar'
                                        className='rounded-full mr-2 w-[40px] h-[40px] border-2 border-violet-500 mb-1'
                                    />
                                    {user.fullName}
                                </FormLabel>
                                <FormControl>
                                    <Textarea
                                        placeholder='Write your feedback here!!!'
                                        className='resize-none'
                                        {...field}
                                    />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                )}
                <RegistrationButtonAdmin status={status} />
            </form>
        </Form>
    );
};

export default RegistrationAdminSection;
