import { useState } from "react";
import FilterItemComponent from "./FilterItemComponent";
import { FilterItemType } from "./Homepage/MainContent";

type PropsType = {
    list: Array<FilterItemType>;
}

export default function ListFilterItem(props: PropsType) {
    const [displayCount, setDisplayCount] = useState(5);

    const handleShowMore = () => {
        setDisplayCount(prevCount => prevCount + 3);
    };

    console.log('List length:', props.list.length, 'Display count:', displayCount); // Debugging line

    return (
        <div className="flex flex-col gap-3">
            {props.list.slice(0, displayCount).map((item, index) => (
                <FilterItemComponent key={index} prop={item} />
            ))}
            {displayCount < props.list.length && (
                <button onClick={handleShowMore} className="mt-2">
                    Xem thêm
                </button>
            )}
        </div>
    );
}