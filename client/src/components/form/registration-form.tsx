import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { registrationSchema } from "../../schemas/registration-schema";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "../ui/form";
import { Input } from "../ui/input";
import { CourseForm } from "./course-form";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../ui/select";

import { useEffect } from "react";
import { RegistrationsProps } from "../user.components/registrations";
import { RegistrationButton } from "../user.components/registration-button";
import { toast } from "sonner";
import blobToFile from "../../utils/convertBlobToFile";
import { createNewRegistration } from "../../apiService/MyRegistration.service";
import { base64ToBlob } from "../../utils/ThumbnailConverter";
import { useSelector } from "react-redux";
import { RootState } from "../../redux/store/store";
import RegistrationAdminSection from "../admin.components/registrations.components/registration-admin-section";
import FeedbackList from "../feedback-list";
import LearningProgress from "../user.components/learning-progress";
import { useRegistrationModal } from "../../hooks/use-registration-modal";

type RegistrationsFormProps = RegistrationsProps & {
  isEdit: boolean;
  setIsEdit: (isEdit: boolean) => void;
  startDate?: string;
  endDate?: string;
};

export const RegistrationsForm = ({
  id,
  duration,
  durationUnit,
  status,
  course,
  isEdit,
  setIsEdit,
  startDate,
  endDate,
  registrationFeedbacks,
}: RegistrationsFormProps) => {
  const form = useForm<z.infer<typeof registrationSchema>>({
    resolver: zodResolver(registrationSchema),
    mode: "onBlur",
    shouldFocusError: false,
    defaultValues: {
      name: "",
      teacherName: "",
      link: "",
      level: "",
      platform: "",
      categories: [],
      duration: 1,
      durationUnit: "DAY",
      thumbnailUrl: "",
    },
  });
  const { open } = useRegistrationModal((state) => state);
  const user = useSelector((state: RootState) => state.user);
  useEffect(() => {
    if (id) {
      form.setValue("duration", duration!);
      form.setValue("durationUnit", durationUnit || "DAY");
      form.setValue("platform", course?.platform || "");
      form.setValue("name", course?.name || "");
      form.setValue("teacherName", course?.teacherName || "");
      form.setValue("link", course?.link || "");
      form.setValue("thumbnailUrl", course?.thumbnailUrl || "");
      form.setValue("level", course?.level || "BEGINNER");
      const categoriesData = course?.categories?.map((category) => ({
        label: category.name,
        value: category.name!,
      }));
      form.setValue("categories", categoriesData || []);
    }
  }, [course, duration, durationUnit, form, id]);
  // @ts-nocheck
  async function onSubmit(values: z.infer<typeof registrationSchema>) {
    let isFile = false;
    let thumbnailFile;

    if (values.thumbnailUrl.startsWith("blob:")) {
      thumbnailFile = await blobToFile(values.thumbnailUrl, values.name);
      isFile = true;
    } else if (values.thumbnailUrl.startsWith("data:")) {
      const thumbnailFromBase64 = base64ToBlob(values.thumbnailUrl);
      thumbnailFile = new File([thumbnailFromBase64], `${values.name}.jpg`, {
        type: thumbnailFromBase64.type,
      });
      isFile = true;
    } else {
      isFile = false;
    }

    const requestBody = new FormData();
    requestBody.append("name", values.name);
    requestBody.append("teacherName", values.teacherName);
    requestBody.append("link", values.link);
    requestBody.append("level", values.level);
    requestBody.append("platform", values.platform);
    values.categories.forEach((category, index) => {
      requestBody.append(`categories[${index}].label`, category.label!);
      requestBody.append(`categories[${index}].value`, category.value);
    });
    requestBody.append("duration", values.duration.toString());
    requestBody.append("durationUnit", values.durationUnit);

    if (isFile) {
      if (thumbnailFile) {
        requestBody.append("thumbnailFile", thumbnailFile);
      }
    } else {
      requestBody.append("thumbnailUrl", values.thumbnailUrl);
    }

    const status = await createNewRegistration(requestBody);
    if (status === 201) {
      toast.success("Create a new registration successfully", {
        description: "",
        style: {
          color: "green",
          fontWeight: "bold",
          textAlign: "center",
        },
        onAutoClose: () => {
          open(false);
        },
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
      toast.error("Create a new registration unsuccessfully", {
        description:
          "Your course request already exists in the system. Please check again!",
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
    <div className="flex flex-col w-full">
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="w-full space-y-8 "
        >
          <CourseForm
            //eslint-disable-next-line
            // @ts-ignore
            form={form}
            course={course}
            isEdit={isEdit}
            registrationStatus={status}
          />
          <div className="flex w-[60%] pr-4 gap-2">
            <FormField
              control={form.control}
              name="duration"
              render={({ field }) => (
                <FormItem className="flex-1">
                  <FormLabel>
                    Duration <span className="text-red-500">*</span>
                  </FormLabel>
                  <FormControl>
                    <Input
                      type="number"
                      placeholder="Duration"
                      {...field}
                      onChange={(event) => field.onChange(+event.target.value)}
                      className=""
                      disabled={!isEdit}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="durationUnit"
              render={({ field }) => (
                <FormItem className="w-[100px] mt-8">
                  <Select
                    onValueChange={field.onChange}
                    defaultValue={durationUnit || form.watch("durationUnit")}
                    disabled={!isEdit}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select a level for this course" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      <SelectItem value="DAY">Days</SelectItem>
                      <SelectItem value="WEEK">Weeks</SelectItem>
                      <SelectItem value="MONTH">Months</SelectItem>
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>
          {startDate && (
            <LearningProgress startDate={startDate} endDate={endDate || ""} />
          )}
          {user.user?.role === "USER" && (
            <div className="space-y-5">
              {registrationFeedbacks.length > 0 && (
                <FeedbackList feedbacks={registrationFeedbacks} />
              )}
              <RegistrationButton
                status={status!}
                setIsEdit={setIsEdit}
                isEdit={isEdit}
                id={id}
              />
            </div>
          )}
        </form>
      </Form>
      {user.user?.role === "ADMIN" && (
        <div className="space-y-5">
          {registrationFeedbacks.length > 0 && (
            <FeedbackList feedbacks={registrationFeedbacks} />
          )}
          <RegistrationAdminSection status={status} />
        </div>
      )}
    </div>
  );
};
