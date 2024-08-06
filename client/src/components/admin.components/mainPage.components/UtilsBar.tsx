import SearchGlass from "../../../assets/images/admin.images/SearchGlass.svg";
import {useState} from "react";
import {Select} from "antd";

import {statusProps} from "../../../utils/registrationStatusList.ts";
import registrationOrderByList from "../../../utils/orderByList.ts";

import {useDispatch} from "react-redux";
import {handleOptionsChangeForAdmin, RegistrationParamsType} from "../../../redux/slice/admin-registration.slice.ts";
import {handleOptionsChangeForAccountant} from "../../../redux/slice/accountant-registration.slice.ts";

type PropType = {
    statusList: statusProps[],
    options: RegistrationParamsType,
    role: string,
}

const UtilsBar = ({statusList, options, role}: PropType) => {
    const dispatch = useDispatch();

    const [searched, setSearched] = useState(false);
    const [searchContent, setSearchContent] = useState(options.search);
    const status: string = options.status;
    const sortOrder: string = options.orderBy.toLowerCase() == "id"
        ? "Created Date" : options.orderBy;

    const convertedStatusList = statusList.map(status => ({
        label: status.content,
        value: status.value
    }))

    const orderByList = registrationOrderByList.map(orderBy => ({
        label: orderBy.content,
        value: orderBy.value
    }));


    const statusValueMapping: string[] = statusList.map((status) => status.content);
    const orderByValueMapping: string[] = registrationOrderByList.map(orderBy => orderBy.content);

    const handleOptionsDispatch = (newOptions: RegistrationParamsType): void => {
        if(role === "admin"){
            dispatch(handleOptionsChangeForAdmin(newOptions));
        }
        else{
            dispatch(handleOptionsChangeForAccountant(newOptions));
        }
    }

    const handleStatusChange = (newStatusId: string): void => {
        const newStatus: string = statusValueMapping[parseInt(newStatusId)]
        if (newStatus != options.status) {
            const newOptions: RegistrationParamsType = {...options, status: newStatus};
            handleOptionsDispatch(newOptions);
        }
    }

    const handleOrderChange = (newOrderById: string): void => {
        const newOrderBy: string = orderByValueMapping[parseInt(newOrderById)]
        if (newOrderBy != options.orderBy) {
            const newOptions: RegistrationParamsType = {...options, orderBy: newOrderBy};
            handleOptionsDispatch(newOptions);
        }
    }


    const handleSearchConfirm = (searchContent: string): void => {
        const newOptions: RegistrationParamsType = {...options, search: searchContent};
        if (searchContent.trim().length != 0) {
            setSearched(true);
            if (searchContent != options.search) {
                handleOptionsDispatch(newOptions);
            }
        } else {
            if (searched) {
                setSearched(false);
                handleOptionsDispatch(newOptions);
            }
        }
    }

    const inputElement: HTMLInputElement | null = document.querySelector('.search_input');
    const handleInputKeyUpdate = (e: React.KeyboardEvent) => {
        if (e.key == 'Enter' || e.key == 'Escape') {
            handleSearchConfirm(searchContent);
            inputElement?.blur();
        }
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
                        className="search_input w-full h-[70%] ml-2 py-2 placeholder-opacity-5 focus:outline-none"
                        placeholder="Search registration by course name or learner"
                        value={searchContent}
                        onChange={(e) => {
                            setSearchContent(e.target.value);
                        }}
                        onBlur={() => handleSearchConfirm(searchContent)}
                        onKeyDown={handleInputKeyUpdate}
                    ></input>
                </div>
            </div>
            <div className="status_select w-[15%]">
                <span className="text-[11px] font-normal">Status: </span>
                <Select
                    value={status}
                    onChange={handleStatusChange}
                    options={convertedStatusList}
                    className="w-full min-h-[42px]"
                />
            </div>
            <div className="sortby_select w-[15%]">
                <span className="text-[11px] font-normal">Sort by: </span>
                <Select
                    value={sortOrder}
                    options={orderByList}
                    onChange={handleOrderChange}
                    className="w-full min-h-[42px]"
                />
            </div>
        </div>
    );
};

export default UtilsBar;
