import { useEffect, useState } from 'react'
import ListCourseCardComponent from '../ListCourseCardComponent.tsx';
import PaginationSection from './PaginationSection';

import EmptyPage from '../EmptyPage.tsx';
import { useDispatch, useSelector } from 'react-redux';
import { AppDispatch, RootState } from '../../../redux/store/store.ts';
import { XIcon } from 'lucide-react';
import { toggleFilterItem } from '../../../redux/slice/filterItemCheckbox.slice.ts';
import { deleteAllFilterItem } from '../../../redux/slice/searchFilterItem.slice.ts';
import { CourseStateType, searchCoursesByFilterNameAndSortBy, updatePage, updateSort } from '../../../redux/slice/course.slice.ts';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../../ui/select.tsx';

export type FilterItemType = {
    id: string;
    name: string;
    checked?: boolean;
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
    const currentPage = useSelector((state: RootState) => state.courses.number);
    const [isLoading, setIsLoading] = useState(true);
    const selectedItems = useSelector((state: RootState) => state.filter);
    const courseState: CourseStateType = useSelector((state: RootState) => state.courses);
    const sortBy = useSelector((state: RootState) => state.courses.sortBy);
    const dispatch: AppDispatch = useDispatch();

    useEffect(() => {
        setIsLoading(true);
        const fetchData = async () => {
            setIsLoading(true);
            dispatch(updateSort(sortBy));
            dispatch(searchCoursesByFilterNameAndSortBy());
            setIsLoading(false);
        }
        fetchData();
    }, [currentPage, sortBy]);


    const onPageNumberClick = (newPageNumber: number) => {
        //update page number
        dispatch(updatePage(newPageNumber));
    }

    const length = Array.from(new Set(selectedItems.flatMap(item => item.listChoice.map(choice => choice.name)))).length;

    return (
        <div className='flex flex-col gap-5 w-[80%] p-2 grow'>
            <div className='flex flex-wrap gap-2'>
                {selectedItems.map((item) => (
                    item.listChoice.map((choice) => (
                        <div onClick={
                            () => {
                                dispatch(toggleFilterItem({
                                    FilterComponentId: item.FilterComponentId,
                                    choiceId: choice.id,
                                    listChoice: [{
                                        id: choice.id,
                                        name: choice.name
                                    }]
                                }))
                                dispatch(searchCoursesByFilterNameAndSortBy());
                            }
                        } key={choice.id} className='px-2 py-1 text-[12px] cursor-pointer bg-gray-100 rounded-md'>
                            <p className='flex items-center'>{choice.name} <XIcon className='w-4 ml-1 hover:text-violet-600' /></p>
                        </div>
                    ))
                ))}
                {length > 1 && <div key={"DeleteAll"} onClick={() => {
                    dispatch(deleteAllFilterItem())
                    dispatch(searchCoursesByFilterNameAndSortBy())
                }
                } className='px-2 py-1 hover:opacity-70 text-[12px] cursor-pointer text-white bg-purple  rounded-md'>
                    <p className='flex items-center'>Delete All <XIcon className='w-4 ml-1 text-white' /></p>
                </div>}
            </div>
            {
                (!isLoading && courseState.content.length == 0)
                    ? <EmptyPage content={'No courses found. Try changing your search terms, adjusting your filters, or exploring different categories to find what you\'re looking for.'} />
                    : <>
                        <div className='flex items-center justify-between'>
                            <div>
                                Showing {(currentPage - 1) * 8 + 1} - {Math.min(currentPage * 8, courseState.totalElements)} of {courseState.totalElements} results
                            </div>
                            {/* <Select listOption={sortList} value={sortBy} onSortByChange={onSortByChange} /> */}
                            <Select value={sortBy} onValueChange={(value) => dispatch(updateSort(value))}>
                                <SelectTrigger className="w-[180px]" >
                                    <SelectValue placeholder="Sort by" />
                                </SelectTrigger>
                                <SelectContent>
                                    {sortList.map((item) => (
                                        <SelectItem key={item.value} value={item.value}
                                        >{item.content}</SelectItem>
                                    ))}
                                </SelectContent>
                            </Select>
                        </div>
                        <ListCourseCardComponent ListCourse={courseState.content} isLoading={isLoading} length={courseState.content.length} />
                        {
                            courseState.totalElements > 8
                            && <PaginationSection isLoading={isLoading} totalItems={courseState.totalElements} currentPage={currentPage} itemPerPage={8} setCurrentPage={onPageNumberClick} />
                        }
                    </>
            }
        </div >
    )
}