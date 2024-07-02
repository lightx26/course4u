import { takeEvery, put } from "redux-saga/effects";
import {
  decreaseSagaStart,
  increaseSagaStart,
  decreaseSagaFinish,
  increaseSagaFinish,
} from "../slice/counter.slice";
import type { PayloadAction } from "@reduxjs/toolkit";
function* handleIncreaseSaga(action: PayloadAction<number>) {
  yield put(increaseSagaFinish(action.payload));
}
function* handleDecreaseSaga(action: PayloadAction<number>) {
  yield put(decreaseSagaFinish(action.payload));
}
function* counterSaga() {
  yield takeEvery(increaseSagaStart, handleIncreaseSaga);
  yield takeEvery(decreaseSagaStart, handleDecreaseSaga);
}
export default counterSaga;
