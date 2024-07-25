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
import { useRegistrationModal } from "../../hooks/use-registration-modal";
import AvatarDropdown from "./AvatarDropdown";
import { handleAvatarUrl } from "../../utils/handleAvatarUrl";

const HeaderHomepage: React.FC = () => {
  const searchTerm = useSelector(
    (state: RootState) => state.courses.searchTerm
  );
  const { open } = useRegistrationModal((state) => state);

  const [countUnread, setCountUnread] = useState(0);

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
    if (
      window.location.pathname.replace(/\/+$/, "") ===
      homePathWithBasePath.replace(/\/+$/, "")
    ) {
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

  const avatarUrl = handleAvatarUrl(userData.avatarUrl);
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
        {userData.role.toUpperCase() === "ACCOUNTANT" && (
          <div className="relative flex-grow mx-6"></div>
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
              <Notification setCountUnread={setCountUnread}>
                <div
                  style={{
                    position: "relative",
                  }}
                  title="Notification"
                >
                  <BellIcon className="cursor-pointer hover:text-purple" />
                  <div>
                    {countUnread > 0 && (
                      <div
                        style={{
                          position: "absolute",
                          width: "20px",
                          height: "20px",
                          display: "flex",
                          justifyContent: "center",
                          alignItems: "center",
                          fontWeight: "semi-bold",
                          fontSize: "12px",
                          top: "-8px",
                          right: "-8px",
                          backgroundColor: 'rgb(128,0,128)',
                          color: "white",
                          borderRadius: "50%",
                        }}
                      >
                        {countUnread}
                      </div>
                    )}
                  </div>
                </div>
              </Notification>
            </div>
          </div>
          <AvatarDropdown avatarUrl={avatarUrl} role={userData.role} />
        </div>
      </div>
    </header>
  );
};
export default HeaderHomepage;
