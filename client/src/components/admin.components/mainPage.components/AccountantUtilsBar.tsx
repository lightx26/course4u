import SearchGlass from "../../../assets/images/admin.images/SearchGlass.svg";
import { useState } from "react";
import { Select } from "antd";

import registrationStatusList from "../../../utils/registrationStatusList";
import registrationOrderByList from "../../../utils/orderByList.ts";

import { handleOptionsChange } from "../../../redux/slice/accountantRegistration.slice.ts";
import { useDispatch, useSelector } from "react-redux";
import {RootState} from "../../../redux/store/store.ts";
import { RegistrationParamsType } from "../../../redux/slice/accountantRegistration.slice.ts";

import { registrationStatusListForAccountant } from "../../../utils/registrationStatusList";

const AccountantUtilsBar = () => {
    const dispatch = useDispatch();

    const options: RegistrationParamsType = useSelector(
        (state: RootState) => state.accountantRegistration.options
    );

    const status: string = options.status;
    const [searchContent, setSearchContent] = useState(options.search);
    const sortOrder: string = options.orderBy.toLowerCase() == "id"
        ? "Created Date" : options.orderBy;

    const statusList = registrationStatusListForAccountant.map(status => ({
        label: status.content,
        value: status.value
    }));

    const orderByList = registrationOrderByList.map(orderBy => ({
        label: orderBy.content,
        value: orderBy.value
    }));


    const statusValueMapping: string[] = registrationStatusList.map((status) => status.content);
    const orderByValueMapping: string[] = registrationOrderByList.map(orderBy => orderBy.content);

    const handleStatusChange = (newStatusId: string): void => {
        const newOptions: RegistrationParamsType = {...options, status: statusValueMapping[parseInt(newStatusId)]};
        dispatch(handleOptionsChange(newOptions));
    }

    const handleOrderChange = (newOrderById: string): void => {
        const newOptions: RegistrationParamsType = {...options, orderBy: orderByValueMapping[parseInt(newOrderById)]};
        dispatch(handleOptionsChange(newOptions));
    }

    const handleSearchConfirm = (newSearchContent: string): void => {
        const newOptions: RegistrationParamsType = {...options, search: newSearchContent};
        dispatch(handleOptionsChange(newOptions));
    }

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
                        onBlur={() => handleSearchConfirm(searchContent)}
                        onKeyDown={(e: React.KeyboardEvent) => {
                            if (e.key == 'Enter') {
                                handleSearchConfirm(searchContent)
                            }
                        }}
                    ></input>
                </div>
            </div>
            <div className="status_select w-[15%]">
                <span className="text-[11px] font-normal">Status: </span>
                <Select
                    value={status}
                    onChange={handleStatusChange}
                    options={statusList}
                    className="w-full min-h-[42px]"
                />
            </div>
            <div className="sortby_select w-[15%]">
                <span className="text-[11px] font-normal">Sort by: </span>
                <Select
                    value={ sortOrder }
                    options={orderByList}
                    onChange={handleOrderChange}
                    className="w-full min-h-[42px]"
                />
            </div>
        </div>
    );
};

export default AccountantUtilsBar;
