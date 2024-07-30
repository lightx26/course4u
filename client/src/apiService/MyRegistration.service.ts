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
    close: VoidFunction
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
    close: VoidFunction,
    setFlag: VoidFunction
) {
    try {
        id
            ? await instance.put(`/registrations/${id}/draft`, data)
            : await instance.postForm("/registrations/draft", data);
        toast.success("Registration saved as draft", {
            style: { color: "green" },
            description: "Registration saved as draft successfully",
        });
        close();
        setFlag();
    } catch (err: any) {
        toast.error("Save registration as draft unsuccessfully", {
            style: { color: "red" },
            description: err.response.data.message,
        });
    }
}
