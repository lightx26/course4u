import React from 'react';

type OptionType = {
    value: string,
    content: string
}

type PropType = {
    listOption: OptionType[],
    value: string,
    onSortByChange: (e: React.ChangeEvent<HTMLSelectElement>) => void,
}


const StatusFilter: React.FC<PropType> = ({ listOption, value, onSortByChange }) => {
    return (
        <div style={{ display: 'flex', flexDirection: 'column' }}>
            <div className="p-3">Status:</div>
            <div className="relative">
                <select
                    className="block w-full px-4 py-3 text-base text-gray-900 border border-gray-300 rounded-lg bg-gray-50 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    value={value}
                    onChange={(event) => { onSortByChange(event) }}
                >
                    {listOption.map((item) => (
                        <option key={item.value} value={item.value}>{item.content}</option>
                    ))}

                </select>
            </div>
        </div>
    );
}

export default StatusFilter;
