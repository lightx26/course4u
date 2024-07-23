import { useDispatch, useSelector } from "react-redux";
import {
    discardRegistration,
    removeRegistration,
    startLearning,
    submitDocument,
} from "../../apiService/Registration.service";
import { useRegistrationDetail } from "../../hooks/use-registration-detail";
import { useRegistrationModal } from "../../hooks/use-registration-modal";
import { Status } from "../../utils/index";
import { Button } from "../ui/button";
import type { UploadFile } from "antd";
import { toast } from "sonner";
import { RootState } from "../../redux/store/store";
import { fetchListOfMyRegistration } from "../../apiService/MyRegistration.service";
import { saveDataListRegistration } from "../../redux/slice/registration.slice";
type Props = {
  status?: Status;
  setIsEdit: (isEdit: boolean) => void;
  isEdit: boolean;
  id?: number;
  listFileCertificate?: UploadFile[];
  listFilePayment?: UploadFile[];
};
export const RegistrationButton = ({
  status = Status.NONE,
  setIsEdit,
  isEdit,
  id,
  listFileCertificate,
  listFilePayment,
}: Props) => {
    const { registration, closeRegistration } = useRegistrationDetail();
    const { close } = useRegistrationModal((state) => state);
    const onEdit = () => {
        setIsEdit(true);
    };

    //Delete a registration
    const handleDeleteButtonClick = async () => {
        const res = await removeRegistration(id!);
        if (res?.status !== 200) {
            return;
        }
        setTimeout(() => {
            closeRegistration(), close(), 1000;
        });
    };
    const handleStartLearning = async () => {
        const res = await startLearning(id!);
        //TODO: Redirect to learning page + return link to redirect
        if (res?.status !== 200) {
            return;
        }
        window.open(registration?.course?.link);
        closeRegistration();
        close();
    };
    
    

    const handleDiscard = async () => {
        const res = await discardRegistration(id!);
        if (res?.status !== 200) {
            return;
        }
        setTimeout(() => {
            closeRegistration(), close(), 1000;
        });
    };

    //Submit Document
    const currentPage = useSelector(
        (state: RootState) => state.registration.currentPage
    );
    const filterBy = useSelector((state: RootState) => state.registration.status);
    const dispatch = useDispatch();
    const handleSubmitDocument = async () => {
        const response = await submitDocument(
            listFileCertificate!,
            listFilePayment!,
            id?.toString()
        );
        if (response && response.status === 200) {
            toast.success("Document Submitted Successfully", {
                style: { color: "green" },
                description: "Your registration has been completed successfully.",
            });
            const result = await fetchListOfMyRegistration(currentPage, filterBy);
            if (result && result.data) {
                dispatch(saveDataListRegistration(result.data));
            }
            close();
        } else {
            toast.error("Document Submission Failed", {
                style: { color: "red" },
                description: response?.data.message
                    ? response.data.message
                    : "There was an error submitting your documents. Please try again later.",
            });
        }
    };
    return (
        <div className='flex justify-end gap-4'>
            {(status === Status.DRAFT || status === Status.DISCARDED) && (
                <Button
                    onClick={handleDeleteButtonClick}
                    type='button'
                    size='lg'
                    variant='danger'
                >
                    DELETE
                </Button>
            )}
            {(status === Status.SUBMITTED ||
                status === Status.DECLINED ||
                status === Status.APPROVED) &&
                !isEdit && (
                    <Button
                        size='lg'
                        variant='outline'
                        type='button'
                        onClick={handleDiscard}
                    >
                        DISCARD
                    </Button>
                )}
            {status === Status.APPROVED && (
                <Button
                    size='lg'
                    variant='blue'
                    type='button'
                    onClick={handleStartLearning}
                >
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
                <Button size='lg' variant='blue' onClick={handleSubmitDocument}
                        type="button">
                    ASSIGN TO REVIEW
                </Button>
            )}
        </div>
    );
};
