import { createSlice, createAction } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";

export interface CounterState {
  value: number;
  valueForSaga: number;
  status: "idle" | "loading" | "failed";
}

//create an initial state
const initialState: CounterState = {
  value: 0,
  valueForSaga: 0,
  status: "idle",
};

//create action for saga
export const increaseSagaStart = createAction<number>("increaseSagaStart");
export const decreaseSagaStart = createAction<number>("decreaseSagaStart");
export const increaseSagaFinish = createAction<number>("increaseSagaFinish");
export const decreaseSagaFinish = createAction<number>("decreaseSagaFinish");

//create a slice
export const counterSlice = createSlice({
  name: "counter",
  initialState,
  reducers: {
    increment: (state) => {
      state.value += 1;
    },
    decrement: (state) => {
      state.value -= 1;
    },
    incrementByAmount: (state, action: PayloadAction<number>) => {
      state.value += action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(increaseSagaStart, (state) => {
        state.status = "loading";
      })
      .addCase(decreaseSagaStart, (state) => {
        state.status = "loading";
      })
      .addCase(increaseSagaFinish, (state, action) => {
        state.status = "idle";
        state.valueForSaga += action.payload;
      })
      .addCase(decreaseSagaFinish, (state, action) => {
        state.status = "idle";
        state.valueForSaga -= action.payload;
      });
  },
});

// Action creators are generated for each case reducer function
export const { increment, decrement, incrementByAmount } = counterSlice.actions;

export default counterSlice.reducer;
