import React from "react";
import ProgressBar from "@ramonak/react-progress-bar";
import Ratings from "react-ratings-declarative";

interface RatingDetailProps {
  rating: number;
  label: string;
  percentage: number;
}

const RatingDetail: React.FC<RatingDetailProps> = (props) => {
  const { rating, label, percentage } = props;
  return (
    <div className="flex items-center gap-1.5 h-[30px]">
      <div className="w-[20%]">
        <Ratings
          rating={rating}
          widgetDimensions="15px"
          widgetSpacings="1px"
          className="rating"
        >
          <Ratings.Widget widgetRatedColor="purple" />
          <Ratings.Widget widgetRatedColor="purple" />
          <Ratings.Widget widgetRatedColor="purple" />
          <Ratings.Widget widgetRatedColor="purple" />
          <Ratings.Widget widgetRatedColor="purple" />
        </Ratings>
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
