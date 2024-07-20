import z from "zod"

export const editUserSchema = z.object({
    fullName: z.string().max(50).optional(),
    username: z.string().max(50).optional(),
    avatarUrl: z.string().optional(),
    telephone: z.string().min(0).max(11).regex(/^\d*$/).optional(),
    dateOfBirth: z.date({
        required_error: "A date of birth is required.",
    }).optional(),
    email: z.string().email({
        message:
            "Invalid email address"
    }),
    gender: z.string().optional(),
})