import { removeRegistration } from "../../apiService/Registration.service";
import { Status } from "../../utils/index";
import { Button } from "../ui/button";

type Props = {
    status?: Status;
    setIsEdit: (isEdit: boolean) => void;
    isEdit: boolean;
    id?: number;
};
export const RegistrationButton = ({
    status = Status.NONE,
    setIsEdit,
    isEdit,
    id
}: Props) => {
    const onEdit = () => {
        setIsEdit(true);
    };

    //Delete a registration
    const handleDeleteButtonClick = async () => {
        await removeRegistration(id!);
        setTimeout(() => {
            window.location.reload(), 2000;
        });
    }

    return (
        <div className='flex justify-end gap-4'>
            {(status === Status.DRAFT ||
                status === Status.DISCARDED) && (
                    <Button onClick={handleDeleteButtonClick} type="button" size='lg' variant='danger'>
                        DELETE
                    </Button>
                )}
            {(status === Status.SUBMITTED ||
                status === Status.DECLINED ||
                status === Status.APPROVED) &&
                !isEdit && (
                    <Button size='lg' variant='outline'>
                        DISCARD
                    </Button>
                )}
            {status === Status.APPROVED && (
                <Button size='lg' variant='blue'>
                    START LEARNING
                </Button>
            )}
            {status !== Status.DRAFT && status !== Status.NONE && isEdit && (
                <Button size='lg' variant='outline' type='button'>
                    DISCARD
                </Button>
            )}
            {status === Status.APPROVED && (
                <Button size='lg' variant='success'>
                    DONE
                </Button>
            )}
            {(status === Status.SUBMITTED || status === Status.DECLINED) &&
                isEdit === false && (
                    <Button
                        size='lg'
                        variant='edit'
                        type='button'
                        onClick={onEdit}
                    >
                        EDIT
                    </Button>
                )}
            {(status === Status.NONE || status === Status.DRAFT || isEdit) && (
                <Button type='submit' size='lg' variant='success'>
                    {status === Status.DRAFT || status === Status.NONE
                        ? "SUBMIT"
                        : "RE-SUBMIT"}
                </Button>
            )}
            {(status === Status.DONE || status === Status.VERIFIED) && (
                <Button type='button' size='lg' variant='pink'>
                    SEND FEEDBACK
                </Button>
            )}
            {status === Status.DONE && (
                <Button size='lg' variant='blue'>
                    ASSIGN TO REVIEW
                </Button>
            )}
        </div>
    );
};
