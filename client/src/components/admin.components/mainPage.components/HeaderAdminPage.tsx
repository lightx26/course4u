import React, {useState} from "react";
import imageLogo from "../../../assets/images/logo_c4u.svg";
import AdminAvatar from "../../../assets/images/admin.images/AdminAvatar.png";

import AdminPersonalization from "./AdminPersonalization.tsx";

const HeaderAdminPage: React.FC = () => {
    const [isDropdownAvatarOpen, setIsDropdownAvatarOpen] = useState(false);

    const toggleDropdown = () => {
        setIsDropdownAvatarOpen(!isDropdownAvatarOpen);
    };

    return (
        <header
            className="bg-white p-4"
            style={{boxShadow: "rgba(99, 99, 99, 0.2) 0px 2px 4px 0px"}}
        >
            <div className="container mx-auto max-w-screen-xl flex justify-between items-center">
                <div className="flex space-x-10 items-center">
                    <div className="flex items-center cursor-pointer">
                        <img src={imageLogo} alt="Logo" className="h-10"/>
                        <span className="text-black text-2xl font-semibold">ourse4U</span>
                    </div>

                    <div>
            <span
                className="text-[#861FA2] text-2xl font-bold h-100"
            >
              ADMIN
            </span>
                    </div>
                </div>

                <div className="flex-grow mx-4 px-16 flex items-center relative">
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        width="2rem"
                        height="2rem"
                        viewBox="0 0 24 24"
                        fill="none"
                        style={{
                            position: "absolute",
                            zIndex: 10,
                            paddingLeft: "10px",
                        }}
                    >
                        <path
                            d="M10.875 18.75C15.2242 18.75 18.75 15.2242 18.75 10.875C18.75 6.52576 15.2242 3 10.875 3C6.52576 3 3 6.52576 3 10.875C3 15.2242 6.52576 18.75 10.875 18.75Z"
                            stroke="#1D2026"
                            strokeWidth="1.5"
                            strokeLinecap="round"
                            strokeLinejoin="round"
                        />
                        <path
                            d="M16.4431 16.4434L20.9994 20.9997"
                            stroke="#1D2026"
                            strokeWidth="1.5"
                            strokeLinecap="round"
                            strokeLinejoin="round"
                        />
                    </svg>
                    <input
                        type="text"
                        placeholder="Explore courses..."
                        className="w-full p-2 px-10 border border-gray-300 rounded-md hover:border-#cccccc-500 focus:border-blue-500 focus:shadow-lg focus:outline-none"
                        style={{
                            color: "black",
                            position: "relative",
                            outline: "none",
                            backgroundColor: "#f5f7fa",
                        }}
                        autoFocus
                    ></input>
                </div>

                <div className="flex items-center space-x-4">
                    <div className="flex items-center justify-center gap-5">
                        <button
                            className="w-44 h-11 border bg-[#861FA2] border-gray-300 rounded-full bg-purple-700 text-white font-semibold"
                        >
                            Create a new course
                        </button>
                        <div className="flex items-center gap-6">
                            <div
                                style={{
                                    position: "relative",
                                }}
                            >
                                <svg
                                    xmlns="http://www.w3.org/2000/svg"
                                    width="1.8rem"
                                    height="1.8rem"
                                    viewBox="0 0 34 40"
                                    fill="none"
                                    style={{
                                        cursor: "pointer",
                                    }}
                                >
                                    <path
                                        d="M19.5089 35.583C19.5089 35.9223 19.444 36.2582 19.3179 36.5716C19.1918 36.885 19.007 37.1698 18.774 37.4097C18.5411 37.6496 18.2645 37.8399 17.9601 37.9697C17.6557 38.0995 17.3295 38.1663 17 38.1663"
                                        stroke="black"
                                        strokeWidth="3"
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                    />
                                    <path
                                        d="M14.4911 35.583C14.4911 35.9223 14.556 36.2582 14.6821 36.5716C14.8082 36.885 14.993 37.1698 15.226 37.4097C15.4589 37.6496 15.7355 37.8399 16.0399 37.9697C16.3443 38.0995 16.6705 38.1663 17 38.1663"
                                        stroke="black"
                                        strokeWidth="3"
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                    />
                                    <path
                                        d="M14.4912 33V35.5833"
                                        stroke="black"
                                        strokeWidth="3"
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                    />
                                    <path
                                        d="M19.5089 33L19.5089 35.5833"
                                        stroke="black"
                                        strokeWidth="3"
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                    />
                                    <path
                                        d="M32.0531 30.417C27.0354 33.0003 19.5089 33.0003 17 33.0003"
                                        stroke="black"
                                        strokeWidth="3"
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                    />
                                    <path
                                        d="M32.0532 30.4171C32.0532 25.2505 27.0355 27.8338 27.0355 14.9171C27.0355 9.14061 22.0178 4.58398 19.5089 4.58398"
                                        stroke="black"
                                        strokeWidth="3"
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                    />
                                    <path
                                        d="M1.9469 30.417C6.9646 33.0003 14.4912 33.0003 17 33.0003"
                                        stroke="black"
                                        strokeWidth="3"
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                    />
                                    <path
                                        d="M1.9469 30.4161C1.9469 25.2495 6.9646 27.8328 6.9646 14.9161C6.9646 9.13964 11.9823 4.58301 14.4912 4.58301"
                                        stroke="black"
                                        strokeWidth="3"
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                    />
                                    <path
                                        d="M14.4911 4.58301C14.4911 3.89787 14.7555 3.24078 15.226 2.75632C15.6965 2.27185 16.3346 1.99967 17 1.99967"
                                        stroke="black"
                                        strokeWidth="3"
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                    />
                                    <path
                                        d="M19.5089 4.58301C19.5089 3.89787 19.2445 3.24078 18.774 2.75632C18.3035 2.27185 17.6654 1.99967 17 1.99967"
                                        stroke="black"
                                        strokeWidth="3"
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                    />
                                </svg>
                                <div
                                    style={{
                                        width: "15px",
                                        height: "15px",
                                        borderRadius: "50%",
                                        backgroundColor: "#e55353",
                                        color: "white",
                                        fontSize: "0.7rem",
                                        position: "absolute",
                                        display: "flex",
                                        alignItems: "center",
                                        justifyContent: "center",
                                        top: 0,
                                        left: "50%",
                                        zIndex: 10,
                                    }}
                                >
                                    <div>6</div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="relative">
                        <img
                            src={AdminAvatar}
                            alt="Admin Avatar"
                            className="h-10 w-10 rounded-full cursor-pointer"
                            onClick={toggleDropdown}
                        />

                        {isDropdownAvatarOpen && (
                            <div
                                className="absolute right-2 mt-1 w-60 bg-white rounded-xl shadow-lg z-50"
                                onMouseLeave={() => {
                                    if (isDropdownAvatarOpen) {
                                        toggleDropdown();
                                    }
                                }}
                            >
                                <AdminPersonalization/>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </header>
    );
};
export default HeaderAdminPage;
