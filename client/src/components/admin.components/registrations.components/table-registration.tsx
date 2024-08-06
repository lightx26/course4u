/* eslint-disable @typescript-eslint/no-unused-vars */
import { RegistrationType } from "../../../App";
import TableHeaderRegistration from "./table-header-registration";
import TableRowRegistration from "./table-row-registration";

type PropsType = {
    ListRegistration: RegistrationType[];
};

const TableHeader = [
    "Course",
    "User",
    "Platform",
    "Period",
    "Status",
];

export default function TableRegistration({ ListRegistration }: PropsType) {
    return (
        <div>
            <TableHeaderRegistration tableHeader={TableHeader} />
            <div className="flex flex-col items-stretch gap-3 mt-3">
                {ListRegistration && ListRegistration.length > 0 ? (
                    ListRegistration.map((registration) => (
                        <TableRowRegistration key={registration.id} registration={registration} />
                    ))
                ) : (
                    <div className="text-center">No registration found</div>
                )}
            </div>
        </div>
    )
}
