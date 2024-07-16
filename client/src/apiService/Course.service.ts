import instance from "../utils/customizeAxios";

async function fetchListAvailableCourse(offset: number = 1, limit: number = 8, sortBy: string = "NEWEST", categoryFilters?: number[], levelFilters?: string[], platformFilters?: string[], minRating: number = 0) {
  // eslint-disable-next-line no-useless-catch
  try {
    const request_params = {
      page: offset,
      pageSize: limit,
      sortBy: sortBy,
      filter: {
        categoryFilters,
        levelFilters,
        platformFilters,
        minRating,
      }
    }
    const response = await instance.post(
      `/courses/search`, request_params
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
    console.error(error);
  }
};

const deleteCourseById = async (id: string | undefined) => {
  // eslint-disable-next-line no-useless-catch
  try {
    const response = await instance.delete(`/courses/${id}`);
    return response;
  } catch (error) {
    throw error;
  }
};

export { fetchListAvailableCourse, fetchDataCourseById, deleteCourseById };
