import React, { useState } from "react";
import imageLogo from "../assets/images/logo_c4u.svg";
import { useDispatch, useSelector } from "react-redux";
import { AppDispatch, RootState } from "../redux/store/store";
import { useNavigate } from "react-router-dom";
import {
    searchCoursesByFilterNameAndSortBy,
    updateSearch,
    updateSort,
} from "../redux/slice/course.slice";
import { Notification } from "./notification";
import { BellIcon, SearchIcon } from "lucide-react";
import { deleteAllFilterItem } from "../redux/slice/searchFilterItem.slice";
import { useRegistrationModal } from "../hooks/use-registration-modal";
import AvatarDropdown from "./user/avatar-dropdown";
import { handleAvatarUrl } from "../utils/handleAvatarUrl";

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
        const homePathWithBasePath = (basePath + homepagePath).replace(
            "//",
            "/"
        );
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
            className='p-4 bg-white'
            style={{ boxShadow: "rgba(99, 99, 99, 0.2) 0px 2px 4px 0px" }}
        >
            <div className='container flex items-center justify-between max-w-screen-xl mx-auto'>
                <div
                    onClick={goToHomePage}
                    className='flex items-center cursor-pointer'
                >
                    <img
                        src={imageLogo}
                        alt='Logo'
                        className='h-10 select-none'
                    />
                    <span className='text-2xl font-semibold text-black select-none'>
                        ourse4U
                    </span>
                </div>
                {userData.role != "USER" && (
                    <div className='px-4 text-2xl font-black text-purple'>
                        {userData.role.toUpperCase()}
                    </div>
                )}
                {userData.role.toUpperCase() != "ACCOUNTANT" && (
                    <div className='relative flex-grow mx-6'>
                        <SearchIcon className='absolute z-50 -translate-y-1/2 left-2 top-1/2' />
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
                )}
                {userData.role.toUpperCase() === "ACCOUNTANT" && (
                    <div className='relative flex-grow mx-6'></div>
                )}

                <div className='flex items-center space-x-4'>
                    <div className='flex items-center justify-center gap-5'>
                        {userData.role.toUpperCase() != "ACCOUNTANT" &&
                            (userData.role.toUpperCase() == "USER" ? (
                                <button
                                    className='font-semibold text-white border border-gray-300 rounded-lg w-44 h-11 bg-violet-700'
                                    style={{ backgroundColor: "purple" }}
                                    onClick={() => open(true)}
                                >
                                    Create a registration
                                </button>
                            ) : (
                                <button
                                    className='font-semibold text-white border border-gray-300 rounded-lg w-44 h-11 bg-violet-700'
                                    style={{ backgroundColor: "purple" }}
                                    onClick={goToCreateCoursePage}
                                >
                                    Create a course
                                </button>
                            ))}
                        <div className='flex items-center gap-6'>
                            {userData.role == "USER" && (
                                <div title='Leader Board'>
                                    <svg
                                        width='32'
                                        height='32'
                                        viewBox='0 0 55 41'
                                        fill='none'
                                        xmlns='http://www.w3.org/2000/svg'
                                        onClick={() => navigate("/leaderboard")}
                                        className='cursor-pointer'
                                    >
                                        <path
                                            d='M23.9273 14.5666C23.8511 14.3345 23.8207 14.0893 23.8378 13.8451C23.8548 13.601 23.919 13.3627 24.0267 13.1437C24.1344 12.9248 24.2834 12.7296 24.4653 12.5693C24.6473 12.4089 24.8584 12.2865 25.0869 12.2091L27.8369 11.2773C28.1125 11.1839 28.406 11.1585 28.6932 11.203C28.9803 11.2475 29.253 11.3608 29.4887 11.5335C29.7243 11.7062 29.9163 11.9333 30.0486 12.1962C30.181 12.4591 30.25 12.7501 30.25 13.0455V22.3636C30.25 22.8579 30.0568 23.3319 29.713 23.6814C29.3692 24.0309 28.9029 24.2273 28.4167 24.2273C27.9304 24.2273 27.4641 24.0309 27.1203 23.6814C26.7765 23.3319 26.5833 22.8579 26.5833 22.3636V15.6312L26.2465 15.7454C26.018 15.8228 25.7769 15.8537 25.5367 15.8364C25.2965 15.819 25.0621 15.7538 24.8467 15.6443C24.6313 15.5349 24.4393 15.3834 24.2815 15.1984C24.1238 15.0135 24.0034 14.7989 23.9273 14.5666ZM55 39.1364C55 39.6306 54.8068 40.1047 54.463 40.4542C54.1192 40.8037 53.6529 41 53.1667 41H1.83333C1.3471 41 0.880787 40.8037 0.536971 40.4542C0.193154 40.1047 0 39.6306 0 39.1364C0 38.6421 0.193154 38.1681 0.536971 37.8186C0.880787 37.4691 1.3471 37.2727 1.83333 37.2727H3.66667V14.9091C3.66667 13.9206 4.05297 12.9725 4.74061 12.2735C5.42824 11.5745 6.36087 11.1818 7.33333 11.1818H16.5V3.72727C16.5 2.73874 16.8863 1.79069 17.5739 1.09169C18.2616 0.392694 19.1942 0 20.1667 0H34.8333C35.8058 0 36.7384 0.392694 37.4261 1.09169C38.1137 1.79069 38.5 2.73874 38.5 3.72727V20.5H47.6667C48.6391 20.5 49.5718 20.8927 50.2594 21.5917C50.947 22.2907 51.3333 23.2387 51.3333 24.2273V37.2727H53.1667C53.6529 37.2727 54.1192 37.4691 54.463 37.8186C54.8068 38.1681 55 38.6421 55 39.1364ZM38.5 24.2273V37.2727H47.6667V24.2273H38.5ZM20.1667 37.2727H34.8333V3.72727H20.1667V37.2727ZM7.33333 37.2727H16.5V14.9091H7.33333V37.2727Z'
                                            fill='black'
                                        />
                                    </svg>
                                </div>
                            )}
                            <Notification
                                setCountUnread={setCountUnread}
                                countUnread={countUnread}
                            >
                                <div
                                    style={{
                                        position: "relative",
                                    }}
                                    title='Notification'
                                >
                                    <BellIcon className='cursor-pointer hover:text-purple' />
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
                                                    backgroundColor:
                                                        "rgb(128,0,128)",
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
                    <AvatarDropdown
                        avatarUrl={avatarUrl}
                        role={userData.role}
                    />
                </div>
            </div>
        </header>
    );
};
export default HeaderHomepage;
