import { OverviewMyRegistrationType } from "./MyRegistrationsPage";
import { RegistrationSkeletonLoader } from "./MyRegistrationCardComponent";
import MyRegistrationCompenents from "./MyRegistrationCardComponent";

type PropsType = {
    listRegistration: OverviewMyRegistrationType[];
    isLoading: boolean;
};

export default function ListMyRegistrationCardComponent(props: PropsType) {
    if (props.isLoading) {
        return (
            <div className='grid gap-4 text-black 2xl:grid-cols-5 md:grid-cols-2 xl:grid-cols-3 scree auto-rows-auto'>
                {Array.from({ length: 10 }).map((_, index) => (
                    <RegistrationSkeletonLoader key={index} />
                ))}
            </div>
        );
    }
    return (
        <div className='grid gap-4 text-black 2xl:grid-cols-5 md:grid-cols-2 xl:grid-cols-3 scree auto-rows-auto'>
            {props.listRegistration.map((registration) => {
                return (
                    <MyRegistrationCompenents
                        registration={registration}
                        key={registration.id}
                    />
                );
            })}
        </div>
    );
}
