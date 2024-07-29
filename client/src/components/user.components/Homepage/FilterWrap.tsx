import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import FilterComponent from "./FilterComponent";
import { FilterItemType } from "./MainContent";
import { AppDispatch, RootState } from "../../../redux/store/store";
import { initializeCategoryState } from "../../../redux/slice/category.slice";
import { platform } from "../../../utils/index";

export default function FilterWrap() {
    const dispatch: AppDispatch = useDispatch();
    //get Category from store
    const listCategory = useSelector((state: RootState) => state.category);
    const [categoryList, setCategoryList] = useState<FilterItemType[]>([]);
    const [ratingList, setRatingList] = useState<FilterItemType[]>([]);
    const [levelList, setLevelList] = useState<FilterItemType[]>([]);
    const [platformList, setPlatformList] = useState<FilterItemType[]>([]);

    //Get Category Filter From Redux
    const filterItemFromRedux = useSelector((state: RootState) => state.filter);

    useEffect(() => {
        const categoryFilterIndex = filterItemFromRedux.findIndex(
            (item) => item.FilterComponentId === "Category"
        );
        if (categoryFilterIndex !== -1) {
            const updatedCategoryList = categoryList.map((item) => {
                const checked =
                    filterItemFromRedux[
                        categoryFilterIndex
                    ].listChoice.findIndex(
                        (choice) => choice.id === item.id
                    ) !== -1;
                return { ...item, checked };
            });
            setCategoryList(updatedCategoryList);
        }
    }, [filterItemFromRedux]);

    //Initialize category state by dispatch action to fetching data from API and save to store (state.category)
    useEffect(() => {
        dispatch(initializeCategoryState());
    }, []);

    useEffect(() => {
        const updatedCategoryList = listCategory.map((item) => ({
            id: `${item.id}`,
            name: item.name,
            countNumber: 1200,
            parentID: "Category",
            checked: false,
        }));
        setCategoryList(updatedCategoryList);
    }, [listCategory]);

    useEffect(() => {
        const initialRatingList = [
            {
                id: "5",
                name: "5 Star",
                countNumber: 1200,
                parentID: "Rating",
                checked: false,
            },
            {
                id: "4",
                name: "4 - 4.9 Star",
                countNumber: 800,
                parentID: "Rating",
                checked: false,
            },
            {
                id: "3",
                name: "3 - 3.9 Star",
                countNumber: 900,
                parentID: "Rating",
                checked: false,
            },
            {
                id: "2",
                name: "2 - 2.9 Star",
                countNumber: 1500,
                parentID: "Rating",
                checked: false,
            },
            {
                id: "1",
                name: "1 - 1.9 Star",
                countNumber: 700,
                parentID: "Rating",
                checked: false,
            },
        ];
        setRatingList(initialRatingList);
    }, []);

    useEffect(() => {
        const initialLevelList = [
            {
                id: "BEGINNER",
                name: "Beginner",
                countNumber: 1200,
                parentID: "Level",
                checked: false,
            },
            {
                id: "INTERMEDIATE",
                name: "Intermediate",
                countNumber: 800,
                parentID: "Level",
                checked: false,
            },
            {
                id: "ADVANCED",
                name: "Advanced",
                countNumber: 900,
                parentID: "Level",
                checked: false,
            },
        ];
        setLevelList(initialLevelList);
    }, []);

    useEffect(() => {
        const initialPlatformList = platform.map(
            (items: { value: string; content: string }) => {
                return {
                    id: items.value,
                    name: items.content,
                    countNumber: 1200,
                    parentID: "Platform",
                    checked: false,
                };
            }
        );
        setPlatformList(initialPlatformList);
    }, []);

    return (
        <div className='sticky max-h-[90vh] custom-scrollbar overflow-y-auto border-2 border-gray-100 border-solid rounded-md'>
            <div className='flex flex-col w-64 max-w-full gap-5 pb-10 select-none'>
                {/*Category need search*/}
                <FilterComponent
                    key={"Category"}
                    isHaveSearch={true}
                    title='Category'
                    list={categoryList}
                />
                {/*Rating can only choose one option/time*/}
                <FilterComponent
                    key={"Rating"}
                    isHaveSearch={false}
                    title='Rating'
                    list={ratingList}
                />
                <FilterComponent
                    key={"Level"}
                    isHaveSearch={false}
                    title='Level'
                    list={levelList}
                />
                <FilterComponent
                    key={"Platform"}
                    isHaveSearch={false}
                    title='Platform'
                    list={platformList}
                />
            </div>
        </div>
    );
}
