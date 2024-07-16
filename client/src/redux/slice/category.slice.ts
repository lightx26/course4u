import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { fetchListAvailableCategory } from "../../apiService/Category.service";

export type CategoryType = {
    id: number;
    name: string;
};

async function getInitialState(): Promise<CategoryType[]> {
    try {
        const response = await fetchListAvailableCategory();
        return response?.data as unknown as CategoryType[];
    } catch (error) {
        console.error("Failed to fetch categories:", error);
        return [];
    }
}

const initialState: CategoryType[] = await getInitialState();

export const categorySlice = createSlice({
    name: "categoryItems",
    initialState,
    reducers: {
        getAllCategoryItems: (state) => {
            return state;
        },
        // Cập nhật để nhận quantity và trả về số lượng category tương ứng
        getCategoryItems: (state, action: PayloadAction<number>) => {
            // Lấy quantity từ action.payload
            const quantity = action.payload;
            // Trả về quantity đầu tiên của state
            return state.slice(0, quantity);
        },
        searchCategoryItems: (state, action: PayloadAction<string>) => {
            // Lấy tên category từ action.payload
            const categoryName = action.payload;
            // Trả về category có tên giống với categoryName
            return state.filter((category) => category.name.includes(categoryName));
        }
    },
});

export const { getAllCategoryItems, getCategoryItems, searchCategoryItems } = categorySlice.actions;
export default categorySlice.reducer;