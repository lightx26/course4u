import { create } from "zustand";

type RefreshState = {
    registrationFlagAdmin: boolean;
    setRegistrationFlagAdmin: () => void;
};

export const useRefreshState = create<RefreshState>((set, get) => ({
    registrationFlagAdmin: false,
    setRegistrationFlagAdmin: () =>
        set(() => ({
            registrationFlagAdmin: !get().registrationFlagAdmin,
        })),
}));
