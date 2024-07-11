import { CourseType } from "../../App";
import { SkeletonLoader } from "./CourseCardComponent";
import React from "react";

const CourseCardComponent = React.lazy(() => import("./CourseCardComponent"));

type PropsType = {
    ListCourse: CourseType[]
    isLoading: boolean
    length?: number;
}

export default function ListCourseCardComponent({ ListCourse, isLoading, length = 8 }: PropsType) {
    if (isLoading) {
        return <div className='grid gap-4 text-black 2xl:grid-cols-4 md:grid-cols-2 xl:grid-cols-3 scree auto-rows-auto'>
            {Array.from({ length: length }).map((_, index) => <SkeletonLoader key={index} />)}
        </div>
    }
    return (
        <div className='grid gap-4 text-black 2xl:grid-cols-4 md:grid-cols-2 xl:grid-cols-3 scree auto-rows-auto'>
            {ListCourse.map((course) => {
                return (
                    <CourseCardComponent course={course} key={course.id} />
                )
            })}
        </div>
    )
}