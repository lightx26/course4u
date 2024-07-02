import instance from "../utils/customizeAxios";

export async function fetchListAvailableCourse(offset : number = 0, limit : number = 8) {
    // eslint-disable-next-line no-useless-catch
    try{
        const response = await instance.get(`/courses/available?page=${offset}&pageSize=${limit}`);
        return response;
    } catch (error) {
        throw error;
    }
}
