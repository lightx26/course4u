export function getTimeDifference(createdTime: string): string {
    const now = new Date();
    const createdDate = new Date(createdTime);
    const diffInMilliseconds = now.getTime() - createdDate.getTime();
    if (diffInMilliseconds <= 0) {
        return "The specified time is in the past";
    }

    let remaining = diffInMilliseconds;

    const hours = Math.floor(remaining / (1000 * 60 * 60));
    remaining %= 1000 * 60 * 60;

    const minutes = Math.floor(remaining / (1000 * 60));
    remaining %= 1000 * 60;

    const seconds = Math.floor(remaining / 1000);

    let result = "";
    if (hours > 0) result += `${hours}h `;
    if (minutes > 0) result += `${minutes}m `;
    if (seconds > 0) result += `${seconds}s`;

    return result.trim();
}


export function timeAgo(inputTime: string): string {
    const localTimeNow = new Date();

    const utcTime = new Date(inputTime);
    const localTimeFromUTC = new Date(utcTime.getTime() - utcTime.getTimezoneOffset() * 60000);

    const timeDiff = localTimeNow.getTime() - localTimeFromUTC.getTime();

    const seconds = Math.floor(timeDiff / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);
    const days = Math.floor(hours / 24);
    const weeks = Math.floor(days / 7);
    const months = Math.max(Math.floor(days / 30.44), 1);
    const years = Math.max(Math.floor(days / 365.25), 1);

    if (seconds < 60) {
        return "Just now";
    } else if (minutes < 60) {
        return minutes === 1 ? `${minutes} min ago` : `${minutes} mins ago`;
    } else if (hours < 24) {
        return hours === 1 ? `${hours} hour ago` : `${hours} hours ago`;
    } else if (days < 7) {
        return days === 1 ? `${days} day ago` : `${days} days ago`;
    } else if (weeks < 4) {
        return weeks === 1 ? `${weeks} week ago` : `${weeks} weeks ago`;
    } else if (months < 12) {
        return months === 1 ? `${months} month ago` : `${months} months ago`;
    } else {
        return years === 1 ? `${years} year ago` : `${years} years ago`;
    }
}
export const formatDate = (date: string) => {
    const newDate = new Date(date);
    const hours = String(newDate.getHours()).padStart(2, "0");
    const minutes = String(newDate.getMinutes()).padStart(2, "0");
    const day = String(newDate.getDate()).padStart(2, "0");
    const month = String(newDate.getMonth() + 1).padStart(2, "0");
    const year = newDate.getFullYear();
    return `${hours}:${minutes} ${month}/${day}/${year}`;
};
