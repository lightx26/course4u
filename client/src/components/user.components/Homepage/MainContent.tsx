import { useEffect, useState } from 'react'
import { CourseType } from '../../../App';
import ListCourseCardComponent from '../ListCourseCardComponent.tsx';
import PaginationSection from './PaginationSection';
import Select from '../Select.tsx';
import { fetchListAvailableCourse } from '../../../apiService/Course.service.ts';

export type FilterItemType = {
    id?: string;
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
    const [totalItem, setTotalItem] = useState(60);
    const [currentPage, setCurrentPage] = useState(1);
    const [listCourse, setListCourse] = useState<CourseType[]>([]);
    const [sortBy, setSortBy] = useState<string>('');
    const [isLoading, setIsLoading] = useState(false);

    const fetchData = async (page : number = 1, limit : number = 8) => {
        setIsLoading(true);
        const result = await fetchListAvailableCourse(page, limit);
        if (result && result.data && result.data.courses){
            setListCourse(result.data.courses);
            setTotalItem(result.data.totalElements);
            setIsLoading(false);
        }
    }

    useEffect(() => {
        fetchData(currentPage, 8);
    }, [currentPage]);

    const onPageNumberClick = (newPageNumber: number) => {
        setCurrentPage(newPageNumber);
    }

    const onSortByChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setSortBy(e.target.value);
    }

    return (
        <div className='flex flex-col gap-5 w-[80%] p-3 grow'>
            <div className='flex items-center justify-between'>
                <Select listOption={sortList} value={sortBy} onSortByChange={onSortByChange} />
                <div>
                    Showing {(currentPage - 1) * 8 + 1} - {Math.min(currentPage * 8, totalItem)} of {totalItem} results
                </div>
            </div>
            <ListCourseCardComponent ListCourse={listCourse} isLoading={isLoading} />
            <PaginationSection totalItems={totalItem} currentPage={currentPage} itemPerPage={8} setCurrentPage={onPageNumberClick} />
        </div>
    )
}