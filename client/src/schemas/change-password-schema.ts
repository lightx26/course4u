import { z } from "zod";

const passwordRegex = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W)/;

export const changePasswordSchema = z.object({
    oldPassword: z.string().min(6).max(50).regex(passwordRegex, "Password must include at least one number, one lowercase letter, one uppercase letter, and one special character"),
    newPassword: z.string().min(6).max(50).regex(passwordRegex, "Password must include at least one number, one lowercase letter, one uppercase letter, and one special character"),
    confirmPassword: z.string().min(6).max(50),
})