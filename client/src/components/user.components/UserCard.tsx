import { useDispatch, useSelector } from 'react-redux';
import { AppDispatch, RootState } from '../../redux/store/store';
import { useNavigate } from 'react-router-dom';
import { PieChartIcon, UserIcon } from 'lucide-react';
import { handleLogout } from '../../redux/slice/user.slice';
import { updateSearch, updateSort } from '../../redux/slice/course.slice';
import { deleteAllFilterItem } from '../../redux/slice/searchFilterItem.slice';

export default function UserCard() {
    const navigate = useNavigate();
    const dispatch: AppDispatch = useDispatch();
    const userData = useSelector((state: RootState) => state.user.user);

    //For role mgmies
    const goToMyProfilePage = () => {
        if (userData.role.toUpperCase() == 'USER') {
            navigate("/personal/account");
        }
        else {
            navigate("/admin/profile");
        }
    };

    const goToMyRegistrationPage = () => {
        navigate("/personal/registration");
    };

    const goToMyScorePage = () => {
        navigate("/personal/score");
    };

    const avatarUrl = userData.avatarUrl?.startsWith("http") || userData.avatarUrl?.startsWith("data")
        ? userData.avatarUrl
        : (userData.avatarUrl != null && userData.avatarUrl != undefined && userData.avatarUrl != "") ? `${import.meta.env.VITE_BACKEND_URL}${userData.avatarUrl}` : (`${import.meta.env.BASE_URL}/avatar/Default Avatar.svg`).replace('//', '/');

    return (
        <div
            className="pt-2 mt-2 bg-white border border-gray-200 rounded-md shadow-xl w-60"
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
                            dispatch(updateSearch(""));
                            dispatch(deleteAllFilterItem());
                            dispatch(updateSort("NEWEST"));
                        }}
                    >
                        Sign out
                    </div>
                </a>
            </div >
        </div >
    )
}
