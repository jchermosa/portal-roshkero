import React from "react";

interface CheckboxFieldProps {
  label: string;
  name: string;
  checked: boolean;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  helperText?: string;
}

const CheckboxField: React.FC<CheckboxFieldProps> = ({
  label,
  name,
  checked,
  onChange,
  helperText,
}) => {
  return (
    <div className="flex items-start mb-4">
      <input
        id={name}
        name={name}
        type="checkbox"
        checked={checked}
        onChange={onChange}
        className="h-4 w-4 text-purple-600 border-gray-300 rounded focus:ring-purple-500"
      />
      <label htmlFor={name} className="ml-2 text-sm text-gray-700">
        {label}
      </label>
      {helperText && <p className="text-xs text-gray-500 mt-1">{helperText}</p>}
    </div>
  );
};

export default CheckboxField;
