import { create } from "zustand";
import { RegistrationsProps } from "../components/registration/registrations";

type RegistrationDetailState = {
    registration: RegistrationsProps | null;
    setRegistration: (registration: RegistrationsProps) => void;
    closeRegistration: () => void;
};

export const useRegistrationDetail = create<RegistrationDetailState>((set) => ({
    registration: null,
    setRegistration: (registrationData) =>
        set({ registration: { ...registrationData } }),
    closeRegistration: () => set({ registration: null }),
}));
