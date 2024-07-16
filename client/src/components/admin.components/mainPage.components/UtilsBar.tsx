import { useState } from "react";
import SearchGlass from "../../../assets/images/admin.images/SearchGlass.svg";
import Select from "../Select";

const UtilsBar = () => {
  const [searchContent, setSearchContent] = useState("");
  const [sortStatusId, setsortStatusId] = useState("1");
  const [sortOrder, setSortOrder] = useState("1");

  return (
    <div className="h-[10%] utils-bar flex gap-7">
      <div className="search-bar w-[70%]">
        <span className="text-[11px] font-normal">Search: </span>
        <div
          className="bg-white flex px-2 py-0.5"
          style={{
            alignItems: "center",
            border: "1px solid rgba(233, 234, 240, 1)",
          }}
        >
          <img
            className="max-w-[20px] max-h-[20px] fill-border border-solid border-[rgba(233, 234, 240, 1)]"
            src={SearchGlass}
            alt="search"
          />
          <input
            className="w-full h-[70%] ml-2 py-2 placeholder-opacity-10 focus:outline-none"
            placeholder="Search registration by course name or learner"
            value={searchContent}
            onChange={(e) => {
              setSearchContent(e.target.value);
            }}
            style={{ fontSize: "12px" }}
          ></input>
        </div>
      </div>
      <div className="status_select w-[15%]">
        <span className="text-[11px] font-normal">Status: </span>
        <Select
          listOption={[
            {
              value: "1",
              content: "Submitted",
            },
            {
              value: "2",
              content: "Verifying",
            },
            {
              value: "3",
              content: "Declined",
            },
            {
              value: "4",
              content: "Declined (document)",
            },
          ]}
          value={sortStatusId}
          onSortByChange={(e) => {
            setsortStatusId(e.target.value);
          }}
        />
      </div>
      <div className="sortby_select w-[15%]">
        <span className="text-[11px] font-normal">Sort by: </span>
        <Select
          listOption={[
            {
              value: "1",
              content: "Created Date",
            },
            {
              value: "2",
              content: "Last modified",
            }
          ]}
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
