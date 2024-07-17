import React from "react";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { AppDispatch } from "../../redux/store/store";
import { isTokenExpired } from "../../utils/validateToken";

const ProtectedRoute = ({ children }: { children: React.ReactNode }) => {
  const navigate = useNavigate();
  const dispatch = useDispatch<AppDispatch>();
  useEffect(() => {
    const access_token: string | null = localStorage.getItem("access_token");
    if (access_token == null || isTokenExpired(access_token)) {
      navigate("/login");
    }
  }, [dispatch, navigate]);

  return <>{children}</>;
};

export { ProtectedRoute };
