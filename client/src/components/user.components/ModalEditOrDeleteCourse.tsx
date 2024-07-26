import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { courseSchema } from "../../schemas/course-schema";
import { Button } from "../ui/button";
import { useState } from "react";
import { CourseForm } from "../../components/form/course-form";
import { Form } from "../ui/form";

import { Modal } from "antd";
import {
  Dialog,
  DialogContent,
  DialogTitle,
  DialogTrigger,
} from "../ui/dialog";
import { deleteCourseById } from "../../apiService/Course.service";
import { useNavigate } from "react-router-dom";

import { toast } from "sonner";

interface CourseType {
  id: string | undefined;
  name: string;
  thumbnailUrl: string;
  platform: string;
  createdDate: string;
  level: string;
  categories: {
    value: string;
    name?: string | undefined;
    label?: string | undefined;
  }[]; // Update the type of 'categories'
  link: string;
  teacherName: string;
  status: string;
}

type Props = {
  children: React.ReactNode;
  courseData?: CourseType;
};

const ModalEditOrDeleteCourse = ({ children, courseData }: Props) => {
  const [openModalConfirm, setOpenModalConfirm] = useState(false);
  const [confirmLoading, setConfirmLoading] = useState(false);
  const navigate = useNavigate();
  const form = useForm<z.infer<typeof courseSchema>>({
    resolver: zodResolver(courseSchema),
    mode: "onBlur",
    defaultValues: {
      id: "",
      name: "",
      teacherName: "",
      link: "",
      level: "",
      platform: "",
      categories: [],
      thumbnailUrl: "",
    },
  });

  if (courseData?.thumbnailUrl.includes("Default Course thumnail 1.svg")) {
    courseData.thumbnailUrl = "";
  }

  const handleConfirm = async () => {
    setConfirmLoading(true);
    const response = await deleteCourseById(courseData?.id);
    if (response && response.status === 200) {
      setOpenModalConfirm(false);
      setConfirmLoading(false);
      toast.success("Course Deleted Successfully!", {
        description:
          "The course has been successfully deleted. You can verify it in the course list.",
      });
      navigate("/admin/courses");
    } else if (response && response.status === 400) {
      setOpenModalConfirm(false);
      setConfirmLoading(false);
      toast.error("Course Delete Failed!", {
        description: response.data.message,
      });
    } else {
      setOpenModalConfirm(false);
      setConfirmLoading(false);
      toast.error("Course Delete Failed!", {
        description: "An error occurred while deleting the course.",
      });
    }
  };

  async function onSubmit(values: z.infer<typeof courseSchema>) {
    values.id = courseData?.id;
    console.log(values);
  }

  return (
    <Dialog>
      <DialogTrigger asChild>{children}</DialogTrigger>
      <DialogContent className="max-w-[1100px] w-full px-3 rounded-xl min-h-[600px]">
        <DialogTitle></DialogTitle>
        <Form {...form}>
          <form className="w-full" onSubmit={form.handleSubmit(onSubmit)}>
            <CourseForm form={form} isEdit={true} course={courseData} />
            <div className="flex justify-end gap-2 mt-8">
              <Button
                type="button"
                className="bg-red-600 text-white w-32 h-9"
                onClick={() => setOpenModalConfirm(true)}
              >
                Delete
              </Button>
              <Modal
                title={<p style={{ fontSize: "1.2rem" }}>Delete a course</p>}
                open={openModalConfirm}
                onOk={handleConfirm}
                onCancel={() => setOpenModalConfirm(false)}
                okText="Yes"
                cancelText="No"
                centered={true}
                confirmLoading={confirmLoading}
                okButtonProps={{
                  style: {
                    backgroundColor: "#861fa2",
                    color: "white",
                    width: "80px",
                  },
                }}
                cancelButtonProps={{
                  style: {
                    borderColor: "#ccc",
                    color: "black",
                    width: "80px",
                  },
                }}
              >
                <p style={{ fontSize: "1rem", margin: "10px 0px 40px 0px" }}>
                  Do you want to delete this course?
                </p>
              </Modal>
              <Button
                type="submit"
                className="bg-green-600 text-white w-32 h-9"
              >
                Save
              </Button>
            </div>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
};

export default ModalEditOrDeleteCourse;
