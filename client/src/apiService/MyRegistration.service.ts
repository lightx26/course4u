import instance from "../utils/customizeAxios";

export async function fetchListOfMyRegistration(page: number = 1, status: String = "default") {
    try{
        const response = await instance.get(`/registrations/my-registration?page=${page}&status=${status}`);
        return response;
    } catch (error) {
        throw error;
    }

}
