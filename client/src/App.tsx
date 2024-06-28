import { createBrowserRouter, Outlet, RouterProvider } from "react-router-dom";
import HeaderHomepage from "./components/user.components/header";
import HomepageScreen from "./screens/user.screens/HomepageScreen";
import Login from "./screens/user.screens/Login.tsx";
import "./assets/css/App.css";

export type CourseType = {
  id: string;
  name: string;
  thumbnailUrl: string;
  assignee?: {
    id: string;
    name: string;
    avatarUrl: string;
    role?: string;
    status?: string;
  };
  platform: string;
  period: {
    startDay: Date;
    endDay: Date;
  };
  rating?: number;
  studentNumber?: number;
  level?: string;
}

const LayoutUser = () => {
  return (
    <div className="app-container">
      <HeaderHomepage />
      <Outlet />
    </div>
  );
};

const router = createBrowserRouter([
  {
    path: "/",
    element: <LayoutUser />,
    errorElement: <div>404 not found</div>,
    children: [
      {
        index: true,
        element: <HomepageScreen />,
      }
    ],
  },
  {
    path: "login",
    element: <Login />,
  },
]);
function App() {
  return <>{<RouterProvider router={router} />}</>;
}

export default App;
