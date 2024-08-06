import { cn } from "../../utils/utils";

type Props = {
    status?: string;
};
export const RegistrationStatus = ({ status = "NONE" }: Props) => {
    return (
        <>
            <div className={cn("py-2 px-4 rounded-lg", status.toLowerCase())}>
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
