import instance from "../utils/customizeAxios";

const getListDocumentByRegistrationId = async (id: number | undefined) => {
  try {
    const response = await instance.get(`/documents/registration/${id}`);
    return response;
  } catch (error) {
    console.log("Error while fetching documents");
  }
};

export { getListDocumentByRegistrationId };
