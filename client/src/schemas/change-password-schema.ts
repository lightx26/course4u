import { z } from 'zod';

const passwordRegex = /(?=.*\d)(?=.*[A-Z])(?=.*\W)/;

export const changePasswordSchema = z.object({
    oldPassword: z.string().min(1, "This field is required"),
    newPassword: z.string().min(1, "This field is required").min(8).regex(passwordRegex, "Password must include at least one number, one uppercase letter, and one special character"),
    confirmPassword: z.string().min(1, "This field is required"),
}).refine((data) => data.newPassword === data.confirmPassword, {
    message: "New password and confirm password must match",
    path: ["confirmPassword"],
});