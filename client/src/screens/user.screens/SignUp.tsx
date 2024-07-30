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
import { DatePicker, Input } from "antd";
import type { DatePickerProps } from "antd";
import PasswordInput from "../../components/user.components/personal/InputPassword";
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

  const [statusChangeGender, setStatusChangeGender] = useState(false);

  const navigate = useNavigate();
  function hasSpecialChar(str: string) {
    const specialCharsRegex = /[!@#$%^&*(),.?":{}|<>`~\[\]\\\/';=+\-_\s]/;
    return specialCharsRegex.test(str);
  }

  const dispatch = useAppDispatch();
  const statusRegister: string = useSelector(
    (state: RootState) => state.user.statusRegister
  );
  function containsVietnameseAccent(str: string) {
    // Regular expression to match Vietnamese characters with accents
    const vietnameseRegex =
      /[àáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐ]/;
    return vietnameseRegex.test(str);
  }

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
        } else if (containsVietnameseAccent(value)) {
          setErrors((prevErrors) => ({
            ...prevErrors,
            username: "Username cannot have Vietnamese accents.",
          }));
        } else if (value.length > 50) {
          setErrors((prevErrors) => ({
            ...prevErrors,
            username: "Max length for username is 50 characters.",
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
    const hasSpecialChar = /[`~!@#$%^&*()\-_=+{};:'"\\|,.<>/?]+/;

    const errors = [];

    if (!password || !confirmPassword)
      return "Password and confirm password are required.";
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
    } else if (hasSpecialChar(username)) {
      setErrors((prevErrors) => ({
        ...prevErrors,
        username: "Username cannot have special characters.",
      }));
      formHasError = true;
    } else if (username.length > 50) {
      setErrors((prevErrors) => ({
        ...prevErrors,
        username: "Max length for username is 50 characters.",
      }));
      formHasError = true;
    } else if (containsVietnameseAccent(username)) {
      setErrors((prevErrors) => ({
        ...prevErrors,
        username: "Username cannot have Vietnamese accents.",
      }));
      formHasError = true;
    }

    if (!email) {
      // Validate email
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
        username: formData.username.toLowerCase(),
        email: formData.email.toLowerCase(),
        password: formData.password,
        confirmPassword: formData.confirmPassword,
        fullname: formData.fullname.trim(),
        dateofbirth: formData.dateOfBirth,
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
        } else if (message === "Username is already in use.") {
          setErrors((prevErrors) => {
            return { ...prevErrors, username: "Username is already in use." };
          });
        }
      }
    }
  };

  const handleChangeDateOfBirth: DatePickerProps["onChange"] = (
    _,
    dateString
  ) => {
    setFormData({ ...formData, dateOfBirth: dateString.toString() });
  };
  return (
    <div className="flex w-full h-screen select-none">
      <div className="relative w-1/2">
        <img
          src={logo}
          alt=""
          className="absolute left-0 right-0 object-cover object-center w-full h-full"
        />
        <div className="absolute top-1/4 left-8">
          <div className="bg-white w-[120px] h-[120px] rounded-full flex justify-center items-center">
            <div className="relative w-[65px] h-[72px]">
              <img
                src={logo_c4u}
                alt=""
                className="absolute top-0 left-0 object-cover object-center w-full h-full"
              />
            </div>
          </div>
          <div className="my-1 text-4xl font-semibold text-white">Course4U</div>
          <div className="text-2xl font-normal text-white">
            Wishing you success on your path to learning
            <br />
            and self-improvement!
          </div>
        </div>
      </div>
      <div className="flex items-center justify-center w-1/2 direction-column">
        <form action="" className="flex flex-col w-3/4 gap-4 p-5">
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
              className={`border h-10 pl-3 rounded-lg text-sm font-normal outline-none ${errors.username ? "border-red-500" : "border-gray-300"
                }`}
              placeholder="Enter your username here"
              value={formData.username}
              onChange={handleChange}
              // title="Username cannot have special characters"
              // required
              pattern="[a-z0-9]+"
            />
            {errors.username && (
              <div className="text-sm text-red-500">{errors.username}</div>
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
              className={`border h-10 pl-3 rounded-lg text-sm font-normal outline-none ${errors.email ? "border-red-500" : "border-gray-300"
                }`}
              placeholder="Enter your email here"
              // required
              value={formData.email}
              onChange={handleChange}
            />
            {errors.email && (
              <div className="text-sm text-red-500">{errors.email}</div>
            )}
          </div>
          <div className="flex flex-col gap-1">
            <div className="flex items-center justify-between gap-5">
              <div className="flex flex-col w-1/2 gap-1">
                <label
                  htmlFor="password"
                  className="text-sm font-normal text-gray-600"
                >
                  Your password<span className="text-red-500">*</span>
                </label>
                <PasswordInput title="Password requirements: must be at least 8 characters long and include 
at least one uppercase letter, one special character, and one number." name="password" onChange={(e) =>
                    setFormData({ ...formData, password: e.target.value })
                  } placeholder="Enter your password" value={formData.password} className="h-10 pl-3 overflow-hidden text-sm font-normal select-none pr-11" />
              </div>
              <div className="flex flex-col w-1/2 gap-1">
                <label
                  htmlFor="confirm_password"
                  className="text-sm font-normal text-gray-600"
                >
                  Confirm password<span className="text-red-500">*</span>
                </label>

                <PasswordInput
                  id="confirm_password"
                  name="confirm_password"
                  placeholder="Confirm your password"
                  value={formData.confirmPassword}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      confirmPassword: e.target.value,
                    })
                  }
                  className="h-10 pl-3 overflow-hidden text-sm font-normal select-none pr-11"
                />
              </div>
            </div>
            {errors.password && (
              <div className="text-sm text-red-500">{errors.password}</div>
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
              className="h-10 pl-3 text-sm font-normal border border-gray-300 rounded-lg outline-none"
              placeholder="Enter your fullname here"
              value={formData.fullname}
              onChange={(e) =>
                setFormData({ ...formData, fullname: e.target.value })
              }
            />
            {errors.fullname && (
              <div className="text-sm text-red-500">{errors.fullname}</div>
            )}
          </div>
          <div className="flex flex-col gap-1">
            <div className="flex items-center justify-between gap-5">
              <div className="flex flex-col w-1/2 gap-1">
                <label
                  htmlFor="dateofbirth"
                  className="text-sm font-normal text-gray-600"
                >
                  Date Of Birth
                </label>

                <DatePicker
                  format="MM/DD/YYYY"
                  placeholder="mm/dd/yyyy"
                  inputReadOnly={true}
                  className="h-10 pl-3 pr-4 text-sm font-normal border border-gray-300 rounded-lg outline-none"
                  onChange={handleChangeDateOfBirth}
                />
              </div>
              <div className="flex flex-col w-1/2 gap-1">
                <label
                  htmlFor="gender"
                  className="text-sm font-normal text-gray-600"
                >
                  Gender
                </label>
                <select
                  name="gender"
                  id="gender"
                  className="h-10 pl-3 pr-4 text-sm font-normal border border-gray-300 rounded-lg outline-none"
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
              <div className="text-sm text-red-500">{errors.dateOfBirth}</div>
            )}
          </div>

          {statusRegister === "pending" ? (
            <div>
              <button
                className="w-full h-12 text-white bg-black border border-gray-300 rounded-2xl"
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
                className="w-full h-12 text-white bg-black border border-gray-300 rounded-2xl"
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
              <a
                href={`${import.meta.env.VITE_BASE_URL}/login`}
                className="underline hover:text-purple"
              >
                Login
              </a>
            </span>
          </div>
        </form>
      </div>
    </div>
  );
};

export default SignUp;
