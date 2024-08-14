import { z } from "zod";
import { Feedback } from "../components/registration/feedback/feedback-list";
import { Status } from "../constant";
import { courseSchema } from "../schemas/course-schema";
import { userRegistrationSchema } from "../schemas/user-schema";

export type RegistrationsProps = {
    id?: number;
    duration?: number;
    durationUnit?: "DAY" | "WEEK" | "MONTH";
    status?: Status;
    course?: z.infer<typeof courseSchema>;
    isBlockedModifiedCourse?: boolean;
    user?: z.infer<typeof userRegistrationSchema>;
    registrationFeedbacks?: Feedback[];
    startDate?: string;
    endDate?: string;
};

export type RegistrationType = {
    id?: string;
    startDate?: Date;
    endDate?: Date;
    registerDate?: Date;
    lastUpdated?: Date;
    courseId?: string;
    courseName?: string;
    courseThumbnailUrl?: string;
    coursePlatform?: string;
    userId?: string;
    userName?: string;
    userFullname?: string;
    userAvatarUrl?: string;
    duration?: number;
    durationUnit?: string;
    status?: string;
};