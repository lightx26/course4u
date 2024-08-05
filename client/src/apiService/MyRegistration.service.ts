import { toast } from "sonner";
import instance from "../utils/customizeAxios";
import { isStatusSuccesful } from "../utils/checkResStatus";
import { Status } from "../utils/index";

export async function fetchListOfMyRegistration(
    page: number = 1,
    status: string = "Submited"
) {
    try {
        const response = await instance.get(
            `/registrations/my-registration?page=${page}&status=${status}`
        );
        return response;
    } catch (error) {
        toast.error("_", {
            style: { color: "red" },
        });
    }
}

export async function createNewRegistration(
    data: FormData,
    close: VoidFunction,
    setFlag: VoidFunction
) {
    try {
        const res = await instance.postForm("/registrations", data);
        if (isStatusSuccesful(res.status)) {
            toast.success("Create a new registration successfully", {
                description: "",
                style: {
                    color: "green",
                    fontWeight: "bold",
                },
            });
            setFlag();
            close();
            return;
        }
    } catch (err) {
        toast.error("Create a new registration unsuccessfully", {
            description:
                "Your course request already exists in the system. Please check again!",
            style: {
                color: "red",
                fontWeight: "bold",
            },
        });
    }
}

export async function editRegistration(
    id: number,
    data: FormData,
    status: Status,
    close: VoidFunction,
    setFlag: VoidFunction
) {
    try {
        const res = await instance.put(`/registrations/${id}/edit`, data);
        if (status === Status.DRAFT && res.status === 201) {
            setFlag();
            close();
            toast.success("Submit registration successfully", {
                description: "",
                style: {
                    color: "green",
                    fontWeight: "bold",
                    textAlign: "center",
                },
            });
        } else {
            setFlag();
            close();
            toast.success("Re-submit registration successfully", {
                description: "",
                style: {
                    color: "green",
                    fontWeight: "bold",
                    textAlign: "center",
                },
            });
        }
    } catch (error: any) {
        if (error.response) return error.response.status;
        else return 503;
    }
}

export async function saveRegistrationAsDraft(
    data: FormData,
    id: number,
    closeModal: VoidFunction
) {
    const res = id
        ? await instance.put(`/registrations/${id}/draft`, data)
        : await instance.postForm("/registrations/draft", data);
    if (isStatusSuccesful(res.status)) {
        toast.success("Registration saved as draft", {
            style: { color: "green" },
            description: "Registration saved as draft successfully",
        });
        closeModal();
        return res.status;
    }
    if (res.status === 409) {
        toast.error("Save registration as draft unsuccessfully", {
            style: { color: "red" },
            description: "Course4U already have this course",
        });
        return res.status;
    }
    toast.error("Save registration as draft unsuccessfully", {
        style: { color: "red" },
        description: "You need to provide at least 1 field to save as draft!!",
    });
}

export async function saveDraftWithCourse(
    data: FormData,
    courseId: number,
    closeModal: VoidFunction
) {
    const res = await instance.post(`/registrations/${courseId}/draft-enroll`, {
        duration: data.get("duration"),
        durationUnit: data.get("durationUnit"),
    });
    if (isStatusSuccesful(res.status)) {
        toast.success("Registration saved as draft", {
            style: { color: "green" },
            description: "Registration saved as draft successfully",
        });
        closeModal();
        return res.status;
    }
    toast.error("Save registration as draft unsuccessfully", {
        style: { color: "red" },
        description: "You need to provide at least 1 field to save as draft!!",
    });
}
