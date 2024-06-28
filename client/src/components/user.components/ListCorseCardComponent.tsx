import React, { Suspense } from 'react'
import { CourseType } from '../../App'
import { SkeletonLoader } from './CourseCardComponent'
// import CourseCardComponent from './CourseCardComponent'

const CourseCardComponent = React.lazy(() => import('./CourseCardComponent'))

type PropsType = {
    ListCourse: CourseType[]
    isLoading: boolean
}

export default function ListCourseCardComponent(props: PropsType) {
    if (props.isLoading) {
        return <div className='grid gap-2 text-black 2xl:grid-cols-4 md:grid-cols-2 xl:grid-cols-3 scree auto-rows-auto'>
            {Array.from({ length: 8 }).map((_, index) => <SkeletonLoader key={index} />)}
        </div>
    }
    return (
        <div className='grid gap-2 text-black 2xl:grid-cols-4 md:grid-cols-2 xl:grid-cols-3 scree auto-rows-auto'>
            {props.ListCourse.map((course) => {
                return (
                    <Suspense fallback={<p>Loading...</p>} key={course.id}>
                        <CourseCardComponent course={course} key={course.id} />
                    </Suspense>
                )
            })}
        </div>
    )
}