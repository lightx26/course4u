import { createSlice } from "@reduxjs/toolkit";
import { Status } from "../../utils/index.ts";
export type OverviewMyRegistrationType = {
  id?: string;
  courseName: string;
  status: Status;
  coursePlatform: string;
  startDate: Date;
  endDate: Date;
  courseThumbnailUrl: string;
};
interface IRegistration {
  inputSearch: string;
  status: string;
  sortBy: string;
  currentPage: number;
  totalItem: number;
  data: OverviewMyRegistrationType[];
}

// Create an initial state
const initialState: IRegistration = {
  inputSearch: "",
  status: "SUBMITTED",
  sortBy: "Last modified",
  currentPage: 1,
  totalItem: 0,
  data: [],
};

// Create a slice
export const registrationSlice = createSlice({
  name: "registration",
  initialState,
  reducers: {
    handleChangeInputSearch: (state, action) => {
      state.inputSearch = action.payload;
    },
    handleChangeStatus: (state, action) => {
      state.status = action.payload;
      state.currentPage = 1;
    },
    handleChangeFilterBy: (state, action) => {
      state.sortBy = action.payload;
    },
    handleChangeCurrentPage: (state, action) => {
      state.currentPage = action.payload;
    },
    handleChangeTotalItem: (state, action) => {
      state.totalItem = action.payload;
    },
    saveDataListRegistration: (state, action) => {
      state.data = action.payload.list;
      state.totalItem = action.payload.totalElements;
    },
  },
  extraReducers: () => {},
});

export const {
  handleChangeInputSearch,
  handleChangeStatus,
  handleChangeFilterBy,
  handleChangeCurrentPage,
  saveDataListRegistration,
} = registrationSlice.actions;


// Export the reducer
export default registrationSlice.reducer;
