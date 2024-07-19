import z from "zod";

export const feedbackSchema = z.object({
    comment: z
        .string()
        .min(1, { message: "Must feedback before decline registration!" })
        .max(500),
});
