import { createBrowserRouter, Outlet, RouterProvider } from "react-router-dom";
import HeaderHomepage from "./components/header.tsx";
import HomepageScreen from "./screens/user/homepage.tsx";
import Login from "./screens/auth/login.tsx";
import Detail_Of_Course from "./screens/user/detail-of-course.tsx";
import { ReactElement, useEffect } from "react";
import { ProtectedRoute } from "./components/auth/protected-route-auth.tsx";
import AccountSettingScreen from "./screens/user/personal/account-setting.tsx";
import Navigation from "./components/user/navigation.tsx";
import MyRegistrationsScreen from "./screens/user/personal/my-registration.tsx";
import { useDispatch, useSelector } from "react-redux";
import { AppDispatch, RootState } from "./redux/store/store.ts";
import { fetchUserDetails } from "./redux/slice/user.slice.ts";
import NotPermitted from "./screens/error/not-permitted.tsx";
import NotFound from "./screens/error/not-found.tsx";
import AdminHomePage from "./screens/admin/admin-home.tsx";

import SignUp from "./screens/auth/sign-up.tsx";
import AdminCoursePageScreen from "./screens/admin/admin-course-page.tsx";
import CreateCoursesScreen from "./screens/admin/create-courses.tsx";
import { ProtectedRouteLogin } from "./components/auth/protected-route-login.tsx";
import { isTokenExpired } from "./utils/validateToken.ts";
import { RegistrationModal } from "./components/modal/registration-modal.tsx";
import LeaderBoard from "./screens/user/leader-board.tsx";
import MyScore from "./screens/user/personal/my-score.tsx";
import AccountantHomePage from "./screens/accountant/accountant-home.tsx";

import "./App.css";
export type CourseType = {
    id: string | undefined;
    name: string;
    thumbnailUrl?: string;
    assignee?: {
        id?: string;
        name?: string;
        avatarUrl?: string;
        role?: string;
        status?: string;
    };
    platform?: string;
    createdDate?: string;
    period?: {
        startDay?: Date;
        endDay?: Date;
    };
    rating?: number;
    enrollmentCount?: number;
    level?: string;
};

export type RegistrationType = {
    id?: string;
    startDate?: Date;
    endDate?: Date;
    registerDate?: Date;
    lastUpdated?: Date;
    courseId?: string;
    courseName?: string;
    courseThumbnailUrl?: string;
    coursePlatform?: string;
    userId?: string;
    userName?: string;
    userFullname?: string;
    userAvatarUrl?: string;
    duration?: number;
    durationUnit?: string;
    status?: string;
};

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
