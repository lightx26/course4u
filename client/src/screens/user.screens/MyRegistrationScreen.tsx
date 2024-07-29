import MyRegistrationPage from "../../components/user.components/personal/MyRegistrationsPage";

const MyRegistrationsScreen: React.FC = () => {
    return (
        <div className='flex items-start gap-2 pb-8 mx-auto max-w-screen-2xl'>
            <MyRegistrationPage />
        </div>
    );
};
export default MyRegistrationsScreen;
