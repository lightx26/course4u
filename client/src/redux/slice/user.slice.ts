import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
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
  };
  statusLogin: "idle" | "loading" | "failed";
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
  },
  statusLogin: "idle",
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
      };
      localStorage.removeItem("access_token");
    },
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
      });
  },
});
export const { handleLogout } = userSlice.actions;
// Export the reducer
export default userSlice.reducer;
