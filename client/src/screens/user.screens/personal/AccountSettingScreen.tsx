import ChangePassword from "../../../components/user.components/personal/ChangePassword";
import UserProfileForm from "../../../components/user.components/personal/UserProfileForm.tsx";

export default function AccountSettingScreen() {
    return (
        <div className="bg-gray-50 min-h-[90vh] flex items-stretch justify-center text-base p-28">
            <UserProfileForm />
            <ChangePassword />
        </div>
    )
}
