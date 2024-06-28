import { FilterItemType } from "../components/user.components/Homepage/MainContent";

// Hàm giả mạo fetchCourseLevels
export const fetchCourseLevels = (): Promise<FilterItemType[]> => {
    const mockCourseLevels: FilterItemType[] = [
        { id: 'lvl1', name: 'Beginner', countNumber: 500 },
        { id: 'lvl2', name: 'Intermediate', countNumber: 300 },
        { id: 'lvl3', name: 'Advance', countNumber: 200 },
    ];
    return new Promise((resolve) => setTimeout(() => resolve(mockCourseLevels), 1000));
};