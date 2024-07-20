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
import { Feedback } from "../feedback-list";
import { useSelector } from "react-redux";
import { RootState } from "../../redux/store/store";
import { useRegistrationDetail } from "../../hooks/use-registration-detail";
import { handleAvatarUrl } from "../../utils/handleAvatarUrl";

export type RegistrationsProps = {
    id?: number;
    duration?: number;
    durationUnit?: "DAY" | "WEEK" | "MONTH";
    status?: Status;
    course?: z.infer<typeof courseSchema>;
    user?: z.infer<typeof userRegistrationSchema>;
    registrationFeedbacks: Feedback[];
    startDate?: string;
    endDate?: string;
};
type Props = {
    id?: number;
    className?: string;
};
const Registrations = ({ className, id }: Props) => {
    const [isEdit, setIsEdit] = useState(true);
    const { registration, setRegistration } = useRegistrationDetail();
    const [isLoading, setIsLoading] = useState(false);
    const user = useSelector((state: RootState) => state.user.user);

  useEffect(() => {
    const getDetailRegistration = async () => {
      if (!id) return;
      setIsLoading(true);
      await instance.get(`/registrations/${id}`).then((res) => {
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
  let avatarPath;
  if (registration && registration.user && registration.user.avatarUrl) {
    avatarPath = handleAvatarUrl(registration.user.avatarUrl);
  } else if (user.role.toUpperCase() === "USER") {
    avatarPath = handleAvatarUrl(user.avatarUrl);
  }

  return (
    <div
      className={cn(
        "w-[1352px] py-5 px-10 flex flex-col items-center gap-5 rounded-[30px] mx-auto my-8",
        className
      )}
    >
      <h2 className="text-[#1E293B] text-[40px] tracking-tighter leading-8 font-semibold font-inter">
        {id ? "Detail of registration" : "Create a registration"}
      </h2>
      <div className="flex items-center justify-between w-full">
        <RegistrationUser
          fullName={registration?.user?.fullName || ""}
          avatarUrl={avatarPath!}
          email={registration?.user?.email || user.email}
          telephone={registration?.user?.telephone || user.telephone}
        />
        <RegistrationStatus status={registration?.status} />
      </div>
      <RegistrationsForm
        id={+id!}
        duration={id ? registration?.duration : undefined}
        durationUnit={id ? registration?.durationUnit : undefined}
        status={id ? registration?.status : undefined}
        course={id ? registration?.course : undefined}
        startDate={id ? registration?.startDate : undefined}
        endDate={id ? registration?.endDate : undefined}
        isEdit={isEdit}
        registrationFeedbacks={registration?.registrationFeedbacks || []}
        setIsEdit={setIsEdit}
      />
    </div>
  );
};

export const RegistrationSkeleton = () => {
    return (
        <div className='w-[1352px] pt-5 px-5 pb-10 flex flex-col items-center gap-5 rounded-[30px] mx-auto my-8'>
            <Skeleton className='w-3/5 h-10 mb-4 skeleton-title'></Skeleton>
            <div className='flex items-center justify-between w-full mb-5'>
                <div className='flex gap-3'>
                    <Skeleton className='skeleton-avatar w-[74px] h-[74px] rounded-full'></Skeleton>
                    <div className='flex flex-col'>
                        <Skeleton className='w-32 h-5 mb-2 skeleton-text'></Skeleton>
                        <Skeleton className='w-40 h-4 mb-2 skeleton-text'></Skeleton>
                        <Skeleton className='w-20 h-4 skeleton-text'></Skeleton>
                    </div>
                </div>
                <Skeleton className='w-24 h-10 skeleton-status'></Skeleton>
            </div>
            <div className='w-full space-y-4'>
                <Skeleton className='w-full h-12 mb-4 skeleton-input'></Skeleton>
                <Skeleton className='w-full h-12 mb-4 skeleton-input'></Skeleton>
                <Skeleton className='w-full h-12 mb-4 skeleton-input'></Skeleton>
            </div>
            <div className='flex w-full gap-6'>
                <div className='w-[60%] flex justify-between flex-col'>
                    <div className='flex gap-4'>
                        <Skeleton className='skeleton-select w-[50%] h-12 mb-4'></Skeleton>
                        <Skeleton className='skeleton-select w-[50%] h-12 mb-4'></Skeleton>
                    </div>
                    <Skeleton className='w-full h-12 skeleton-input'></Skeleton>
                </div>
                <div className='w-[40%] flex gap-4'>
                    <Skeleton className='skeleton-thumbnail w-full h-[192px] rounded-xl'></Skeleton>
                    <div className='flex flex-col w-full gap-5'>
                        <Skeleton className='h-16 skeleton-text'></Skeleton>
                        <Skeleton className='h-10 skeleton-button w-28'></Skeleton>
                    </div>
                </div>
            </div>
            <Skeleton className='w-full h-12 my-4 skeleton-select'></Skeleton>
            <div className='flex justify-end w-full gap-2 pr-4 mt-6'>
                <Skeleton className='skeleton-select w-[100px] h-12'></Skeleton>
                <Skeleton className='skeleton-select w-[100px] h-12'></Skeleton>
                <Skeleton className='skeleton-select w-[100px] h-12'></Skeleton>
            </div>
        </div>
    );
};

export default Registrations;
