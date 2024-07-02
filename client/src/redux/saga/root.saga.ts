import { all } from "redux-saga/effects";
import counterSaga from "./counter.saga";
function* RootSaga() {
  yield all([counterSaga()]);
}

export default RootSaga;
