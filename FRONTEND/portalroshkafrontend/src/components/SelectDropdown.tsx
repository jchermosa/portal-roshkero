import React from "react";

export interface Option {
  value: string | number;
  label: string;
}

interface SelectDropdownProps {
  label: string;
  name: string;
  value: string | number;
  onChange: (e: React.ChangeEvent<HTMLSelectElement>) => void;
  options: Option[];
  required?: boolean;
  placeholder?: string;
}

const SelectDropdown: React.FC<SelectDropdownProps> = ({
  label,
  name,
  value,
  onChange,
  options,
  required = false,
  placeholder,
}) => {
  return (
    <div className="mb-4">
      <label
        htmlFor={name}
        className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1"
      >
        {label} {required && <span className="text-red-500">*</span>}
      </label>
      <select
        id={name}
        name={name}
        value={value}
        onChange={onChange}
        required={required}
        className="w-full px-3 py-2 border rounded-lg 
                   focus:ring-2 focus:ring-purple-500 focus:outline-none
                   bg-white text-gray-900 border-gray-300
                   dark:bg-gray-700 dark:text-gray-100 dark:border-gray-600"
      >
        {placeholder && <option value="">{placeholder}</option>}
        {options.map((opt) => (
          <option key={opt.value} value={opt.value}>
            {opt.label}
          </option>
        ))}
      </select>
    </div>
  );
};

export default SelectDropdown;
