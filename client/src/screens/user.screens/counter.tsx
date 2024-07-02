import type { RootState } from "../../redux/store/store";
import { useSelector } from "react-redux";
import {
  decrement,
  increment,
  increaseSagaStart,
  decreaseSagaStart,
} from "../../redux/slice/counter.slice";
import { useDispatch } from "react-redux";

const Counter = () => {
  const count = useSelector((state: RootState) => state.counter.value);
  const countSaga = useSelector(
    (state: RootState) => state.counter.valueForSaga
  );
  const dispatch = useDispatch();
  return (
    <div>
      <div>
        <p style={{ color: "black" }}>Redux Toolkit</p>
        <button
          aria-label="Increment value"
          onClick={() => dispatch(increment())}
          style={{
            color: "black",
            border: "1px solid #ccc",
            width: "100px",
            height: "30px",
            borderRadius: "5px",
            margin: "0px 10px",
          }}
        >
          Increment
        </button>
        <span style={{ color: "black" }}>{count}</span>
        <button
          aria-label="Decrement value"
          onClick={() => dispatch(decrement())}
          style={{
            color: "black",
            border: "1px solid #ccc",
            width: "100px",
            height: "30px",
            borderRadius: "5px",
            margin: "0px 10px",
          }}
        >
          Decrement
        </button>
      </div>

      <div>
        <p style={{ color: "black" }}>Redux Saga</p>
        <button
          aria-label="Increment value"
          onClick={() => dispatch(increaseSagaStart(2))}
          style={{
            color: "black",
            border: "1px solid #ccc",
            width: "100px",
            height: "30px",
            borderRadius: "5px",
            margin: "0px 10px",
          }}
        >
          Increment
        </button>
        <span style={{ color: "black" }}>{countSaga}</span>
        <button
          aria-label="Decrement value"
          onClick={() => dispatch(decreaseSagaStart(2))}
          style={{
            color: "black",
            border: "1px solid #ccc",
            width: "100px",
            height: "30px",
            borderRadius: "5px",
            margin: "0px 10px",
          }}
        >
          Decrement
        </button>
      </div>
    </div>
  );
};
export default Counter;
