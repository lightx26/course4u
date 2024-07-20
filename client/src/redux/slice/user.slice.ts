import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { handleRegister } from "../../apiService/user.service";
import { handleLogin, getUserDetails } from "../../apiService/user.service";

export interface IUser {
  isAuthenticated: boolean;
  user: {
    id: string;
    username: string;
    fullName: string;
    email: string;
    telephone: string;
    avatarUrl: string;
    dateOfBirth: string;
    role: string;
    gender?: string;
  };
  statusLogin: "idle" | "loading" | "failed";
  statusRegister: "idle" | "loading" | "failed";
}
interface ISignUpRequest {
  username: string;
  password: string;
  confirmPassword: string;
  email: string;
  fullname: string;
  dateofbirth: string | null;
  gender: string | null;
}

// Create an initial state
const initialState: IUser = {
  isAuthenticated: false,
  user: {
    id: "",
    username: "",
    fullName: "",
    email: "",
    telephone: "",
    avatarUrl: "",
    dateOfBirth: "",
    role: "",
    gender: ""
  },
  statusLogin: "idle",
  statusRegister: "idle",
};

// Create async thunk for user login
export const userLogin = createAsyncThunk(
  "users/userLogin",
  async (payload: { username: string; password: string }) => {
    const response = await handleLogin(payload.username, payload.password);
    return response;
  }
);

// Create async thunk to fetch user details
export const fetchUserDetails = createAsyncThunk(
  "users/fetchUserDetails",
  async () => {
    const userDetails = await getUserDetails();
    return userDetails;
  }
);
export const userRegister = createAsyncThunk(
  "users/userRegister",
  async (payload: ISignUpRequest) => {
    const response = await handleRegister(payload);
    return response;
  }
);
// Create a slice
export const userSlice = createSlice({
  name: "users",
  initialState,
  reducers: {
    handleLogout: (state) => {
      state.isAuthenticated = false;
      state.user = {
        id: "",
        username: "",
        fullName: "",
        email: "",
        telephone: "",
        avatarUrl: "",
        dateOfBirth: "",
        role: "",
        gender: "",
      };
      localStorage.removeItem("access_token");
    },
    updateUser: (state, payload) => {
      state.user = { ...state.user, ...payload.payload };
    }
  },
  extraReducers: (builder) => {
    builder
      .addCase(userLogin.pending, (state) => {
        state.statusLogin = "loading";
      })
      .addCase(userLogin.fulfilled, (state, action) => {
        const access_token: string | undefined = action.payload?.access_token;
        if (access_token) {
          localStorage.setItem("access_token", access_token);
          state.isAuthenticated = true;
          state.statusLogin = "idle";
        } else {
          state.statusLogin = "failed";
        }
      })
      .addCase(userLogin.rejected, (state) => {
        state.statusLogin = "failed";
      })
      .addCase(fetchUserDetails.fulfilled, (state, action) => {
        if (action.payload) {
          state.isAuthenticated = true;
          state.user = { ...state.user, ...action.payload };
        }
      })
      .addCase(userRegister.pending, (state) => {
        state.statusRegister = "loading";
      })
      .addCase(userRegister.fulfilled, (state) => {
        state.statusRegister = "idle";
      })
      .addCase(userRegister.rejected, (state) => {
        state.statusRegister = "failed";
      });
  },
});
export const { handleLogout, updateUser } = userSlice.actions;
// Export the reducer
export default userSlice.reducer;
