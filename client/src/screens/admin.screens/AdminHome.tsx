import { useEffect, useState } from "react";
import { RegistrationType } from "../../App";

import AdminCard from "../../components/admin.components/mainPage.components/AdminCard.tsx";
import PaginationSection from "../../components/user.components/Homepage/PaginationSection.tsx";

import UtilsBar from "../../components/admin.components/mainPage.components/UtilsBar.tsx";
import RegistrationList from "../../components/admin.components/registrations.components/RegistrationList.tsx";
import registrationStatusList from "../../utils/registrationStatusList.ts";

import { fetchAllRegistrations } from "../../apiService/Registration.service.ts";
import { handleAvatarUrl } from "../../utils/handleAvatarUrl.ts";

import {useRefreshState} from "../../hooks/use-refresh-state.ts";

import {RootState} from "../../redux/store/store.ts";
import {useDispatch, useSelector} from "react-redux";
import {RegistrationParamsType} from "../../redux/slice/adminRegistration.slice.ts";
import {
    handleCurrentPageChange,
    handleTotalItemChange,
    handleShowingMessageChange,
    saveRegistrationsData,
} from "../../redux/slice/adminRegistration.slice.ts";

function AdminHomePage() {
    const { registrationFlagAdmin } = useRefreshState((state) => state);

    const dispatch = useDispatch();

    const currentPage: number = useSelector(
        (state: RootState) => state.adminRegistration.currentPage
    );

    const totalItem: number = useSelector(
        (state: RootState) => state.adminRegistration.totalItem
    );

    const registrationList: RegistrationType[] = useSelector(
        (state: RootState) => state.adminRegistration.data
    );

    const showingMessage: string = useSelector(
        (state: RootState) => state.adminRegistration.showingMessage
    )

    const options: RegistrationParamsType = useSelector(
        (state: RootState) => state.adminRegistration.options
    );

    const [isLoading, setIsLoading] = useState(true);
    const avatarUrl = handleAvatarUrl(
        useSelector((state: RootState) => state.user.user.avatarUrl)
    );

    const handleFetchRegistrations = async (params: RegistrationParamsType, page: number = 1) => {
        setIsLoading(true);
        try {
            const data = await fetchAllRegistrations(params, page);
            if (data) {
                dispatch(saveRegistrationsData(data.content));
                dispatch(handleTotalItemChange(data.totalElements));

                const endInterval = Math.min(page * 8, data.totalElements);
                const startInterval = endInterval == 0 ? 0 : (page - 1) * 8 + 1;
                dispatch(handleShowingMessageChange(`Showing ${startInterval}-${endInterval} of ${data.totalElements} registrations`));
            }
        } catch (e) {
            console.error(e);
        }
        setIsLoading(false);
    };

    useEffect(() => {
        handleFetchRegistrations(options, 1)
        dispatch(handleCurrentPageChange(1));
    }, [options]);

    useEffect(() => {
        handleFetchRegistrations(options, currentPage)
    }, [currentPage, registrationFlagAdmin]);

    const handlePageNumberClick = (newPageNumber: number) => {
        dispatch(handleCurrentPageChange(newPageNumber));
    }

    return (
        <div className='bg-[#F5F7FA] h-[100dvh] min-h-[1024px]'>
            <div className='flex w-screen gap-10 pt-10 mx-auto my-0 body px-14 max-w-screen-2xl'>
                <div className='w-56 card'>
                    <AdminCard avatarUrl={avatarUrl} />
                </div>
                <div className='registration-section flex flex-col gap-8 w-[80%]'>
                    <div className='filters'>
                        <UtilsBar statusList={registrationStatusList} options={options} role={"admin"}/>
                    </div>
                    <div className='registration-list min-h-[200px]'>
                        <div className='mb-2 showing-status'>
                            {showingMessage}
                        </div>
                        <RegistrationList
                            ListRegistration={registrationList}
                            isLoading={isLoading}
                        />
                    </div>
                    <div className='w-full pagination mt-7'>
                        <PaginationSection
                            totalItems={totalItem}
                            itemPerPage={8}
                            currentPage={currentPage}
                            setCurrentPage={handlePageNumberClick}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AdminHomePage;
