import RegistrationList from "../../../assets/images/admin.images/RegistrationList.svg";
import CourseList from "../../../assets/images/admin.images/CourseList.svg";
import Notification from "../../../assets/images/admin.images/Notification.svg";
import CreateCourse from "../../../assets/images/admin.images/CreateCourse.svg";
import EditProfile from "../../../assets/images/admin.images/EditProfile.svg";
import SignOut from "../../../assets/images/admin.images/SignOut.svg";

function CardFunctionList() {
  return (
    <div
      className="function_list flex-col"
      style={{ justifyItems: "space-between", gap: "100px" }}
    >
      <div className="function flex align-center justify-start gap-2 cursor-pointer hover:text-purple  mb-2">
        <div className="icon flex" style={{ alignItems: "center" }}>
          <img
            className="h-[70%]"
            src={RegistrationList}
            alt="registration_list_icon"
          />
        </div>
        <span className="text-purple">Registration List</span>
      </div>
      <div className="function flex align-center justify-start gap-2 cursor-pointer  mb-2">
        <div className="icon flex" style={{ alignItems: "center" }}>
          <img
            className="h-[70%]"
            src={CourseList}
            alt="registration_list_icon"
          />
        </div>
        <span>Course List</span>
      </div>
      <div className="function flex align-center justify-start gap-2 cursor-pointer  mb-2">
        <div className="icon flex" style={{ alignItems: "center" }}>
          <img
            className="h-[70%]"
            src={Notification}
            alt="registration_list_icon"
          />
        </div>
        <span>Create a notification</span>
      </div>
      <div className="function flex align-center justify-start gap-2 cursor-pointer mb-2">
        <div className="icon flex" style={{ alignItems: "center" }}>
          <img
            className="h-[70%]"
            src={CreateCourse}
            alt="registration_list_icon"
          />
        </div>
        <span>Create a course</span>
      </div>
      <div className="function flex align-center justify-start gap-2 cursor-pointer  mb-2">
        <div className="icon flex" style={{ alignItems: "center" }}>
          <img
            className="h-[70%]"
            src={EditProfile}
            alt="registration_list_icon"
          />
        </div>
        <span>Edit profile</span>
      </div>
      <div className="function flex align-center justify-start gap-1 cursor-pointer">
        <div className="icon flex" style={{ alignItems: "center" }}>
          <img className="h-[70%]" src={SignOut} alt="registration_list_icon" />
        </div>
        <span>Sign out</span>
      </div>
    </div>
  );
}

export default CardFunctionList;
