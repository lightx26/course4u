import { z } from "zod";

export const userRegistrationSchema = z.object({
    username: z.string(),
    email: z.string().email(),
    telephone: z.string(),
    avatarUrl: z.string(),
    dateOfBirth: z.string(),
    role: z.string(),
    gender: z.string(),
    id:z.number()
});
