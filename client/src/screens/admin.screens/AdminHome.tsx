import { useEffect, useState } from "react";
import { RegistrationType } from "../../App";

import AdminCard from "../../components/admin.components/mainPage.components/AdminCard.tsx";
import PaginationSection from "../../components/user.components/Homepage/PaginationSection.tsx";

import UtilsBar from "../../components/admin.components/mainPage.components/UtilsBar.tsx";
import RegistrationList from "../../components/admin.components/registrations.components/RegistrationList.tsx";

import { fetchAllRegistrations } from "../../apiService/Admin.service.ts";
import { RootState } from "../../redux/store/store.ts";
import { useSelector } from "react-redux";
import { handleAvatarUrl } from "../../utils/handleAvatarUrl.ts";
import { useRefreshState } from "../../hooks/use-refresh-state.ts";

function AdminHomePage() {
    const [currentPage, setCurrentPage] = useState(1);
    const [ListRegistration, setListRegistration] = useState<
        RegistrationType[]
    >([]);
    const [isLoading, setIsLoading] = useState(true);
    const [totalItems, setTotalItems] = useState(0);
    const [showingStatus, setShowingStatus] = useState("");
    const { registrationFlagAdmin } = useRefreshState((state) => state);
    const avatarUrl = handleAvatarUrl(
        useSelector((state: RootState) => state.user.user.avatarUrl)
    );

    const handleFetchRegistrations = async (
        page: number = 1,
        status: string = "all"
    ) => {
        try {
            const data = await fetchAllRegistrations(page, status);
            if (data) {
                setListRegistration(data.content);
                setTotalItems(data.totalElements);

                const startInterval = (page - 1) * 8 + 1;
                const endInterval = Math.min(page * 8, data.totalElements);
                setShowingStatus(
                    `Showing ${startInterval}-${endInterval} of ${data.totalElements} registrations`
                );
            }
        } catch (e) {
            console.error(e);
        }
        setIsLoading(false);
    };

    useEffect(() => {
        handleFetchRegistrations(currentPage);
    }, [currentPage, registrationFlagAdmin]);

    return (
        <div className='bg-[#F5F7FA] h-[100dvh] min-h-[1024px]'>
            <div className='flex w-screen gap-10 pt-10 mx-auto my-0 body px-14 max-w-screen-2xl'>
                <div className='w-56 card'>
                    <AdminCard avatarUrl={avatarUrl} />
                </div>
                <div className='registration-section flex flex-col gap-8 w-[80%]'>
                    <div className='filters'>
                        <UtilsBar />
                    </div>
                    <div className='registration-list min-h-[200px]'>
                        <div className='mb-2 showing-status'>
                            {showingStatus}
                        </div>
                        <RegistrationList
                            ListRegistration={ListRegistration}
                            isLoading={isLoading}
                        />
                    </div>
                    <div className='w-full pagination mt-7'>
                        <PaginationSection
                            totalItems={totalItems}
                            itemPerPage={8}
                            currentPage={currentPage}
                            setCurrentPage={(page: number) => {
                                setCurrentPage(page);
                            }}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AdminHomePage;
