import AdminCard from "../../components/user/admin/admin-card";
import CreateCourse from "../../components/course/course";

export default function CreateCoursesScreen() {
    return (
        <div className='flex justify-center items-start min-h-[100vh] p-14 gap-6'>
            <div className='w-[18%]'>
                <AdminCard />
            </div>
            <CreateCourse />
        </div>
    );
}
