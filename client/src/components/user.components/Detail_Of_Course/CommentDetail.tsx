import React, { useState, useEffect, useRef } from "react";
import { Rate } from "antd";
import { timeAgo } from "../../../utils/convertTime";
import { handleAvatarUrl } from "../../../utils/handleAvatarUrl";
import { v4 as uuidv4 } from "uuid";
interface IReview {
  userAvatar: string;
  userFullName: string;
  comment: string;
  createdAt: string;
  rating: number;
}

const CommentDetail: React.FC<IReview> = (props) => {
  const { userAvatar, rating, userFullName, comment, createdAt } = props;
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
          src={handleAvatarUrl(userAvatar)}
          alt=""
          className="absolute object-cover object-center w-full h-full rounded-full"
        />
      </div>
      <div className="flex flex-col items-start gap-1.25 max-w-[calc(100%-60px)] flex-wrap">
        <div className="flex flex-col">
          <div className="flex items-center h-[30px] gap-1.25">
            <div className="text-[15px] font-medium">
              {userFullName || "Anonymous"}
            </div>
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
              {timeAgo(createdAt)}
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
            className={`text-[15px] mt-2 ${isExpanded ? "line-clamp-none" : "line-clamp-6"
              } text-justify`}
            style={{ marginBottom: showSeeMore ? "20px" : "0px" }}
            key={uuidv4()}
          >
            {comment}
          </p>

          {showSeeMore && !isExpanded && (
            <button
              onClick={handleToggle}
              className="absolute bottom-0 right-0 text-blue-500 hover:underline"
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
