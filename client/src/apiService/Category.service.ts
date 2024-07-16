import instance from "../utils/customizeAxios";

// Fetch list of available (status = available) categories from Database
export const fetchListAvailableCategory = async () => {
    try {
        const response = await instance.get(`/categories/available`);
        return response;
    }
    catch (error) {
        console.error(error);
    }
}