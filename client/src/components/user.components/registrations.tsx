import { useEffect, useState } from "react";
import instance from "../../utils/customizeAxios";
import { RegistrationsForm } from "../form/registration-form";
import { z } from "zod";
import { cn } from "../../utils/utils";
import { courseSchema } from "../../schemas/course-schema";
import { Status } from "../../utils/index";
import { RegistrationUser } from "./registration-user";
import { RegistrationStatus } from "./registration-status";
import { Skeleton } from "../ui/skeleton";
import { userRegistrationSchema } from "../../schemas/user-schema";

export type RegistrationsProps = {
    id?: number;
    duration?: number;
    durationUnit?: "DAY" | "WEEK" | "MONTH";
    status?: Status;
    course?: z.infer<typeof courseSchema>;
    user?: z.infer<typeof userRegistrationSchema>;
};
type Props = {
    id?: number;
    className?: string;
};
const Registrations = ({ className, id }: Props) => {
    const [isEdit, setIsEdit] = useState(true);
    const [registration, setRegistration] = useState<RegistrationsProps | null>(
        null
    );
    const [isLoading, setIsLoading] = useState(false);
    useEffect(() => {
        const getDetailRegistration = async () => {
            if (!id) return;
            setIsLoading(true);
            await instance.get(`/registrations/${id}`).then((res) => {
                console.log(res.data);
                if (res.data.status === "DRAFT" || res.data.status === "NONE") {
                    setIsEdit(true);
                } else {
                    setIsEdit(false);
                }
                setRegistration(res.data);
            });
            setTimeout(() => {
                setIsLoading(false);
            }, 1000);
        };
        getDetailRegistration();
    }, [id]);
    if (isLoading) return <RegistrationSkeleton />;
    return (
        <div
            className={cn(
                "w-[1352px] pt-5 pb-10 px-10 flex flex-col items-center gap-5 rounded-[30px] mx-auto my-8",
                className
            )}
        >
            <h2 className='text-[#1E293B] text-[40px] tracking-tighter leading-8 font-semibold font-inter'>
                Detail of registration
            </h2>
            <div className='w-full flex justify-between items-center'>
                <RegistrationUser
                    fullName={registration?.user?.fullName || ""}
                    avatarUrl={registration?.user?.avatarUrl || ""}
                    email={registration?.user?.email || ""}
                    telephone={registration?.user?.telephone || ""}
                />
                <RegistrationStatus status={registration?.status} />
            </div>
            <RegistrationsForm
                id={+id!}
                duration={id ? registration?.duration : undefined}
                durationUnit={id ? registration?.durationUnit : undefined}
                status={id ? registration?.status : undefined}
                course={id ? registration?.course : undefined}
                isEdit={isEdit}
                setIsEdit={setIsEdit}
            />
        </div>
    );
};

export const RegistrationSkeleton = () => {
    return (
        <div className='w-[1352px] pt-5 px-10 pb-10 flex flex-col items-center gap-5 rounded-[30px] mx-auto my-8'>
            <Skeleton className='skeleton-title w-3/5 h-10 mb-4'></Skeleton>
            <div className='w-full flex justify-between items-center mb-5'>
                <div className='flex gap-3'>
                    <Skeleton className='skeleton-avatar w-[74px] h-[74px] rounded-full'></Skeleton>
                    <div className='flex flex-col'>
                        <Skeleton className='skeleton-text w-32 h-5 mb-2'></Skeleton>
                        <Skeleton className='skeleton-text w-40 h-4 mb-2'></Skeleton>
                        <Skeleton className='skeleton-text w-20 h-4'></Skeleton>
                    </div>
                </div>
                <Skeleton className='skeleton-status w-24 h-10'></Skeleton>
            </div>
            <div className='space-y-4 w-full'>
                <Skeleton className='skeleton-input w-full h-12 mb-4'></Skeleton>
                <Skeleton className='skeleton-input w-full h-12 mb-4'></Skeleton>
                <Skeleton className='skeleton-input w-full h-12 mb-4'></Skeleton>
            </div>
            <div className='flex w-full gap-6'>
                <div className='w-[60%] flex justify-between flex-col'>
                    <div className='flex gap-4'>
                        <Skeleton className='skeleton-select w-[50%] h-12 mb-4'></Skeleton>
                        <Skeleton className='skeleton-select w-[50%] h-12 mb-4'></Skeleton>
                    </div>
                    <Skeleton className='skeleton-input w-full h-12'></Skeleton>
                </div>
                <div className='w-[40%] flex gap-4'>
                    <Skeleton className='skeleton-thumbnail w-full h-[192px] rounded-xl'></Skeleton>
                    <div className='flex flex-col gap-5 w-full'>
                        <Skeleton className='skeleton-text h-16'></Skeleton>
                        <Skeleton className='skeleton-button w-28 h-10'></Skeleton>
                    </div>
                </div>
            </div>
            <Skeleton className='skeleton-select w-full h-12 my-4'></Skeleton>
            <div className='flex w-full justify-end pr-4 gap-2 mt-6'>
                <Skeleton className='skeleton-select w-[100px] h-12'></Skeleton>
                <Skeleton className='skeleton-select w-[100px] h-12'></Skeleton>
                <Skeleton className='skeleton-select w-[100px] h-12'></Skeleton>
            </div>
        </div>
    );
};

export default Registrations;
