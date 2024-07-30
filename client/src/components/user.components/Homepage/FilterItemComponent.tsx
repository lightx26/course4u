import { useEffect, useState } from 'react';
import { FilterItemType } from './MainContent';
import { useDispatch, useSelector } from 'react-redux';
import { toggleFilterItem } from '../../../redux/slice/filterItemCheckbox.slice';
import { AppDispatch, RootState } from '../../../redux/store/store';
import { searchCoursesByFilterNameAndSortBy } from '../../../redux/slice/course.slice';


type PropsType = {
    prop: FilterItemType;
    onClick: () => void;
    isMultipleChoice?: boolean;
}

export default function FilterItemComponent({ prop, isMultipleChoice }: PropsType) {
    const [checked, setChecked] = useState(prop.checked);
    const dispatch: AppDispatch = useDispatch();

    // Sử dụng useSelector để lấy state từ store
    const selectedItems = useSelector((state: RootState) => state.filter);

    useEffect(() => {
        // Kiểm tra xem item hiện tại có được chọn trong store hay không
        const isItemSelected = selectedItems.some(item =>
            item.FilterComponentId === prop.parentID &&
            item.listChoice.some(choice => choice.id === prop.id)
        );
        prop.checked = isItemSelected;
        setChecked(prop.checked);
    }, [selectedItems, prop.parentID, prop.id, prop.checked]); // Rerun khi filterItems hoặc prop thay đổi

    const onFilterItemClick = () => {
        if (isMultipleChoice === false) {
            // Duyệt qua tất cả các mục đã chọn có cùng parentID
            selectedItems.forEach(item => {
                if (item.FilterComponentId === prop.parentID) {
                    item.listChoice.forEach(choice => {
                        // Nếu mục này không phải là mục hiện tại và đang được chọn, hủy chọn nó
                        if (choice.id !== prop.id) {
                            dispatch(toggleFilterItem({
                                FilterComponentId: prop.parentID,
                                choiceId: choice.id,
                                listChoice: [{
                                    id: choice.id,
                                    name: choice.name
                                }]
                            }));
                        }
                    });
                }
            });
        }

        // Chọn hoặc hủy chọn mục hiện tại
        dispatch(toggleFilterItem({
            FilterComponentId: prop.parentID,
            choiceId: prop.id,
            listChoice: [{
                id: prop.id,
                name: prop.name
            }]
        }));
        dispatch(searchCoursesByFilterNameAndSortBy())
    }

    return (
        <div className="flex max-w-60 justify-between text-sm cursor-pointer text-[#4E5566] hover:text-[#7E22CE]" onClick={onFilterItemClick}>
            <div className="flex items-center gap-2 max-w-60">
                <input className="sr-only" type='checkbox' readOnly={true} id={prop.id} checked={checked} />
                <div className={`w-4 h-4 rounded border ${checked ? 'bg-violet-600 border-transparent' : 'border-gray-400'} flex items-center justify-center`}>
                    {checked && <svg className="w-3 h-3 text-white" fill="none" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" viewBox="0 0 24 24" stroke="currentColor">
                        <path d="M5 13l4 4L19 7"></path>
                    </svg>}
                </div>
                <div className="text-[13px] line-clamp-1 h-[20px] overflow-ellipsis max-w-[170px]" title={prop.name}>{prop.name}</div>
            </div>
        </div>
    )
}