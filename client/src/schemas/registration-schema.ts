import { z } from "zod";
import { courseSchema } from "./course-schema";

export const registrationSchema = courseSchema.extend({
    duration: z
        .number()
        .int()
        .positive({ message: "Duration must be a positive integer!" }),
    durationUnit: z.enum(["DAY", "WEEK", "MONTH"]),
});
