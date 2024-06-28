import { FilterItemType } from "../components/user.components/Homepage/MainContent";


// Hàm giả mạo fetchCoursePlatforms
export const fetchCoursePlatforms = (): Promise<FilterItemType[]> => {
    const mockCoursePlatforms: FilterItemType[] = [
        { id: 'plt1', name: 'Udemy', countNumber: 1200 },
        { id: 'plt2', name: 'Coursera', countNumber: 800 },
        { id: 'plt3', name: 'edX', countNumber: 600 },
        { id: 'plt4', name: 'Udacity', countNumber: 400 },
        { id: 'plt5', name: 'LinkedIn Learning', countNumber: 200 },
    ];

    return new Promise((resolve) => setTimeout(() => resolve(mockCoursePlatforms), 1000));
};