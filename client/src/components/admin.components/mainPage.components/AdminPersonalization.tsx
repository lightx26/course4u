import PersonalizationFunctionList from "./PersonalizationFunctionList.tsx";
import { useSelector } from "react-redux";
import { RootState } from "../../../redux/store/store.ts";
import { handleAvatarUrl } from "../../../utils/handleAvatarUrl.ts";

const AdminPersonalization = ({ avatarUrl }: { avatarUrl?: string }) => {
  const adminData = useSelector((state: RootState) => state.user.user);
  const adminAvatar = avatarUrl ?? handleAvatarUrl(adminData.avatarUrl);
  const adminDisplayName: string = adminData.fullName
    ? adminData.fullName
    : adminData.username
    ? adminData.username
    : "Admin";

  const userRole = adminData.role;
  return (
    <div className="absolute right-0.5 mt-1 w-60 bg-white rounded-xl shadow-lg z-50">
      <div
        className={`bg-white ${
          userRole === "ACCOUNTANT" ? "h-fit" : "h-[19rem]"
        }  pt-4`}
      >
        <div className="personalizaion h-[44%] border-b-2">
          <div className="avatar h-[50%] w-full flex align-center justify-center mb-2.5">
            <div className="flex align-center justify-center">
              <img
                className="rounded-full w-[70px] h-[70px]"
                src={adminAvatar}
                alt="adm_avt"
                style={{ backgroundColor: "gray", textAlign: "center" }}
              />
            </div>
          </div>
          <div className="name max-w-[200px] max-h-[28px] line-clamp-1 text-center mx-auto justify-center text-xl font-semibold overflow-hidden overflow-ellipsis">
            {adminDisplayName}
          </div>
          <div className="email max-w-[150px] max-h-[20px] line-clamp-1 text-center mx-auto text-sm font-semibold overflow-hidden overflow-ellipsis">
            {adminData.email}
          </div>
        </div>
        <div
          className={`function_list flex justify-start align-center ml-4 mt-4  ${
            userRole === "ACCOUNTANT" ? "mb-0" : "mb-4"
          } `}
        >
          <PersonalizationFunctionList />
        </div>
      </div>
    </div>
  );
};

export default AdminPersonalization;
