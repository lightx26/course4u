import { create } from "zustand";

type RefreshState = {
  registrationFlagAdmin: boolean;
  registrationFlagAccountant: boolean;
  courseDetailFlag: boolean;
  setRegistrationFlagAdmin: () => void;
  setRegistrationFlagAccountant: () => void;
  setCourseDetailFlag: () => void;
};

export const useRefreshState = create<RefreshState>((set, get) => ({
  registrationFlagAdmin: false,
  courseDetailFlag: false,
  setRegistrationFlagAdmin: () =>
    set(() => ({
      registrationFlagAdmin: !get().registrationFlagAdmin,
    })),
  registrationFlagAccountant: false,
  setRegistrationFlagAccountant: () =>
    set(() => ({
      registrationFlagAccountant: !get().registrationFlagAccountant,
    })),
  setCourseDetailFlag: () => set(() => ({ courseDetailFlag: !get().courseDetailFlag })),
}));
