import { z } from "zod";

const optionSchema = z.object({
    label: z.string().optional(),
    value: z.string(),
    name: z.string().optional(),
});

export const courseSchema = z.object({
    name: z
        .string()
        .min(1, "Name required!")
        .min(3, { message: "Name must be at least 3 characters long!" })
        .max(100, { message: "Name must be at most 100 characters long!" }),
    teacherName: z.string().min(3, {
        message: "Teacher name must be at least 3 characters long!",
    }),
    link: z.string().url({ message: "Link must be a valid URL!" }),
    level: z.enum(["BEGINNER", "INTERMEDIATE", "ADVANCED"], {
        message:
            "Level must be one of 'beginner', 'intermediate', or 'advanced'!",
    }),
    platform: z.string({ message: "Platform required!" }),
    categories: z.array(optionSchema).min(1),
    thumbnailUrl: z.string(),
});
