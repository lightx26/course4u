import {
    AlertDialog,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle,
    AlertDialogTrigger,
} from "../ui/alert-dialog";
import { Button } from "../ui/button";

type Props = {
    children: React.ReactNode;
    handleConfirm: () => void;
    handleCancel?: () => void;
    title: string;
    description?: string;
    acceptButtonTitle?: string;
    cancelButtonTitle?: string;
    isLoading?: boolean;
};

const ModalConfirm = ({
    children,
    handleCancel,
    handleConfirm,
    title,
    description,
    acceptButtonTitle = "Yes",
    cancelButtonTitle = "No",
    isLoading = false,
}: Props) => {
    return (
        <AlertDialog>
            <AlertDialogTrigger asChild>{children}</AlertDialogTrigger>
            <AlertDialogContent>
                <AlertDialogTitle className='font-semibold'>
                    {title}
                </AlertDialogTitle>
                <AlertDialogDescription className=' text-slate-700'>
                    {description}
                </AlertDialogDescription>
                <div className='flex justify-end gap-2'>
                    <AlertDialogCancel asChild>
                        <Button
                            className='hover:bg-gray-300 text-sm'
                            onClick={handleCancel}
                            type='button'
                            variant='secondary'
                            size='sm'
                        >
                            {cancelButtonTitle}
                        </Button>
                    </AlertDialogCancel>
                    <Button
                        type='button'
                        className='text-sm text-white bg-purple h-9'
                        size='sm'
                        onClick={handleConfirm}
                        disabled={isLoading}
                    >
                        {acceptButtonTitle}
                    </Button>
                </div>
            </AlertDialogContent>
        </AlertDialog>
    );
};

export default ModalConfirm;
