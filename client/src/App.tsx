import Counter from "../src/screens/user.screens/counter.tsx";
import { createBrowserRouter, Outlet, RouterProvider } from "react-router-dom";
import HeaderHomepage from "../src/components/user.components/header.tsx";
import Login from "./screens/user.screens/Login.tsx";

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
        element: <Counter />,
      },
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
