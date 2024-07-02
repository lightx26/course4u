import instance from "../utils/customizeAxios";

const handleLogin = async (username: string, password: string) => {
  try {
    console.log(username, password);
    const response = await instance.post("/auth/login", {
      username,
      password,
    });

    if (response)
      return {
        access_token: response?.access_token,
      };
  } catch (error: any) {
    throw new Error(error.message);
  }
};

export { handleLogin };
