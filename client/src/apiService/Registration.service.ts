import { toast } from "sonner";
import instance from "../utils/customizeAxios";

export const approveRegistration = async (id: number, close: VoidFunction) => {
    try {
        await instance.post(`/registrations/${id}/approve`);
        toast.success("Registration approved", {
            style: { color: "green" },
            description: "Registration approved successfully",
        });
        setTimeout(() => {
            close();
        }, 1000);
    } catch (error) {
        toast.error("Failed to approve registration", {
            style: { color: "red" },
            description: "Failed to approve registration",
        });
    }
};

export const declineRegistration = async (
    id: number,
    comment: string,
    close: VoidFunction
) => {
    try {
        await instance.post(`/registrations/${id}/decline`, {
            comment,
        });
        toast.success("Registration declined", {
            style: { color: "green" },
            description: "Registration declined successfully",
        });
        setTimeout(() => {
            close();
        }, 1000);
    } catch (error) {
        toast.error("Failed to decline registration", {
            style: { color: "red" },
            description: "Failed to decline registration",
        });
    }
};

export const deleteRegistration = async (id: number) => {
    try {
        await instance.delete(`/registrations/${id}`);
        toast.success("Registration deleted", {
            style: { color: "green" },
            description: "Registration deleted successfully",
        });
    } catch (error) {
        toast.error("Failed to delete registration", {
            style: { color: "red" },
            description: "Failed to delete registration",
        });
    }
}