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
