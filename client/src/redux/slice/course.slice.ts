import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { CourseType } from "../../App";
import { RootState } from "../store/store";
import { fetchListAvailableCourse } from "../../service/course";

export type CourseStateType = {
    searchTerm?: string;
    sortBy?: string;
    limit?: number;
    content: CourseType[];
    empty: boolean;
    first: boolean;
    last: boolean;
    number: number;
    //Number element in current page
    numberOfElements: number;
    //No use data of pageable (May be wrong or bug sometimes)
    pageable: {
        //Current page
        pageNumber: number;
        //Page size
        pageSize: number;
        sort: {
            empty: boolean;
            sorted: boolean;
            unsorted: boolean;
        };
        offset: number;
        unpaged: boolean;
    };
    //Page size
    size: number;
    sort: {
        empty: boolean;
        sorted: boolean;
        unsorted: boolean;
    };
    //Total element in all page
    totalElements: number;
    //Total page
    totalPages: number;
};

// Define the initial state
const initialState: CourseStateType = {
    searchTerm: "",
    sortBy: "NEWEST",
    limit: 8,
    content: [],
    empty: false,
    first: false,
    last: false,
    number: 1,
    numberOfElements: 0,
    pageable: {
        pageNumber: 0,
        pageSize: 0,
        sort: {
            empty: false,
            sorted: false,
            unsorted: false,
        },
        offset: 0,
        unpaged: false,
    },
    size: 0,
    sort: {
        empty: false,
        sorted: false,
        unsorted: false,
    },
    totalElements: 0,
    totalPages: 0,
};

// Define the async thunk
export const searchCoursesByFilterNameAndSortBy = createAsyncThunk(
    "courses/searchCoursesByFilterNameAndSortBy",
    async (_, { getState }) => {
        // get all state

        const state: RootState = getState() as RootState;
        // get filter state
        const filterState = state.filter;
        const courseState = state.courses;
        // Get Category, level, platform
        const extractFilterIds = (filterComponentId: string) =>
            filterState
                .filter((item) => item.FilterComponentId === filterComponentId)
                .flatMap((item) =>
                    item.listChoice.map((choice) => choice.id.toUpperCase())
                );
        // Get all filter
        const params = {
            searchTerm: courseState.searchTerm,
            categoryFilter: extractFilterIds("Category"),
            levelFilter: extractFilterIds("Level"),
            platformFilter: extractFilterIds("Platform"),
            rating: extractFilterIds("Rating"),
            page: courseState.number ?? 1,
            limit: courseState.limit ?? 8,
            sortBy: courseState.sortBy,
        };
        const response = await fetchListAvailableCourse(params);
        return {
            ...response?.data,
            searchTerm: courseState.searchTerm,
            sortBy: courseState.sortBy,
            number: response?.data.number + 1,
        };
    }
);

// Create the slice
export const courseSlice = createSlice({
    name: "course",
    initialState,
    reducers: {
        updateSearch: (state, action) => {
            state.searchTerm = action.payload;
        },
        updateSort: (state, action) => {
            state.sortBy = action.payload;
        },
        updatePage: (state, action) => {
            state.number = action.payload;
        },
    },
    extraReducers: (builder) => {
        builder.addCase(
            searchCoursesByFilterNameAndSortBy.fulfilled,
            (_, action) => {
                return action.payload;
            }
        );
    },
});

// Export the action creators
export const { updateSearch, updateSort, updatePage } = courseSlice.actions;

export default courseSlice.reducer;
