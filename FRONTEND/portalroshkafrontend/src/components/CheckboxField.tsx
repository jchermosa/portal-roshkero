import React from "react";

interface CheckboxFieldProps {
  label: string;
  name: string;
  checked: boolean;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  helperText?: string;
  disabled?: boolean;
}

const CheckboxField: React.FC<CheckboxFieldProps> = ({
  label,
  name,
  checked,
  onChange,
  helperText,
  disabled = false,
}) => {
  return (
    <div className={`flex flex-col gap-1 ${disabled ? "opacity-60 cursor-not-allowed" : ""}`}>
      <label htmlFor={name} className="flex items-center gap-2 cursor-pointer select-none">
        <input
          id={name}
          name={name}
          type="checkbox"
          checked={checked}
          onChange={onChange}
          disabled={disabled}
          className="h-4 w-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500"
        />
        <span className="text-sm text-gray-700">{label}</span>
      </label>
      {helperText && (
        <p className="text-xs text-gray-500 pl-6">
          {helperText}
        </p>
      )}
    </div>
  );
};

export default CheckboxField;
