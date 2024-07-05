import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { handleLogin, getUserDetails } from "../../apiService/user.service";

export interface IUser {
  isAuthenticated: boolean;
  user: {
    id: string;
    Username: string;
    FullName: string;
    AvatarUrl: string;
    Email: string;
    Role: string;
    DateOfBirth: string;
    Gender: string;
    Telephone: string;
  };
  statusLogin: "idle" | "loading" | "failed";
}

// Create an initial state
const initialState: IUser = {
  isAuthenticated: false,
  user: {
    id: "",
    Username: "",
    FullName: "",
    AvatarUrl: "",
    Email: "",
    Role: "",
    DateOfBirth: "",
    Gender: "",
    Telephone: "",
  },
  statusLogin: "idle",
};

// Create async thunk for user login
export const userLogin = createAsyncThunk(
  "users/userLogin",
  async (payload: { username: string; password: string }) => {
    const response = await handleLogin(payload.username, payload.password);
    console.log("check access_token", response);
    return response;
  }
);

// Create async thunk to fetch user details
export const fetchUserDetails = createAsyncThunk(
  "users/fetchUserDetails",
  async (access_token: string) => {
    const userDetails = await getUserDetails(access_token);
    return userDetails;
  }
);

// Create a slice
export const userSlice = createSlice({
  name: "users",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(userLogin.pending, (state) => {
        state.statusLogin = "loading";
      })
      .addCase(userLogin.fulfilled, (state, action) => {
        const access_token: string | undefined = action.payload?.access_token;
        if (access_token) {
          localStorage.setItem("access_token", access_token); // No need to stringify a simple string
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
          state.user = { ...state.user, ...action.payload }; // Simplified spreading of user details
        }
      });
  },
});

// Export the reducer
export default userSlice.reducer;
