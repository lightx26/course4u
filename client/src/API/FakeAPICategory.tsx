import { FilterItemType } from "../components/user.components/Homepage/MainContent";

export const fetchCategories = (): Promise<FilterItemType[]> => {
    const mockCategories: FilterItemType[] = [
        {
            id: 'cat1',
            name: 'Web Development',
            countNumber: 1200,
        },
        {
            id: 'cat2',
            name: 'Front-end Development',
            countNumber: 800,
        },
        {
            id: 'cat3',
            name: 'Back-end Development',
            countNumber: 900,
        },
        {
            id: 'cat4',
            name: 'Full Stack Development',
            countNumber: 1500,
        },
        {
            id: 'cat5',
            name: 'Mobile Development',
            countNumber: 700,
        },
        {
            id: 'cat6',
            name: 'Data Science',
            countNumber: 600,
        },
        {
            id: 'cat7',
            name: 'Machine Learning',
            countNumber: 500,
        },
        {
            id: 'cat8',
            name: 'Cybersecurity',
            countNumber: 400,
        },
        {
            id: 'cat9',
            name: 'Cloud Computing',
            countNumber: 300,
        },
        {
            id: 'cat10',
            name: 'Blockchain',
            countNumber: 200,
        },
        // Thêm nhiều phân loại khác nếu cần
    ];

    // Mô phỏng việc gọi API bất đồng bộ và trả về dữ liệu sau 1 giây
    return new Promise((resolve) => {
        setTimeout(() => resolve(mockCategories), 1000);
    });
};