import { OverviewMyRegistrationType } from "./MyRegistrationsPage";
import { Skeleton } from "../../ui/skeleton";
import { RegistrationModal } from "../../modal/registration-modal";

type PropsType = {
    registration: OverviewMyRegistrationType;
};

export default function MyRegistrationComponents({ registration }: PropsType) {
    const startDate =
        typeof registration.startDate === "string"
            ? new Date(registration.startDate)
            : registration.startDate;
    const endDate = registration.endDate
        ? typeof registration.endDate === "string"
            ? new Date(registration.endDate)
            : registration.endDate
        : null;

    return (
        <>
            <RegistrationModal id={+registration.id!}>
                <div className='pb-5 mb-3 w-full overflow-hidden text-sm leading-normal transition-shadow bg-white rounded-md shadow cursor-pointer duration-250'>
                    <div>
                        <img
                            src={registration.thumbnailUrl}
                            alt=''
                            className='object-cover w-full bg-gray-300 h-52'
                        />
                        <div className='flex flex-col gap-2 px-2 pt-2 text-left'>
                            <h2 className='text-lg font-normal'>
                                {registration.courseName}
                            </h2>
                            <p
                                className='text-white text-l text-clip'
                                style={{
                                    backgroundColor: getStatusColor(
                                        registration.status
                                    ),
                                    height: "auto",
                                    width: "fit-content",
                                    padding: "4px",
                                    paddingLeft: "12px",
                                    paddingRight: "12px",
                                    borderRadius: "20px",
                                }}
                            >
                                {registration.status !== "DOCUMENT_DECLINED" ? (
                                    <span>{registration.status}</span>
                                ) : (
                                    <span>DECLINED(DOCUMENT)</span>
                                )}
                            </p>
                            <div className='flex items-center gap-1'>
                                <i className='fas fa-clock'></i>
                                <p className='text-opacity-100 text-neutral-950'>
                                    {registration.platform}
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
                                            {startDate === null
                                                ? "..."
                                                : `${String(
                                                      startDate.getDate()
                                                  ).padStart(2, "0")}/${String(
                                                      startDate.getMonth() + 1
                                                  ).padStart(
                                                      2,
                                                      "0"
                                                  )}/${startDate.getFullYear()}`}
                                            {endDate === null
                                                ? "..."
                                                : `${
                                                      " - " +
                                                      String(
                                                          endDate.getDate()
                                                      ).padStart(2, "0")
                                                  }/${String(
                                                      endDate.getMonth() + 1
                                                  ).padStart(
                                                      2,
                                                      "0"
                                                  )}/${endDate.getFullYear()}`}
                                        </p>{" "}
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </RegistrationModal>
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
type Status =
    | "DRAFT"
    | "DISCARDED"
    | "SUBMITTED"
    | "DECLINED"
    | "DONE"
    | "APPROVED"
    | "VERIFYING"
    | "VERIFIED"
    | "CLOSED"
    | "DOCUMENT_DECLINED";

function getStatusColor(status: Status) {
    const statusColors: Record<Status, string> = {
        DRAFT: "rgba(210, 204, 207, 1)", // darker
        DISCARDED: "rgba(139, 0, 0, 1)", // dark red
        SUBMITTED: "rgba(204, 204, 0, 1)", // darker yellow
        DECLINED: "rgba(139, 0, 0, 1)", // dark red
        DONE: "rgba(0, 100, 0, 1)", // darker green
        APPROVED: "rgba(0, 100, 0, 1)", // darker green
        VERIFYING: "rgba(0, 0, 139, 1)", // dark blue
        VERIFIED: "rgba(0, 128, 0, 1)", // green
        CLOSED: "rgba(128, 128, 128, 1)", // gray
        DOCUMENT_DECLINED: "rgba(236, 84, 84, 1)", // red
    };
    return statusColors[status];
}
