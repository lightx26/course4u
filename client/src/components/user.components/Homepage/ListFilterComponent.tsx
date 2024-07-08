import { useState } from "react";
import FilterItemComponent from "./FilterItemComponent";
import { FilterItemType } from "./MainContent";
import { v4 } from "uuid";

type PropsType = {
    list: Array<FilterItemType>;
}

export default function ListFilterItem(props: PropsType) {
    const initialDisplayCount = 5;
    const additionalDisplayCount = 3;
    const [displayCount, setDisplayCount] = useState(initialDisplayCount);
    const [isExpanded, setIsExpanded] = useState(false);

    const handleShowMore = () => {
        if (isExpanded && displayCount >= props.list.length) {
            setDisplayCount(initialDisplayCount);
            setIsExpanded(false);
        } else {
            const newDisplayCount = Math.min(displayCount + additionalDisplayCount, props.list.length);
            setDisplayCount(newDisplayCount);
            setIsExpanded(newDisplayCount >= props.list.length);
        }
    };

    return (
        <div className="flex flex-col gap-3">
            {props.list.slice(0, displayCount).map((item) => (
                <FilterItemComponent key={v4()} prop={item} />
            ))}
            {props.list.length > initialDisplayCount && (
                <button onClick={handleShowMore} className="flex items-center justify-center w-full gap-2 p-2 transition-colors border border-gray-200 border-solid rounded-full cursor-pointer hover:bg-gray-50">
                    <h3>{isExpanded ? 'Collapse' : 'See more'}</h3>
                    <svg xmlns="http://www.w3.org/2000/svg" width="7" height="8" viewBox="0 0 7 8" fill="none">
                        <path d={isExpanded ? "M6.1775 8L7 7.15884L3.5 3.57942L0 7.15884L0.8225 8L3.5 5.26771L6.1775 8ZM6.1775 4.42058L7 3.57942L3.5 0L0 3.57942L0.8225 4.42058L3.5 1.68829L6.1775 4.42058Z" : "M6.1775 -7.19052e-08L7 0.841164L3.5 4.42058L1.49008e-07 0.841163L0.8225 -5.40054e-07L3.5 2.73229L6.1775 -7.19052e-08ZM6.1775 3.57942L7 4.42058L3.5 8L-1.63914e-07 4.42058L0.8225 3.57942L3.5 6.31171L6.1775 3.57942Z"} fill="black" />
                    </svg>
                </button>
            )}
        </div>
    );
}