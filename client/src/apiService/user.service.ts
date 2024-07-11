import instance from "../utils/customizeAxios";

interface ISignUpRequest {
  username: string;
  password: string;
  confirmPassword: string;
  email: string;
  fullname: string;
  dateofbirth: string | null;
  gender: string | null;
}

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

const handleRegister = async (data: ISignUpRequest) => {
  try {
    const response = await instance.post("/auth/signup", data);
    if (response) return response.data;
  } catch (error: any) {
    throw new Error(error.message);
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

export { handleLogin, handleRegister ,getUserDetails};

