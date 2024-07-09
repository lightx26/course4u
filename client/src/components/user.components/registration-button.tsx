import { Status } from "../../utils/index";
import { Button } from "../ui/button";

type Props = {
    status?: Status;
    setIsEdit: (isEdit: boolean) => void;
    isEdit: boolean;
};
export const RegistrationButton = ({
    status = Status.NONE,
    setIsEdit,
    isEdit,
}: Props) => {
    const onEdit = () => {
        setIsEdit(true);
    };
    return (
        <div className='flex justify-end gap-4'>
            {(status === Status.DRAFT ||
                status === Status.DISCARDED ||
                status === Status.CLOSED) && (
                <Button size='lg' variant='danger'>
                    DELETE
                </Button>
            )}
            {(status === Status.SUBMITTED ||
                status === Status.DECLINED ||
                status === Status.APPROVED) && (
                <Button size='lg' variant='outline'>
                    DISCARD
                </Button>
            )}
            {status === Status.APPROVED && (
                <Button size='lg' variant='blue'>
                    START LEARNING
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
                    SUBMIT
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
