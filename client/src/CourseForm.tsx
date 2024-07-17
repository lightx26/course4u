import React, { useState, ChangeEvent, FormEvent } from "react";
import axios from "axios";
import cheerio from "cheerio";

interface FormData {
  name: string;
  link: string;
  platform: string;
  teacherName: string;
  status: "PENDING" | "APPROVED" | "REJECTED";
  level: "BEGINNER" | "INTERMEDIATE" | "ADVANCED";
  categories: string;
  thumbnailUrl: string | null;
  thumbnailFile: File | null;
}

const CourseForm: React.FC = () => {
  const [formData, setFormData] = useState<FormData>({
    name: "",
    link: "",
    platform: "",
    teacherName: "",
    status: "PENDING",
    level: "BEGINNER",
    categories: "",
    thumbnailUrl: null,
    thumbnailFile: null,
  });

  const [uploadedImage, setUploadedImage] = useState<string | null>(null);
  const [imageUrls, setImageUrls] = useState<string[]>([]);

  const handleChange = (
    e: ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setFormData({
        ...formData,
        thumbnailFile: e.target.files[0],
      });

      // Preview the image
      const reader = new FileReader();
      reader.onload = () => {
        if (reader.readyState === 2) {
          setUploadedImage(reader.result as string);
        }
      };
      reader.readAsDataURL(e.target.files[0]);
    }
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    const formDataToSend = new FormData();
    formDataToSend.append("name", formData.name);
    formDataToSend.append("link", formData.link);
    formDataToSend.append("platform", formData.platform);
    formDataToSend.append("teacherName", formData.teacherName);
    formDataToSend.append("status", formData.status);
    formDataToSend.append("level", formData.level);
    formDataToSend.append("categories", formData.categories);

    // Check if there's a thumbnail URL fetched from the link
    if (formData.thumbnailUrl) {
      formDataToSend.append("thumbnailUrl", formData.thumbnailUrl);
    } else if (formData.thumbnailFile) {
      formDataToSend.append("thumbnailFile", formData.thumbnailFile);
    }

    try {
      const response = await fetch("http://localhost:8080/api/courses", {
        method: "POST",
        body: formDataToSend,
      });
      const data = await response.json();
      setUploadedImage(data.thumbnail); // Assuming the response returns the URL of the uploaded image
    } catch (error) {
      console.error("Error:", error);
    }
  };

  const showAllImages = () => {
    fetch("http://localhost:8080/api/courses", {
      method: "GET",
    })
      .then((response) => response.json())
      .then((data) => {
        setImageUrls(data); // Assuming data is an array of image URLs
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  };

  const fetchOpenGraphData = async (url: string) => {
    try {
      const response = await axios.get(url);
      const html = response.data;
      const $ = cheerio.load(html);

      const ogData: Partial<FormData> = {};
      $('meta[property^="og:"]').each((_, meta) => {
        const property = $(meta).attr("property");
        const content = $(meta).attr("content");
        if (property && content) {
          const key = property.replace("og:", "");
          if (key === "title") {
            ogData.name = content;
          } else if (key === "url") {
            ogData.link = content;
          } else if (key === "site_name") {
            ogData.platform = content;
          } else if (key === "image") {
            ogData.thumbnailUrl = content; // Store as thumbnail URL
          }
        }
      });

      setFormData({
        ...formData,
        ...ogData,
      });
    } catch (error) {
      console.error("Error fetching Open Graph data:", error);
    }
  };

  const handleLinkChange = async (e: ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target;
    setFormData({
      ...formData,
      link: value,
    });
    if (value.trim() !== "") {
      await fetchOpenGraphData(value);
    }
  };

  return (
    <div>
      <h2>Create Course</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Course Name:</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Link:</label>
          <input
            type="text"
            name="link"
            value={formData.link}
            onChange={handleLinkChange}
            required
          />
        </div>
        <div>
          <label>Platform:</label>
          <input
            type="text"
            name="platform"
            value={formData.platform}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Teacher Name:</label>
          <input
            type="text"
            name="teacherName"
            value={formData.teacherName}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Status:</label>
          <select
            name="status"
            value={formData.status}
            onChange={handleChange}
            required
          >
            <option value="PENDING">Pending</option>
            <option value="APPROVED">Approved</option>
            <option value="REJECTED">Rejected</option>
          </select>
        </div>
        <div>
          <label>Level:</label>
          <select
            name="level"
            value={formData.level}
            onChange={handleChange}
            required
          >
            <option value="BEGINNER">Beginner</option>
            <option value="INTERMEDIATE">Intermediate</option>
            <option value="ADVANCED">Advanced</option>
          </select>
        </div>
        <div>
          <label>Categories:</label>
          <input
            type="text"
            name="categories"
            value={formData.categories}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Thumbnail:</label>
          <input type="file" name="thumbnail" onChange={handleFileChange} />
        </div>
        {uploadedImage && (
          <div>
            <h2>Thumbnail Preview</h2>
            <img
              src={uploadedImage}
              alt="Thumbnail Preview"
              style={{ maxWidth: "200px", maxHeight: "200px" }}
            />
          </div>
        )}
        <button type="submit">Create Course</button>
      </form>

      <button onClick={showAllImages}>Show All Images</button>

      <div>
        <h2>All Images</h2>
        {imageUrls.map((imageUrl, index) => (
          <img
            key={index}
            src={imageUrl}
            alt={`Image ${index}`}
            style={{
              maxWidth: "200px",
              maxHeight: "200px",
              marginRight: "10px",
            }}
          />
        ))}
      </div>
    </div>
  );
};

export default CourseForm;
