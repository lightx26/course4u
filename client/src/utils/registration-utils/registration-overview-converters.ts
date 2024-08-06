const convertStatus = (status: string): string => {
    return status == "DOCUMENT_DECLINED"
        ? "Declined (Document)"
        : status.charAt(0).toUpperCase() + status.slice(1).toLowerCase();
};

//
// Since JS Date format is in yyyy-mm-dd,
// We need to convert it to mm-dd-yyyy to fit the project
//
const convertJSDatesToCorrectFormat = (date: Date): string => {
    const newDate = date.toString().split("-");
    [newDate[0], newDate[1], newDate[2]] = [
        newDate[1],
        newDate[2],
        newDate[0],
    ];
    return newDate.join("/");
};

const convertPeriod = (
    startDate: Date | undefined,
    endDate: Date | undefined
): string => {
    if (!startDate) {
        return `Not started yet`;
    }

    const handledStartDate = convertJSDatesToCorrectFormat(startDate);
    const handledEndDate = endDate
        ? convertJSDatesToCorrectFormat(endDate)
        : "";

    return `Period: ${ handledStartDate } - ${ handledEndDate || "Not finished yet"}`;
};

export {
    convertStatus,
    convertPeriod,
    convertJSDatesToCorrectFormat
}