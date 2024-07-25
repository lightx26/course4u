import { z } from "zod";

const trimAndSingleSpace = (val: string) => val.trim().replace(/\s+/g, ' ');
const urlPattern = /^https?:\/\/(www\.)?[-a-zA-Z0-9@:%.+~#=]{1,256}\.[a-zA-Z]{2,6}\b([-a-zA-Z0-9()@:%_+.~#?&//=]*)$/;

const removeQueryParams = (url: string) => {
    try {
        const urlObj = new URL(url);
        urlObj.search = '';
        return urlObj.toString();
    } catch (e) {
        return url;
    }
};
const optionSchema = z.object({
    label: z.string().optional(),
    value: z.string()
        .transform(trimAndSingleSpace)
        .refine(val => val.length > 0, { message: "This field is required!" }),
    name: z.string().optional(),
});

export const courseSchema = z.object({
    id: z.string().optional(),
    name: z.string()
        .transform(trimAndSingleSpace)
        .refine(val => val.length > 0, { message: "This field is required!" }),
    teacherName: z.string()
        .transform(trimAndSingleSpace)
        .refine(val => val.length > 0, { message: "This field is required!" }),
    link: z.string()
        .transform(trimAndSingleSpace)
        .transform(removeQueryParams)
        .refine(val => val.length > 0, { message: "This field is required!" })
        .refine(value => urlPattern.test(value), { message: "Invalid URL format - URL must include protocol" }),
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
