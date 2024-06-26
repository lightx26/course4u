import React from "react";

interface InputFieldProps {
  label: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  type?: string;
  maxLength?: number;
  placeholder?: string;
  required?: boolean;
  editable?: boolean;
  onBlur?: () => void;
}

const InputField: React.FC<InputFieldProps> = ({
  label,
  value,
  onChange,
  type = "text",
  maxLength,
  onBlur,
  placeholder,
  required = false,
  editable = true,
}) => {
  return (
    <div>
      <label className="block text-sm font-medium text-gray-700">
        {label}
        {required && <span className="text-red-500">*</span>}
      </label>
      <input
        type={type}
        value={value || ""}
        onChange={onChange}
        className="mt-1 p-2 border border-gray-300 rounded w-full"
        maxLength={maxLength}
        placeholder={placeholder}
        disabled={!editable}
        onBlur={onBlur}
      />
    </div>
  );
};

export default InputField;
