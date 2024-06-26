import instance from "../utils/customizeAxios";

export async function fetchListOfMyRegistration(page: number = 1, status: String = "Submited") {
    try{
        const response = await instance.get(`/registrations/my-registration?page=${page}&status=${status}`);
        return response;
    } catch (error) {
        throw error;
    }

}
export async function createNewRegistration(data: any): Promise<number> {
    try {
      const res = await instance.postForm("/registrations", data);
      return res.status;
    } catch (error: any) {
      if (error.response) {
        return error.response.status;
      } else {
        return 503;
      }
    }
  };