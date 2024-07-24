import { ChevronDownIcon, ChevronUpIcon } from "lucide-react";

export default function SeemoreButton({ length, initital, displayCount, setDisplayCount }: { length: number, initital: number, displayCount: number, setDisplayCount: React.Dispatch<React.SetStateAction<number>> }) {
    const handleShowMore = () => {
        if (displayCount >= length) {
            setDisplayCount(initital);
        } else {
            setDisplayCount(prev => Math.min(prev + 3, length));
        }
    }
    return (
        <>
            {(length > initital) &&
                <button onClick={handleShowMore} className="flex items-center justify-center w-full gap-2 p-2 transition-colors border border-gray-200 border-solid rounded-full cursor-pointer hover:bg-gray-50">
                    <h3>{displayCount >= length ? 'Collapse' : 'See more'}</h3>
                    {displayCount >= length
                        ? <ChevronUpIcon />
                        : <ChevronDownIcon />
                    }
                </button>}
        </>
    )
}