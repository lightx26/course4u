import {
    Dialog,
    DialogContent,
    DialogTitle,
    DialogTrigger,
} from "../ui/dialog";
import Registrations from "../user.components/registrations";

type Props = {
    children: React.ReactNode;
};
export const RegistrationModal = ({ children }: Props) => {
    return (
        <Dialog>
            <DialogTrigger asChild>{children}</DialogTrigger>
            <DialogContent className='max-w-[1500px] w-full p-0 rounded-xl'>
                <DialogTitle></DialogTitle>
                <Registrations className='border-0 w-full' />
            </DialogContent>
        </Dialog>
    );
};
