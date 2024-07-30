import instance from "../utils/customizeAxios";

export type SearchParams = {
  searchTerm?: string;
  categoryFilter?: string[];
  levelFilter?: string[];
  platformFilter?: string[];
  rating?: string[];
  limit?: number;
  sortBy?: string;
  page?: number;
};

async function fetchListAvailableCourse(Searcher: SearchParams) {
  try {
    const request_params = {
      page: Searcher.page ?? 1,
      pageSize: Searcher.limit ?? 8,
      sortBy: Searcher.sortBy ?? "NEWEST",
      search: Searcher.searchTerm ?? "",
      filter: {
        categoryFilters: Searcher.categoryFilter ?? [],
        levelFilters: Searcher.levelFilter ?? [],
        platformFilters: Searcher.platformFilter ?? [],
        ratingFilters: Searcher.rating ?? [],
      },
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
const createNewCourse = async (data: any): Promise<number> => {
  try {
    const res = await instance.postForm("/courses", data);
    return res.status;
  } catch (error: any) {
    if (error.response) {
      return error.response.status;
    } else {
      return 503;
    }
  }
};

const fetchDataRatingsCourseById = async (id: string | undefined) => {
  try {
    const response = await instance.get(`/courses/${id}/ratings`);
    return response;
  } catch (error) {
    console.error(error);
  }
};

const fetchDataReviewsCourseById = async (
  id: string | undefined,
  page: number,
  size: number = 3,
  starFilter: number
) => {
  try {
    const response = await instance.get(
      `/courses/${id}/reviews?page=${page}&size=${size}&starFilter=${starFilter}`
    );
    return response;
  } catch (error) {
    console.error(error);
  }
};

//@ts-ignore
const fetchDataRelatedCourseById = async (id: string | undefined) => {
  try {
    const response = await instance.get(`/courses/${id}/relation`);
    return response;
  } catch (error) {
    console.error(error);
  }
};

const editCourse = async (data: any): Promise<number> => {
  try {
    const res = await instance.patchForm("/courses", data);
    return res.status;
  } catch (error: any) {
    if (error.response) {
      return error.response.status;
    } else {
      return 503;
    }
  }
};
export {
  fetchListAvailableCourse,
  fetchDataCourseById,
  deleteCourseById,
  fetchDataRatingsCourseById,
  fetchDataReviewsCourseById,
  fetchDataRelatedCourseById,
  createNewCourse,
  editCourse,
};
