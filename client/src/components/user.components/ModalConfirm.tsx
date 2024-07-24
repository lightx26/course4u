import { Button } from "../ui/button";

import {
    Dialog,
    DialogClose,
    DialogContent,
    DialogDescription,
    DialogTitle,
    DialogTrigger,
} from "../ui/dialog";



type Props = {
    children: React.ReactNode;
    handleConfirm: () => void;
    handleCancel?: () => void;
    title: string;
    description?: string;
    acceptButtonTitle?: string;
    cancelButtonTitle?: string;
};

const ModalConfirm = ({ children, handleCancel, handleConfirm, title, description, acceptButtonTitle = 'Yes', cancelButtonTitle = 'No' }: Props) => {
    return (
        <Dialog>
            <DialogTrigger asChild>{children}</DialogTrigger>
            <DialogContent>
                <DialogTitle className="font-semibold text-center">{title}</DialogTitle>
                <DialogDescription className="text-center text-slate-700">{description}</DialogDescription>
                <div className="flex justify-between gap-2 mt-4">
                    <DialogClose asChild>
                        <Button className="w-32 hover:bg-gray-300 text-[16px]" onClick={handleCancel} type="button" variant="secondary">
                            {cancelButtonTitle}
                        </Button>
                    </DialogClose>
                    <Button
                        type="button"
                        className="w-32 text-[16px] text-white bg-purple h-9"
                        onClick={handleConfirm}
                    >
                        {acceptButtonTitle}
                    </Button>
                </div>
            </DialogContent>
        </Dialog>
    );
};

export default ModalConfirm;
