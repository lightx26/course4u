import { z } from "zod";
import { registrationSchema } from "../schemas/registration-schema";
import blobToFile, { base64ToBlob } from "./convertBlobToFile";

export const convertToFormData = async (
    values: z.infer<typeof registrationSchema>
) => {
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
    requestBody.append("platform", values.platform.toUpperCase());
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
    return requestBody;
};
