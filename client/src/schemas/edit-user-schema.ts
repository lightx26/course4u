import z from "zod"

export const editUserSchema = z.object({
    name: z.string().max(50).optional(),
    username: z.string().max(50).optional(),
    avatar: z.string().url().optional(),
    phoneNumber: z.string().max(10).regex(/^\d+$/).optional(),
    dateOfBirth: z.date({
        required_error: "A date of birth is required.",
    }).optional(),
    email: z.string().email({
        message:
            "Invalid email address"
    }),
    gender: z.string().optional(),
})