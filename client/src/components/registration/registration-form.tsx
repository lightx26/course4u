import { useForm } from "react-hook-form";
import { z } from "zod";
import type { UploadFile } from "antd";
import { useSelector } from "react-redux";
import { zodResolver } from "@hookform/resolvers/zod";
import { useEffect, useState } from "react";

import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "../ui/form";
import { Input } from "../ui/input";
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "../ui/select";

import { RegistrationButton } from "./button/registration-button";
import { RootState } from "@store/store";
import RegistrationAdminSection from "./registration-admin-section";
import FeedbackList from "./feedback/feedback-list";
import LearningProgress from "./learning-progress";
import { useRegistrationModal } from "@hooks/use-registration-modal";
import FormDocument from "./document/document";
import VerifyDocumentForAccountant from "./document/verify-document-for-accoutant";
import { RegistrationButtonForAccountant } from "./button/registration-button-accountant";
import FeedBackFromAccountant from "./feedback/feed-back-from-accountant";
import { getListDocumentByRegistrationId } from "@service/document";
import { isExistAvailableCourseWithId } from "@service/course";
import { DocumentType } from "../../types/document";
import { RegistrationsProps } from "../../types/registration";
import { useRefreshState } from "@hooks/use-refresh-state";
import { convertToFormData } from "@utils/convertToFormData";
import { Status } from "@constant/index";
import { createNewRegistration, editRegistration } from "@service/registration";
import { registrationSchema } from "@schemas/registration-schema";
import { CourseForm } from "@components/course/course-form";

type RegistrationsFormProps = RegistrationsProps & {
    isEdit: boolean;
    setIsEdit: (isEdit: boolean) => void;
    startDate?: string;
    endDate?: string;
};

export const RegistrationsForm = ({
    id,
    duration,
    durationUnit,
    status,
    course,
    isEdit,
    setIsEdit,
    startDate,
    endDate,
    registrationFeedbacks,
    isBlockedModifiedCourse,
}: RegistrationsFormProps) => {
    const form = useForm<z.infer<typeof registrationSchema>>({
        resolver: zodResolver(registrationSchema),
        mode: "onBlur",
        shouldFocusError: false,
        defaultValues: {
            name: "",
            teacherName: "",
            link: "",
            level: "",
            platform: "",
            categories: [],
            duration: 1,
            durationUnit: "DAY",
            thumbnailUrl: "",
        },
    });
    const { setRegistrationFlagAdmin } = useRefreshState((state) => state);
    const { close, id: registrationId } = useRegistrationModal(
        (state) => state
    );
    const user = useSelector((state: RootState) => state.user);
    const [blockEditCourseForm, setBlockEditCourseForm] = useState<boolean>(
        isBlockedModifiedCourse ?? false
    );
    useEffect(() => {
        if (id) {
            form.setValue("duration", duration!);
            form.setValue("durationUnit", durationUnit || "DAY");
        }
    }, [duration, durationUnit, form, id]);

    async function onSubmit(values: z.infer<typeof registrationSchema>) {
        const requestBody = await convertToFormData(values);
        if (
            isEdit &&
            (status === Status.DRAFT ||
                status === Status.DECLINED ||
                status === Status.SUBMITTED)
        ) {
            await editRegistration(
                +id!,
                requestBody!,
                status,
                close,
                setRegistrationFlagAdmin
            );
        } else {
            await createNewRegistration(
                requestBody!,
                close,
                setRegistrationFlagAdmin
            );
        }
    }

    //Submit Document
    const [listFileCertificate, setListFileCertificate] = useState<
        UploadFile[]
    >([]);
    const [listFilePayment, setListFilePayment] = useState<UploadFile[]>([]);

    //Accoutant
    const [documentRegistration, setDocumentRegistration] = useState<
        DocumentType[]
    >([]);

    //Document For User Re-submit
    const [documentRegistrationResubmit, setDocumentRegistrationResubmit] =
        useState<DocumentType[]>([]);
    const [listIdDocumentRemove, setListIdDocumentRemove] = useState<number[]>(
        []
    );

    const [feedBackFromAccountant, setFeedBackFromAccountant] =
        useState<string>("");

    const fetchListDocument = async () => {
        if (id !== undefined) {
            const result = await getListDocumentByRegistrationId(id);
            if (result && result.data) {
                setDocumentRegistrationResubmit(result.data);
                setDocumentRegistration(result.data);
            }
        }
    };
    useEffect(() => {
        fetchListDocument();
    }, [id]);

    useEffect(() => {
        const checkExist = async () => {
            const res = await isExistAvailableCourseWithId(registrationId!);
            if (res.data && status === Status.DRAFT)
                setBlockEditCourseForm(true);
        };
        status === Status.DRAFT && checkExist();
    }, [registrationId, status]);
    return (
        <div className='flex flex-col w-full'>
            <Form {...form}>
                <form
                    onSubmit={form.handleSubmit(onSubmit)}
                    className='w-full space-y-8 '
                    onKeyDown={(event) => {
                        if (
                            event.key === "Enter" &&
                            !(event.target instanceof HTMLTextAreaElement)
                        ) {
                            event.preventDefault();
                        }
                    }}
                >
                    <CourseForm
                        form={form}
                        course={course}
                        isEdit={isEdit}
                        registrationStatus={status}
                        blockEditCourseForm={blockEditCourseForm}
                    />
                    <div className='flex w-[60%] pr-4 gap-2'>
                        <FormField
                            control={form.control}
                            name='duration'
                            render={({ field }) => (
                                <FormItem className='flex-1'>
                                    <FormLabel>
                                        Duration{" "}
                                        <span className='text-red-500'>*</span>
                                    </FormLabel>
                                    <FormControl>
                                        <Input
                                            type='text'
                                            placeholder='Duration'
                                            {...field}
                                            onChange={(event) => {
                                                const value =
                                                    event.target.value;
                                                if (
                                                    (value.length == 0 ||
                                                        /^[0-9]\d*$/.test(
                                                            value
                                                        )) &&
                                                    value.length < 10
                                                ) {
                                                    field.onChange(
                                                        +value.replace(
                                                            /^0+/,
                                                            ""
                                                        )
                                                    );
                                                }
                                            }}
                                            defaultValue={1}
                                            disabled={!isEdit}
                                        />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name='durationUnit'
                            render={({ field }) => (
                                <FormItem className='w-[100px] mt-8'>
                                    <Select
                                        onValueChange={field.onChange}
                                        defaultValue={
                                            durationUnit ||
                                            form.watch("durationUnit")
                                        }
                                        disabled={!isEdit}
                                    >
                                        <FormControl>
                                            <SelectTrigger>
                                                <SelectValue placeholder='Select a level for this course' />
                                            </SelectTrigger>
                                        </FormControl>
                                        <SelectContent>
                                            <SelectItem value='DAY'>
                                                Day
                                            </SelectItem>
                                            <SelectItem value='WEEK'>
                                                Week
                                            </SelectItem>
                                            <SelectItem value='MONTH'>
                                                Month
                                            </SelectItem>
                                        </SelectContent>
                                    </Select>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                    </div>
                    {startDate && (
                        <LearningProgress
                            startDate={startDate}
                            endDate={endDate || ""}
                        />
                    )}

                    {/* View Document and Feedback for User */}
                    {user.user?.role === "USER" && (
                        <div className='space-y-5'>
                            {status === "DONE" && (
                                <>
                                    <FormDocument
                                        listFileCertificate={
                                            listFileCertificate
                                        }
                                        setListFileCertificate={
                                            setListFileCertificate
                                        }
                                        listFilePayment={listFilePayment}
                                        setListFilePayment={setListFilePayment}
                                    />
                                    {registrationFeedbacks &&
                                        registrationFeedbacks.length > 0 && (
                                            <FeedbackList
                                                feedbacks={
                                                    registrationFeedbacks
                                                }
                                            />
                                        )}
                                    <RegistrationButton
                                        status={status!}
                                        setIsEdit={setIsEdit}
                                        isEdit={isEdit}
                                        id={id}
                                        isStatrted={startDate != undefined}
                                        listFileCertificate={
                                            listFileCertificate
                                        }
                                        listFilePayment={listFilePayment}
                                        blockEditCourseForm={
                                            blockEditCourseForm
                                        }
                                        setBlockEditCourseForm={
                                            setBlockEditCourseForm
                                        }
                                        form={form}
                                    />
                                </>
                            )}

                            {status !== "DONE" &&
                                status !== "DOCUMENT_DECLINED" &&
                                status !== "VERIFIED" &&
                                status !== "CLOSED" &&
                                status !== "VERIFYING" && (
                                    <>
                                        {registrationFeedbacks &&
                                            registrationFeedbacks.length >
                                                0 && (
                                                <FeedbackList
                                                    feedbacks={
                                                        registrationFeedbacks
                                                    }
                                                />
                                            )}
                                        <RegistrationButton
                                            status={status!}
                                            setIsEdit={setIsEdit}
                                            isEdit={isEdit}
                                            id={id}
                                            isStatrted={startDate != undefined}
                                            listFileCertificate={
                                                listFileCertificate
                                            }
                                            listFilePayment={listFilePayment}
                                            blockEditCourseForm={
                                                blockEditCourseForm
                                            }
                                            setBlockEditCourseForm={
                                                setBlockEditCourseForm
                                            }
                                            form={form}
                                        />
                                    </>
                                )}
                            {(status === "VERIFIED" ||
                                status === "CLOSED" ||
                                status === "VERIFYING") && (
                                <>
                                    <VerifyDocumentForAccountant
                                        documentRegistration={
                                            documentRegistration
                                        }
                                        setDocumentRegistration={
                                            setDocumentRegistration
                                        }
                                        status={status}
                                    />
                                    {registrationFeedbacks &&
                                        registrationFeedbacks.length > 0 && (
                                            <FeedbackList
                                                feedbacks={
                                                    registrationFeedbacks
                                                }
                                            />
                                        )}
                                </>
                            )}
                        </div>
                    )}
                    {user.user?.role === "USER" && (
                        <div className='space-y-5'>
                            {status === "DOCUMENT_DECLINED" && (
                                <>
                                    <FormDocument
                                        listFileCertificate={
                                            listFileCertificate
                                        }
                                        setListFileCertificate={
                                            setListFileCertificate
                                        }
                                        listFilePayment={listFilePayment}
                                        setListFilePayment={setListFilePayment}
                                        documentRegistrationResubmit={
                                            documentRegistrationResubmit
                                        }
                                        setDocumentRegistrationResubmit={
                                            setDocumentRegistrationResubmit
                                        }
                                        setListIdDocumentRemove={
                                            setListIdDocumentRemove
                                        }
                                    />
                                    {registrationFeedbacks &&
                                        registrationFeedbacks.length > 0 && (
                                            <FeedbackList
                                                feedbacks={
                                                    registrationFeedbacks
                                                }
                                            />
                                        )}

                                    <RegistrationButton
                                        status={status!}
                                        setIsEdit={setIsEdit}
                                        isEdit={isEdit}
                                        id={id}
                                        isStatrted={startDate != undefined}
                                        listFileCertificate={
                                            listFileCertificate
                                        }
                                        listFilePayment={listFilePayment}
                                        listIdDocumentRemove={
                                            listIdDocumentRemove
                                        }
                                        blockEditCourseForm={
                                            blockEditCourseForm
                                        }
                                        setBlockEditCourseForm={
                                            setBlockEditCourseForm
                                        }
                                        form={form}
                                    />
                                </>
                            )}
                        </div>
                    )}

                    {user.user?.role === "USER" && status === "VERIFIED" && (
                        <div className='space-y-5'>
                            <RegistrationButton
                                status={status!}
                                setIsEdit={setIsEdit}
                                isEdit={isEdit}
                                setBlockEditCourseForm={setBlockEditCourseForm}
                                form={form}
                            />
                        </div>
                    )}

                    {/* View Document and Feedback for Accounant */}
                    {user.user?.role === "ACCOUNTANT" && (
                        <div className='space-y-5'>
                            {status === "VERIFYING" && (
                                <>
                                    <VerifyDocumentForAccountant
                                        documentRegistration={
                                            documentRegistration
                                        }
                                        setDocumentRegistration={
                                            setDocumentRegistration
                                        }
                                        status={status}
                                    />
                                    {registrationFeedbacks &&
                                        registrationFeedbacks.length > 0 && (
                                            <FeedbackList
                                                feedbacks={
                                                    registrationFeedbacks
                                                }
                                            />
                                        )}
                                    {registrationFeedbacks &&
                                        registrationFeedbacks.length === 0 && (
                                            <h4 className='mb-5 text-xl font-semibold'>
                                                Feedback
                                            </h4>
                                        )}
                                    <FeedBackFromAccountant
                                        setFeedBackFromAccountant={
                                            setFeedBackFromAccountant
                                        }
                                    />
                                    <RegistrationButtonForAccountant
                                        status={status!}
                                        id={id}
                                        feedBackFromAccountant={
                                            feedBackFromAccountant
                                        }
                                        document={documentRegistration}
                                    />
                                </>
                            )}
                            {(status === "VERIFIED" ||
                                status === "CLOSED" ||
                                status === "DOCUMENT_DECLINED") && (
                                <>
                                    <VerifyDocumentForAccountant
                                        documentRegistration={
                                            documentRegistration
                                        }
                                        setDocumentRegistration={
                                            setDocumentRegistration
                                        }
                                        status={status}
                                    />
                                    {registrationFeedbacks &&
                                        registrationFeedbacks.length > 0 && (
                                            <FeedbackList
                                                feedbacks={
                                                    registrationFeedbacks
                                                }
                                            />
                                        )}
                                </>
                            )}
                        </div>
                    )}
                </form>
            </Form>
            {user.user?.role === "ADMIN" && (
                <div className='space-y-5'>
                    <VerifyDocumentForAccountant
                        documentRegistration={documentRegistration}
                        setDocumentRegistration={setDocumentRegistration}
                        status={status}
                    />

                    {registrationFeedbacks &&
                        registrationFeedbacks?.length > 0 && (
                            <FeedbackList feedbacks={registrationFeedbacks!} />
                        )}
                    <RegistrationAdminSection status={status} />
                </div>
            )}
        </div>
    );
};
