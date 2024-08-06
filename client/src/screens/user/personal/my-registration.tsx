import MyRegistrationPage from "../../../components/registration/grid-view/my-registrations";

const MyRegistrationsScreen: React.FC = () => {
    return (
        <div className='flex items-start gap-2 pb-8 mx-auto max-w-screen-2xl'>
            <MyRegistrationPage />
        </div>
    );
};
export default MyRegistrationsScreen;
