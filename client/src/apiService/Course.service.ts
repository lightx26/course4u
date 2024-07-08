import instance from "../utils/customizeAxios";

async function fetchListAvailableCourse(offset: number = 0, limit: number = 8) {
  // eslint-disable-next-line no-useless-catch
  try {
    const response = await instance.get(
      `/courses/available?page=${offset}&pageSize=${limit}`
    );
    return response;
  } catch (error) {
    throw error;
  }
}
const fetchDataCourseById = async (id: string | null) => {
  try {
    const response = await instance.get(`/courses/${id}`);
    return response;
  } catch (error) {
    throw new Error(error.message);
  }
};

export { fetchListAvailableCourse, fetchDataCourseById };
