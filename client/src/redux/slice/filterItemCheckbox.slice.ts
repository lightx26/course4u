import { createSlice, PayloadAction } from "@reduxjs/toolkit";
type FilterItemSlice = {
    id: string;
    name: string;
}

type FilterItemSliceType = {
    FilterComponentId: string;
    listChoice: FilterItemSlice[];
}


const initialState: FilterItemSliceType[] = [];

export const filterItemSlice = createSlice({
    name: "filterItems",
    initialState,
    reducers: {
        addFilterItem: (state, action: PayloadAction<FilterItemSliceType>) => {
            state.push(action.payload);
        },

        removeFilterItem: (state, action: PayloadAction<FilterItemSliceType>) => {
            return state.filter(item => item !== action.payload);
        },

        toggleFilterItem: (state, action: PayloadAction<FilterItemSliceType & { choiceId: string }>) => {
            console.log('State', state);
            const componentIndex = state.findIndex(item => item.FilterComponentId === action.payload.FilterComponentId);

            if (componentIndex !== -1) {
                const choiceIndex = state[componentIndex].listChoice.findIndex(choice => choice.id === action.payload.choiceId);

                if (choiceIndex !== -1) {
                    state[componentIndex].listChoice.splice(choiceIndex, 1);
                } else {
                    state[componentIndex].listChoice.push({
                        id: action.payload.choiceId,
                        name: action.payload.listChoice[0].name
                    });
                }
            } else {
                state.push({
                    FilterComponentId: action.payload.FilterComponentId,
                    listChoice: [{
                        id: action.payload.choiceId,
                        name: action.payload.listChoice[0].name
                    }]
                });
            }
        },
        getAllFilterItems: (state) => {
            return state;
        },
        deleteAllFilterItem: () => {
            return [];
        },
    },
});

export const { addFilterItem, removeFilterItem, toggleFilterItem, getAllFilterItems } = filterItemSlice.actions;

export default filterItemSlice.reducer;