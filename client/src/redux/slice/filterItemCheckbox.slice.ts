import { createSlice, PayloadAction } from "@reduxjs/toolkit";

// Định nghĩa trạng thái ban đầu
const initialState: string[] = [];

// Tạo slice
export const filterItemSlice = createSlice({
    name: "filterItems",
    initialState,
    reducers: {
        addFilterItem: (state, action: PayloadAction<string>) => {
            state.push(action.payload);
        },

        removeFilterItem: (state, action: PayloadAction<string>) => {
            return state.filter(item => item !== action.payload);
        },

        toggleFilterItem: (state, action: PayloadAction<string>) => {
            const index = state.findIndex(item => item === action.payload);
            if (index !== -1) {
                state.splice(index, 1);
            } else {
                state.push(action.payload);
            }
        },
    },
});

export const { addFilterItem, removeFilterItem, toggleFilterItem } = filterItemSlice.actions;

export default filterItemSlice.reducer;