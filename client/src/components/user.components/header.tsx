import React, { useState } from "react";
import imageLogo from "../../assets/images/logo_c4u.svg";
import { useDispatch, useSelector } from "react-redux";
import { AppDispatch, RootState } from "../../redux/store/store";
import { handleLogout } from "../../redux/slice/user.slice";
import { useNavigate } from "react-router-dom";
import {
  searchCoursesByFilterNameAndSortBy,
  updateSearch,
} from "../../redux/slice/course.slice";
import { Notification } from "../notification";
import { BarChart3Icon, BellIcon, PieChartIcon, SearchIcon, UserIcon } from "lucide-react";
const HeaderHomepage: React.FC = () => {
  const [isDropdownAvatarOpen, setIsDropdownAvatarOpen] =
    useState<boolean>(false);

  const searchTerm = useSelector(
    (state: RootState) => state.courses.searchTerm
  );

  const toggleDropdown = () => {
    setIsDropdownAvatarOpen(!isDropdownAvatarOpen);
  };

  const userData = useSelector((state: RootState) => state.user.user);
  const navigate = useNavigate();
  const dispatch: AppDispatch = useDispatch();
  const basePath = `${import.meta.env.VITE_BASE_URL}` || '/';
  const goToHomePage = () => {
    if (window.location.pathname === basePath) {
      window.location.reload();
    } else {
      navigate("/");
    }
  };

  const goToMyProfilePage = () => {
    navigate("/personal/account");
  };

  const goToMyRegistrationPage = () => {
    navigate("/personal/registration");
  };

  const goToMyScorePage = () => {
    navigate("/personal/score");
  };

  const handleSearch = () => {
    if (window.location.pathname !== basePath) {
      goToHomePage();
    }
    dispatch(updateSearch(searchTerm));
    dispatch(searchCoursesByFilterNameAndSortBy());
  };

  const avatarUrl = userData.avatarUrl?.startsWith("http")
    ? userData.avatarUrl
    : `${import.meta.env.VITE_BACKEND_URL}${userData.avatarUrl}`;

  return (
    <header
      className="p-4 bg-white"
      style={{ boxShadow: "rgba(99, 99, 99, 0.2) 0px 2px 4px 0px" }}
    >
      <div className="container flex items-center justify-between max-w-screen-xl mx-auto">
        <div
          onClick={goToHomePage}
          className="flex items-center cursor-pointer"
        >
          <img src={imageLogo} alt="Logo" className="h-10" />
          <span className="text-2xl font-semibold text-black select-none">ourse4U</span>
        </div>

        <div className='relative flex-grow mx-6'>
          <SearchIcon className="absolute z-50 -translate-y-1/2 left-2 top-1/2" />
          <input
            type='search'
            placeholder='Explore courses...'
            className='w-full p-2 px-10 border border-gray-300 rounded-md hover:border-#cccccc-500 focus:border-blue-500 focus:shadow-lg focus:outline-none'
            style={{
              color: "black",
              position: "relative",
              outline: "none",
              width: "100%",
              backgroundColor: "#f5f7fa",
            }}
            value={searchTerm}
            onChange={(e) => {
              dispatch(updateSearch(e.target.value));
            }}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                handleSearch();
              }
            }}
            autoFocus
          ></input>
        </div>

        <div className='flex items-center space-x-4'>
          <div className='flex items-center justify-center gap-5'>
            <button
              className='font-semibold text-white border border-gray-300 rounded-lg w-44 h-11 bg-violet-700'
              style={{ backgroundColor: "purple" }}
            >
              Create a registration
            </button>
            <div className='flex items-center gap-6'>
              <div title="Leader Board">
                <BarChart3Icon className="cursor-pointer hover:text-purple" onClick={() => navigate("/leaderboard")} />
              </div>
              <Notification>
                <div
                  style={{
                    position: "relative",
                  }}
                  title="Notification"
                >
                  <BellIcon className="cursor-pointer hover:text-purple" />
                </div>
              </Notification>
            </div>
          </div>

          <div className="relative w-[40px] h-[40px] rounded-[50%]">
            <img
              src={avatarUrl}
              alt="User Avatar"
              className="absolute left-0 right-0 object-cover object-center w-full h-full cursor-pointer rounded-[50%]"
              onClick={toggleDropdown}
            />

            {isDropdownAvatarOpen && (
              <div
                className="absolute pt-2 mt-2 bg-white border border-gray-200 rounded-md shadow-xl right-2 top-10 w-60"
                style={{ zIndex: "999999" }}
                onMouseLeave={() => {
                  if (isDropdownAvatarOpen) {
                    toggleDropdown();
                  }
                }}
              >
                <div className="flex flex-col items-center justify-center py-2 w-[100%]">
                  <div className="flex items-center justify-center w-12 h-12">
                    <img
                      src={avatarUrl}
                      alt=""
                      className="object-cover w-full h-full border border-gray-300 rounded-full"
                    />
                  </div>
                  <div className="flex flex-col items-center justify-center px-2">
                    <div className="text-lg font-medium text-black w-60">
                      <h1
                        className="w-full px-2 overflow-hidden text-center text-ellipsis"
                        style={{
                          display: "-webkit-box",
                          WebkitLineClamp: 2,
                          WebkitBoxOrient: "vertical",
                          textOverflow: "ellipsis",
                          whiteSpace: "normal",
                          wordWrap: "break-word",
                        }}
                      >
                        {userData.fullName}
                      </h1>
                    </div>
                    <div className="px-2 text-sm font-normal text-black w-60">
                      <h1
                        className="w-full px-2 overflow-hidden text-center text-ellipsis"
                        style={{
                          display: "-webkit-box",
                          WebkitLineClamp: 2,
                          WebkitBoxOrient: "vertical",
                          textOverflow: "ellipsis",
                          whiteSpace: "normal",
                          wordWrap: "break-word",
                        }}
                      >
                        {userData.email}
                      </h1>
                    </div>
                  </div>
                </div>
                <hr></hr>
                <div>
                  <a className="flex items-center gap-4 px-4 py-2 text-gray-800 hover:bg-gray-100">
                    <UserIcon />
                    <div
                      className="w-full font-medium cursor-pointer"
                      onClick={goToMyProfilePage}
                    >
                      My Profile
                    </div>
                  </a>
                  <a
                    className="flex items-center gap-4 px-4 py-2 text-gray-800 hover:bg-gray-100"
                    onClick={goToMyRegistrationPage}
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="1.6rem"
                      height="1.6rem"
                      viewBox="0 0 22 22"
                      fill="none"
                    >
                      <path
                        d="M19.5556 2H17.1111V8.5L13.4444 6.25L9.77778 8.5V2H2.44444V20H19.5556V2ZM12.2222 2V4.5L13.4444 3.75L14.6667 4.5V2H12.2222ZM22 22H0V0H22V22Z"
                        fill="#141718"
                      />
                    </svg>
                    <div className="w-full font-medium cursor-pointer">
                      My Registration
                    </div>
                  </a>
                  <a
                    className="flex items-center gap-4 px-4 py-2 text-gray-800 hover:bg-gray-100"
                    href=""
                  >
                    <PieChartIcon />
                    <div
                      onClick={goToMyScorePage}
                      className="w-full font-medium"
                    >
                      My score
                    </div>
                  </a>
                </div>
                <hr></hr>
                <div style={{ cursor: "pointer" }}>
                  <a className="flex items-center gap-4 px-4 py-2 text-gray-800 hover:bg-gray-100">
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="1.6rem"
                      height="1.6rem"
                      viewBox="0 0 24 24"
                      fill="none"
                    >
                      <path
                        d="M23 12H5.4M23 12L15.96 4.66667M23 12L15.96 19.3333M8.04 23H1V1H8.04"
                        stroke="#141718"
                        strokeWidth="2"
                        strokeLinecap="round"
                        strokeLinejoin="round"
                      />
                    </svg>
                    <div
                      className="w-full font-medium"
                      onClick={(e) => {
                        e.preventDefault();
                        navigate("/login");
                        dispatch(handleLogout());
                      }}
                    >
                      Sign out
                    </div>
                  </a>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </header>
  );
};
export default HeaderHomepage;
