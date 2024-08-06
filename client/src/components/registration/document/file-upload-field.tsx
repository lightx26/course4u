import React from "react";

interface FileUploadFieldProps {
  label: string;
  file: File | null;
  onFileChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  accept: string;
  required?: boolean;
}

const FileUploadField: React.FC<FileUploadFieldProps> = ({
  label,
  file,
  onFileChange,
  accept,
  required,
}) => {
  return (
    <div className="flex items-center">
      <label className="block text-sm font-medium text-gray-700">{label}</label>
      <input
        type="file"
        accept={accept}
        onChange={onFileChange}
        className="ml-2 py-2 px-3 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
        required={required}
      />
      {file && (
        <div className="ml-2">
          <img
            src={URL.createObjectURL(file)}
            alt="Thumbnail Preview"
            className="h-40 w-auto object-contain"
          />
        </div>
      )}
    </div>
  );
};

export default FileUploadField;
