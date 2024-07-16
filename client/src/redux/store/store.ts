import { configureStore, ThunkAction, Action } from "@reduxjs/toolkit";
import counterReducer from "../slice/counter.slice";
import createSagaMiddleware from "redux-saga";
import RootSaga from "../saga/root.saga";
import userReducer from "../slice/user.slice";
import filterItemCheckboxSlice from "../slice/filterItemCheckbox.slice";
import categoryReducer from "../slice/category.slice";
const sagaMiddleware = createSagaMiddleware();
export const store = configureStore({
  reducer: {
    counter: counterReducer,
    user: userReducer,
    filter: filterItemCheckboxSlice,
    category: categoryReducer
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(sagaMiddleware),
});
sagaMiddleware.run(RootSaga);

export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof store.getState>;
export type AppThunk<ReturnType = void> = ThunkAction<
  ReturnType,
  RootState,
  unknown,
  Action<string>
>;
