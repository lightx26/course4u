import { create } from "zustand";

type RegistrationModalState = {
    isOpen: boolean;
    id: number | null;
    open: (state: boolean, id?: number ) => void;
    close: () => void;
};

export const useRegistrationModal = create<RegistrationModalState>((set) => ({
    isOpen: false,
    id: null,
    open: (state, id) => set({ isOpen: state, id: id }),
    close: () => set({ isOpen: false, id: null }),
}));
