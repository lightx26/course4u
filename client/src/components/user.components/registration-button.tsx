import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
    finishLearning,
    discardRegistration,
    removeRegistration,
    startLearning,
    submitDocument,
    submitWithExistedCourse,
} from "../../apiService/Registration.service";
import { useRegistrationDetail } from "../../hooks/use-registration-detail";
import { useRegistrationModal } from "../../hooks/use-registration-modal";
import { Status } from "../../utils/index";
import { Button } from "../ui/button";
import ModalConfirm from "./ModalConfirm";

import type { UploadFile } from "antd";
import { toast } from "sonner";
import { RootState } from "../../redux/store/store";
import { fetchListOfMyRegistration } from "../../apiService/MyRegistration.service";
import { saveDataListRegistration } from "../../redux/slice/registration.slice";
import { useRefreshState } from "../../hooks/use-refresh-state";
import { isStatusSuccesful } from "../../utils/checkResStatus";
import SendReviewModal from "../modal/send-review-modal";
import { checkExistReview, sendReview } from "../../apiService/courseReview.service";

type Props = {
    status?: Status;
    setIsEdit: (isEdit: boolean) => void;
    isEdit: boolean;
    id?: number;
    isStatrted?: boolean;
    listFileCertificate?: UploadFile[];
    listFilePayment?: UploadFile[];
    isBlockedMofiedCourse?: boolean;
    duration: number,
    durationUnit: string
};
export const RegistrationButton = ({
    status = Status.NONE,
    setIsEdit,
    isEdit,
    id,
    listFileCertificate,
    listFilePayment, isStatrted,
    isBlockedMofiedCourse,
    duration,
    durationUnit,
}: Props) => {
    const { registration, closeRegistration } = useRegistrationDetail();
    const { close } = useRegistrationModal((state) => state);
    const [isLoading, setIsLoading] = useState(false);
    const { setRegistrationFlagAdmin } = useRefreshState((state) => state);
    const [haveReview, setHaveReview] = useState<boolean>(false);

    //state to manange review and send rating
    const [rating, setRating] = useState<number>(0);
    const [reviewContent, setReviewContent] = useState<string>("");

    const closeModal = () => {
        close();
        closeRegistration();
        setRegistrationFlagAdmin();
    };
    const onEdit = () => {
        setIsEdit(true);
    };

    //Delete a registration
    const handleDeleteButtonClick = async () => {
        setIsLoading(true);
        const res = await removeRegistration(id!);
        if (res !== 200) {
            setIsLoading(false);
            return;
        }
        closeModal();
        setIsLoading(false);
    };
    const handleStartLearning = async () => {
        if (registration?.startDate) {
            window.open(registration?.course?.link);
            return;
        }
        setIsLoading(true);
        const res = await startLearning(id!);
        if (res !== 200) {
            setIsLoading(false);
            return;
        }
        window.open(registration?.course?.link);
        closeModal();
        setIsLoading(false);
    };

    const handleFinishLearning = async () => {
        setIsLoading(true);
        await finishLearning(id!);
        closeModal();
        setIsLoading(false)
    }

    const handleDiscard = async () => {
        setIsLoading(true);
        const res = await discardRegistration(id!);
        if (res !== 200) {
            setIsLoading(false);
            return;
        }
        closeModal();
        setIsLoading(false);
    };

    const handleSubmitWithExistedCourse = async () => {
        try {
            const res = await submitWithExistedCourse(
                {
                    courseId: registration!.course!.id!,
                    duration: duration,
                    durationUnit: durationUnit,
                }
            );
            if (isStatusSuccesful(res.status)) {
                toast.success("Create a registration successfully", {
                    style: { color: "green" },
                    description: "The registration was created successfully.",
                });

                closeModal();
            }
        }
        catch (error) {
            toast.error("Failed to Create a registration", {
                style: { color: "red" },
                description: "Opps.., some thing went wrong. Please, reload the page and try again",
            });
        }
    }

    const handleSendReview = async () => {
        const response = await sendReview(rating, reviewContent, registration!.course!.id!);
        if (isStatusSuccesful(response.status)) {
            toast.success("Review sent successfully", {
                style: { color: "green" },
                description: "Your review has been sent successfully.",
            });
            handleCheckExistReview();
        } else {
            toast.error("Failed to send review", {
                style: { color: "red" },
                description: "Failed to send review" + response.statusText,
            });
        }
    }

    //Submit Document
    const currentPage = useSelector(
        (state: RootState) => state.registration.currentPage
    );
    const filterBy = useSelector(
        (state: RootState) => state.registration.status
    );
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
                description:
                    "Your registration has been completed successfully.",
            });
            const result = await fetchListOfMyRegistration(
                currentPage,
                filterBy
            );
            if (result && result.data) {
                dispatch(saveDataListRegistration(result.data));
            }
            closeModal();
        } else {
            toast.error("Document Submission Failed", {
                style: { color: "red" },
                description: response?.data.message
                    ? response.data.message
                    : "There was an error submitting your documents. Please try again later.",
            });
        }
    };
    const handleCheckExistReview = async () => {
        const response = await checkExistReview(registration!.course!.id!);
        if (isStatusSuccesful(response.status)) {
            if (response.data === true)
                setHaveReview(true);
        }
    };
    useEffect(() => {
        if (status === Status.DONE || status === Status.VERIFIED || status === Status.VERIFYING || status === Status.CLOSED || status === Status.DOCUMENT_DECLINED) {
            handleCheckExistReview();
        }
    }, [status]);
    return (
        <div className='flex justify-end gap-4'>
            {(status === Status.DRAFT || status === Status.DISCARDED) && (
                <ModalConfirm
                    title='Delete this registration?'
                    description='Are you sure you want to delete this form? This action cannot be undone.'
                    cancelButtonTitle='Cancel'
                    acceptButtonTitle='Yes, Delete'
                    handleConfirm={handleDeleteButtonClick}
                >
                    <Button
                        type='button'
                        size='lg'
                        variant='danger'
                        disabled={isLoading}
                    >
                        DELETE
                    </Button>
                </ModalConfirm>
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
                        disabled={isLoading}
                    >
                        DISCARD
                    </Button>
                )}
            {status === Status.APPROVED && !registration?.startDate && (
                <ModalConfirm
                    handleConfirm={handleStartLearning}
                    title='Do you want to start learning this course?'
                    description='Start learning cannot undo !!! Are you sure you want to start learning this course?'
                >
                    <Button
                        size='lg'
                        variant='blue'
                        type='button'
                        disabled={isLoading}
                    >
                        START LEARNING
                    </Button>
                </ModalConfirm>
            )}
            {status === Status.APPROVED && registration?.startDate && (
                <Button
                    size='lg'
                    variant='blue'
                    type='button'
                    onClick={handleStartLearning}
                    disabled={isLoading}
                >
                    START LEARNING
                </Button>
            )}
            {status === Status.APPROVED && (
                <ModalConfirm
                    handleConfirm={handleFinishLearning}
                    title='Do you want to finish this course?'
                    description='Are you sure you have completed the course and want to confirm completion?'
                >
                    <Button
                        className={
                            "" +
                            (!isStatrted &&
                                "select-none pointer-events-none opacity-30")
                        }
                        size='lg'
                        variant='success'
                        disabled={isLoading}
                    >
                        DONE
                    </Button>
                </ModalConfirm>
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
                isBlockedMofiedCourse
                    ?
                    <Button type='button' onClick={handleSubmitWithExistedCourse} size='lg' variant='success'>
                        SUBMIT
                    </Button>
                    :
                    <Button type='submit' size='lg' variant='success'>
                        {status === Status.DRAFT || status === Status.NONE
                            ? "SUBMIT"
                            : "RE-SUBMIT"}
                    </Button>
            )}
            {((!haveReview) && (status === Status.DONE || status === Status.VERIFIED || status === Status.VERIFYING || status === Status.DOCUMENT_DECLINED || status === Status.CLOSED)) && (
                <SendReviewModal
                    title="Give your review"
                    rating={rating}
                    setRating={setRating}
                    reviewContent={reviewContent}
                    setReviewContent={setReviewContent}
                    handleConfirm={handleSendReview}>
                    <Button
                        type='button'
                        size='lg'
                        variant='pink'
                        disabled={isLoading}
                    >
                        SEND FEEDBACK
                    </Button>
                </SendReviewModal>
            )}
            {status === Status.DONE && (
                <Button
                    size='lg'
                    variant='blue'
                    disabled={isLoading}
                    type='button'
                    onClick={handleSubmitDocument}
                >
                    ASSIGN TO REVIEW
                </Button>
            )}
        </div>
    );
};
