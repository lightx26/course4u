import { useEffect, useState } from "react";
import { RegistrationType } from "../../App";

import AdminCard from "../../components/admin.components/mainPage.components/AdminCard.tsx";
import PaginationSection from "../../components/user.components/Homepage/PaginationSection.tsx";

import UtilsBar from "../../components/admin.components/mainPage.components/UtilsBar.tsx";
import RegistrationList from "../../components/admin.components/registrations.components/RegistrationList.tsx";

import { fetchAllRegistrations } from "../../apiService/Admin.service.ts";

function AdminHomePage() {
  const [currentPage, setCurrentPage] = useState(1);
  const [ListRegistration, setListRegistration] = useState<RegistrationType[]>(
    []
  );
  const [isLoading, setIsLoading] = useState(true);
  const [totalItems, setTotalItems] = useState(0);

  const handleFetchRegistrations = async (page: number = 1, status:string = "all") => {
    try{
      const data = await fetchAllRegistrations(page, status);
      if (data){
        setListRegistration(data.content);
        setTotalItems(data.totalElements);
      }
    } catch(e){
      console.error(e);
    }
    setIsLoading(false);
  }

  useEffect(() => {
    handleFetchRegistrations(currentPage);
  }, [currentPage]);

  return (
    <div className="bg-[#F5F7FA] h-[100dvh] min-h-[1024px]">
      <div className="body flex gap-10 px-14 pt-10 w-screen max-w-screen-2xl my-0 mx-auto">
        <div className="card w-56 rounded-2xl">
          <AdminCard />
        </div>
        <div className="registration-section flex flex-col gap-10 w-[80%]">
          <div className="filters">
            <UtilsBar />
          </div>
          <div className="registration-list min-h-[200px] mt-6">
            <RegistrationList
              ListRegistration={ListRegistration}
              isLoading={isLoading}
            />
          </div>
          <div className="pagination w-full mt-7">
            <PaginationSection
              totalItems={totalItems}
              itemPerPage={8}
              currentPage={currentPage}
              setCurrentPage={(page: number) => {setCurrentPage(page)}}
            />
          </div>
        </div>
      </div>
    </div>
  );
}

export default AdminHomePage;
