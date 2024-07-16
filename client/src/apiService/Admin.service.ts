import instance from "../utils/customizeAxios";

async function fetchAllRegistrations(page: number, status: string) {
  try {
    const response = await instance.get(
      `/admin/registrations?page=${page}&status=${status}`
    );
    return response.data;
  } catch (error) {
    throw new Error("Error while fetching registrations");
  }
}

export { fetchAllRegistrations };
