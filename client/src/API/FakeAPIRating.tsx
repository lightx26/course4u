import { FilterItemType } from "../components/user.components/Homepage/MainContent";



// Hàm giả mạo fetchCoursenames
export const fetchCourseRatings = (): Promise<FilterItemType[]> => {
    const mockCoursenames: FilterItemType[] = [
        { name: '5', countNumber: 100 },
        { name: '4', countNumber: 200 },
        { name: '3', countNumber: 300 },
        { name: '2', countNumber: 400 },
        { name: '1', countNumber: 500 },
    ];

    return new Promise((resolve) => setTimeout(() => resolve(mockCoursenames), 1000));
};