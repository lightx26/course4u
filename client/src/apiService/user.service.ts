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
  } catch (error: any) {
    throw new Error(error.message);
  }
};

export { handleLogin };
