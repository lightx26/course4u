import { configureStore, ThunkAction, Action } from "@reduxjs/toolkit";
import counterReducer from "../slice/counter.slice";
import createSagaMiddleware from "redux-saga";
import RootSaga from "../saga/root.saga";
import userReducer from "../slice/user.slice";
import filterItemCheckboxSlice from "../slice/filterItemCheckbox.slice";
import courseReducer from "../slice/course.slice";
import categoryReducer from "../slice/category.slice";
import registrationReducer from "../slice/registration.slice";
const sagaMiddleware = createSagaMiddleware();
export const store = configureStore({
  reducer: {
    counter: counterReducer,
    user: userReducer,
    filter: filterItemCheckboxSlice,
    courses: courseReducer,
    category: categoryReducer,
    registration: registrationReducer,
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
