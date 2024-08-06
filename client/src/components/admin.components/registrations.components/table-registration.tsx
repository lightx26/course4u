import {RegistrationType} from "../../../App";
import TableHeaderRegistration from "./table-header-registration";
import TableRowRegistration from "./table-row-registration";
import {Spin} from 'antd';

type PropsType = {
    ListRegistration: RegistrationType[];
    isLoading: boolean;
};

const TableHeader = [
    "Course",
    "User",
    "Platform",
    "Period",
    "Status",
];

export default function TableRegistration({ListRegistration, isLoading}: PropsType) {
    if (isLoading) return (
        <div className="w-full h-full flex items-center">
            <Spin className="mx-auto" size={"large"}/>
        </div>
    )

    return (
        <div>
            <TableHeaderRegistration tableHeader={TableHeader}/>
            <div className="flex flex-col items-stretch gap-3 mt-3">
                {ListRegistration && ListRegistration.length > 0 ? (
                    ListRegistration.map((registration) => (
                        <TableRowRegistration key={registration.id} registration={registration}/>
                    ))
                ) : (
                    <div className="text-center">No registration found</div>
                )}
            </div>
        </div>
    )
}
