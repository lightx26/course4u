import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { AppDispatch } from "../store/store";
import { fetchListAvailableCategory } from "../../service/category";

export type CategoryType = {
    id: number;
    name: string;
};

const initialState: CategoryType[] = [];

export const categorySlice = createSlice({
    name: "categoryItems",
    initialState,
    reducers: {
        setInitialState: (_, action: PayloadAction<CategoryType[]>) => {
            return action.payload;
        },
        getAllCategoryItems: (state) => {
            return state;
        },
        getCategoryItems: (state, action: PayloadAction<number>) => {
            const quantity = action.payload;
            return state.slice(0, quantity);
        },
        searchCategoryItems: (state, action: PayloadAction<string>) => {
            const categoryName = action.payload;
            return state.filter((category) =>
                category.name.includes(categoryName)
            );
        },
    },
});

export const {
    setInitialState,
    getAllCategoryItems,
    getCategoryItems,
    searchCategoryItems,
} = categorySlice.actions;
export default categorySlice.reducer;

export const initializeCategoryState = () => async (dispatch: AppDispatch) => {
    try {
        const response = await fetchListAvailableCategory();
        const categories = response?.data as unknown as CategoryType[];
        dispatch(setInitialState(categories));
    } catch (error) {
        console.error("Failed to fetch categories:", error);
    }
};
