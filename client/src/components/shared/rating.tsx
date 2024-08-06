import React, { useState } from 'react';

interface RatingProps {
    value: number;
    onChange: (rating: number) => void;
}

const Rating: React.FC<RatingProps> = ({ value, onChange }) => {
    const [hoveredIndex, setHoveredIndex] = useState<number | null>(null);

    const handleMouseEnter = (index: number) => {
        setHoveredIndex(index);
    };

    const handleMouseLeave = () => {
        setHoveredIndex(null);
    };

    const handleClick = (index: number) => {
        onChange(index);
    };

    const getStarClass = (index: number) => {
        if (hoveredIndex !== null && hoveredIndex >= index) {
            return 'text-yellow-300';
        }
        if (value >= index) {
            return 'text-yellow-300';
        }
        return 'text-gray-300';
    };

    return (
        <div className="items-center">
            <div className="w-full text-base font-medium text-center text-gray-500 ms-1">{value} out of 5</div>
            <div className='flex justify-center w-full gap-1 my-3'>
                {[1, 2, 3, 4, 5].map((index) => (
                    <svg
                        key={index}
                        className={`w-8 h-8 me-1 cursor-pointer ${getStarClass(index)}`}
                        aria-hidden="true"
                        xmlns="http://www.w3.org/2000/svg"
                        fill="currentColor"
                        viewBox="0 0 22 20"
                        onMouseEnter={() => handleMouseEnter(index)}
                        onMouseLeave={handleMouseLeave}
                        onClick={() => handleClick(index)}
                    >
                        <path d="M20.924 7.625a1.523 1.523 0 0 0-1.238-1.044l-5.051-.734-2.259-4.577a1.534 1.534 0 0 0-2.752 0L7.365 5.847l-5.051.734A1.535 1.535 0 0 0 1.463 9.2l3.656 3.563-.863 5.031a1.532 1.532 0 0 0 2.226 1.616L11 17.033l4.518 2.375a1.534 1.534 0 0 0 2.226-1.617l-.863-5.03L20.537 9.2a1.523 1.523 0 0 0 .387-1.575Z" />
                    </svg>
                ))}
            </div>
        </div>
    );
};

export default Rating;
