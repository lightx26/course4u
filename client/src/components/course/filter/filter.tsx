import { useEffect, useState } from "react";
import ListFilterItem from "./list-filter";
import { FilterItemType } from "../../user/main-content";

// FilterComponent is a component that displays a list of filter items.
type Prop = {
    title: string;
    list: FilterItemType[];
    isHaveSearch?: boolean;
    isMultipleChoice?: boolean;
};
// FilterComponent is a component that displays a list of filter items (checkbox, name and countCourse).
export default function FilterComponent(prop: Prop) {
    //a list of filter items.
    const [listFilterItem, setListFilterItem] = useState<FilterItemType[]>(
        prop.list
    );
    //Check if the list is visible or not.
    const [isListVisible, setIsListVisible] = useState(true);

    //Update the list of filter items when the list of filter items changes.
    useEffect(() => {
        setListFilterItem(prop.list);
    }, [prop.list]);

    //Toggle the visibility of the list of filter items.
    const toggleListVisibility = () => {
        setIsListVisible(!isListVisible);
    };

    //Handle the search change event (for FilterComponent have search bar).
    const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        //Get the search text and remove all space
        const searchText = event.target.value.trim().toLowerCase();
        //Filter the list of filter items based on the search text.
        const filteredList = prop.list.filter((item) =>
            item.name.toLowerCase().includes(searchText)
        );
        //Show result after search filter cafeteria
        setListFilterItem(filteredList);
    };

    return (
        <div className='text-black bg-white border border-gray-300 border-solid rounded-lg max-w-64'>
            <div
                className='flex justify-between p-2 border border-gray-300 border-solid rounded-lg max-w-64'
                onClick={toggleListVisibility}
            >
                <h3>{prop.title}</h3>
                <svg
                    style={{
                        transform: isListVisible ? "rotate(180deg)" : "none",
                        transition: "transform 0.2s ease-in-out",
                    }}
                    xmlns='http://www.w3.org/2000/svg'
                    width='24'
                    height='24'
                    viewBox='0 0 24 24'
                    fill='none'
                >
                    <path
                        d='M19.5 15L12 7.5L4.5 15'
                        stroke='#1D2026'
                        strokeWidth='1.5'
                        strokeLinecap='round'
                        strokeLinejoin='round'
                    />
                </svg>
            </div>
            {isListVisible && (
                <div className='p-3 ease-linear delay-75'>
                    {prop.isHaveSearch && (
                        <div className='relative py-2'>
                            <input
                                onChange={handleSearchChange}
                                type='text'
                                className='box-border block py-2 bg-white border border-gray-300 rounded-md max-w-[232px] px-7 hover:border-violet-600'
                            />
                            <svg
                                className='absolute left-1 top-1/2 -translate-y-2/4 hover:stroke-violet-600'
                                xmlns='http://www.w3.org/2000/svg'
                                width='24'
                                height='24'
                                viewBox='0 0 24 24'
                                fill='none'
                            >
                                <path
                                    d='M15.5 15.5L20 20'
                                    stroke='#1D2026'
                                    strokeWidth='1.5'
                                    strokeLinecap='round'
                                    strokeLinejoin='round'
                                />
                                <circle
                                    cx='10.5'
                                    cy='10.5'
                                    r='5.75'
                                    stroke='#1D2026'
                                    strokeWidth='1.5'
                                />
                            </svg>
                        </div>
                    )}
                    {/*Check if not have any item, FilterComponent will shown no item found */}
                    {listFilterItem.length > 0 ? (
                        <ListFilterItem
                            list={listFilterItem}
                            setList={setListFilterItem}
                            isMultipleChoice={prop.isMultipleChoice}
                        />
                    ) : (
                        <>No item found</>
                    )}
                </div>
            )}
        </div>
    );
}
