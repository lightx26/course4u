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
export const startLearning = async (id: number) => {
    try {
        await instance.post(`/registrations/start-learning/${id}`);
        toast.success("Start learning", {
            style: { color: "green" },
            description: "Start learning successfully",
        });
        return { status: 200 };
    } catch (error) {
        toast.error("Failed to start learning", {
            style: { color: "red" },
            description: "Failed to start learning",
        });
    }
};

export const closeRegistration = async (
    id: number,
    comment: string,
    close: VoidFunction
) => {
    try {
        await instance.post(`/registrations/${id}/close`, {
            comment,
        });
        toast.success("Registration closed", {
            style: { color: "green" },
            description: "Registration closed successfully",
        });
        setTimeout(() => {
            close();
        }, 1000);
    } catch (error) {
        toast.error("Failed to close registration", {
            style: { color: "red" },
            description: "Failed to close registration",
        });
    }
};

export const removeRegistration = async (id: number) => {
    try {
        await instance.delete(`/registrations/${id}`);
        toast.success("Registration removed", {
            style: { color: "green" },
            description: "Registration removed successfully!!!",
        });
        return { status: 200 };
    } catch (error) {
        toast.error("Failed to remove registration", {
            style: { color: "red" },
            description: "Something went wrong, please try again!!!",
        });
    }
};

export const discardRegistration = async (id: number) => {
    try {
        await instance.post(`/registrations/${id}/discard`);
        toast.success("Registration discarded", {
            style: { color: "green" },
            description: "Registration discarded successfully",
        });
        return { status: 200 };
    } catch (error) {
        toast.error("Failed to discard registration", {
            style: { color: "red" },
            description: "Failed to discard registration",
        });
    }
};
