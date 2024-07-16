import instance from "../utils/customizeAxios";

export const fetchListAvailableCategory = async () => {
    try {
        const response = await instance.get(`/categories/available`);
        return response;
    }
    catch (error) {
        console.error(error);
    }
}