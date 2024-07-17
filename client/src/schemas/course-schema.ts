import { z } from "zod";

const trimAndSingleSpace = (val: string) => val.trim().replace(/\s+/g, ' ');

const optionSchema = z.object({
    label: z.string().optional(),
    value: z.string()
        .transform(trimAndSingleSpace)
        .refine(val => val.length > 0, { message: "This field is required!" }),
    name: z.string().optional(),
});

export const courseSchema = z.object({
    name: z.string()
        .transform(trimAndSingleSpace)
        .refine(val => val.length > 0, { message: "This field is required!" }),
    teacherName: z.string()
        .transform(trimAndSingleSpace)
        .refine(val => val.length > 0, { message: "This field is required!" }),
    link: z.string()
        .transform(trimAndSingleSpace)
        .refine(val => val.length > 0, { message: "This field is required!" })
        .refine(value => {
            try {
                new URL(value);
                return true;
            } catch {
                return false;
            }
        }, { message: "Invalid URL format" }),
    level: z.string()
        .transform(trimAndSingleSpace)
        .refine(val => val.length > 0, { message: "This field is required!" }),
    platform: z.string()
        .transform(trimAndSingleSpace)
        .refine(val => val.length > 0, { message: "This field is required!" }),
    categories: z.array(optionSchema)
        .refine(arr => arr.length > 0, { message: "This field is required!" }),
    thumbnailUrl: z.string(),
});
