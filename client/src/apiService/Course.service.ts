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
      sortBy: Searcher.sortBy ?? "NEWEST",
      search: Searcher.searchTerm ?? "",
      filter: {
        categoryFilters: Searcher.categoryFilter ?? [],
        levelFilters: Searcher.levelFilter ?? [],
        platformFilters: Searcher.platformFilter ?? [],
        minRating: Searcher.minRating ?? "0",
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

const fetchDataRelatedCourseById = async (id: string | undefined) => {
  try {
    // const response = await instance.get(
    //   `/courses/${id}/relation`
    // );
    // return response;
    console.log("check id", id);
    return [
      {
        id: 3,
        name: "Django for Beginners aaa",
        platform: "Udemy",
        level: "BEGINNER",
        createdDate: "2021-01-01",
        thumbnailUrl:
          "https://img-c.udemycdn.com/course/480x270/822444_a6db.jpg",
        rating: 3.0,
        enrollmentCount: 14,
        status: "AVAILABLE",
      },
      {
        id: 5,
        name: "Data Science and Machine Learning Bootcamp",
        platform: "Udemy",
        level: "BEGINNER",
        createdDate: "2021-01-01",
        thumbnailUrl:
          "https://img-c.udemycdn.com/course/480x270/903744_8eb2.jpg",
        rating: 3.0,
        enrollmentCount: 1,
        status: "AVAILABLE",
      },
      {
        id: 10,
        name: "abchdnd",
        platform: "LinkedIn",
        level: "BEGINNER",
        createdDate: "2013-09-02",
        thumbnailUrl:
          "https://media.thethaovanhoa.vn/Upload/QDN4HPIpMrJuoPNyIvLDA/files/2022/07/Jin-BTS-VIP2.jpg",
        rating: 0.0,
        enrollmentCount: 1,
        status: "AVAILABLE",
      },
      {
        id: 8,
        name: "Vue JS - The Complete Guide",
        platform: "Udemy",
        level: "INTERMEDIATE",
        createdDate: "2021-01-01",
        thumbnailUrl:
          "https://img-c.udemycdn.com/course/480x270/995016_ebf4.jpg",
        rating: 0.0,
        enrollmentCount: 0,
        status: "AVAILABLE",
      },
      {
        id: 9,
        name: "Python for Data Science and Machine Learning",
        platform: "Udemy",
        level: "BEGINNER",
        createdDate: "2021-01-01",
        thumbnailUrl:
          "https://img-c.udemycdn.com/course/480x270/903744_8eb2.jpg",
        rating: 0.0,
        enrollmentCount: 0,
        status: "AVAILABLE",
      },
      {
        id: 13,
        name: "Spring Boot",
        platform: "Udemy",
        level: "ADVANCED",
        createdDate: "2021-01-01",
        thumbnailUrl:
          "https://img-b.udemycdn.com/course/480x270/543600_64d1_4.jpg",
        rating: 0.0,
        enrollmentCount: 0,
        status: "AVAILABLE",
      },
      {
        id: 6,
        name: "JavaScript - The Complete Guide",
        platform: "Udemy",
        level: "INTERMEDIATE",
        createdDate: "2021-01-01",
        thumbnailUrl:
          "https://img-c.udemycdn.com/course/480x270/1501104_967d_9.jpg",
        rating: 0.0,
        enrollmentCount: 0,
        status: "AVAILABLE",
      },
      {
        id: 7,
        name: "Complete C# Masterclass",
        platform: "Udemy",
        level: "ADVANCED",
        createdDate: "2021-01-01",
        thumbnailUrl:
          "https://img-c.udemycdn.com/course/480x270/382002_5d4d.jpg",
        rating: 0.0,
        enrollmentCount: 1,
        status: "AVAILABLE",
      },
    ];
  } catch (error) {
    console.error(error);
  }
};

export {
  fetchListAvailableCourse,
  fetchDataCourseById,
  deleteCourseById,
  fetchDataRatingsCourseById,
  fetchDataReviewsCourseById,
  fetchDataRelatedCourseById, createNewCourse
};
