import CardFunctionList from "./CardFunctionList";
import { useSelector } from "react-redux";
import { RootState } from "../../../redux/store/store";


function AdminCard() {
  const userData = useSelector((state: RootState) => state.user.user);

  const avatarUrl = userData.avatarUrl?.startsWith("http") || userData.avatarUrl?.startsWith("data")
    ? userData.avatarUrl
    : (userData.avatarUrl != null && userData.avatarUrl != undefined && userData.avatarUrl != "") ? `${import.meta.env.VITE_BACKEND_URL}${userData.avatarUrl}` : `/avatar/Default Avatar.svg`;

  return (
    <div
      className="bg-white h-[20rem] rounded-2xl pt-4 min-w-60"
      style={{ boxShadow: "0px 0px 4px 0px rgba(0, 0, 0, 0.25)" }}
    >
      <div className="personalizaion h-[49%] border-b-2">
        <div className="avatar h-[50%] w-full flex align-center justify-center mb-2.5">
          <div className="flex justify-center align-center">
            <img
              className="rounded-full"
              src={avatarUrl}
              alt="adm_avt"
              style={{ backgroundColor: "gray", textAlign: "center" }}
            />
          </div>
        </div>
        <div className="flex justify-center w-full mb-1 text-xl font-semibold name align-center">
          {userData.username}
        </div>
        <div className="flex justify-center w-full mb-1 text-xs font-semibold email align-center">
          {userData.email}
        </div>
      </div>
      <div className="flex justify-start my-4 ml-4 function_list align-center">
        <CardFunctionList />
      </div>
    </div>
  );
}

export default AdminCard;
