import { z } from "zod";

const optionSchema = z.object({
    label: z.string(),
    value: z.string(),
    disable: z.boolean().optional(),
});

export const courseSchema = z.object({
    name: z
        .string()
        .min(3, { message: "Name must be at least 3 characters long!" })
        .max(80, { message: "Name must be at most 80 characters long!" }),
    teacherName: z.string().min(3, {
        message: "Teacher name must be at least 3 characters long!",
    }),
    link: z.string().url({ message: "Link must be a valid URL!" }),
    level: z.enum(["BEGINNER", "INTERMEDIATE", "ADVANCED"], {
        message:
            "Level must be one of 'beginner', 'intermediate', or 'advanced'!",
    }),
    platform: z.string({ message: "Platform required!" }),
    category: z.array(optionSchema).min(1),
    thumbnailUrl: z.string(),
});
