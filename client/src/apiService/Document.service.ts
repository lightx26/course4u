import { toast } from "sonner";
import instance from "../utils/customizeAxios";

const getListDocumentByRegistrationId = async (id: number | undefined) => {
  try {
    const response = await instance.get(`/documents/registration/${id}`);
    return response;
  } catch (error) {
    toast.error("Error while fetching documents", {
      style: {
        color: "red"
      },
      description: "Error while fetching documents, please try again"
    });
  }
};

export { getListDocumentByRegistrationId };
