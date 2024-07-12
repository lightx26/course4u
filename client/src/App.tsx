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
import CreateCourse from "./components/user.components/create-course.tsx";

import SignUp from "./screens/user.screens/SignUp.tsx";
export type CourseType = {
    id: string;
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

const LayoutUser = ({ children }: { children?: ReactElement }) => {
    const isUserRoute = window.location.pathname.startsWith(
        `${import.meta.env.VITE_BASE_URL}`
    );
    const user = useSelector((state: RootState) => state.user.user);
    const userRole = user.role;
    return (
        <div className='app-container'>
            {isUserRoute && userRole === "USER" && (
                <>
                    <HeaderHomepage />
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
            {isAdminRoute && userRole === "ADMIN" && (
                <div className='app-container'>
                    <div>this is admin header</div>
                    <Outlet />
                </div>
            )}
            {isAdminRoute &&
                (userRole === "USER" || userRole === "ACCOUNTANT") && (
                    <>
                        <NotPermitted />
                    </>
                )}
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
            {isAccountRoute && userRole === "ACCOUNTANT" && (
                <div className='app-container'>
                    <div>this is accountant header</div>
                    <Outlet />
                </div>
            )}
            {isAccountRoute &&
                (userRole === "USER" || userRole === "ADMIN") && (
                    <>
                        <NotPermitted />
                    </>
                )}
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
          element: (
            <ProtectedRoute>
              <Detail_Of_Course />
            </ProtectedRoute>
          ),
        },
          {
              path: "create-course",
              element: (<ProtectedRoute>
                  <CreateCourse />
              </ProtectedRoute>),
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
          element: <div> My Score </div>,
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
          element: <>this is admin homepage</>,
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
      element: <Login />,
    },
      {
          path: "signup",
          element: <SignUp />,
      },
  ],
  {
    basename: import.meta.env.VITE_BASE_URL,
  }
);

function App() {
    const dispatch = useDispatch<AppDispatch>();
    const getAccount = async () => {
        if (
            window.location.pathname ===
                `${import.meta.env.VITE_BASE_URL}/login` ||
            window.location.pathname ===
                `${import.meta.env.VITE_BASE_URL}/signup`
        )
            return;
        await dispatch(fetchUserDetails());
    };
    useEffect(() => {
        getAccount();
    }, []);
    return <>{<RouterProvider router={router} />}</>;
}
export default App;
