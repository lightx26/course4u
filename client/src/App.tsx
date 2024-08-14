import { createBrowserRouter, Outlet, RouterProvider } from "react-router-dom";

import "./App.css";
import { ReactElement, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { AppDispatch, RootState } from "@store/store";
import HeaderHomepage from "@components/header";
import { RegistrationModal } from "@components/modal/registration-modal";
import NotPermitted from "@screen/error/not-permitted";
import { ProtectedRoute } from "@components/auth/protected-route-auth";
import NotFound from "@screen/error/not-found";
import HomepageScreen from "@screen/user/homepage";
import Detail_Of_Course from "@screen/user/detail-of-course";
import LeaderBoard from "@screen/user/leader-board";
import Navigation from "@components/user/navigation";
import AccountSettingScreen from "@screen/user/personal/account-setting";
import MyRegistrationsScreen from "@screen/user/personal/my-registration";
import MyScore from "@screen/user/personal/my-score";
import AdminHomePage from "@screen/admin/admin-home";
import AdminCoursePageScreen from "@screen/admin/admin-course-page";
import CreateCoursesScreen from "@screen/admin/create-courses";
import AccountantHomePage from "@screen/accountant/accountant-home";
import { ProtectedRouteLogin } from "@components/auth/protected-route-login";
import Login from "@screen/auth/login";
import SignUp from "@screen/auth/sign-up";
import { isTokenExpired } from "@utils/validateToken";
import { fetchUserDetails } from "redux/slice/user.slice";

const LayoutUser = ({ children }: { children?: ReactElement }) => {
    const isUserRoute = window.location.pathname.startsWith(
        `${import.meta.env.VITE_BASE_URL}`
    );
    const user = useSelector((state: RootState) => state.user.user);
    const userRole = user.role;
    return (
        <div className='app-container bg-gray-50'>
            {isUserRoute && userRole === "USER" && (
                <>
                    <HeaderHomepage />
                    <RegistrationModal />
                    {children}
                    <Outlet />
                </>
            )}

            {isUserRoute &&
                (userRole === "ADMIN" || userRole === "ACCOUNTANT") && (
                    <>
                        <NotPermitted />
                    </>
                )}
        </div>
    );
};

const LayoutAdmin = () => {
    const isAdminRoute = window.location.pathname.startsWith(
        `${import.meta.env.VITE_BASE_URL}/admin`
    );
    const user = useSelector((state: RootState) => state.user.user);
    const userRole = user.role;
    return (
        <>
            <div className='app-container '>
                {isAdminRoute && userRole === "ADMIN" && (
                    <>
                        <HeaderHomepage />
                        <RegistrationModal />
                        <Outlet />
                    </>
                )}
                {isAdminRoute &&
                    (userRole === "USER" || userRole === "ACCOUNTANT") && (
                        <>
                            <NotPermitted />
                        </>
                    )}
            </div>
        </>
    );
};

const LayoutAccountant = () => {
    const isAccountRoute = window.location.pathname.startsWith(
        `${import.meta.env.VITE_BASE_URL}/accountant`
    );
    const user = useSelector((state: RootState) => state.user.user);
    const userRole = user.role;
    return (
        <>
            <div className='app-container'>
                {isAccountRoute && userRole === "ACCOUNTANT" && (
                    <>
                        <HeaderHomepage />
                        <RegistrationModal />
                        <Outlet />
                    </>
                )}
                {isAccountRoute &&
                    (userRole === "USER" || userRole === "ADMIN") && (
                        <>
                            <NotPermitted />
                        </>
                    )}
            </div>
        </>
    );
};

const router = createBrowserRouter(
    [
        {
            path: "/",
            element: (
                <ProtectedRoute>
                    <LayoutUser />
                </ProtectedRoute>
            ),
            errorElement: <NotFound />,
            children: [
                {
                    index: true,
                    element: <HomepageScreen />,
                },
                {
                    path: "courses/:id",
                    element: <Detail_Of_Course />,
                },
                {
                    path: "leaderboard",
                    element: <LeaderBoard />,
                },
            ],
        },
        {
            path: "personal",
            element: (
                <ProtectedRoute>
                    <LayoutUser>
                        <Navigation />
                    </LayoutUser>
                </ProtectedRoute>
            ),
            errorElement: <NotFound />,
            children: [
                {
                    index: true,
                    path: "account",
                    element: <AccountSettingScreen />,
                },
                {
                    path: "registration",
                    element: <MyRegistrationsScreen />,
                },
                {
                    path: "score",
                    element: <MyScore />,
                },
            ],
        },
        {
            path: "/admin",
            element: (
                <ProtectedRoute>
                    <LayoutAdmin />
                </ProtectedRoute>
            ),
            errorElement: <NotFound />,
            children: [
                {
                    index: true,
                    element: <AdminHomePage />,
                },
                {
                    path: "courses",
                    element: <AdminCoursePageScreen />,
                },
                {
                    path: "courses/create",
                    element: <CreateCoursesScreen />,
                },
                {
                    path: "courses/:id",
                    element: <Detail_Of_Course />,
                },
                {
                    path: "profile",
                    element: <AccountSettingScreen />,
                },
            ],
        },
        {
            path: "/accountant",
            element: (
                <ProtectedRoute>
                    <LayoutAccountant />
                </ProtectedRoute>
            ),
            errorElement: <NotFound />,
            children: [
                {
                    index: true,
                    element: <AccountantHomePage />,
                },
                {
                    path: "profile",
                    element: <AccountSettingScreen />,
                },
            ],
        },
        {
            path: "login",
            element: (
                <ProtectedRouteLogin>
                    <Login />
                </ProtectedRouteLogin>
            ),
        },
        {
            path: "signup",
            element: (
                <ProtectedRouteLogin>
                    <SignUp />
                </ProtectedRouteLogin>
            ),
        },
    ],
    {
        basename: import.meta.env.VITE_BASE_URL,
    }
);

function App() {
    const dispatch = useDispatch<AppDispatch>();
    const getAccount = async () => {
        const access_token: string | null =
            localStorage.getItem("access_token");
        if (access_token && !isTokenExpired(access_token)) {
            const userData = await dispatch(fetchUserDetails());
            if (userData.payload.status === 403) {
                localStorage.removeItem("access_token");
                window.location.href = `${import.meta.env.VITE_BASE_URL}/login`;
            }
        }
    };
    useEffect(() => {
        getAccount();
    }, []);
    return <>{<RouterProvider router={router} />}</>;
}

export default App;
