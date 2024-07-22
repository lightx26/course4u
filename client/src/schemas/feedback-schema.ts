import z from "zod";

export const feedbackSchema = z.object({
    comment: z
        .string().trim()
        .min(1, { message: "Must give feedback before decline registration!" })
        .max(500),
});
