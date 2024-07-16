import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import FilterComponent from './FilterComponent';
import { FilterItemType } from './MainContent';
import { AppDispatch, RootState } from '../../../redux/store/store';
import { initializeCategoryState } from '../../../redux/slice/category.slice';

export default function FilterWrap() {
    const dispatch: AppDispatch = useDispatch();
    //get Category from store
    const listCategory = useSelector((state: RootState) => state.category);
    const [categoryList, setCategoryList] = useState<FilterItemType[]>([]);
    const [ratingList, setRatingList] = useState<FilterItemType[]>([]);
    const [levelList, setLevelList] = useState<FilterItemType[]>([]);
    const [platformList, setPlatformList] = useState<FilterItemType[]>([]);

    //Initialize category state by dispatch action to fetching data from API and save to store (state.category)
    useEffect(() => {
        dispatch(initializeCategoryState());
    }, [dispatch]);

    useEffect(() => {
        console.log('listCategory:', listCategory); // Log Redux state
        const updatedCategoryList = listCategory.map((item) => ({
            id: `cat${item.id}`,
            name: item.name,
            countNumber: 1200,
            parentID: 'cat1',
            checked: false
        }));
        setCategoryList(updatedCategoryList);
    }, [listCategory]);

    useEffect(() => {
        const initialRatingList = [
            {
                id: 'rat1',
                name: '5 Star',
                countNumber: 1200,
                parentID: 'cat2',
                checked: false
            },
            {
                id: 'rat2',
                name: '4 Star & up',
                countNumber: 800,
                parentID: 'cat2',
                checked: false
            },
            {
                id: 'rat3',
                name: '3 Star & up',
                countNumber: 900,
                parentID: 'cat2',
                checked: false
            },
            {
                id: 'rat4',
                name: '2 Star & up',
                countNumber: 1500,
                parentID: 'cat2',
                checked: false
            },
            {
                id: 'rat5',
                name: '1 Star & up',
                countNumber: 700,
                parentID: 'cat2',
                checked: false
            }
        ];
        setRatingList(initialRatingList);
    }, []);

    useEffect(() => {
        const initialLevelList = [
            {
                id: 'lev1',
                name: 'Beginner',
                countNumber: 1200,
                parentID: 'cat3',
                checked: false
            },
            {
                id: 'lev2',
                name: 'Intermediate',
                countNumber: 800,
                parentID: 'cat3',
                checked: false
            },
            {
                id: 'lev3',
                name: 'Advanced',
                countNumber: 900,
                parentID: 'cat3',
                checked: false
            }
        ];
        setLevelList(initialLevelList);
    }, []);

    useEffect(() => {
        const initialPlatformList = [
            {
                id: 'plat1',
                name: 'Udemy',
                countNumber: 1200,
                parentID: 'cat4',
                checked: false
            },
            {
                id: 'plat2',
                name: 'Coursera',
                countNumber: 800,
                parentID: 'cat4',
                checked: false
            },
            {
                id: 'plat3',
                name: 'Linkedin',
                countNumber: 900,
                parentID: 'cat4',
                checked: false
            }
        ];
        setPlatformList(initialPlatformList);
    }, []);

    return (
        <div className='sticky max-h-[90vh] custom-scrollbar overflow-y-auto border-2 border-gray-100 border-solid p-1 rounded-md'>
            <div className='flex flex-col w-64 max-w-full gap-5 pb-10 select-none'>
                {/*Category need search*/}
                <FilterComponent key={'cat1'} isHaveSearch={true} title='Category' list={categoryList} />
                {/*Rating can only choose one option/time*/}
                <FilterComponent key={'cat2'} isMultipleChoice={false} isHaveSearch={false} title='Rating' list={ratingList} />
                <FilterComponent key={'cat3'} isHaveSearch={false} title='Level' list={levelList} />
                <FilterComponent key={'cat4'} isHaveSearch={false} title='Platform' list={platformList} />
            </div>
        </div>
    );
}
