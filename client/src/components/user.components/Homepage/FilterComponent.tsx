import { useState } from 'react';
import ListFilterItem from './ListFilterComponent';
import { FilterItemType } from './MainContent';

type Prop = {
    title: string;
    list: FilterItemType[]
    isHaveSearch?: boolean;
}

export default function FilterComponent(prop: Prop) {
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const [listFilterItem, setListFilterItem] = useState<FilterItemType[]>(prop.list);

    const [isListVisible, setIsListVisible] = useState(true);

    const toggleListVisibility = () => {
        setIsListVisible(!isListVisible);
    };

    return (
        <div className='text-black bg-white border border-gray-300 border-solid rounded-lg'>
            <div className='flex justify-between p-4 border border-gray-300 border-solid rounded-lg' onClick={toggleListVisibility}>
                <h3>{prop.title}</h3>
                <svg style={{ transform: isListVisible ? 'rotate(180deg)' : 'none', transition: 'transform 0.2s ease-in-out' }} xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                    <path d="M19.5 15L12 7.5L4.5 15" stroke="#1D2026" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
                </svg>
            </div>
            {isListVisible && (
                <div className='h-full p-3 ease-linear delay-75'>
                    {
                        prop.isHaveSearch &&
                        <div className='relative w-full py-2'>
                            <input type='search' className='box-border w-full py-2 bg-white border border-gray-300 rounded-md px-7 hover:border-violet-600' />
                            <svg className='absolute left-1 top-1/2 -translate-y-2/4 hover:stroke-violet-600' xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                <path d="M15.5 15.5L20 20" stroke="#1D2026" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
                                <circle cx="10.5" cy="10.5" r="5.75" stroke="#1D2026" strokeWidth="1.5" />
                            </svg>
                        </div>
                    }
                    <ListFilterItem list={listFilterItem} />
                </div>
            )}
        </div>
    );
}
