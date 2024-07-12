import { useEffect, useState } from "react";
import RegistrationPaginationSection from "./RegistrationPaginationSection.tsx";
import SatusFilter from "../StatusFilter.tsx";
import { fetchListOfMyRegistration } from "../../../apiService/MyRegistration.service.ts";
import ListMyRegistrationCardComponent from "./ListMyRegistrationCardComponent.tsx";
import { Status, statusList } from "../../../utils/index.ts";


export type OverviewMyRegistrationType = {
  id?: string;
  courseName: string;
  status: Status;
  platform: string;
  startDate: Date;
  endDate: Date;
  thumbnailUrl: string;
};

export default function MyRegistrationPage() {
  const [totalItem, setTotalItem] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [listMyRegistration, setListMyRegistration] = useState<
    OverviewMyRegistrationType[]
  >([]);
  const [filterBy, setFilter] = useState("SUBMITTED");
  const [isLoading, setIsLoading] = useState(false);

  const fetchData = async (page: number = 1, status: string = "SUBMITTED") => {
    setIsLoading(true);
    const result = await fetchListOfMyRegistration(page, status);
    if (result && result.data && result.data) {
      setListMyRegistration(result.data.list);
      setTotalItem(result.data.totalElements);
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchData(currentPage, filterBy);
  }, [currentPage, filterBy]);

  const onPageNumberClick = (newPageNumber: number) => {
    setCurrentPage(newPageNumber);
  };

  const onFilterByChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setFilter(e.target.value);
    setCurrentPage(1);
  };

  return (
    <div className="flex flex-col gap-5 w-[80%] p-3 grow">
      <div className="flex items-center justify-between">
        {listMyRegistration === null || listMyRegistration.length === 0 ? (
          <div>Showing 0 results</div>
        ) : (
          <div>
            Showing {(currentPage - 1) * 10 + 1} -{" "}
            {Math.min(currentPage * 10, totalItem)} of {totalItem} results
          </div>
        )}
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
        <RegistrationPaginationSection
          totalItems={totalItem}
          currentPage={currentPage}
          itemPerPage={10}
          setCurrentPage={onPageNumberClick}
        />
      )}
    </div>
  );
}
