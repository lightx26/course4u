import ChangePassword from "../../../components/user/change-password.tsx";
import UserProfileForm from "../../../components/user/user-profile-form.tsx";

export default function AccountSettingScreen() {
    return (
        <div className='bg-gray-50 min-h-[90vh] flex items-stretch gap-4 text-base flex-wrap p-10'>
            <UserProfileForm />
            <ChangePassword />
        </div>
    );
}
