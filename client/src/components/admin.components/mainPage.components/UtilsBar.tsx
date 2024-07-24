import { useState } from "react";
import SearchGlass from "../../../assets/images/admin.images/SearchGlass.svg";
import Select from "../Select";

import registrationStatusList from "../../../utils/registrationStatusList";
import orderByList from "../../../utils/orderByList.ts";

import { registrationStatusListForAccountant } from "../../../utils/registrationStatusList";
import { useSelector } from "react-redux";
import { RootState } from "../../../redux/store/store.ts";

const UtilsBar = () => {
  const userRole = useSelector((state: RootState) => state.user.user.role);
  const [searchContent, setSearchContent] = useState("");
  const [sortStatusId, setSortStatusId] = useState(
    `${userRole === "ACCOUNTANT" ? "7" : "0"}`
  );
  const [sortOrder, setSortOrder] = useState("0");

  return (
    <div className="h-[10%] utils-bar flex gap-7">
      <div className="search-bar w-[70%]">
        <span className="text-[11px] font-normal">Search: </span>
        <div
          className="bg-white flex px-2 py-0.5 items-center"
          style={{
            border: "1px solid rgba(233, 234, 240, 1)",
          }}
        >
          <img
            className="max-w-[20px] max-h-[20px] fill-border border-solid border-[rgba(233, 234, 240, 1)]"
            src={SearchGlass}
            alt="search"
          />
          <input
            className="w-full h-[70%] ml-2 py-2 placeholder-opacity-5 focus:outline-none"
            placeholder="Search registration by course name or learner"
            value={searchContent}
            onChange={(e) => {
              setSearchContent(e.target.value);
            }}
          ></input>
        </div>
      </div>
      <div className="status_select w-[15%]">
        <span className="text-[11px] font-normal">Status: </span>
        <Select
          listOption={
            userRole === "ACCOUNTANT"
              ? registrationStatusListForAccountant
              : registrationStatusList
          }
          value={sortStatusId}
          onSortByChange={(e) => {
            setSortStatusId(e.target.value);
          }}
        />
      </div>
      <div className="sortby_select w-[15%]">
        <span className="text-[11px] font-normal">Sort by: </span>
        <Select
          listOption={orderByList}
          value={sortOrder}
          onSortByChange={(e) => {
            setSortOrder(e.target.value);
          }}
        />
      </div>
    </div>
  );
};

export default UtilsBar;
