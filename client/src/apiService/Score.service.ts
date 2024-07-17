import instance from "../utils/customizeAxios";

const getAllYearsLeadBoard = async () => {
  try {
    const response = await instance.get("/leaderboards/years");
    return response.data;
  } catch (error) {
    console.error(error);
  }
};
const getDataLeaderBoard = async (year: string) => {
  try {
    const response = await instance.get(`/leaderboards?year=${year}`);
    return response.data;
  } catch (error) {
    console.error(error);
  }
};
export { getDataLeaderBoard, getAllYearsLeadBoard };
