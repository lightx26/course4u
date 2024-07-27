import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../redux/store/store.ts";
import { RegistrationType } from "../../App.tsx";
import {
  handleCurrentPageChange,
  handleShowingMessageChange,
  handleTotalItemChange,
  RegistrationParamsType,
  saveRegistrationsData,
} from "../../redux/slice/accountantRegistration.slice.ts";
import { useEffect, useState } from "react";
import { handleAvatarUrl } from "../../utils/handleAvatarUrl.ts";
import { fetchAllRegistrations } from "../../apiService/Admin.service.ts";
import AdminCard from "../../components/admin.components/mainPage.components/AdminCard.tsx";
import RegistrationList from "../../components/admin.components/registrations.components/RegistrationList.tsx";
import PaginationSection from "../../components/user.components/Homepage/PaginationSection.tsx";
import AccountantUtilsBar from "../../components/admin.components/mainPage.components/AccountantUtilsBar.tsx";
import { useRefreshState } from "../../hooks/use-refresh-state.ts";

const AccountantHomePage = () => {
  const dispatch = useDispatch();
  const { registrationFlagAccountant } = useRefreshState((state) => state);
  const currentPage: number = useSelector(
    (state: RootState) => state.accountantRegistration.currentPage
  );

  const totalItem: number = useSelector(
    (state: RootState) => state.accountantRegistration.totalItem
  );

  const registrationList: RegistrationType[] = useSelector(
    (state: RootState) => state.accountantRegistration.data
  );

  const showingMessage: string = useSelector(
    (state: RootState) => state.accountantRegistration.showingMessage
  );

  const options: RegistrationParamsType = useSelector(
    (state: RootState) => state.accountantRegistration.options
  );

  const [isLoading, setIsLoading] = useState(true);
  const avatarUrl = handleAvatarUrl(
    useSelector((state: RootState) => state.user.user.avatarUrl)
  );

  const handleFetchRegistrations = async (
    params: RegistrationParamsType = options,
    page: number = 1
  ) => {
    setIsLoading(true);
    try {
      const data = await fetchAllRegistrations(params, page);
      if (data) {
        dispatch(saveRegistrationsData(data.content));
        dispatch(handleTotalItemChange(data.totalElements));

        const endInterval = Math.min(page * 8, data.totalElements);
        const startInterval = endInterval == 0 ? 0 : (page - 1) * 8 + 1;
        dispatch(
          handleShowingMessageChange(
            `Showing ${startInterval}-${endInterval} of ${data.totalElements} registrations`
          )
        );
      }
    } catch (e) {
      console.error(e);
    }
    setIsLoading(false);
  };

  useEffect(() => {
    const initialOptions: RegistrationParamsType = {
      status: "Verified",
      search: "",
      orderBy: "id",
      isAscending: false,
    };
    handleFetchRegistrations(initialOptions, 1);
    dispatch(handleCurrentPageChange(1));
  }, []);

  useEffect(() => {
    handleFetchRegistrations(options, 1);
    dispatch(handleCurrentPageChange(1));
  }, [options]);

  useEffect(() => {
    handleFetchRegistrations(options, currentPage);
  }, [currentPage, registrationFlagAccountant]);

  const handlePageNumberClick = (newPageNumber: number) => {
    dispatch(handleCurrentPageChange(newPageNumber));
  };

  return (
    <div className="bg-[#F5F7FA] h-[100dvh] min-h-[1024px]">
      <div className="flex w-screen gap-10 pt-10 mx-auto my-0 body px-14 max-w-screen-2xl">
        <div className="w-56 card">
          <AdminCard avatarUrl={avatarUrl} />
        </div>
        <div className="registration-section flex flex-col gap-8 w-[80%]">
          <div className="filters">
            <AccountantUtilsBar />
          </div>
          <div className="registration-list min-h-[200px]">
            <div className="mb-2 showing-status">{showingMessage}</div>
            <RegistrationList
              ListRegistration={registrationList}
              isLoading={isLoading}
            />
          </div>
          <div className="w-full pagination mt-7">
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
};

export default AccountantHomePage;
