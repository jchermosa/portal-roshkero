import React from "react";

interface InputTextFieldProps {
  label: string;
  name: string;
  value: string | number;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  type?: "text" | "email" | "password" | "number";
  required?: boolean;
  placeholder?: string;
  disabled?: boolean;
  helperText?: string;
}

const InputTextField: React.FC<InputTextFieldProps> = ({
  label,
  name,
  value,
  onChange,
  type = "text",
  required = false,
  placeholder = "",
  disabled = false,
  helperText,
}) => {
  return (
    <div className="mb-4">
      <label
        htmlFor={name}
        className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1"
      >
        {label} {required && <span className="text-red-500">*</span>}
      </label>
      <input
        id={name}
        name={name}
        type={type}
        value={value}
        onChange={onChange}
        required={required}
        placeholder={placeholder}
        disabled={disabled}
        className="w-full px-3 py-2 border rounded-lg 
                   focus:ring-2 focus:ring-purple-500 focus:outline-none
                   bg-white text-gray-900 border-gray-300
                   dark:bg-gray-700 dark:text-gray-100 dark:border-gray-600
                   disabled:bg-gray-100 dark:disabled:bg-gray-800"
      />
      {helperText && (
        <p className="text-xs text-gray-500 dark:text-gray-400 mt-1">
          {helperText}
        </p>
      )}
    </div>
  );
};

export default InputTextField;
