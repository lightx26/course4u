import React, { useState } from "react";
import logo from "../../assets/images/logo.jpg";
import logo_c4u from "../../assets/images/logo_c4u.svg";
import "../../assets/css/login.css";
import { useSelector } from "react-redux";
import { userRegister } from "../../redux/slice/user.slice";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { RootState } from "../../redux/store/store";
import "../../assets/css/login.css";
import { useAppDispatch } from "../../redux/store/hook";
interface ISignUpRequest {
  username: string;
  password: string;
  confirmPassword: string;
  email: string;
  fullname: string;
  dateofbirth: string | null;
  gender: string | null;
}

const SignUp: React.FC = () => {
  const [formData, setFormData] = useState({
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
    fullname: "",
    dateOfBirth: "",
    gender: "",
  });

  const [errors, setErrors] = useState({
    username: "",
    email: "",
    password: "",
    fullname: "",
    dateOfBirth: "",
  });

  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [statusChangeGender, setStatusChangeGender] = useState(false);
  const [statusChangeDateOfBirth, setStatusChangeDateOfBirth] = useState(false);

  const navigate = useNavigate();
  function hasSpecialChar(str: string) {
    const specialCharsRegex = /[`~!@#$%^&*()\-_=+{};:'"\\|,.<>\/? ]+/;
    return specialCharsRegex.test(str);
  }

  const dispatch = useAppDispatch();
  const statusRegister: string = useSelector(
    (state: RootState) => state.user.statusRegister
  );

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });

    switch (name) {
      case "username":
        if (hasSpecialChar(value)) {
          setErrors((prevErrors) => ({
            ...prevErrors,
            username: "Username cannot have special characters.",
          }));
        } else if (!value) {
          setErrors((prevErrors) => ({
            ...prevErrors,
            username: "Username is required.",
          }));
        } else {
          setErrors((prevErrors) => ({ ...prevErrors, username: "" }));
        }
        break;

      case "email":
        const emailPattern = /^[a-zA-Z0-9._%+-]+@mgm-tp\.com$/;
        if (!value) {
          setErrors((prevErrors) => ({
            ...prevErrors,
            email: "Email is required.",
          }));
        } else if (!emailPattern.test(value)) {
          setErrors((prevErrors) => ({
            ...prevErrors,
            email: "Please enter a valid email address (*@mgm-tp.com).",
          }));
        } else {
          setErrors((prevErrors) => ({ ...prevErrors, email: "" }));
        }
        break;

      default:
        break;
    }
  };

  function validatePassword(password: string, confirmPassword: string) {
    const minLength = /^.{8,}$/;
    const hasUpperCase = /[A-Z]/;
    const hasNumber = /\d/;
    const hasSpecialChar = /[`~!@#$%^&*()\-_=+{};:'"\\|,.<>\/?]+/;

    const errors = [];

    if (!password || !confirmPassword)
      return "Password and confirm password is required.";
    if (!minLength.test(password)) errors.push("8 characters long");
    if (!hasUpperCase.test(password)) errors.push("one uppercase letter");
    if (!hasNumber.test(password)) errors.push("one number");
    if (!hasSpecialChar.test(password)) errors.push("one special character");

    if (errors.length > 0) {
      return `Password requirements: must be at least ${errors.join(", ")}.`;
    }

    if (password !== confirmPassword) {
      return "Password and confirm password must be the same.";
    }

    return "";
  }

  function convertDateFormat(dateString: string) {
    // Split the input date string by the hyphen
    const [year, month, day] = dateString.split("-");

    // Return the date in dd/mm/yyyy format
    return `${day}/${month}/${year}`;
  }
  const handleSubmitForm = async (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    const {
      username,
      email,
      password,
      confirmPassword,
      fullname,
      dateOfBirth,
    } = formData;
    let formHasError = false;

    // Validate username
    if (!username) {
      setErrors((prevErrors) => ({
        ...prevErrors,
        username: "Username is required.",
      }));
      formHasError = true;
    } else if (username.length > 50) {
      setErrors((prevErrors) => ({
        ...prevErrors,
        username: "Max length for username is 50 characters.",
      }));
      formHasError = true;
    }

    // Validate email
    if (!email) {
      setErrors((prevErrors) => ({
        ...prevErrors,
        email: "Email is required.",
      }));
      formHasError = true;
    }

    // Validate password
    const passwordError = validatePassword(password, confirmPassword);
    if (passwordError) {
      setErrors((prevErrors) => ({ ...prevErrors, password: passwordError }));
      formHasError = true;
    }

    // Validate fullname
    if (fullname && fullname.length > 50) {
      setErrors((prevErrors) => ({
        ...prevErrors,
        fullname: "Max length for fullname is 50 characters.",
      }));
      formHasError = true;
    }

    if (dateOfBirth) {
      const today = new Date();
      const selectedDate = new Date(dateOfBirth);
      if (selectedDate > today) {
        setErrors((prevErrors) => ({
          ...prevErrors,
          dateOfBirth: "Date of birth cannot be a future date.",
        }));
        formHasError = true;
      } else {
        setErrors((prevErrors) => ({
          ...prevErrors,
          dateOfBirth: "",
        }));
      }
    }

    if (formHasError) {
      return;
    } else {
      const dataToSubmit: ISignUpRequest = {
        username: formData.username,
        email: formData.email,
        password: formData.password,
        confirmPassword: formData.confirmPassword,
        fullname: formData.fullname,
        dateofbirth: formData.dateOfBirth
          ? convertDateFormat(formData.dateOfBirth)
          : "",
        gender: formData.gender,
      };
      const result = await dispatch(userRegister(dataToSubmit));
      if (userRegister.fulfilled.match(result)) {
        const message = result.payload?.message;
        if (message === "User registered successfully") {
          toast("User registered successfully", {
            description:
              "You have been successfully registered. Welcome to Course4U!",
          });
          navigate("/login", {
            state: {
              username: result.payload?.username,
            },
          });
        } else if (message === "Email is already in use.") {
          setErrors((prevErrors) => ({
            ...prevErrors,
            email: "Email is already in use.",
          }));
        }
      }
    }
  };
  return (
    <div className="w-full h-screen flex">
      <div className=" w-1/2 flex justify-center items-center direction-column ">
        <form action="" className="w-3/4 flex flex-col p-5 gap-4">
          <div className="flex flex-col gap-1">
            <label
              htmlFor="username"
              className="text-sm font-normal text-gray-600"
            >
              Username<span className="text-red-500">*</span>
            </label>
            <input
              type="text"
              id="username"
              name="username"
              className={`border h-10 pl-3 rounded-lg text-sm font-normal outline-none ${
                errors.username ? "border-red-500" : "border-gray-300"
              }`}
              placeholder="Enter your username here"
              value={formData.username}
              onChange={handleChange}
              // title="Username cannot have special characters"
              // required
              pattern="[a-z0-9]+"
            />
            {errors.username && (
              <div className="text-red-500 text-sm">{errors.username}</div>
            )}
          </div>
          <div className="flex flex-col gap-1">
            <label
              htmlFor="email"
              className="text-sm font-normal text-gray-600"
            >
              Email address<span className="text-red-500">*</span>
            </label>
            <input
              type="email"
              id="email"
              name="email"
              className={`border h-10 pl-3 rounded-lg text-sm font-normal outline-none ${
                errors.email ? "border-red-500" : "border-gray-300"
              }`}
              placeholder="Enter your email here"
              // required
              value={formData.email}
              onChange={handleChange}
            />
            {errors.email && (
              <div className="text-red-500 text-sm">{errors.email}</div>
            )}
          </div>
          <div className="flex flex-col gap-1">
            <div className="flex justify-between items-center gap-5">
              <div className="flex flex-col gap-1 w-1/2">
                <div className="flex justify-between items-center">
                  <label
                    htmlFor="password"
                    className="text-sm font-normal text-gray-600"
                  >
                    Your password<span className="text-red-500">*</span>
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
                    </div>
                  )}
                </div>
                <label
                  htmlFor="password"
                  className="text-sm font-normal text-gray-600"
                >
                  Your password<span className="text-red-500">*</span>
                </label>

                <input
                  type={showPassword ? "text" : "password"}
                  id="password"
                  name="password"
                  className="border border-gray-300 h-10 pl-3 rounded-lg text-sm font-normal outline-none"
                  placeholder="Your password"
                  title="
                  Password requirements: must be at least 8 characters long and include 
at least one uppercase letter, one special character, and one number."
                  value={formData.password}
                  onChange={(e) =>
                    setFormData({ ...formData, password: e.target.value })
                  }
                />
              </div>
              <div className="flex flex-col gap-1 w-1/2">
                <div className="flex justify-between items-center">
                  <label
                    htmlFor="confirm_password"
                    className="text-sm font-normal text-gray-600"
                  >
                    Confirm password<span className="text-red-500">*</span>
                  </label>
                  {showConfirmPassword && (
                    <div
                      className="flex justify-center items-center cursor-pointer"
                      onClick={() =>
                        setShowConfirmPassword(!showConfirmPassword)
                      }
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
                    </div>
                  )}
                  {!showConfirmPassword && (
                    <div
                      className="flex justify-center items-center cursor-pointer"
                      onClick={() =>
                        setShowConfirmPassword(!showConfirmPassword)
                      }
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
                    </div>
                  )}
                </div>
                <label
                  htmlFor="confirm_password"
                  className="text-sm font-normal text-gray-600"
                >
                  Confirm password<span className="text-red-500">*</span>
                </label>

                <input
                  type={showConfirmPassword ? "text" : "password"}
                  id="confirm_password"
                  name="confirm_password"
                  className="border border-gray-300 h-10 pl-3 rounded-lg text-sm font-normal outline-none"
                  placeholder="Confirm your password"
                  value={formData.confirmPassword}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      confirmPassword: e.target.value,
                    })
                  }
                />
              </div>
            </div>
            {errors.password && (
              <div className="text-red-500 text-sm">{errors.password}</div>
            )}
          </div>

          <div className="flex flex-col gap-1">
            <label
              htmlFor="fullname"
              className="text-sm font-normal text-gray-600"
            >
              Fullname
            </label>
            <input
              type="text"
              id="fullname"
              name="fullname"
              className="border border-gray-300 h-10 pl-3 rounded-lg text-sm font-normal outline-none"
              placeholder="Enter your fullname here"
              value={formData.fullname}
              onChange={(e) =>
                setFormData({ ...formData, fullname: e.target.value })
              }
            />
            {errors.fullname && (
              <div className="text-red-500 text-sm">{errors.fullname}</div>
            )}
          </div>
          <div className="flex flex-col gap-1">
            <div className="flex justify-between items-center gap-5">
              <div className="flex flex-col gap-1 w-1/2">
                <label
                  htmlFor="dateofbirth"
                  className="text-sm font-normal text-gray-600"
                >
                  Date Of Birth
                </label>
                <input
                  type="date"
                  id="dateofbirth"
                  name="dateofbirth"
                  className="border border-gray-300 h-10 pl-3 pr-4 rounded-lg text-sm font-normal outline-none"
                  onChange={(e) => {
                    setStatusChangeDateOfBirth(true);
                    setFormData({ ...formData, dateOfBirth: e.target.value });
                  }}
                  style={{
                    color: statusChangeDateOfBirth ? "black" : "#a4abb6",
                  }}
                  onKeyDown={(e) => e.preventDefault()}
                />
              </div>
              <div className="flex flex-col gap-1 w-1/2">
                <label
                  htmlFor="gender"
                  className="text-sm font-normal text-gray-600"
                >
                  Gender
                </label>
                <select
                  name="gender"
                  id="gender"
                  className="border border-gray-300 h-10 pl-3 pr-4 rounded-lg text-sm font-normal outline-none"
                  onChange={(e) => {
                    setStatusChangeGender(true);
                    setFormData({ ...formData, gender: e.target.value });
                  }}
                  defaultValue=""
                  style={{ color: statusChangeGender ? "black" : "#a4abb6" }}
                >
                  <option value="" disabled hidden>
                    Gender
                  </option>
                  <option value="MALE" style={{ color: "black" }}>
                    Male
                  </option>
                  <option value="FEMALE" style={{ color: "black" }}>
                    Female
                  </option>
                </select>
              </div>
            </div>

            {errors.dateOfBirth && (
              <div className="text-red-500 text-sm">{errors.dateOfBirth}</div>
            )}
          </div>

          {statusRegister === "pending" ? (
            <div>
              <button
                className="border border-gray-300 w-full h-12 text-white bg-black rounded-2xl"
                type="submit"
                onClick={(e) => handleSubmitForm(e)}
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
                Sign up
              </button>
            </div>
          ) : (
            <div>
              <button
                className="border border-gray-300 w-full h-12 text-white bg-black rounded-2xl"
                type="submit"
                onClick={(e) => handleSubmitForm(e)}
              >
                Sign up
              </button>
            </div>
          )}
          <div>
            <span>
              You have an account?{" "}
              <a href="/login" className="underline hover:text-purple">
                Login
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
          <div className="bg-white w-[120px] h-[120px] rounded-full flex justify-center items-center">
            <div className="relative w-[65px] h-[72px]">
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

export default SignUp;
