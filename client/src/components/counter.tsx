import type { RootState } from "../redux/store/store";
import { useSelector, useDispatch } from "react-redux";
import {
  decrement,
  increment,
  increaseSagaStart,
  decreaseSagaStart,
} from "../redux/slice/counter.slice";

const Counter = () => {
  const count = useSelector((state: RootState) => state.counter.value);
  const countSaga = useSelector(
    (state: RootState) => state.counter.valueForSaga
  );
  const dispatch = useDispatch();

  return (
    <div>
      <div>
        <p>Redux Toolkit</p>
        <button
          aria-label="Increment value"
          onClick={() => dispatch(increment())}
        >
          Increment
        </button>
        <span>{count}</span>
        <button
          aria-label="Decrement value"
          onClick={() => dispatch(decrement())}
        >
          Decrement
        </button>
      </div>

      <div>
        <p>Redux Saga</p>
        <button
          aria-label="Increment value"
          onClick={() => dispatch(increaseSagaStart(2))}
        >
          Increment
        </button>
        <span>{countSaga}</span>
        <button
          aria-label="Decrement value"
          onClick={() => dispatch(decreaseSagaStart(2))}
        >
          Decrement
        </button>
      </div>
    </div>
  );
};
export default Counter;
