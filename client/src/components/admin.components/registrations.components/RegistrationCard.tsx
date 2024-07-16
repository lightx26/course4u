import { RegistrationType } from "../../../App";
import { Skeleton } from "../../ui/skeleton";

// TBU when placeholder image is ready
import RegistraionPlaceholder from "../../../assets/images/admin.images/RegistrationPlaceholder.png";

type PropsType = {
  registration: RegistrationType;
};

function RegistrationCardComponent({ registration }: PropsType) {
  let backgroundColor;
  let fontColor;
  switch (registration.status?.toLowerCase()) {
    case "submitted":
      backgroundColor = "#FBBC05";
      fontColor = "#FFFFFF";
      break;
    case "declined":
      backgroundColor = "#EE1D52";
      fontColor = "#FFFFFF";
      break;
    case "approved":
      backgroundColor = "#34A853";
      fontColor = "#FFFFFF";
      break;
    case "done":
      backgroundColor = "#4285F4";
      fontColor = "#FFFFFF";
      break;
    case "verifying":
      backgroundColor = "#FBBC05";
      fontColor = "#FFFFFF";
      break;
    case "document_declined":
      backgroundColor = "#EE1D52";
      fontColor = "#FFFFFF";
      break;
    case "verified":
      backgroundColor = "#4285F4";
      fontColor = "#FFFFFF";
      break;
    case "discard":
      backgroundColor = "#FF0000";
      fontColor = "#FFFFFF";
      break;
    case "closed":
      backgroundColor = "#FF0000";
      fontColor = "#FFFFFF";
      break;
    default:
      backgroundColor = "#CCCCCC";
      fontColor = "#333333";
      break;
  }

  const convertStatus = (status: string): string => {
    return status == "DOCUMENT_DECLINED"
      ? "Declined (Document)"
      : status.charAt(0).toUpperCase() + status.slice(1).toLowerCase();
  };

  //
  // Since JS Date format is in yyyy-mm-dd,
  // We need to convert it to mm-dd-yyyy to fit the project
  //
  const convertJSDatesToCorrectFormat = (date?: Date): string => {
    if (!date) return "";
    const newDate = date.toString().split("-");
    [newDate[0], newDate[1], newDate[2]] = [newDate[1], newDate[2], newDate[0]];
    return newDate.join("/");
  }

  const handlePeriod = (startDate?: Date, endDate?: Date): string => {
    return `Period: ${convertJSDatesToCorrectFormat(startDate)} - ${convertJSDatesToCorrectFormat(endDate)}`
  }

  const handlePopup = () => {
    alert("Popup has appeared")
  }

  return (
    <div
      className="w-full overflow-hidden text-sm rounded-xl leading-normal transition-shadow bg-white border border-gray-200 border-solid shadow cursor-pointer duration-250 hover:shadow-lg grow"
      onClick={handlePopup}
    >
      <div className="course-information rounded-3xl">
        <img
          className="object-cover w-full h-52 rounded-xl"
          src={
            registration.courseThumbnailUrl
              ? registration.courseThumbnailUrl
              : RegistraionPlaceholder
          }
          alt={registration.courseName}
        />
      </div>
      <div className="informations px-4 py-3">
        <div className="flex flex-col gap-2 text-left">
          <h3 className="text-sm font-medium text-left line-clamp-2 h-12 overflow-ellipsis">
            {registration.courseName}
          </h3>
        </div>
        <div className="user_information flex justify-between">
          <div className="flex gap-1 align-center">
            <div className="my-auto">
              <img
                className="object-cover w-10 h-10 rounded-full"
                src={registration.userAvatarUrl}
                alt="user-avatar"
              />
            </div>
            <div className="username flex flex-col">
              <span className="text-base">{registration.userName}</span>
              <span className="text-xs">User</span>
            </div>
          </div>
          <div
            className="status my-auto text-[11px] font-normal py-2 px-4 rounded-lg min-w-[90px] text-center"
            style={{
              backgroundColor: `${backgroundColor}`,
              color: `${fontColor}`,
            }}
          >
            {convertStatus(registration.status ? registration.status : "")}
          </div>
        </div>
        <div className="platform mt-3 text-xs">{registration.coursePlatform}</div>
        <div className="period text-xs">
          {handlePeriod(registration.startDate, registration.endDate)}
        </div>
      </div>
    </div>
  );
}

export default RegistrationCardComponent;

const SkeletonLoader = () => {
  return (
    <div className="w-full overflow-hidden text-sm leading-normal transition-shadow bg-white rounded-md shadow cursor-pointer duration-250">
      <div>
        <Skeleton className="object-cover w-full bg-gray-300 h-52 animate-pulse"></Skeleton>
        <div className="flex flex-col gap-2 px-2 pt-2 text-left">
          <Skeleton className="h-6 bg-gray-300 animate-pulse"></Skeleton>
          <Skeleton className="w-1/2 h-4 bg-gray-300 animate-pulse"></Skeleton>
          <div className="flex items-center gap-2">
            <Skeleton className="object-cover bg-gray-300 rounded-full w-11 h-11 animate-pulse"></Skeleton>
            <div>
              <Skeleton className="w-24 h-4 bg-gray-300 animate-pulse"></Skeleton>
              <Skeleton className="w-16 h-3 mt-2 bg-gray-300 animate-pulse"></Skeleton>
            </div>
          </div>

          <div className="flex justify-between p-2 border-t-2">
            <div className="flex items-center gap-1">
              <Skeleton className="w-5 h-5 bg-gray-300 animate-pulse"></Skeleton>
              <Skeleton className="w-10 h-4 bg-gray-300 animate-pulse"></Skeleton>
            </div>
            <div className="flex items-center gap-1">
              <Skeleton className="w-5 h-5 bg-gray-300 animate-pulse"></Skeleton>
              <Skeleton className="w-10 h-4 bg-gray-300 animate-pulse"></Skeleton>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export { SkeletonLoader };
