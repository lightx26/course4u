import { OverviewMyRegistrationType } from "./MyRegistrationsPage";
import { Skeleton } from "../../ui/skeleton";
import React from "react";
import { useRegistrationModal } from "../../../hooks/use-registration-modal.ts";
import handleThumbnailUrl from "../../../utils/handleThumbnailUrl.ts";
import { cn } from "../../../utils.ts";

type PropsType = {
    registration: OverviewMyRegistrationType;
};

export default function MyRegistrationComponents({ registration }: PropsType) {
    const { open } = useRegistrationModal((state) => state);
    const styles = {
        base: {
            paddingBottom: "1.25rem",
            marginBottom: "0.75rem",
            width: "100%",
            overflow: "hidden",
            fontSize: "0.875rem",
            lineHeight: "1.5",
            transition: "box-shadow 0.25s ease",
            backgroundColor: "white",
            borderRadius: "0.375rem",
            boxShadow: "0 2px 4px rgba(0, 0, 0, 0.1)",
            cursor: "pointer",
        },
        hover: {
            backgroundColor: "#f0f0f0",
            boxShadow: "0 4px 8px rgba(0, 0, 0, 0.2)",
        },
    };

    const [hover, setHover] = React.useState(false);
    const startDate =
        typeof registration.startDate === "string"
            ? new Date(registration.startDate)
            : registration.startDate;
    const endDate = registration.endDate
        ? typeof registration.endDate === "string"
            ? new Date(registration.endDate)
            : registration.endDate
        : null;
    const thumbnailUrl = handleThumbnailUrl(registration.courseThumbnailUrl);
    return (
        <>
            <div
                style={
                    hover ? { ...styles.base, ...styles.hover } : styles.base
                }
                onClick={() => open(true, +registration.id!)}
                onMouseEnter={() => setHover(true)}
                onMouseLeave={() => setHover(false)}
                className='w-full pb-5 mb-3 overflow-hidden text-sm leading-normal transition-shadow bg-white rounded-md shadow cursor-pointer duration-250'
            >
                <div>
                    <img
                        src={thumbnailUrl}
                        alt=''
                        className='object-cover w-full bg-gray-300 h-52'
                    />
                    <div className='flex flex-col gap-2 px-2 pt-2 text-left'>
                        <h2 className='text-lg font-normal h-14 line-clamp-2'>
                            {registration.courseName}
                        </h2>
                        <p
                            className={cn(
                                `text-lg overflow-hidden bg-statusColor px-3 py-1 rounded-full w-[200px] text-center font-semibold text-[15px]} ${registration.status.toLowerCase()}`
                            )}
                        >
                            {registration.status !== "DOCUMENT_DECLINED" ? (
                                <span>
                                    {registration.status &&
                                        registration.status
                                            .charAt(0)
                                            .toUpperCase() +
                                            registration.status
                                                .slice(1)
                                                .toLowerCase()}
                                </span>
                            ) : (
                                <span>Declined (Document)</span>
                            )}
                        </p>
                        <div className='flex items-center gap-1'>
                            <i className='fas fa-clock'></i>
                            <p className='text-gray-600 text-opacity-100 text-neutral-950'>
                                {registration.coursePlatform}
                            </p>
                        </div>
                        <div
                            className='flex justify-start p-2'
                            style={{ padding: "0px" }}
                        >
                            <div className='flex items-start gap-1'>
                                <i className='fas fa-calendar-alt'></i>
                                <p className='text-gray-600' style={{}}>
                                    <p className='text-gray-600'>
                                        Period:
                                        {startDate === null
                                            ? " Not started yet"
                                            : ` ${String(
                                                  startDate.getDate()
                                              ).padStart(2, "0")}/${String(
                                                  startDate.getMonth() + 1
                                              ).padStart(
                                                  2,
                                                  "0"
                                              )}/${startDate.getFullYear()}`}
                                        {endDate === null
                                            ? startDate === null
                                                ? ""
                                                : " - unknown"
                                            : ` - ${String(
                                                  endDate.getDate()
                                              ).padStart(2, "0")}/${String(
                                                  endDate.getMonth() + 1
                                              ).padStart(
                                                  2,
                                                  "0"
                                              )}/${endDate.getFullYear()}`}
                                    </p>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}
export const RegistrationSkeletonLoader = () => {
    return (
        <div className='w-full overflow-hidden text-sm leading-normal transition-shadow bg-white rounded-md shadow cursor-pointer duration-250'>
            <div>
                <Skeleton className='object-cover w-full bg-gray-300 h-52 animate-pulse'></Skeleton>
                <div className='flex flex-col gap-2 px-2 pt-2 text-left'>
                    <Skeleton className='h-6 bg-gray-300 animate-pulse'></Skeleton>
                    <Skeleton className='w-1/2 h-4 bg-gray-300 animate-pulse'></Skeleton>
                    <div className='flex items-center gap-2'>
                        <Skeleton className='object-cover bg-gray-300 rounded-full w-11 h-11 animate-pulse'></Skeleton>
                        <div>
                            <Skeleton className='w-24 h-4 bg-gray-300 animate-pulse'></Skeleton>
                            <Skeleton className='w-16 h-3 mt-2 bg-gray-300 animate-pulse'></Skeleton>
                        </div>
                    </div>

                    <div className='flex justify-between p-2 border-t-2'>
                        <div className='flex items-center gap-1'>
                            <Skeleton className='w-5 h-5 bg-gray-300 animate-pulse'></Skeleton>
                            <Skeleton className='w-10 h-4 bg-gray-300 animate-pulse'></Skeleton>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

// function getStatusColor(status: Status) {
//   const statusColors: Record<Status, string> = {
//     DRAFT: "rgba(210, 204, 207, 1)",
//     DISCARDED: "rgba(139, 0, 0, 1)",
//     SUBMITTED: "rgba(204, 204, 0, 1)",
//     DECLINED: "rgba(139, 0, 0, 1)",
//     DONE: "rgba(0, 100, 0, 1)",
//     APPROVED: "rgba(0, 100, 0, 1)",
//     VERIFYING: "rgba(0, 0, 139, 1)",
//     VERIFIED: "rgba(0, 128, 0, 1)",
//     CLOSED: "rgba(128, 128, 128, 1)",
//     DOCUMENT_DECLINED: "rgba(236, 84, 84, 1)",
//     NONE: "rgba(255, 255, 255, 1)", // add this line
//   };
//   return statusColors[status];
// }
