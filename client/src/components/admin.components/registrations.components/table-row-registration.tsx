import { RegistrationType } from "../../../App";

type TableRowRegistrationProps = {
    registration: RegistrationType;
};

export default function TableRowRegistration({ registration }: TableRowRegistrationProps) {
    return (
        <div className="text-[13px] flex items-center justify-between bg-white border-b-2 text-[#3A4A49]">
            <div className="flex items-center gap-2 course-info w-[35%] grow">
                <img src={registration.courseThumbnailUrl} alt="" className="w-32 h-full" />
                <p className="overflow-hidden font-medium w-96 line-clamp-2 text-ellipsis">
                    {registration.courseName}
                </p>
            </div>
            <div className="grow w-[20%] flex justify-center items-center gap-2">
                <img src={registration.userAvatarUrl} alt="" className="w-10 h-10 rounded-full" />
                <p className="text-black">{(registration.userFullname && registration.userFullname.length > 0) ? registration.userFullname : 'anonymous'}</p>
            </div>
            <p className="grow w-[10%] text-center">{registration.coursePlatform}</p>
            {registration.startDate ?
                <p className="grow w-[20%] text-center">{`${registration.startDate} ${registration.endDate ?? '-------'}`}</p>
                : <p className="grow w-[20%] text-center">Not started yet</p>
            }
            <div className="w-[15%] grow flex justify-center">
                <div>{registration.status}</div>
            </div>
        </div>
    )
}
