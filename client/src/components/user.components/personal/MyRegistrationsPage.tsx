import { useEffect, useState } from "react";
import SatusFilter from "../StatusFilter.tsx";
import { fetchListOfMyRegistration } from "../../../apiService/MyRegistration.service.ts";
import ListMyRegistrationCardComponent from "./ListMyRegistrationCardComponent.tsx";
import { Status, statusList } from "../../../utils/index.ts";
import PaginationSection from "../Homepage/PaginationSection.tsx";
import { useDispatch, useSelector } from "react-redux";
import {
    handleChangeCurrentPage,
    handleChangeStatus,
    saveDataListRegistration,
} from "../../../redux/slice/registration.slice.ts";
import { RootState } from "../../../redux/store/store.ts";
import { useRefreshState } from "../../../hooks/use-refresh-state.ts";

export type OverviewMyRegistrationType = {
    id?: string;
    courseName: string;
    status: Status;
    coursePlatform: string;
    startDate: Date;
    endDate: Date;
    courseThumbnailUrl: string;
};

export default function MyRegistrationPage() {
    const [isLoading, setIsLoading] = useState(false);
    const dispatch = useDispatch();
    const { registrationFlagAdmin } = useRefreshState((state) => state);
    const totalItem = useSelector(
        (state: RootState) => state.registration.totalItem
    );
    const currentPage = useSelector(
        (state: RootState) => state.registration.currentPage
    );
    const filterBy = useSelector(
        (state: RootState) => state.registration.status
    );
    const listMyRegistration = useSelector(
        (state: RootState) => state.registration.data
    );

    const fetchData = async (
        page: number = currentPage,
        status: string = filterBy
    ) => {
        setIsLoading(true);
        const result = await fetchListOfMyRegistration(page, status);
        if (result && result.data) {
            dispatch(saveDataListRegistration(result.data));
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchData(currentPage, filterBy);
    }, [currentPage, filterBy, registrationFlagAdmin]);

    useEffect(() => {
        dispatch(handleChangeStatus("SUBMITTED"));
        fetchData(1, "SUBMITTED");
    }, []);

    const onPageNumberClick = (newPageNumber: number) => {
        dispatch(handleChangeCurrentPage(newPageNumber));
    };

    const onFilterByChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        dispatch(handleChangeStatus(e.target.value));
    };

    return (
        <div className='flex flex-col gap-5 w-[80%] p-3 grow min-h-[1000px]'>
            <div className='flex items-center justify-between'>
                {(listMyRegistration && listMyRegistration.length > 0)
                    ? (
                        <div>
                            Showing {(currentPage - 1) * 10 + 1} -{" "}
                            {Math.min(currentPage * 10, totalItem)} of {totalItem}{" "}
                            results
                        </div>
                    )
                    : <div>Showing 0 results</div>}
                <SatusFilter
                    listOption={statusList}
                    onSortByChange={onFilterByChange}
                />
            </div>
            {listMyRegistration != null && listMyRegistration.length !== 0 && (
                <ListMyRegistrationCardComponent
                    listRegistration={listMyRegistration}
                    isLoading={isLoading}
                />
            )}
            {totalItem > 10 && (
                <PaginationSection
                    isLoading={isLoading}
                    totalItems={totalItem}
                    currentPage={currentPage}
                    itemPerPage={10}
                    setCurrentPage={onPageNumberClick}
                />
            )}
        </div>
    );
}
