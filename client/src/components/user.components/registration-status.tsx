import { cn } from "../../utils/utils";

type Props = {
    status?: string;
};
export const RegistrationStatus = ({ status = "NONE" }: Props) => {
    return (
        <>
            <div
                className={cn(
                    "py-2 px-4 rounded-lg",
                    status === "DRAFT" && "bg-gray-200 text-gray-600",
                    status === "NONE" && "bg-gray-200 text-gray-600",
                    status === "SUBMITTED" && "bg-orange-200 text-orange-600",
                    status === "DECLINED" && "bg-red-100 text-red-600",
                    status === "APPROVED" && "bg-green-100 text-green-600",
                    status === "DONE" && "bg-green-100 text-green-600",
                    status === "VERIFYING" && "bg-yellow-100 text-yellow-600",
                    status === "DOCUMENT_DECLINED" && "bg-red-100 text-red-600",
                    status === "VERIFIED" && "bg-blue-100 text-blue-400",
                    status === "CLOSED" && "bg-gray-200 text-gray-600",
                    status === "DISCARDED" && "bg-rose-200 text-rose-600"
                )}
            >
                <p className='font-semibold'>
                    {status === "DOCUMENT_DECLINED"
                        ? status.replace("_", " ")
                        : status === "NONE"
                            ? "DRAFT"
                            : status}
                </p>
            </div>
        </>
    );
};
