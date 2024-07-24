export const isStatusSuccesful = (status: number) => {
    const statusText = `${status}`;
    if (statusText.startsWith("2")) {
        return true;
    }
    return false;
};
