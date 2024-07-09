import { useState } from 'react';
import { FilterItemType } from './MainContent';
import { useDispatch } from 'react-redux';
import { toggleFilterItem } from '../../../redux/slice/filterItemCheckbox.slice';


type PropsType = {
    prop: FilterItemType;
    onClick: (id: string) => void;
}

export default function FilterItemComponent({ prop }: PropsType) {
    const [checked, setChecked] = useState(false);
    const dispatch = useDispatch();

    const onFilterItemClick = () => {
        setChecked(!checked);
        dispatch(toggleFilterItem(prop.name));
        toggleFilterItem(prop.name);
    }
    return (
        <div className="flex justify-between text-base cursor-pointer text-[#4E5566] hover:text-[#7E22CE]" onClick={onFilterItemClick}>
            <div className="flex items-center gap-2">
                <input className="sr-only" type='checkbox' readOnly={true} id={prop.id} checked={checked} />
                <div className={`w-4 h-4 rounded border ${checked ? 'bg-violet-600 border-transparent' : 'border-gray-400'} flex items-center justify-center`}>
                    {checked && <svg className="w-3 h-3 text-white" fill="none" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" viewBox="0 0 24 24" stroke="currentColor">
                        <path d="M5 13l4 4L19 7"></path>
                    </svg>}
                </div>
                <div className="block text-sm">{prop.name}</div>
            </div>
            <div className="pt-1 text-sm">{prop.countNumber}</div>
        </div>
    )
}