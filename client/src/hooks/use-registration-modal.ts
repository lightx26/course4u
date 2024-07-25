import { create } from "zustand";
import { CourseType } from "../screens/user.screens/DetailOfCourse";

type RegistrationModalState = {
    isOpen: boolean;
    id: number | null;
    open: (state: boolean, id?: number, course?: CourseType, isBlockedModifiedCourse?: boolean) => void;
    close: () => void;
    course?: CourseType | null;
    isBlockedModifiedCourse?: boolean;
};

export const useRegistrationModal = create<RegistrationModalState>((set) => ({
    isOpen: false,
    id: null,
    course: null,
    open: (state, id, course, isBlockedModifiedCourse) => set({ isOpen: state, id: id, course: course, isBlockedModifiedCourse }),
    close: () => set({ isOpen: false, id: null, course: null }),
}));
