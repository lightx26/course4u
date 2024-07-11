import axios from "axios";
import { isTokenExpired } from "./validateToken";
const instance = axios.create({
  baseURL: `${import.meta.env.VITE_BACKEND_URL}`,
  withCredentials: true,
});

// Add a request interceptor to set the Authorization header
instance.interceptors.request.use(
  (config) => {
    const authPaths = ["/auth/login", "/auth/signup"];
    const token = localStorage.getItem("access_token");

    if (authPaths.includes(config.url as string)) {
      return config;
    }

    if (token && !isTokenExpired(token)) {
      config.headers.Authorization = `Bearer ${token}`;
    } else if (!token || isTokenExpired(token)) {
      if (
        window.location.pathname !== `${import.meta.env.VITE_BASE_URL}/login`
      ) {
        window.location.href = `${import.meta.env.VITE_BASE_URL}/login`;
      }
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add a request interceptor
instance.interceptors.request.use();

// Add a response interceptor
instance.interceptors.response.use();

//export the instance
export default instance;
