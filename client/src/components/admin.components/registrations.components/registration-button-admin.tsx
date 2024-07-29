import { useRegistrationModal } from "../../../hooks/use-registration-modal";
import { Status } from "../../../utils/index";
import {
    approveRegistration,
    closeRegistration,
} from "../../../apiService/Registration.service";
import { useRefreshState } from "../../../hooks/use-refresh-state";
import { Button } from "../../ui/button";
import { useState } from "react";

type Props = {
    status?: Status;
};
export const RegistrationButtonAdmin = ({ status = Status.NONE }: Props) => {
    const { close, id } = useRegistrationModal((store) => store);
    const { setRegistrationFlagAdmin } = useRefreshState((state) => state);
    const [isLoading, setIsLoading] = useState(false);
    const closeModal = () => {
        close();
        setRegistrationFlagAdmin();
    };
    // Add the handleApprove function
    const handleApprove = async () => {
        setIsLoading(true);
        await approveRegistration(id!, close);

        closeModal();
        setIsLoading(false);
    };

    const handleClose = async () => {
        setIsLoading(true);
        await closeRegistration(id!, "", close);

        closeModal();
        setIsLoading(false);
    };

    return (
        <div className='flex justify-end gap-4'>
            {status === Status.SUBMITTED && (
                <Button variant='danger'>Decline and send feedback</Button>
            )}
            {status === Status.SUBMITTED && (
                <Button
                    variant='success'
                    type='button'
                    onClick={handleApprove}
                    disabled={isLoading}
                >
                    Approve
                </Button>
            )}
            {status === Status.VERIFIED && (
                <Button
                    variant='danger'
                    onClick={handleClose}
                    disabled={isLoading}
                >
                    Close
                </Button>
            )}
            {(status === Status.DONE ||
                status === Status.VERIFYING ||
                status === Status.DOCUMENT_DECLINED) && (
                <Button variant='danger'>Close and send feedback</Button>
            )}
        </div>
    );
};
