import React, { useState, useEffect, useRef } from "react";
import { Rate } from "antd";
interface IReview {
  userAvatar: string;
  userFullName: string;
  comment: string;
  createdDate: string;
  rating: number;
}
function timeAgo(inputTime: string): string {
  const now = new Date();
  const timeDiff = now.getTime() - new Date(inputTime).getTime();

  const seconds = Math.floor(timeDiff / 1000);
  const minutes = Math.floor(seconds / 60);
  const hours = Math.floor(minutes / 60);
  const days = Math.floor(hours / 24);
  const weeks = Math.floor(days / 7);
  const months = Math.floor(days / 30.44);
  const years = Math.floor(days / 365.25);

  if (seconds < 60) {
    return "Just now";
  } else if (minutes < 60) {
    return `${minutes} mins ago`;
  } else if (hours < 24) {
    return `${hours} hours ago`;
  } else if (days < 7) {
    return `${days} days ago`;
  } else if (weeks < 4) {
    return `${weeks} weeks ago`;
  } else if (months < 12) {
    return `${months} months ago`;
  } else {
    return `${years} years ago`;
  }
}

const CommentDetail: React.FC<IReview> = (props) => {
  const { userAvatar, rating, userFullName, comment, createdDate } = props;
  const [isExpanded, setIsExpanded] = useState(false);
  const [showSeeMore, setShowSeeMore] = useState(false);
  const commentRef = useRef<HTMLParagraphElement>(null);

  useEffect(() => {
    const handleResize = () => {
      if (commentRef.current) {
        const lineHeight = parseFloat(
          getComputedStyle(commentRef.current).lineHeight
        );
        const maxLines = 6;
        const maxHeight = lineHeight * maxLines;

        if (commentRef.current.scrollHeight > maxHeight) {
          setShowSeeMore(true);
        } else {
          setShowSeeMore(false);
        }
      }
    };

    handleResize();
    window.addEventListener("resize", handleResize);

    return () => window.removeEventListener("resize", handleResize);
  }, [comment]);

  const handleToggle = () => {
    setIsExpanded(!isExpanded);
  };

  return (
    <div className="flex items-start gap-2.5">
      <div className="relative w-[50px] h-[50px] rounded-full">
        <img
          src={userAvatar}
          alt=""
          className="absolute w-full h-full object-cover object-center rounded-full"
        />
      </div>
      <div className="flex flex-col items-start gap-1.25 max-w-[calc(100%-60px)] flex-wrap">
        <div className="flex flex-col">
          <div className="flex items-center h-[30px] gap-1.25">
            <div className="text-[15px] font-medium">{userFullName}</div>
            <div>
              <svg
                width="24"
                height="24"
                viewBox="0 0 24 24"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  d="M12 10C11.4696 10 10.9609 10.2107 10.5858 10.5858C10.2107 10.9609 10 11.4696 10 12C10 12.5304 10.2107 13.0391 10.5858 13.4142C10.9609 13.7893 11.4696 14 12 14C13.11 14 14 13.11 14 12C14 11.4696 13.7893 10.9609 13.4142 10.5858C13.0391 10.2107 12.5304 10 12 10Z"
                  fill="#858585"
                />
              </svg>
            </div>
            <div className="text-[12px] font-semibold leading-none text-gray-500 mt-0.5">
              {timeAgo(createdDate)}
            </div>
          </div>
          <div className="flex justify-start">
            <Rate
              disabled
              value={rating}
              allowHalf
              style={{ fontSize: "12px", color: "black" }}
            />
          </div>
        </div>
        <div className="relative">
          <p
            ref={commentRef}
            className={`text-[15px] mt-2 ${
              isExpanded ? "line-clamp-none" : "line-clamp-6"
            } text-justify`}
            style={{ marginBottom: showSeeMore ? "20px" : "0px" }}
          >
            {comment}
          </p>

          {showSeeMore && !isExpanded && (
            <button
              onClick={handleToggle}
              className="text-blue-500 hover:underline absolute bottom-0 right-0"
            >
              See More
            </button>
          )}
          {isExpanded && (
            <button
              onClick={handleToggle}
              className="text-blue-500 hover:underline"
            >
              See Less
            </button>
          )}
        </div>
      </div>
    </div>
  );
};

export default CommentDetail;
