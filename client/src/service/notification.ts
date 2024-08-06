import instance from "../utils/customizeAxios";

export const getAllNotificationsByCurrUser = async (
    batchSize: number,
    timestamp?: Date
) => {
    const params: any = { batchSize };
    if (timestamp) {
        params.timestamp = timestamp.toISOString();
    }

    const res = await instance.get("/notifications", { params });
    return res.data;
};

export const markAllNotificationsAsRead = async () => {
    await instance.put("/notifications");
};
