import React, { useState } from 'react';

type OptionType = {
  content: string;
};

type PropType = {
  listOption: OptionType[];
  onSortByChange: (e: React.ChangeEvent<HTMLSelectElement>) => void;
};

const StatusFilter: React.FC<PropType> = ({ listOption, onSortByChange }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [selected, setSelected] = useState<string | null>(null);

  const toggleDropdown = () => setIsOpen(!isOpen);
  
  const handleSelect = (value: string) => {
    let selectedValue = value;
    setSelected(selectedValue);
    setIsOpen(false);

    if (selectedValue.includes("Document")) {
      selectedValue = "DOCUMENT_DECLINED";
    }

    const event = {
      target: { value: selectedValue },
    } as React.ChangeEvent<HTMLSelectElement>;
    onSortByChange(event);
  };

  return (
    <div className="relative inline-block text-left">
      <div className='w-52'>
        <button
          type="button"
          onClick={toggleDropdown}
          className="inline-flex justify-between w-full rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-100 focus:ring-indigo-500"
        >
          {selected ? selected : 'Submitted'}
          <svg
            className="ml-2 -mr-1 h-5 w-5"
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 20 20"
            fill="currentColor"
            aria-hidden="true"
          >
            <path
              fillRule="evenodd"
              d="M5.292 7.292a1 1 0 011.414 0L10 10.586l3.293-3.294a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
              clipRule="evenodd"
            />
          </svg>
        </button>
      </div>

      {isOpen && (
        <div className="origin-top-right absolute right-0 mt-2 w-52 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none">
          <div className="py-1" role="menu" aria-orientation="vertical" aria-labelledby="options-menu">
            {listOption.map((option, index) => (
              <button
                key={index}
                onClick={() => handleSelect(option.content)}
                className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900 w-full text-left"
                role="menuitem"
              >
                {option.content}
              </button>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default StatusFilter;
