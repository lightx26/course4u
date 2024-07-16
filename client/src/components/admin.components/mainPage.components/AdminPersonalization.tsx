import PersonalizationFunctionList from "./PersonalizationFunctionList.tsx";
import AdminAvatar from "../../../assets/images/admin.images/AdminAvatar.png";

interface AdminCardProps {
    username: string;
    email: string;
    avatarUrl: string;
}

const AdminPersonalization = () =>{
    // Placeholder information
    const adminInformation: AdminCardProps = {
        username: "Admin",
        email: "admin@mgm-tp.com",
        avatarUrl: AdminAvatar,
    };

    return (
        <div
            className="bg-white h-[24rem] pt-4"
        >
            <div className="personalizaion h-[35%] border-b-2">
                <div className="avatar h-[50%] w-full flex align-center justify-center mb-2.5">
                    <div className="flex align-center justify-center">
                        <img
                            className="rounded-full"
                            src={adminInformation.avatarUrl}
                            alt="adm_avt"
                            style={{backgroundColor: "gray", textAlign: "center"}}
                        />
                    </div>
                </div>
                <div className="name w-full flex align-center justify-center text-xl font-semibold">
                    {adminInformation.username}
                </div>
                <div className="email w-full flex align-center justify-center text-sm font-semibold">
                    {adminInformation.email}
                </div>
            </div>
            <div className="function_list flex justify-start align-center ml-4 my-4">
                <PersonalizationFunctionList/>
            </div>
        </div>
    )
}

export default AdminPersonalization;