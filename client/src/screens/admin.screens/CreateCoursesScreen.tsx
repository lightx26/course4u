import AdminCard from "../../components/admin.components/mainPage.components/AdminCard";
import CreateCourse from "../../components/user.components/create-course";

export default function CreateCoursesScreen() {
  return (
    <div className="flex justify-center items-start min-h-[100vh] p-14 gap-6">
      <div className="w-[18%]">
        <AdminCard />
      </div>
      <CreateCourse />
    </div>
  );
}
