import instance from "../utils/customizeAxios";

export const sendReview = async (rating: number, comment: string, courseId: string) => {
    const params = {
        rating,
        comment,
        courseId
    }
    const result = await instance.post(`/course-reviews`, params);
    return result;
}

export const checkExistReview = async (courseId: string) => {
    const result = await instance.post(`/courses/${courseId}/checkReviewed`);
    return result;
}