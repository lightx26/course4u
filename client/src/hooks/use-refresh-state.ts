import { create } from "zustand";

type RefreshState = {
  registrationFlagAdmin: boolean;
  registrationFlagAccountant: boolean;
  setRegistrationFlagAdmin: () => void;
  setRegistrationFlagAccountant: () => void;
};

export const useRefreshState = create<RefreshState>((set, get) => ({
  registrationFlagAdmin: false,
  setRegistrationFlagAdmin: () =>
    set(() => ({
      registrationFlagAdmin: !get().registrationFlagAdmin,
    })),
  registrationFlagAccountant: false,
  setRegistrationFlagAccountant: () =>
    set(() => ({
      registrationFlagAccountant: !get().registrationFlagAccountant,
    })),
}));
