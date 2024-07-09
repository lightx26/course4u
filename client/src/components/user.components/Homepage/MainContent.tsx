import { useEffect, useState } from 'react'
import { CourseType } from '../../../App';
import ListCourseCardComponent from '../ListCourseCardComponent.tsx';
import PaginationSection from './PaginationSection';
import Select from '../Select.tsx';
import { fetchListAvailableCourse } from '../../../apiService/Course.service.ts';
import EmptyPage from '../EmptyPage.tsx';
import { useSelector } from 'react-redux';
import { RootState } from '../../../redux/store/store.ts';
import { XIcon } from 'lucide-react';

export type FilterItemType = {
    id: string;
    name: string;
    checked?: boolean;
    countNumber?: number;
}

const sortList = [
    {
        value: 1,
        content: 'Created Date'
    },
    {
        value: 2,
        content: 'Trending'
    },
    {
        value: 3,
        content: 'Rating'
    },
    {
        value: 4,
        content: 'Total register'
    },
]

export default function MainContent() {
    const [totalItem, setTotalItem] = useState(0);
    const [currentPage, setCurrentPage] = useState(1);
    const [listCourse, setListCourse] = useState<CourseType[]>([]);
    const [sortBy, setSortBy] = useState<string>('');
    const [isLoading, setIsLoading] = useState(false);
    const selectedItems = useSelector((state: RootState) => state.filter);

    const fetchData = async (page: number = 1, limit: number = 8) => {
        setIsLoading(true);
        const result = await fetchListAvailableCourse(page, limit);
        if (result && result.data && result.data.courses) {
            setListCourse(result.data.courses);
            setTotalItem(result.data.totalElements);
            setIsLoading(false);
        }
    }

    useEffect(() => {
        fetchData(currentPage, 8);
    }, [currentPage, sortBy]);

    const onPageNumberClick = (newPageNumber: number) => {
        setCurrentPage(newPageNumber);
    }

    const onSortByChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setSortBy(e.target.value);
    }

    return (
        <div className='flex flex-col gap-5 w-[80%] p-3 grow'>
            {
                (listCourse.length > 0) ?
                    <>
                        <div className='flex flex-wrap gap-2'>
                            {selectedItems.map(item => (
                                <div key={item} className='p-2 text-sm bg-gray-200 rounded-md'>
                                    <p>{item}</p>
                                </div>
                            ))}
                            {selectedItems && selectedItems.length > 1 && <div key={"DelteAll"} className='p-2 text-sm bg-gray-200 rounded-md'>
                                <p className='flex items-center'>Delete All <XIcon /></p>
                            </div>}
                        </div>
                        <div className='flex items-center justify-between'>
                            <Select listOption={sortList} value={sortBy} onSortByChange={onSortByChange} />
                            <div>
                                Showing {(currentPage - 1) * 8 + 1} - {Math.min(currentPage * 8, totalItem)} of {totalItem} results
                            </div>
                        </div>
                        <ListCourseCardComponent ListCourse={listCourse} isLoading={isLoading} />
                        {totalItem > 8 && <PaginationSection totalItems={totalItem} currentPage={currentPage} itemPerPage={8} setCurrentPage={onPageNumberClick} />}
                    </>
                    : <EmptyPage content={'No courses found. Try changing your search terms, adjusting your filters, or exploring different categories to find what you\'re looking for.'} />
            }
        </div>
    )
}