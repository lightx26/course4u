import { File } from "buffer";
import instance from "../utils/customizeAxios";
import { toast } from "sonner";

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
    toast.error("Failed to login", {
      style: {
        color: "red",
      },
      description: "Please check your username and password again",
    });
  }
};

const handleRegister = async (data: ISignUpRequest) => {
  try {
    const response = await instance.post("/auth/signup", data);
    if (response) return response.data;
  } catch (error) {
    toast.error("Failed to register", {
      style: {
        color: "red",
      },
      description: "Creating account failed, please try again",
    });
  }
};

const getUserDetails = async () => {
  try {
    const response = await instance.get("/users/my-profile");
    return response.data;
  } catch (error) {
    toast.error("Can't get user details", {
      style: {
        color: "red",
      },
      description: "Please refresh the page",
    });
  }
};

const handleChangePassword = async (oldPassword: string, newPassword: string) => {
  const params = {
    oldPassword,
    newPassword,
  };
  try {
    const response = await instance.put("/users/change-password", params);
    return response.status;
  } catch (error) {
    toast.error("Something went wrong when applying the new password", {
      style: {
        color: "red",
      }
    });
  }
}



export type userInfoType = {
  id?: string;
  fullName?: string;
  telephone?: string;
  imageFile?: File;
  dateOfBirth?: Date;
  role?: string;
  gender?: string;
}


const handleEditProfile = async (formData: FormData) => {
  try {
    const response = await instance.put("/users/edit", formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    // Explicitly return the data and status properties
    return { data: response.data, status: response.status };
  } catch (error) {
    toast.error("Failed to change your persional information", {
      style: {
        color: "red"
      }
    });
    throw error;
  }
};

export { handleLogin, handleRegister, getUserDetails, handleChangePassword, handleEditProfile };
