import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { courseSchema } from "../../schemas/course-schema";
import { CourseForm } from "../form/course-form";
import { Form } from "../ui/form";
import { Button } from "../ui/button";
import instance from "../../utils/customizeAxios";

const CreateCourse = () => {
  const form = useForm<z.infer<typeof courseSchema>>({
    resolver: zodResolver(courseSchema),
    mode: "onBlur",
    defaultValues: {
      name: "",
      teacherName: "",
      link: "",
      level: "",
      platform: "",
      categories: [],
      thumbnailUrl: "",
      //  In backend side, if thumbnail url can be get from Course link, then request should be thumbnailUrl
      // Otherwise, if users upload a thumbnail image, request name should be thumbnailFile
    },
  });
  async function onSubmit(values: z.infer<typeof courseSchema>) {
    console.log(values);
    // Helper function to convert blob URL to File object
    async function blobToFile(blobUrl: string, fileName: string) {
      const response = await fetch(blobUrl);
      const blob = await response.blob();
      return new File([blob], `${fileName}.jpg`, { type: "image/jpeg" });
    }
    const formData = new FormData();
    // Append regular values to FormData
    for (const [key, value] of Object.entries(values)) {
      if (key !== "categories" && key !== "thumbnailUrl") {
        if (typeof value === "string") {
          formData.append(key, value);
        }
      }
    }
    // Append formatted categories to FormData
    values.categories.forEach((category, index) => {
      formData.append(`categories[${index}].label`, category.label!);
      formData.append(`categories[${index}].value`, category.value);
    });
    // Handle the thumbnailUrl if it starts with "blob:"
    if (values.thumbnailUrl.startsWith("blob:")) {
      const thumbnailFile = await blobToFile(values.thumbnailUrl, values.name);
      formData.append("thumbnailFile", thumbnailFile);
    } else {
      formData.append("thumbnailUrl", values.thumbnailUrl);
    }

    instance.postForm("/courses", formData)
      .then((res) => {
        console.log(res.data);
        alert("Create course successfully");
      })
      .catch((error) => {
        console.error(error);
        alert("An error occurred while creating the course");
      });
  }

  return (
    <div className="w-full px-20 py-10 bg-white border-2 border-gray-200 rounded-3xl">
      <h2 className="text-[32px] text-center mb-5 font-semibold">
        Create Course
      </h2>
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="w-full"
        >
          <CourseForm form={form} isEdit={true} />
          <div className="flex justify-end">
            <Button variant="success" className="mt-10">
              CREATE
            </Button>
          </div>
        </form>
      </Form>
    </div>
  );
};

export default CreateCourse;
