import React from "react";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { fetchUserDetails } from "../../redux/slice/user.slice";
import { AppDispatch } from "../../redux/store/store";
const isTokenExpired = (token: string) => {
  if (!token) return true;

  const payloadBase64 = token.split(".")[1];
  const decodedPayload = JSON.parse(atob(payloadBase64));
  const currentTime = Math.floor(Date.now() / 1000);

  return decodedPayload.exp < currentTime;
};

const ProtectedRoute = ({ children }: { children: React.ReactNode }) => {
  const navigate = useNavigate();
  const dispatch = useDispatch<AppDispatch>();

  useEffect(() => {
    const access_token = localStorage.getItem("access_token");
    if (access_token && !isTokenExpired(access_token)) {
      dispatch(fetchUserDetails(access_token));
    } else {
      navigate("/login");
    }
  }, [dispatch, navigate]);

  return children;
};

export default ProtectedRoute;
