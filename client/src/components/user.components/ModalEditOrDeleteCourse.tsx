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
import { deleteCourseById, editCourse } from "../../apiService/Course.service";
import { useNavigate } from "react-router-dom";

import { toast } from "sonner";
import blobToFile from "../../utils/convertBlobToFile";
import { base64ToBlob } from "../../utils/ThumbnailConverter";

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
    const formData = new FormData();
    values.id = courseData?.id;
    formData.append("id", values.id!);
    Object.entries(values).forEach(([key, value]) => {
      if (key !== "categories" && key !== "thumbnailUrl") {
        if (typeof value === "string") {
          formData.append(key, value);
        }
      }
    });

    values.categories.forEach((category, index) => {
      formData.append(`categories[${index}].label`, category.label!);
      formData.append(`categories[${index}].value`, category.value);
    });

    // Handle the thumbnailUrl if it starts with "blob:" or "data:"
    if (values.thumbnailUrl.startsWith("blob:")) {
      const thumbnailFile = await blobToFile(values.thumbnailUrl, values.name);
      if (thumbnailFile) {
        formData.append("thumbnailFile", thumbnailFile);
      }
    } else if (values.thumbnailUrl.startsWith("data:")) {
      const thumbnailFromBase64 = base64ToBlob(values.thumbnailUrl);
      if (thumbnailFromBase64) {
        const thumbnailFile = new File(
          [thumbnailFromBase64],
          `${values.name}.jpg`,
          { type: thumbnailFromBase64.type }
        );
        formData.append("thumbnailFile", thumbnailFile);
      }
    } else {
      formData.append("thumbnailUrl", values.thumbnailUrl);
    }
    console.log(formData.get("thumbnailUrl")?.toString());
    if (formData.get("thumbnailUrl")?.toString().match("/api/thumbnail/")) {
      formData.delete("thumbnailUrl");
    }
    const status = await editCourse(formData);
    if (status === 200) {
      toast.success("Edit course succesfully", {
        description:
          "Course information already updated! refresh to see the changes",
        style: {
          color: "green",
          fontWeight: "bold",
          textAlign: "center",
        },
        // onAutoClose: () => {
        //   navigate("/admin", {
        //     replace: true,
        //     state: { refresh: true },
        //   });
        // },
      });
    } else if (status === 500) {
      toast.error("Oops! Something went wrong. Please try again later", {
        description: "Contact the admin for further assistance!",
        style: {
          color: "red",
          fontWeight: "bold",
          textAlign: "center",
        },
      });
    } else if (status === 409) {
      toast.error("Edit course unsuccessfully", {
        description:
          "Course link already exists in the system. Please check again!",
        style: {
          color: "red",
          fontWeight: "bold",
          textAlign: "center",
        },
      });
    } else if (status === 404) {
      toast.error("Edit course unsuccessfully", {
        description: "Course not found in the system. Please check again!",
        style: {
          color: "red",
          fontWeight: "bold",
          textAlign: "center",
        },
      });
    } else {
      toast.error("Oops! Something went wrong. Please try again later", {
        description: "Contact the admin for further assistance!",
        style: {
          color: "red",
          fontWeight: "bold",
          textAlign: "center",
        },
      });
    }
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
