export type CourseFormValues = {
    name: string;
    teacherName: string;
    link: string;
    level: string;
    platform: string;
    categories: {
      value: string;
      name?: string;
      label?: string;
    }[];
    thumbnailUrl: string;
    id?: string;
    duration: number; // Add this line
    durationUnit: "DAY" | "WEEK" | "MONTH"; // Add this line
  };

export type CourseType = {
    id: string | undefined;
    name: string;
    thumbnailUrl?: string;
    assignee?: {
        id?: string;
        name?: string;
        avatarUrl?: string;
        role?: string;
        status?: string;
    };
    platform?: string;
    createdDate?: string;
    period?: {
        startDay?: Date;
        endDay?: Date;
    };
    rating?: number;
    enrollmentCount?: number;
    level?: string;
};