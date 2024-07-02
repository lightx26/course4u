import React, { useState } from "react";
import logo from "../../assets/images/logo.jpg";
import logo_c4u from "../../assets/images/logo_c4u.svg";
import "../../assets/css/login.css";
import { useSelector } from "react-redux";
import { userLogin } from "../../redux/slice/user.slice";
import { useDispatch } from "react-redux";
import { RootState } from "../../redux/store/store";
import { AppDispatch } from "../../redux/store/store";
import { useNavigate } from "react-router-dom";
const Login: React.FC = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState({ username: "", password: "" });
  const [showPassword, setShowPassword] = useState(false);
  const dispatch = useDispatch<AppDispatch>();
  const navigate = useNavigate();
  const statusLogin: string = useSelector(
    (state: RootState) => state.user.statusLogin
  );

  const validate = () => {
    const tempErrors = { username: "", password: "" };
    let isValid = true;
    if (username.trim() === "") {
      tempErrors.username = "Username is required";
      isValid = false;
    }

    if (password.trim() === "") {
      tempErrors.password = "Password is required";
      isValid = false;
    }

    setErrors(tempErrors);
    return isValid;
  };

  const handleSubmit = async (
    e: React.FormEvent,
    dispatch: AppDispatch,
    navigate: (path: string) => void,
    username: string,
    password: string
  ) => {
    e.preventDefault();
    if (validate()) {
      const result = await dispatch(userLogin({ username, password }));
      if (userLogin.fulfilled.match(result)) {
        const access_token = result.payload?.access_token;
        if (access_token) {
          navigate("/");
        }
      } else {
        // Handle the case where login failed
        console.error("Login failed");
      }
    }
  };
  const handleFormSubmit = async (e: React.FormEvent) => {
    await handleSubmit(e, dispatch, navigate, username, password);
  };

  return (
    <div className="w-full h-screen flex">
      <div
        className=" w-1/2 flex justify-center items-center "
        style={{ flexDirection: "column" }}
      >
        <form
          className="w-3/4 flex flex-col p-5 gap-4"
          onSubmit={handleFormSubmit}
        >
          {statusLogin == "failed" && (
            <div
              style={{
                backgroundColor: "#fff3f5",
                width: "100%",
                height: "50px",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                borderRadius: "10px",
                gap: "10px",
              }}
            >
              <div>
                <svg
                  width="20"
                  height="20"
                  viewBox="0 0 12 12"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    d="M6.43042 8.38037C6.58625 8.38037 6.71697 8.32757 6.82257 8.22197C6.92817 8.11637 6.98078 7.98584 6.98042 7.83037C6.98005 7.6749 6.92725 7.54437 6.82202 7.43877C6.71678 7.33317 6.58625 7.28037 6.43042 7.28037C6.27458 7.28037 6.14405 7.33317 6.03882 7.43877C5.93358 7.54437 5.88078 7.6749 5.88042 7.83037C5.88005 7.98584 5.93285 8.11655 6.03882 8.22252C6.14478 8.32848 6.27532 8.3811 6.43042 8.38037ZM5.88042 6.18037H6.98042V2.88037H5.88042V6.18037ZM6.43042 11.1304C5.66958 11.1304 4.95459 10.9859 4.28542 10.697C3.61625 10.408 3.03417 10.0163 2.53917 9.52162C2.04417 9.02699 1.65239 8.4449 1.36382 7.77537C1.07525 7.10584 0.930787 6.39084 0.930421 5.63037C0.930054 4.8699 1.07452 4.1549 1.36382 3.48537C1.65312 2.81584 2.0449 2.23375 2.53917 1.73912C3.03344 1.24449 3.61552 0.852704 4.28542 0.563771C4.95532 0.274838 5.67032 0.130371 6.43042 0.130371C7.19052 0.130371 7.90552 0.274838 8.57542 0.563771C9.24531 0.852704 9.8274 1.24449 10.3217 1.73912C10.8159 2.23375 11.2079 2.81584 11.4976 3.48537C11.7872 4.1549 11.9315 4.8699 11.9304 5.63037C11.9293 6.39084 11.7848 7.10584 11.497 7.77537C11.2092 8.4449 10.8174 9.02699 10.3217 9.52162C9.82593 10.0163 9.24385 10.4082 8.57542 10.6975C7.90698 10.9868 7.19198 11.1311 6.43042 11.1304ZM6.43042 10.0304C7.65875 10.0304 8.69917 9.60412 9.55166 8.75162C10.4042 7.89912 10.8304 6.8587 10.8304 5.63037C10.8304 4.40204 10.4042 3.36162 9.55166 2.50912C8.69917 1.65662 7.65875 1.23037 6.43042 1.23037C5.20208 1.23037 4.16167 1.65662 3.30917 2.50912C2.45667 3.36162 2.03042 4.40204 2.03042 5.63037C2.03042 6.8587 2.45667 7.89912 3.30917 8.75162C4.16167 9.60412 5.20208 10.0304 6.43042 10.0304Z"
                    fill="#EF1010"
                  />
                </svg>
              </div>
              <div>Invalid username or password. Please try again.</div>
            </div>
          )}

          <div className="flex flex-col gap-1">
            <label
              htmlFor="username"
              className="text-base font-normal text-gray-600"
            >
              Username
            </label>
            <input
              type="text"
              name="username"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="border border-gray-300 h-12 pl-3 rounded-lg text-sm font-normal outline-none"
              placeholder="Enter your username here"
            />
            {errors.username && (
              <span className="text-red-500 text-sm">{errors.username}</span>
            )}
          </div>
          <div className="flex flex-col gap-1">
            <div className="flex justify-between items-center">
              <label
                htmlFor="pwd"
                className="text-base font-normal text-gray-600"
              >
                Your password
              </label>
              {showPassword && (
                <div
                  className="flex justify-center items-center cursor-pointer"
                  onClick={() => setShowPassword(!showPassword)}
                >
                  <svg
                    width="24"
                    height="24"
                    viewBox="0 0 25 24"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                    style={{ marginTop: "1px" }}
                  >
                    <path
                      d="M20.0189 4.88109L19.283 4.14515C19.075 3.93716 18.691 3.96917 18.451 4.25711L15.8908 6.80108C14.7388 6.30514 13.4749 6.06514 12.1468 6.06514C8.1947 6.08107 4.77092 8.38504 3.1228 11.6973C3.02677 11.9052 3.02677 12.1612 3.1228 12.3372C3.89073 13.9052 5.04281 15.2012 6.48281 16.1772L4.38682 18.3051C4.14682 18.5451 4.11481 18.9291 4.27485 19.1371L5.0108 19.8731C5.21879 20.081 5.60277 20.049 5.84277 19.7611L19.8907 5.71321C20.1947 5.47334 20.2267 5.08938 20.0187 4.88137L20.0189 4.88109ZM12.9948 9.71298C12.7228 9.64896 12.4349 9.56901 12.1628 9.56901C10.8028 9.56901 9.71489 10.657 9.71489 12.0169C9.71489 12.2889 9.77891 12.5769 9.85887 12.8489L8.78675 13.9049C8.4668 13.345 8.29081 12.7209 8.29081 12.017C8.29081 9.88899 10.0028 8.17697 12.1308 8.17697C12.8349 8.17697 13.4588 8.35295 14.0188 8.67291L12.9948 9.71298Z"
                      fill="#666666"
                      fill-opacity="0.8"
                    />
                    <path
                      d="M21.1709 11.6974C20.6109 10.5774 19.8749 9.56945 18.963 8.75342L15.9869 11.6974V12.0174C15.9869 14.1454 14.2749 15.8574 12.1469 15.8574H11.8269L9.93896 17.7454C10.643 17.8893 11.379 17.9854 12.099 17.9854C16.0511 17.9854 19.4749 15.6814 21.123 12.3532C21.267 12.1292 21.267 11.9053 21.1709 11.6973L21.1709 11.6974Z"
                      fill="#666666"
                      fill-opacity="0.8"
                    />
                  </svg>
                  <div style={{ color: "#858585" }}>Hide</div>
                </div>
              )}
              {!showPassword && (
                <div
                  className="flex justify-center items-center cursor-pointer"
                  onClick={() => setShowPassword(!showPassword)}
                >
                  <svg
                    width="21"
                    height="21"
                    viewBox="0 0 24 24"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                    style={{ marginTop: "1px" }}
                  >
                    <path
                      d="M12 9C11.2044 9 10.4413 9.31607 9.87868 9.87868C9.31607 10.4413 9 11.2044 9 12C9 12.7956 9.31607 13.5587 9.87868 14.1213C10.4413 14.6839 11.2044 15 12 15C12.7956 15 13.5587 14.6839 14.1213 14.1213C14.6839 13.5587 15 12.7956 15 12C15 11.2044 14.6839 10.4413 14.1213 9.87868C13.5587 9.31607 12.7956 9 12 9ZM12 17C10.6739 17 9.40215 16.4732 8.46447 15.5355C7.52678 14.5979 7 13.3261 7 12C7 10.6739 7.52678 9.40215 8.46447 8.46447C9.40215 7.52678 10.6739 7 12 7C13.3261 7 14.5979 7.52678 15.5355 8.46447C16.4732 9.40215 17 10.6739 17 12C17 13.3261 16.4732 14.5979 15.5355 15.5355C14.5979 16.4732 13.3261 17 12 17ZM12 4.5C7 4.5 2.73 7.61 1 12C2.73 16.39 7 19.5 12 19.5C17 19.5 21.27 16.39 23 12C21.27 7.61 17 4.5 12 4.5Z"
                      fill="#858585"
                    />
                  </svg>
                  <div style={{ color: "#858585" }}>Show</div>
                </div>
              )}
            </div>

            <input
              type={showPassword ? "text" : "password"}
              name="pwd"
              id="pwd"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="border border-gray-300 h-12 pl-3 rounded-lg text-sm font-normal outline-none"
              placeholder="Enter your password here"
            />
            {errors.password && (
              <span className="text-red-500 text-sm">{errors.password}</span>
            )}
          </div>
          <div className="flex justify-end items-center">
            <div>
              <a
                href=""
                className="text-base text-black underline hover:text-purple"
              >
                Forgot password
              </a>
            </div>
          </div>
          {(statusLogin == "idle" || statusLogin == "failed") && (
            <div>
              <button
                className="border border-gray-300 w-full h-12 text-white bg-btnLoginHover rounded-lg "
                type="submit"
              >
                Sign in
              </button>
            </div>
          )}
          {statusLogin == "loading" && (
            <div>
              <button
                className="border border-gray-300 w-full h-12 text-white bg-btnLoginHover rounded-lg "
                type="submit"
                style={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                  gap: "10px",
                }}
              >
                <div className="lds-roller">
                  <div></div>
                  <div></div>
                  <div></div>
                  <div></div>
                  <div></div>
                  <div></div>
                  <div></div>
                  <div></div>
                </div>
                Sign in
              </button>
            </div>
          )}
          <div>
            <span>
              Don't have an account?{" "}
              <a href="" className="underline hover:text-purple">
                Sign up
              </a>
            </span>
          </div>
        </form>
      </div>

      <div className="w-1/2 relative">
        <img
          src={logo}
          alt=""
          className="w-full h-full absolute left-0 right-0 object-cover object-center"
        />
        <div className="absolute top-1/4 left-8">
          <div
            style={{
              backgroundColor: "white",
              width: "120px",
              height: "120px",
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              borderRadius: "50%",
            }}
          >
            <div className="relative" style={{ width: "65px", height: "72px" }}>
              <img
                src={logo_c4u}
                alt=""
                className="absolute left-0 top-0 object-cover object-center w-full h-full"
              />
            </div>
          </div>
          <div className="text-white font-semibold text-4xl my-1">Course4U</div>
          <div className="text-white font-normal text-2xl">
            Learn Efficiently - Earn Your Rewards
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;
