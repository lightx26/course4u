import { toast } from "sonner";
import instance from "../utils/customizeAxios";
import { isStatusSuccesful } from "../utils/checkResStatus";

import type { UploadFile } from "antd";
export const approveRegistration = async (id: number, close: VoidFunction) => {
    const res = await instance.post(`/registrations/${id}/approve`);
    if (isStatusSuccesful(res.status)) {
        toast.success("Registration approved", {
            style: { color: "green" },
            description: "Registration approved successfully",
        });
        close();
        return res.status;
    }

    toast.error("Failed to approve registration", {
        style: { color: "red" },
        description: "Failed to approve registration",
    });
};

export const declineRegistration = async (
    id: number,
    comment: string,
    close: VoidFunction
) => {
    const res = await instance.post(`/registrations/${id}/decline`, {
        comment,
    });
    if (isStatusSuccesful(res.status)) {
        toast.success("Registration declined", {
            style: { color: "green" },
            description: "Registration declined successfully",
        });
        close();
        return res.status;
    }

    toast.error("Failed to decline registration", {
        style: { color: "red" },
        description: "Failed to decline registration",
    });
};
export const startLearning = async (id: number) => {
    const res = await instance.post(`/registrations/start-learning/${id}`);
    if (isStatusSuccesful(res.status)) {
        toast.success("Start learning", {
            style: { color: "green" },
            description: "Start learning successfully",
        });
        return res.status;
    }

    toast.error("Failed to start learning", {
        style: { color: "red" },
        description: "Failed to start learning",
    });
};

export const closeRegistration = async (
    id: number,
    comment: string,
    close: VoidFunction
) => {
    const res = await instance.post(`/registrations/${id}/close`, {
        comment,
    });
    if (isStatusSuccesful(res.status)) {
        toast.success("Registration closed", {
            style: { color: "green" },
            description: "Registration closed successfully",
        });
        close();
        return res.status;
    }

    toast.error("Failed to close registration", {
        style: { color: "red" },
        description: "Failed to close registration",
    });
};

export const removeRegistration = async (id: number) => {
    const res = await instance.delete(`/registrations/${id}`);
    if (isStatusSuccesful(res.status)) {
        toast.success("Registration removed", {
            style: { color: "green" },
            description: "Registration removed successfully!!!",
        });
        return res.status;
    }

    toast.error("Failed to remove registration", {
        style: { color: "red" },
        description: "Something went wrong, please try again!!!",
    });
};

export const submitDocument = async (
    listFileCertificate: UploadFile[],
    listFilePayment: UploadFile[],
    id: string | undefined
) => {
    try {
        const formData = new FormData();
        // Append payment files
        listFilePayment.forEach((file) => {
            if (file.originFileObj) {
                formData.append("payment", file.originFileObj);
            }
        });

        // Append certificate files
        listFileCertificate.forEach((file) => {
            if (file.originFileObj) {
                formData.append("certificate", file.originFileObj);
            }
        });

        const response = await instance.post(
            `/documents/registrations/${id}`,
            formData
        );

        return response;
    } catch (error) {
        toast.error("Document Submission Failed", {
            style: { color: "red" },
            description:
                "There was an error submitting your documents. Please try again later.",
        });
    }
};

export const finishLearning = async (id: string | number) => {
        const response = await instance.put(`/registrations/${id}/finish-learning`);
        response.status == 200 ? toast.success("Finish learning", {
            style: { color: "green" },
            description: "Congratulations! You have successfully completed the course.",
        }) : toast.error("Failed to finish learning", {
            style: { color: "red" },
            description: "Opps.., some thing went wrong. Please, reload the page and try again",
        });
        return response
}

export const discardRegistration = async (id: number) => {
    const res = await instance.post(`/registrations/${id}/discard`);
    if (isStatusSuccesful(res.status)) {
        toast.success("Registration discarded", {
            style: { color: "green" },
            description: "Registration discarded successfully",
        });
        return res.status;
    }

    toast.error("Failed to discard registration", {
        style: { color: "red" },
        description: "Failed to discard registration",
    });
};
