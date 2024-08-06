import {createSlice} from "@reduxjs/toolkit";
import {Status} from "../../utils/index.ts";

// Define registrations type
export type OverviewRegistrationsType = {
    id?: string;
    courseName: string;
    status: Status;
    coursePlatform: string;
    startDate: Date;
    endDate: Date;
    courseThumbnailUrl: string;
};

export type RegistrationParamsType = {
    status: string
    search: string
    orderBy: string
    isAscending: boolean
}

interface IAdminRegistration {
    options: RegistrationParamsType;
    view: string;
    showingMessage: string;
    currentPage: number;
    totalItem: number;
    data: OverviewRegistrationsType[];
}

// Define an initial State
const initialState: IAdminRegistration = {
    options: {
        status: "All",
        search: "",
        orderBy: "id",
        isAscending: false
    },
    view: "grid",
    showingMessage: "",
    currentPage: 1,
    totalItem: 0,
    data: [],
}

// Create a slice
export const adminPageRegistrationsSlice = createSlice({
    name: "adminRegistrationPage",
    initialState,
    reducers: {
        handleCurrentPageChange: (state, action) => {
            state.currentPage = action.payload;
        },
        handleTotalItemChange: (state, action) => {
            state.totalItem = action.payload;
        },
        handleShowingMessageChange: (state, action) => {
            state.showingMessage = action.payload;
        },
        saveRegistrationsData: (state, action) => {
            state.data = action.payload;
            state.totalItem = action.payload.totalElements;
        },
        handleOptionsChangeForAdmin: (state, action) => {
            state.options = action.payload;
        },
        refreshAdmin: (state) => {
            state.options = initialState.options;
            state.currentPage = 1;
            state.view = 'grid';
        },
        setAdminView: (state, action) => {
            state.view = action.payload;
        }
    },
    extraReducers: () => {},
});

export const {
    handleCurrentPageChange,
    handleTotalItemChange,
    handleShowingMessageChange,
    saveRegistrationsData,
    handleOptionsChangeForAdmin,
    refreshAdmin,
    setAdminView
} = adminPageRegistrationsSlice.actions

export const adminInitialStateOption: RegistrationParamsType = initialState.options;

// Export the reducer
export default adminPageRegistrationsSlice.reducer;