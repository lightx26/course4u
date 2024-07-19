import { useRegistrationModal } from "../../../hooks/use-registration-modal";
import { Status } from "../../../utils/index";
import { Button } from "../../ui/button";
import { approveRegistration } from "../../../apiService/Registration.service";

type Props = {
    status?: Status;
};
export const RegistrationButtonAdmin = ({ status = Status.NONE }: Props) => {
    const { close, id } = useRegistrationModal((store) => store);
    // Add the handleApprove function
    const handleApprove = async () => {
        await approveRegistration(id!, close);
    };
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
        </div>
    );
};
