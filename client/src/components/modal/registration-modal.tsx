import { Dialog, DialogContent, DialogTitle } from "../ui/dialog";
import Registrations from "../user.components/registrations";
import { useRegistrationModal } from "../../hooks/use-registration-modal";

export const RegistrationModal = () => {
    const { isOpen, close, id } = useRegistrationModal();
    return (
        <Dialog open={isOpen} onOpenChange={close}>
            <DialogContent
                className='max-w-[70%] lg:max-w-[1352px] w-full p-0 rounded-xl min-h-[783px] max-h-[85%] overflow-y-auto'
                onOpenAutoFocus={(e) => e.preventDefault()}
            >
                <DialogTitle></DialogTitle>
                <Registrations className='border-0 w-full' id={id!} />
            </DialogContent>
        </Dialog>
    );
};
