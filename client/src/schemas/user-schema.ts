import { z } from "zod";

export const userRegistrationSchema = z.object({
    avatarUrl: z.string(),
    email: z.string().email(),
    role: z.string(),
    telephone: z.string(),
    username: z.string(),
    id: z.number(),
});
