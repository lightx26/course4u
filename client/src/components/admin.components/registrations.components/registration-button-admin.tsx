import { useRegistrationModal } from "../../../hooks/use-registration-modal";
import { Status } from "../../../utils/index";
import { Button } from "../../ui/button";
import { approveRegistration, closeRegistration } from "../../../apiService/Registration.service";

type Props = {
    status?: Status;
};
export const RegistrationButtonAdmin = ({ status = Status.NONE }: Props) => {
    const { close, id } = useRegistrationModal((store) => store);
    // Add the handleApprove function
    const handleApprove = async () => {
        await approveRegistration(id!, close);
    };

    const handleClose = async() => {
        await closeRegistration(id!, "", close);
    }

    return (
        <div className='flex justify-end gap-4'>
            {status === Status.SUBMITTED && (
                <Button variant='danger'>Decline and send feedback</Button>
            )}
            {status === Status.SUBMITTED && (
                <Button variant='success' type='button' onClick={handleApprove}>
                    Approve
                </Button>
            )}
            {status === Status.VERIFIED && (
                <Button variant='danger' onClick={handleClose}>Close</Button>
            )}
            {(status === Status.DONE || status === Status.VERIFYING || status === Status.DOCUMENT_DECLINED) && (
                <Button variant='danger'>Send feedback and close</Button>
            )}
        </div>
    );
};
