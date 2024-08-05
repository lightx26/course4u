import {createSlice} from "@reduxjs/toolkit";
import { Status } from "../../utils/index.ts";

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

interface IAccountantRegistration {
    options: RegistrationParamsType;
    showingMessage: string;
    currentPage: number;
    totalItem: number;
    data: OverviewRegistrationsType[];
}

// Define an initial State
const initialState: IAccountantRegistration = {
    options: {
        status: "Verified",
        search: "",
        orderBy: "id",
        isAscending: false
    },
    showingMessage: "",
    currentPage: 1,
    totalItem: 0,
    data: [],
}

export const accountantPageRegistrationsSlice = createSlice({
    name: "accountantRegistrationPage",
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
        handleOptionsChangeForAccountant: (state, action) => {
            state.options = action.payload;
        },
        refreshAccountant: (state) => {
            state.options = initialState.options;
            state.currentPage = 1;
        }
    },
    extraReducers: () => {},
});

export const {
    handleCurrentPageChange,
    handleTotalItemChange,
    handleShowingMessageChange,
    saveRegistrationsData,
    handleOptionsChangeForAccountant,
    refreshAccountant
} = accountantPageRegistrationsSlice.actions

// Export the reducer
export default accountantPageRegistrationsSlice.reducer;