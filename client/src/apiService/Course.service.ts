import instance from "../utils/customizeAxios";

export type SearchParams = {
  searchTerm?: string;
  categoryFilter?: string[];
  levelFilter?: string[];
  platformFilter?: string[];
  minRating?: string;
  limit?: number;
  sortBy?: string;
  page?: number;
};

async function fetchListAvailableCourse(Searcher: SearchParams) {
  try {
    const request_params = {
      page: Searcher.page ?? 1,
      pageSize: Searcher.limit ?? 8,
      sortBy: Searcher.sortBy ?? 'NEWEST',
      search: Searcher.searchTerm ?? '',
      filter: {
        categoryFilters: Searcher.categoryFilter ?? [],
        levelFilters: Searcher.levelFilter ?? [],
        platformFilters: Searcher.platformFilter ?? [],
        minRating: Searcher.minRating ?? '0',
      }
    };
    const response = await instance.post(`/courses/search`, request_params);
    return response;
  } catch (error) {
    console.error(error);
  }
}

const fetchDataCourseById = async (id: string | undefined) => {
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
