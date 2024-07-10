import React from "react";
import ProgressBar from "@ramonak/react-progress-bar";
import { Rate } from "antd";
interface RatingDetailProps {
  rating: number;
  label: string;
  percentage: number;
}

const RatingDetail: React.FC<RatingDetailProps> = (props) => {
  const { rating, label, percentage } = props;
  return (
    <div className="flex items-center gap-1.5 h-[25px]">
      <div className="w-[30%] h-[100%]">
        <Rate
          disabled
          defaultValue={rating}
          allowHalf
          style={{ fontSize: "12px", color: "purple" }}
        />
      </div>
      <div className="text-[13px] w-[25%]">{label}</div>
      <div className="w-full text-[13px] flex items-center gap-2.5">
        <div className="w-[90%]">
          <ProgressBar
            completed={percentage}
            borderRadius="20"
            height="8px"
            baseBgColor="#fff2e5"
            isLabelVisible={false}
          />
        </div>
        <div>{percentage}%</div>
      </div>
    </div>
  );
};

export default RatingDetail;
