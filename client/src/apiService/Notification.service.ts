import instance from "../utils/customizeAxios";

export const getAllNotificationsByCurrUser = async () => {
    const res = await instance.get("/notifications");
    return res.data;
};
