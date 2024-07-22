import React, { useState } from "react";
import imageLogo from "../../assets/images/logo_c4u.svg";
import { useDispatch, useSelector } from "react-redux";
import { AppDispatch, RootState } from "../../redux/store/store";
import { useNavigate } from "react-router-dom";
import {
  searchCoursesByFilterNameAndSortBy,
  updateSearch,
  updateSort,
} from "../../redux/slice/course.slice";
import { Notification } from "../notification";
import { BarChart3Icon, BellIcon, SearchIcon } from "lucide-react";
import { deleteAllFilterItem } from "../../redux/slice/searchFilterItem.slice";
import UserCard from "./UserCard";
import AdminCard from "../admin.components/mainPage.components/AdminCard";
import { useRegistrationModal } from "../../hooks/use-registration-modal";

const HeaderHomepage: React.FC = () => {
  const searchTerm = useSelector(
    (state: RootState) => state.courses.searchTerm
  );
  const [isDropdownAvatarOpen, setIsDropdownAvatarOpen] =
    useState<boolean>(false);
  const { open } = useRegistrationModal((state) => state);
  const toggleDropdown = () => {
    setIsDropdownAvatarOpen(!isDropdownAvatarOpen);
  };
  const userData = useSelector((state: RootState) => state.user.user);
  const navigate = useNavigate();
  const dispatch: AppDispatch = useDispatch();
  const basePath = `${import.meta.env.VITE_BASE_URL}` || "/";
  const goToHomePage = () => {
    const homepagePath =
      userData.role.toUpperCase() == "ADMIN"
        ? "/admin"
        : userData.role.toUpperCase() == "ACCOUNTANT"
        ? "/accountant"
        : "/";
    const homePathWithBasePath = (basePath + homepagePath).replace("//", "/");
    if (window.location.pathname === homePathWithBasePath) {
      window.location.reload();
    } else {
      navigate(homepagePath);
    }
  };

  const handleSearch = () => {
    const currentSearchTerm = searchTerm;
    if (
      userData.role.toUpperCase() == "USER" &&
      window.location.pathname != basePath
    ) {
      goToHomePage();
    } else if (
      userData.role.toUpperCase() == "ADMIN" &&
      window.location.pathname !=
        (basePath + "/admin/courses").replace("//", "/")
    ) {
      goToAdminCoursesPage();
    }
    dispatch(deleteAllFilterItem());
    dispatch(updateSort("NEWEST"));
    dispatch(updateSearch(currentSearchTerm));
    dispatch(searchCoursesByFilterNameAndSortBy());
  };

  const goToAdminCoursesPage = () => {
    const adminCoursesPagePath = (basePath + "/admin/courses").replace(
      "//",
      "/"
    );
    if (window.location.pathname === adminCoursesPagePath) {
      window.location.reload();
    } else {
      navigate("/admin/courses");
    }
  };

  const goToCreateCoursePage = () => {
    navigate("/admin/courses/create");
  };

  //For Role Accountant (TBU - To be Updated)

  const avatarUrl =
    userData.avatarUrl?.startsWith("http") ||
    userData.avatarUrl?.startsWith("data")
      ? userData.avatarUrl
      : userData.avatarUrl != null &&
        userData.avatarUrl != undefined &&
        userData.avatarUrl != ""
      ? `${import.meta.env.VITE_BACKEND_URL}${userData.avatarUrl}`
      : `${import.meta.env.BASE_URL}/avatar/Default Avatar.svg`.replace(
          "//",
          "/"
        );

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
          <span className="text-2xl font-semibold text-black select-none">
            ourse4U
          </span>
        </div>
        {userData.role != "USER" && (
          <div className="px-4 text-2xl font-black text-purple">
            {userData.role.toUpperCase()}
          </div>
        )}
        {userData.role.toUpperCase() != "ACCOUNTANT" && (
          <div className="relative flex-grow mx-6">
            <SearchIcon className="absolute z-50 -translate-y-1/2 left-2 top-1/2" />
            <input
              type="search"
              placeholder="Explore courses..."
              className="w-full p-2 px-10 border border-gray-300 rounded-md hover:border-#cccccc-500 focus:border-blue-500 focus:shadow-lg focus:outline-none"
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
        )}

        <div className="flex items-center space-x-4">
          <div className="flex items-center justify-center gap-5">
            {userData.role.toUpperCase() != "ACCOUNTANT" &&
              (userData.role.toUpperCase() == "USER" ? (
                <button
                  className="font-semibold text-white border border-gray-300 rounded-lg w-44 h-11 bg-violet-700"
                  style={{ backgroundColor: "purple" }}
                  onClick={() => open(true)}
                >
                  Create a registration
                </button>
              ) : (
                <button
                  className="font-semibold text-white border border-gray-300 rounded-lg w-44 h-11 bg-violet-700"
                  style={{ backgroundColor: "purple" }}
                  onClick={goToCreateCoursePage}
                >
                  Create a course
                </button>
              ))}
            <div className="flex items-center gap-6">
              {userData.role == "USER" && (
                <div title="Leader Board">
                  <BarChart3Icon
                    className="cursor-pointer hover:text-purple"
                    onClick={() => navigate("/leaderboard")}
                  />
                </div>
              )}
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

          <div className="relative w-[40px] h-[40px] rounded-[50%] group">
            <img
              src={avatarUrl}
              alt="User Avatar"
              className="absolute left-0 right-0 object-cover object-center w-full h-full cursor-pointer rounded-[50%]"
              onClick={toggleDropdown}
            />
            <div className="absolute hidden group-hover:block right-2 top-10 z-[1000]">
              {userData.role.toUpperCase() == "USER" ? (
                <UserCard />
              ) : userData.role.toUpperCase() == "ADMIN" ? (
                <AdminCard />
              ) : (
                <></>
              )}
            </div>
          </div>
        </div>
      </div>
    </header>
  );
};
export default HeaderHomepage;
