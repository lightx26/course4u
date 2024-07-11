import instance from "../utils/customizeAxios";

const handleLogin = async (username: string, password: string) => {
  try {
    const response = await instance.post("/auth/login", {
      username,
      password,
    });

    if (response)
      return {
        access_token: response?.data?.access_token,
      };
  } catch (error) {
    console.error(error);
  }
};

const getUserDetails = async () => {
  try {
    const response = await instance.get("/users/my-profile");
    return response.data;
  } catch (error) {
    console.error(error);
  }
};

export { handleLogin, getUserDetails };
