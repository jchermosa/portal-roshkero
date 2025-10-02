import React, { useEffect, useState } from "react";

type Option = { value: string | number; label: string };
type Field = {
  name: string;
  label: string;
  type: "text" | "email" | "password" | "number" | "date" | "select" | "checkbox" | "textarea" | "custom" | "slider" | "file";
  required?: boolean;
  placeholder?: string;
  options?: Option[];
  helperText?: string;
  disabled?: boolean;
  validation?: (value: any) => string | null;
  onChange?: (e: React.ChangeEvent<any>) => void;
  render?: () => React.ReactNode;
  className?: string;
  fullWidth?: boolean;
  min?: number;
  max?: number;
  step?: number;
  accept?: string;
  multiple?: boolean;
};

type Section = {
  title: string;
  icon: string | React.ReactNode;
  fields: Field[];
  className?: string;
};

type Props = {
  id?: string;
  sections: Section[];
  initialData?: Record<string, any>;
  onSubmit: (data: Record<string, any>) => Promise<void>;
  onChange?: (data: Record<string, any>) => void;
  loading?: boolean;
  className?: string;
  readonly?: boolean;
};

const DynamicModalForm: React.FC<Props> = ({
  id,
  sections,
  initialData = {},
  onSubmit,
  onChange,
  loading = false,
  readonly = false,
  className = "",
}) => {
  const [formData, setFormData] = useState<Record<string, any>>(initialData);
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    setFormData(initialData);
    setErrors({});
  }, [initialData]);

  const hasData = initialData && Object.keys(initialData).length > 0;
  const isDisabled = (f?: Field) =>
    !!(f?.disabled || readonly || submitting );

  const setField = (name: string, value: any) => {
    setFormData((prev) => {
      const next = { ...prev, [name]: value };
      onChange?.(next);
      return next;
    });
    if (errors[name]) setErrors((p) => ({ ...p, [name]: "" }));
  };

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    const t = e.currentTarget as HTMLInputElement;
    const { name, type, value } = t;
    if (type === "checkbox") setField(name, t.checked);
    else if (type === "number") setField(name, value === "" ? "" : Number(value));
    else setField(name, value);
  };

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {};
    sections.forEach((section) => {
      section.fields.forEach((field) => {
        const v = formData[field.name];
        if (field.required && field.type !== "checkbox") {
          if (v === undefined || v === "" || v === null) {
            newErrors[field.name] = `${field.label} es requerido`;
          }
        }
        if (field.validation && v !== undefined && v !== "" && v !== null) {
          const msg = field.validation(v);
          if (msg) newErrors[field.name] = msg;
        }
      });
    });
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validateForm()) return;
    setSubmitting(true);
    try {
      await onSubmit(formData);
    } finally {
      setSubmitting(false);
    }
  };

  const renderField = (field: Field) => {
    const value = formData[field.name];
    const error = errors[field.name];

    if (field.type === "select") {
      const numeric = typeof field.options?.[0]?.value === "number";
      return (
        <div key={field.name} className="space-y-2">
          <label className="text-sm font-semibold text-gray-700 dark:text-gray-300">
            {field.label}
            {field.required && <span className="text-red-500 ml-1">*</span>}
          </label>
          <select
            name={field.name}
            value={value != null ? String(value) : ""}
            onChange={(e) => {
              const v = e.currentTarget.value;
              setField(field.name, v === "" ? "" : numeric ? Number(v) : v);
              field.onChange?.(e);
            }}
            disabled={isDisabled(field)}
            className={`w-full rounded-lg px-3 py-2 text-sm border focus:outline-none
                        ${error ? "border-red-500" : "border-gray-300"}
                        ${isDisabled(field) ? "bg-gray-100 dark:bg-gray-800 cursor-not-allowed" : ""}`}
          >
            <option value="" disabled>{field.placeholder ?? "Seleccionar..."}</option>
            {field.options?.map((opt) => (
              <option key={String(opt.value)} value={String(opt.value)}>
                {opt.label}
              </option>
            ))}
          </select>
          {error && <p className="text-red-500 text-xs">⚠️ {error}</p>}
        </div>
      );
    }

    if (field.type === "checkbox") {
      return (
        <label key={field.name} className={`flex items-center gap-3 p-3 rounded-xl border
                    ${isDisabled(field) ? "opacity-60 cursor-not-allowed" : "cursor-pointer"}`}>
          <input
            type="checkbox"
            name={field.name}
            checked={!!value}
            onChange={(e) => {
              setField(field.name, e.currentTarget.checked);
              field.onChange?.(e);
            }}
            disabled={isDisabled(field)}
          />
          <span>{field.label}</span>
          {error && <span className="text-red-500 text-sm">⚠️ {error}</span>}
        </label>
      );
    }

    if (field.type === "textarea") {
      return (
        <div key={field.name} className="space-y-2">
          <label className="text-sm font-semibold">{field.label}{field.required && <span className="text-red-500 ml-1">*</span>}</label>
          <textarea
            name={field.name}
            value={value ?? ""}
            onChange={(e) => { setField(field.name, e.currentTarget.value); field.onChange?.(e); }}
            placeholder={field.placeholder}
            disabled={isDisabled(field)}
            rows={4}
            className={`w-full rounded-lg px-3 py-2 text-sm border ${error ? "border-red-500" : "border-gray-300"}`}
          />
          {field.helperText && <p className="text-gray-500 text-xs">{field.helperText}</p>}
          {error && <p className="text-red-500 text-xs">⚠️ {error}</p>}
        </div>
      );
    }

    // text, email, password, number, date, file, slider (sin soporte especial para file/slider en esta versión mínima)
    return (
      <div key={field.name} className={`space-y-2 ${field.fullWidth ? "w-full col-span-full" : ""}`}>
        <label className="text-sm font-semibold">{field.label}{field.required && <span className="text-red-500 ml-1">*</span>}</label>
        <input
          type={field.type}
          name={field.name}
          value={field.type === "number" ? (value ?? "") : (value ?? "")}
          onChange={handleInputChange}
          placeholder={field.placeholder}
          disabled={isDisabled(field)}
          className={`w-full rounded-lg px-3 py-2 text-sm border ${error ? "border-red-500" : "border-gray-300"}`}
        />
        {field.helperText && <p className="text-gray-500 text-xs">{field.helperText}</p>}
        {error && <p className="text-red-500 text-xs">⚠️ {error}</p>}
      </div>
    );
  };

  return (
    <form id={id ?? "dynamic-modal-form"} onSubmit={handleSubmit} className={`flex flex-col h-full ${className}`}>
      <div className="flex-1 overflow-y-auto p-2 md:p-4 space-y-6">
        {sections.map((section, idx) => (
          <div key={idx} className="space-y-4">
            <div className="flex items-center gap-3">
              <div className="w-7 h-7 bg-gray-100 rounded-lg flex items-center justify-center">
                {section.icon}
              </div>
              <h2 className="font-semibold text-base md:text-lg">{section.title}</h2>
              <div className="flex-1 h-px bg-gray-200" />
            </div>
            <div className={`grid gap-3 ${section.className || "grid-cols-1"}`}>
              {section.fields.map((f) => renderField(f))}
            </div>
          </div>
        ))}
      </div>
    </form>
  );
};

export default DynamicModalForm;
