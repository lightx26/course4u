import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { isTokenExpired } from "../../utils/validateToken";
import { useSelector } from "react-redux";
import { RootState } from "../../redux/store/store";

const ProtectedRouteLogin = ({ children }: { children: React.ReactNode }) => {
  const [isChecking, setIsChecking] = useState(true);
  const navigate = useNavigate();
  const userRole = useSelector((state: RootState) => state.user.user.role);
  useEffect(() => {
    const access_token: string | null = localStorage.getItem("access_token");
    if (access_token && !isTokenExpired(access_token)) {
      switch (userRole) {
        case "ADMIN":
          navigate("/admin");
          break;
        case "USER":
          navigate("/");
          break;
        case "ACCOUNTANT":
          navigate("/accountant");
          break;
      }
    } else {
      setIsChecking(false);
    }
  }, [navigate, userRole]);

  if (isChecking) {
    return null;
  }

  return <>{children}</>;
};

export { ProtectedRouteLogin };
