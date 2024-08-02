import { z } from "zod";
import { courseSchema } from "./course-schema";

export const registrationSchema = courseSchema.extend({
    duration: z.number(),
    durationUnit: z.enum(["DAY", "WEEK", "MONTH"]),
});
