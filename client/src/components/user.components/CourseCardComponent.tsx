import { useNavigate } from "react-router-dom";
import { CourseType } from "../../App";
import { cn } from "../../utils";
import { Skeleton } from "../ui/skeleton";
import { useSelector } from "react-redux";
import { RootState } from "../../redux/store/store";
import handleThumbnailUrl from "../../utils/handleThumbnailUrl";
type PropsType = {
  course: CourseType;
};

export default function CourseCardComponent({ course }: PropsType) {
  const navigate = useNavigate();
  const userRole = useSelector((state: RootState) => state.user.user.role);
  const handleClickCourseDetail =
    (id: string | undefined) => (e: React.MouseEvent) => {
      e.preventDefault();
      if (userRole === "ADMIN") {
        navigate(`/admin/courses/${id}`);
      } else if (userRole === "USER") {
        navigate(`/courses/${id}`);
      }
      window.scrollTo(0, 0);
    };

  const thumbnailUrl = handleThumbnailUrl(course.thumbnailUrl);

  const color =
    course.level?.toLowerCase() === "beginner"
      ? "bg-green-200 text-green-600"
      : course.level?.toLowerCase() === "intermediate"
      ? "bg-yellow-100 text-yellow-600"
      : "bg-rose-100 text-rose-600";
  return (
    <div
      onClick={handleClickCourseDetail(course.id)}
      className="w-full overflow-hidden text-sm leading-normal transition-shadow bg-white border border-gray-200 border-solid rounded-md shadow cursor-pointer duration-250 hover:shadow-lg grow"
    >
      <img
        className="object-cover w-full h-52"
        src={thumbnailUrl}
        alt={course.name}
      />
      <div className="flex flex-col gap-2 px-2 pt-2 text-left">
        <div className="h-[40px] overflow-hidden flex items-center">
          <h3 className="text-sm font-medium text-left text line-clamp-2 overflow-ellipsis h-[40px]">
            {course.name}
          </h3>
        </div>
        {/*Assignee role and status are optional*/}
        {course.assignee && (
          <div className="flex items-center gap-2">
            <img
              className="object-cover rounded-full w-11 h-11"
              src={course.assignee?.avatarUrl}
              alt={course.assignee?.name}
            />
            <div>
              <p className="text-base font-medium">{course.assignee?.name}</p>
              {course.assignee?.role && (
                <p className="text-xs text-gray-500">{course.assignee?.role}</p>
              )}
            </div>
            {course.assignee?.status && <div>{course.assignee?.status}</div>}
          </div>
        )}
        <div className="flex items-center gap-3">
          <p className="text-sm font-medium">{course.platform}</p>
          {course.level && (
            <p className={cn("px-2 py-1 rounded text-xs font-medium", color)}>
              {" "}
              {course.level}
            </p>
          )}
        </div>
        {/*<div>{course.createdDate}</div>*/}
        {course.period && (
          <p>
            {course?.period?.startDay?.toDateString()} -{" "}
            {course?.period?.endDay?.toDateString()}
          </p>
        )}
        <div className="flex justify-between p-2 border-t-2">
          <div className="flex gap-1">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="15"
              height="15"
              viewBox="0 0 20 20"
              fill="none"
            >
              <path
                d="M10.3446 14.901L14.2849 17.3974C14.7886 17.7165 15.4139 17.2419 15.2644 16.654L14.126 12.1756C14.0939 12.0509 14.0977 11.9197 14.137 11.797C14.1762 11.6743 14.2492 11.5652 14.3477 11.4822L17.8811 8.54132C18.3453 8.1549 18.1057 7.38439 17.5092 7.34567L12.8949 7.0462C12.7706 7.03732 12.6514 6.99332 12.5511 6.91931C12.4509 6.84531 12.3737 6.74435 12.3286 6.62819L10.6076 2.29436C10.5609 2.17106 10.4777 2.06492 10.3692 1.99002C10.2606 1.91511 10.1319 1.875 10 1.875C9.86813 1.875 9.73938 1.91511 9.63085 1.99002C9.52232 2.06492 9.43914 2.17106 9.39236 2.29436L7.6714 6.62819C7.62631 6.74435 7.54914 6.84531 7.4489 6.91931C7.34865 6.99332 7.22944 7.03732 7.10515 7.0462L2.49078 7.34567C1.89429 7.38439 1.65466 8.1549 2.11894 8.54132L5.65232 11.4822C5.75079 11.5652 5.82383 11.6743 5.86305 11.797C5.90226 11.9197 5.90606 12.0509 5.874 12.1756L4.81824 16.3288C4.63889 17.0343 5.38929 17.6038 5.99369 17.2209L9.65539 14.901C9.75837 14.8354 9.87792 14.8006 10 14.8006C10.1221 14.8006 10.2416 14.8354 10.3446 14.901Z"
                fill="#FD8E1F"
              />
            </svg>
            <p className="text-[12px]">
              {course.rating ? course.rating.toFixed(1) : "No rating yet"}
            </p>
          </div>

          <div className="flex gap-1">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="15"
              height="15"
              viewBox="0 0 20 20"
              fill="none"
            >
              <path
                d="M10 12.5C12.7614 12.5 15 10.2614 15 7.5C15 4.73858 12.7614 2.5 10 2.5C7.23858 2.5 5 4.73858 5 7.5C5 10.2614 7.23858 12.5 10 12.5Z"
                stroke="#564FFD"
                strokeWidth="1.5"
                strokeMiterlimit="10"
              />
              <path
                d="M2.42114 16.8743C3.18955 15.5442 4.29443 14.4398 5.6248 13.672C6.95517 12.9042 8.46417 12.5 10.0002 12.5C11.5363 12.5 13.0453 12.9043 14.3756 13.6721C15.706 14.44 16.8108 15.5444 17.5792 16.8744"
                stroke="#564FFD"
                strokeWidth="1.5"
                strokeLinecap="round"
                strokeLinejoin="round"
              />
            </svg>
            <p className="text-[12px]">{course.enrollmentCount} mgmies</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export const SkeletonLoader = () => {
  return (
    <div className="h-[342px] w-full overflow-hidden text-sm leading-normal transition-shadow bg-white rounded-md shadow cursor-pointer duration-250">
      <div>
        <Skeleton className="object-cover w-full bg-gray-300 h-52 animate-pulse"></Skeleton>
        <div className="flex flex-col gap-2 px-2 pt-2 text-left">
          <Skeleton className="h-6 bg-gray-300 animate-pulse"></Skeleton>
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
