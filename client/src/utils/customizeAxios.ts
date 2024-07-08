import axios from "axios";

const instance = axios.create({
  baseURL: `${import.meta.env.VITE_BACKEND_URL}`,
  withCredentials: true,
});

// Add token to the header
instance.defaults.headers.common = {
  Authorization: `Bearer ${localStorage.getItem("access_token")}`,
};

// Add a request interceptor
instance.interceptors.request.use();

// Add a response interceptor
instance.interceptors.response.use();

//export the instance
export default instance;
