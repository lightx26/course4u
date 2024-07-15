import { useEffect, useState } from 'react'
import { CourseType } from '../../../App';
import ListCourseCardComponent from '../ListCourseCardComponent.tsx';
import PaginationSection from './PaginationSection';
import Select from '../Select.tsx';
import { fetchListAvailableCourse } from '../../../apiService/Course.service.ts';
import EmptyPage from '../EmptyPage.tsx';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../../redux/store/store.ts';
import { XIcon } from 'lucide-react';
import { toggleFilterItem } from '../../../redux/slice/filterItemCheckbox.slice.ts';
import { deleteAllFilterItem } from '../../../redux/slice/searchFilterItem.slice.ts';

export type FilterItemType = {
    id: string;
    name: string;
    checked?: boolean;
    countNumber?: number;
    parentID: string;
}

const sortList = [
    {
        value: 'NEWEST',
        content: 'Newest'
    },
    {
        value: 'RATING',
        content: 'Rating'
    },
    {
        value: 'MOST_ENROLLED',
        content: 'Most Enrolled'
    },
]

export default function MainContent() {
    const [totalItem, setTotalItem] = useState(0);
    const [currentPage, setCurrentPage] = useState(1);
    const [listCourse, setListCourse] = useState<CourseType[]>([]);
    const [sortBy, setSortBy] = useState<string>('NEWEST');
    const [isLoading, setIsLoading] = useState(true);
    const selectedItems = useSelector((state: RootState) => state.filter);
    const dispatch = useDispatch();
    const fetchData = async (page: number = 1, limit: number = 8, sortBy: string = "NEWEST") => {
        setIsLoading(true);
        const result = await fetchListAvailableCourse(page, limit, sortBy);
        if (result && result.data && result.data.content) {
            setListCourse(result.data.content);
            setTotalItem(result.data.totalElements);
            setIsLoading(false);
        }
    }

    useEffect(() => {
        fetchData(currentPage, 8, sortBy);
    }, [currentPage, sortBy]);

    const onPageNumberClick = (newPageNumber: number) => {
        setCurrentPage(newPageNumber);
    }

    const onSortByChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setSortBy(e.target.value);
    }

    const length = Array.from(new Set(selectedItems.flatMap(item => item.listChoice.map(choice => choice.name)))).length;

    return (
        <div className='flex flex-col gap-5 w-[80%] p-3 grow'>
            <div className='flex flex-wrap gap-2'>
                {selectedItems.map((item) => (
                    item.listChoice.map((choice) => (
                        <div onClick={() => dispatch(toggleFilterItem({
                            FilterComponentId: item.FilterComponentId,
                            choiceId: choice.id,
                            listChoice: [{
                                id: choice.id,
                                name: choice.name
                            }]
                        }))} key={choice.id} className='px-2 py-1 text-[12px] cursor-pointer bg-gray-100 rounded-md'>
                            <p className='flex items-center'>{choice.name} <XIcon className='w-4 ml-1 hover:text-violet-600' /></p>
                        </div>
                    ))
                ))}
                {length > 1 && <div key={"DeleteAll"} onClick={() => dispatch(deleteAllFilterItem())} className='px-2 py-1 hover:opacity-70 text-[12px] cursor-pointer text-white bg-purple  rounded-md'>
                    <p className='flex items-center'>Delete All <XIcon className='w-4 ml-1 text-white' /></p>
                </div>}
            </div>
            {
                (!isLoading && listCourse.length == 0)
                    ? <EmptyPage content={'No courses found. Try changing your search terms, adjusting your filters, or exploring different categories to find what you\'re looking for.'} />
                    : <>
                        <div className='flex items-center justify-between'>
                            <div>
                                Showing {(currentPage - 1) * 8 + 1} - {Math.min(currentPage * 8, totalItem)} of {totalItem} results
                            </div>
                            <Select listOption={sortList} value={sortBy} onSortByChange={onSortByChange} />
                        </div>
                        <ListCourseCardComponent ListCourse={listCourse} isLoading={isLoading} length={listCourse.length} />
                        {totalItem > 8 && <PaginationSection totalItems={totalItem} currentPage={currentPage} itemPerPage={8} setCurrentPage={onPageNumberClick} />}
                    </>
            }
        </div>
    )
}