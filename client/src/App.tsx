import { createBrowserRouter, Outlet, RouterProvider } from "react-router-dom";
import HeaderHomepage from "./components/user.components/header";
import HomepageScreen from "./screens/user.screens/HomepageScreen";
import Login from "./screens/user.screens/Login.tsx";
import Detail_Of_Course from "./screens/user.screens/DetailOfCourse.tsx";
import { ReactElement, useEffect } from "react";
import { ProtectedRoute } from "./components/user.components/ProtectedRouteAuth.tsx";
import AccountSettingScreen from "./screens/user.screens/personal/AccountSettingScreen.tsx";
import Navigation from "./components/user.components/personal/NavigationComponent.tsx";
import MyRegistrationsScreen from "./screens/user.screens/MyRegistrationScreen.tsx";
import { useDispatch, useSelector } from "react-redux";
import { AppDispatch, RootState } from "./redux/store/store.ts";
import { fetchUserDetails } from "./redux/slice/user.slice.ts";
import NotPermitted from "./screens/user.screens/NotPermitted.tsx";
import NotFound from "./screens/user.screens/NotFound.tsx";
import AdminHomePage from "./screens/admin.screens/AdminHome.tsx";

import SignUp from "./screens/user.screens/SignUp.tsx";
import AdminCoursePageScreen from "./screens/admin.screens/AdminCoursePageScreen.tsx";
import CreateCoursesScreen from "./screens/admin.screens/CreateCoursesScreen.tsx";
import { ProtectedRouteLogin } from "./components/user.components/ProtectedRouteLogin.tsx";
import { isTokenExpired } from "./utils/validateToken.ts";
import { RegistrationModal } from "./components/modal/registration-modal.tsx";
import LeaderBoard from "./screens/user.screens/LeaderBoard.tsx";
import MyScore from "./screens/user.screens/MyScore.tsx";
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
    <div className="app-container bg-gray-50">
      {isUserRoute && userRole === "USER" && (
        <>
          <HeaderHomepage />
          <RegistrationModal />
          {children}
          <Outlet />
        </>
      )}

      {isUserRoute && (userRole === "ADMIN" || userRole === "ACCOUNTANT") && (
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
      <div className="app-container ">
        {isAdminRoute && userRole === "ADMIN" && (
          <>
            <HeaderHomepage />
            <RegistrationModal />
            <Outlet />
          </>
        )}
        {isAdminRoute && (userRole === "USER" || userRole === "ACCOUNTANT") && (
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
      <div className="app-container">
        {isAccountRoute && userRole === "ACCOUNTANT" && (
          <>
            <div>this is accountant header</div>
            <Outlet />
          </>
        )}
        {isAccountRoute && (userRole === "USER" || userRole === "ADMIN") && (
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
          element: <>this is accountant homepage</>,
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
    const access_token: string | null = localStorage.getItem("access_token");
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
