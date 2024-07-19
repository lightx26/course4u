import { toast } from "sonner";
import instance from "../utils/customizeAxios";

export const approveRegistration = async (id: number, close: VoidFunction) => {
    try {
        await instance.post(`/registrations/approve?id=${id}`);
        toast.success("Registration approved", {
            style: { color: "green" },
        });
        setTimeout(() => {
            close();
        }, 1000);
    } catch (error) {
        toast.error("Failed to approve registration", {
            style: { color: "red" },
        });
    }
};

export const declineRegistration = async (
    id: number,
    comment: string,
    userId: number,
    close: VoidFunction
) => {
    try {
        await instance.post(`/registrations/decline`, {
            registrationId: id,
            comment,
            userId,
        });
        toast.success("Registration declined", {
            style: { color: "green" },
        });
        setTimeout(() => {
            close();
        }, 1000);
    } catch (error) {
        toast.error("Failed to decline registration", {
            style: { color: "red" },
        });
    }
};
