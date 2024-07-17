import CardFunctionList from "./CardFunctionList";
import AdminAvatar from "../../../assets/images/admin.images/AdminAvatar.png";

interface AdminCardProps {
  username: string;
  email: string;
  avatarUrl: string;
}

function AdminCard() {
  // Placeholder information
  const adminInformation: AdminCardProps = {
    username: "Admin",
    email: "admin@mgm-tp.com",
    avatarUrl: AdminAvatar,
  };

  return (
    <div
      className="bg-white h-[20rem] rounded-2xl pt-4"
      style={{ boxShadow: "0px 0px 4px 0px rgba(0, 0, 0, 0.25)" }}
    >
      <div className="personalizaion h-[49%] border-b-2">
        <div className="avatar h-[50%] w-full flex align-center justify-center mb-2.5">
          <div className="flex align-center justify-center">
            <img
              className="rounded-full"
              src={adminInformation.avatarUrl}
              alt="adm_avt"
              style={{ backgroundColor: "gray", textAlign: "center" }}
            />
          </div>
        </div>
        <div className="name w-full flex align-center justify-center mb-1 text-xl font-semibold">
          {adminInformation.username}
        </div>
        <div className="email w-full flex align-center justify-center mb-1 text-xs font-semibold">
          {adminInformation.email}
        </div>
      </div>
      <div className="function_list flex justify-start align-center ml-4 my-4">
        <CardFunctionList />
      </div>
    </div>
  );
}

export default AdminCard;
