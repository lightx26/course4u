import { useEffect, useState } from 'react'
import { fetchCourses, fetchCourses2 } from '../../../API/FakeAPICourse';
import { CourseType } from '../../../App';
import ListCourseCardComponent from '../ListCorseCardComponent';
import PaginationSection from './PaginationSection';
import Select from '../Select.tsx';

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
    useEffect(() => {
        const fetchData = async () => {
            setIsLoading(true);
            await fetchCourses2(currentPage - 1, 8).then((res) => {
                setListCourse(res);
            });

            await fetchCourses().then((res) => {
                setTotalItem(res.length)
                setIsLoading(false);
            });
            setIsLoading(false);
        }
        fetchData();
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
                    Showing {(currentPage - 1) * 8 + 1} - {currentPage * 8} of {totalItem} results
                </div>
            </div>
            <ListCourseCardComponent ListCourse={listCourse} isLoading={isLoading} />
            <PaginationSection totalItems={45} currentPage={currentPage} itemPerPage={5} setCurrentPage={onPageNumberClick} />
        </div>
    )
}