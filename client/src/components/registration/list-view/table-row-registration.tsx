import { RegistrationType } from "../../../App.tsx";
import { useRegistrationModal } from "../../../hooks/use-registration-modal.ts";
import {
    convertStatus,
    convertJSDatesToCorrectFormat,
} from "../../../utils/registration-utils/registration-overview-converters.ts";
import { handleAvatarUrl } from "../../../utils/handleAvatarUrl.ts";
import handleThumbnailUrl from "../../../utils/handleThumbnailUrl.ts";

type TableRowRegistrationProps = {
    registration: RegistrationType;
};

export default function TableRowRegistration({
    registration,
}: TableRowRegistrationProps) {
    const { open } = useRegistrationModal();
    const handlePopup = () => {
        open(true, +registration.id!);
    };

    const handlePeriod = (
        startDate: undefined | Date,
        endDate: undefined | Date
    ): string => {
        if (!startDate) {
            return `Not started yet`;
        }

        const handledStartDate: string =
            convertJSDatesToCorrectFormat(startDate);
        const handledEndDate: string = endDate
            ? convertJSDatesToCorrectFormat(endDate)
            : "Not finished yet";
        return `${handledStartDate} - ${handledEndDate}`;
    };

    const period = handlePeriod(registration.startDate, registration.endDate);

    const displayedStatus = convertStatus(
        registration.status ? registration.status : ""
    );

    const userAvatar = handleAvatarUrl(registration.userAvatarUrl);
    const courseThumbnail = handleThumbnailUrl(registration.courseThumbnailUrl);

    return (
        <div>
            <div
                className={`text-[13px] h-[80px] flex items-center bg-white border-2 text-[#3A4A49] hover:cursor-pointer rounded-md`}
                onClick={handlePopup}
            >
                <div className='w-[35%]'>
                    <div className='flex items-center gap-2 course-info w-full'>
                        <img
                            src={courseThumbnail}
                            alt={registration.courseName}
                            className='object-cover min-w-32 w-32 h-[80px] border-[2px] rounded-md'
                        />
                        <p className='font-medium ml-[1px] line-clamp-2'>
                            {registration.courseName}
                        </p>
                    </div>
                </div>
                <div className='grow w-[20%] flex justify-center items-center gap-2'>
                    <img
                        src={userAvatar}
                        alt='user-avatar'
                        className='w-10 h-10 rounded-full object-cover border-2 border-purple'
                    />
                    <span className='text-black w-[70px] text-center truncate'>
                        {registration.userFullname || 'Anonymous'}
                    </span>
                </div>
                <p className='grow max-w-[5%] text-center truncate'>
                    {registration.coursePlatform}
                </p>
                <p className='min-w-[25%] text-center'>{period}</p>
                <div className='min-w-[15%] grow flex justify-center'>
                    <div
                        className={`status my-auto text-[11px] rounded-lg min-h-[30px] min-w-[90px] font-semibold text-center 
                                    ${registration.status?.toLowerCase()}
                                    ${
                                        registration.status ===
                                        "DOCUMENT_DECLINED"
                                            ? "py-0.5 px-1.5 leading-6"
                                            : "py-1.5 px-4"
                                    }
                        `}
                    >
                        {displayedStatus.toUpperCase()}
                    </div>
                </div>
            </div>
        </div>
    );
}
