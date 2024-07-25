import React from "react";
import { handleLogout } from "../redux/slice/user.slice.ts";
import { Dispatch } from "@reduxjs/toolkit";
import { UnknownAction } from "redux-saga";
import { NavigateFunction } from "react-router-dom";
import { updateSearch, updateSort } from "../redux/slice/course.slice.ts";
import { deleteAllFilterItem } from "../redux/slice/searchFilterItem.slice.ts";

const handleRegistrationListClick = (
  e: React.MouseEvent,
  navigate: NavigateFunction,
  userRole: string
) => {
  e.preventDefault();
  if (userRole === "ADMIN") navigate("/admin");
  else if (userRole === "ACCOUNTANT") navigate("/accountant");
};

const handleCourseListClick = (
  e: React.MouseEvent,
  navigate: NavigateFunction
) => {
  e.preventDefault();
  navigate("/admin/courses");
};

const handleEditProfileClick = (
  e: React.MouseEvent,
  navigate: NavigateFunction,
  userRole: string
) => {
  e.preventDefault();
  if (userRole === "ADMIN") navigate("/admin/profile");
  else if (userRole === "ACCOUNTANT") navigate("/accountant/profile");
};

const handleSignOutClick = (
  e: React.MouseEvent,
  navigate: NavigateFunction,
  dispatch: Dispatch<UnknownAction>
) => {
  e.preventDefault();
  navigate("/login");
  dispatch(handleLogout());
  dispatch(updateSearch(""));
  dispatch(deleteAllFilterItem());
  dispatch(updateSort("NEWEST"));
};

const functionMapping = (
  e: React.MouseEvent,
  navigate: NavigateFunction,
  dispatch: Dispatch<UnknownAction>,
  itemAlt: string,
  userRole: string
) => {
  switch (itemAlt) {
    case "registration_list":
      handleRegistrationListClick(e, navigate, userRole);
      break;
    case "course_list":
      handleCourseListClick(e, navigate);
      break;
    case "edit_profile":
      handleEditProfileClick(e, navigate, userRole);
      break;
    case "sign_out":
      handleSignOutClick(e, navigate, dispatch);
      break;
  }
};

export default functionMapping;
