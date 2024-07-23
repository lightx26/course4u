import CardFunctionList from "./CardFunctionList";
import {useSelector} from "react-redux";
import {RootState} from "../../../redux/store/store";
import {useNavigate} from "react-router-dom";
import {handleAvatarUrl} from "../../../utils/handleAvatarUrl";


function AdminCard({avatarUrl}: { avatarUrl?: string }) {
    const adminData = useSelector((state: RootState) => state.user.user);
    const navigate = useNavigate();

    const adminDisplayName: string =
        adminData.fullName ? adminData.fullName
        : adminData.username ? adminData.username
        : "Admin";

    const adminAvatar = avatarUrl ?? handleAvatarUrl(adminData.avatarUrl);
    return (
        <div
            className="bg-white h-[20rem] rounded-2xl pt-4 min-w-60"
            style={{boxShadow: "0px 0px 4px 0px rgba(0, 0, 0, 0.25)"}}
        >
            <div className="personalizaion h-[49%] border-b-2">
                <div className="avatar h-[50%] w-full flex align-center justify-center mb-2.5">
                    <div className="flex justify-center align-center">
                        <img
                            className="rounded-full"
                            src={adminAvatar}
                            alt="adm_avt"
                            style={{backgroundColor: "gray", textAlign: "center", cursor: "pointer"}}
                            onClick={() => {
                                navigate("/admin/profile");
                            }}
                        />
                    </div>
                </div>
                <div
                    className="max-w-[200px] max-h-[30px]
                    mx-auto text-center mb-1 text-xl font-semibold
                    overflow-hidden overflow-ellipsis">
                    {adminDisplayName}
                </div>
                <div className="max-w-[200px] max-h-[20px]
                    mx-auto text-center mb-1 text-xs font-semibold
                    overflow-hidden overflow-ellipsis">
                    {adminData.email}
                </div>
            </div>
            <div className="flex justify-start my-4 ml-4 function_list align-center">
                <CardFunctionList/>
            </div>
        </div>
    );
}

export default AdminCard;
