// src/components/DynamicForm.tsx
import React, { useEffect, useState } from "react";

export interface FormField {
  name: string;
  label: string;
  type:
    | "text"
    | "email"
    | "password"
    | "number"
    | "date"
    | "select"
    | "checkbox"
    | "textarea"
    | "custom"
    | "slider"
    | "file";
  required?: boolean;
  placeholder?: string;
  options?: Array<{ value: string | number; label: string }>;
  helperText?: string;
  disabled?: boolean;
  validation?: (value: any) => string | null;
  value?: any;
  onChange?: (e: React.ChangeEvent<any>) => void;
  render?: () => React.ReactNode;
  className?: string;
  fullWidth?: boolean;
  min?: number;
  max?: number;
  step?: number;
  accept?: string;
  multiple?: boolean;
}

export interface FormSection {
  title: string;
  icon: string | React.ReactNode;
  fields: FormField[];
  className?: string;
}

export interface DynamicFormProps {
  id?: string;
  sections: FormSection[];
  initialData?: Record<string, any>;
  onSubmit: (data: Record<string, any>) => Promise<void>;
  onCancel?: () => void;
  onChange?: (data: Record<string, any>) => void;
  submitLabel?: string;
  cancelLabel?: string;
  loading?: boolean;
  className?: string;

  /** Errores externos (por ejemplo, venidos del backend o chequeos async). */
  externalErrors?: Record<string, string>;
  /** (Opcional) Se llama al escribir en un campo que tiene error externo para que el padre lo limpie. */
  onClearExternalError?: (name: string) => void;
}

export interface FormMessage {
  type: "success" | "error";
  text: string;
}

const DynamicForm: React.FC<DynamicFormProps> = ({
  id,
  sections,
  initialData = {},
  onSubmit,
  onChange,
  loading = false,
  className = "",
  externalErrors = {},
  onClearExternalError,
}) => {
  const [formData, setFormData] = useState<Record<string, any>>(initialData);
  const [message, setMessage] = useState<FormMessage | null>(null);
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    setFormData(initialData);
  }, [initialData]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>
  ) => {
    const { name, type, value } = e.target as HTMLInputElement;
    let processedValue: any = value;

    if (type === "checkbox")
      processedValue = (e.target as HTMLInputElement).checked;
    else if (type === "number")
      processedValue = value === "" ? "" : Number(value);

    setFormData((prev) => {
      const next = { ...prev, [name]: processedValue };
      onChange?.(next);
      return next;
    });

    if (errors[name]) setErrors((p) => ({ ...p, [name]: "" }));
    if (externalErrors[name]) onClearExternalError?.(name);
    if (message) setMessage(null);
  };

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {};
    sections.forEach((section) => {
      section.fields.forEach((field) => {
        const value = formData[field.name];
        if (field.required) {
          if (
            field.type !== "checkbox" &&
            (value === undefined || value === "" || value === null)
          ) {
            newErrors[field.name] = `${field.label} es requerido`;
            return;
          }
        }
        if (
          field.validation &&
          value !== undefined &&
          value !== "" &&
          value !== null
        ) {
          const validationError = field.validation(value);
          if (validationError) newErrors[field.name] = validationError;
        }
      });
    });
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage(null);
    if (!validateForm()) {
      setMessage({
        type: "error",
        text: "Por favor corrige los errores en el formulario",
      });
      return;
    }
    setSubmitting(true);
    try {
      await onSubmit(formData);
      setMessage({ type: "success", text: "Datos guardados correctamente" });
    } catch (error: any) {
      setMessage({ type: "error", text: error?.message || "Error al guardar los datos" });
    } finally {
      setSubmitting(false);
    }
  };

  const renderField = (field: FormField) => {
    const value = formData[field.name] ?? "";
    const error = errors[field.name];
    const isFieldDisabled = field.disabled || loading || readonly || submitting;

    const baseProps = {
      name: field.name,
      value: field.type === "checkbox" ? undefined : (formData[field.name] ?? ""),
      onChange: field.onChange ?? handleChange,
      required: field.required,
      placeholder: field.placeholder,
      disabled: isFieldDisabled,
    };

    switch (field.type) {
      case "select":
        return (
          <div key={field.name} className="space-y-2">
            <label className="text-sm font-semibold text-gray-700 dark:text-gray-300">
              {field.label}
              {field.required && <span className="text-red-500 ml-1">*</span>}
            </label>
            <select
              {...baseProps}
              className={`w-full rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500
                          bg-white text-gray-800 border
                          border-gray-300 dark:bg-gray-700 dark:text-gray-100 dark:border-gray-600
                          ${mergedError ? "border-red-500 focus:ring-red-500" : ""}
                          ${
                            isFieldDisabled
                              ? "bg-gray-100 dark:bg-gray-800 cursor-not-allowed"
                              : ""
                          }`}
            >
              <option value="" disabled>
                {field.placeholder ?? "Seleccionar..."}
              </option>
              {field.options?.map((opt) => (
                <option key={String(opt.value)} value={opt.value}>
                  {opt.label}
                </option>
              ))}
            </select>
            {mergedError && (
              <p className="text-red-500 dark:text-red-400 text-xs">
                ⚠️ {mergedError}
              </p>
            )}
          </div>
        );

      case "checkbox":
        return (
          <label
            key={field.name}
            className={`group flex items-center gap-3 p-3 rounded-xl border
                        bg-gray-50 border-gray-200 hover:bg-gray-100
                        dark:bg-gray-800 dark:border-gray-700 dark:hover:bg-gray-700
                        ${
                          isFieldDisabled
                            ? "opacity-60 cursor-not-allowed"
                            : "cursor-pointer"
                        }`}
          >
            <input
              type="checkbox"
              name={field.name}
              checked={!!formData[field.name]}
              onChange={handleChange}
              disabled={isFieldDisabled}
              className="w-5 h-5 text-blue-600 border-gray-300 rounded focus:ring-blue-500 dark:bg-gray-800 dark:border-gray-600"
            />
            <span className="text-gray-800 dark:text-gray-200 flex-1">
              {field.label}
            </span>
            {mergedError && (
              <span className="text-red-500 dark:text-red-400 text-sm">
                ⚠️ {mergedError}
              </span>
            )}
          </label>
        );

      case "custom":
        return field.render ? (
          <div
            key={field.name}
            className={`space-y-2 ${field.fullWidth ? "w-full col-span-full" : ""}`}
          >
            {field.render()}
          </div>
        ) : null;

      case "slider":
        return (
          <div
            key={field.name}
            className={`space-y-2 ${field.fullWidth ? "w-full col-span-full" : ""}`}
          >
            <label className="text-sm font-semibold text-gray-700 dark:text-gray-300">
              {field.label}
              {field.required && <span className="text-red-500 ml-1">*</span>}
            </label>
            <input
              type="range"
              name={field.name}
              min={field.min ?? 0}
              max={field.max ?? 100}
              step={field.step ?? 1}
              value={formData[field.name] ?? field.min ?? 0}
              onInput={(e) => {
                const target = e.target as HTMLInputElement;
                handleChange({
                  ...e,
                  target: {
                    ...target,
                    name: field.name,
                    value: target.value,
                    type: "number",
                  },
                } as React.ChangeEvent<HTMLInputElement>);
              }}
              disabled={isFieldDisabled}
              className="w-full accent-blue-600"
            />
            <span className="text-sm font-medium text-gray-700 dark:text-gray-300">
              {formData[field.name] ?? 0}%
            </span>
            {error && <p className="text-red-500 dark:text-red-400 text-xs">⚠️ {error}</p>}
          </div>
        );

      case "file":
        return (
          <div key={field.name} className="space-y-2">
            <label className="text-sm font-semibold text-gray-700 dark:text-gray-300">
              {field.label}
              {field.required && <span className="text-red-500 ml-1">*</span>}
            </label>
            <input
              type="file"
              name={field.name}
              accept={field.accept}
              multiple={field.multiple}
              disabled={isFieldDisabled}
              onChange={(e) => {
                const files = e.target.files;
                let newValue: File | File[] | null = null;
                if (files) {
                  newValue = field.multiple ? Array.from(files) : files[0];
                }
                setFormData((prev) => {
                  const next = { ...prev, [field.name]: newValue };
                  onChange?.(next);
                  return next;
                });
              }}
              className={`w-full text-sm text-gray-700 dark:text-gray-200
                          file:mr-4 file:py-2 file:px-4
                          file:rounded-full file:border-0
                          file:text-sm file:font-semibold
                          file:bg-blue-50 file:text-blue-700
                          hover:file:bg-blue-100
                          ${isFieldDisabled ? "cursor-not-allowed opacity-60" : ""}`}
            />
            {error && <p className="text-red-500 dark:text-red-400 text-xs">⚠️ {error}</p>}
          </div>
        );

      case "textarea":
        return (
          <div key={field.name} className={`space-y-2 ${field.fullWidth ? "w-full col-span-full" : ""}`}>
            <label className="text-sm font-semibold text-gray-700 dark:text-gray-300">
              {field.label}
              {field.required && <span className="text-red-500 ml-1">*</span>}
            </label>
            <textarea
              name={field.name}
              value={String(value)}
              onChange={handleChange}
              placeholder={field.placeholder}
              disabled={isFieldDisabled}
              className={`w-full rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500
                          bg-white text-gray-800 border
                          border-gray-300 dark:bg-gray-700 dark:text-gray-100 dark:border-gray-600
                          ${error ? "border-red-500 focus:ring-red-500" : ""}
                          ${isFieldDisabled ? "bg-gray-100 dark:bg-gray-800 cursor-not-allowed" : ""}`}
              rows={4}
            />
            {field.helperText && (
              <p className="text-gray-500 dark:text-gray-400 text-xs">{field.helperText}</p>
            )}
            {error && <p className="text-red-500 dark:text-red-400 text-xs">⚠️ {error}</p>}
          </div>
        );

      default:
        return (
          <div
            key={field.name}
            className={`space-y-2 ${field.fullWidth ? "w-full col-span-full" : ""}`}
          >
            <label className="text-sm font-semibold text-gray-700 dark:text-gray-300">
              {field.label}
              {field.required && <span className="text-red-500 ml-1">*</span>}
            </label>
            <input
              type={field.type}
              {...baseProps}
              className={`w-full rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500
                          bg-white text-gray-800 border
                          border-gray-300 dark:bg-gray-700 dark:text-gray-100 dark:border-gray-600
                          ${error ? "border-red-500 focus:ring-red-500" : ""}
                          ${isFieldDisabled ? "bg-gray-100 dark:bg-gray-800 cursor-not-allowed" : ""}`}
            />
            {field.helperText && (
              <p className="text-gray-500 dark:text-gray-400 text-xs">{field.helperText}</p>
            )}
            {error && <p className="text-red-500 dark:text-red-400 text-xs">⚠️ {error}</p>}
          </div>
        );
    }
  };

  return (
    <form
      id={id ?? "dynamic-form"}
      onSubmit={handleSubmit}
      className={`flex flex-col h-full ${className}`}
    >
      {message && (
        <div
          className={`p-4 rounded-lg text-sm border ${
            message.type === "error"
              ? "bg-red-50 text-red-700 border-red-200 dark:bg-red-900/30 dark:text-red-300 dark:border-red-700"
              : "bg-green-50 text-green-700 border-green-200 dark:bg-green-900/30 dark:text-green-300 dark:border-green-700"
          }`}
        >
          <div className="flex items-center gap-2">
            {message.type === "error" ? "❌" : "✅"}
            <span>{message.text}</span>
          </div>
        </div>
      )}

      <div className="flex-1 overflow-y-auto p-6 space-y-10">
        {sections.map((section, idx) => (
          <div key={idx} className="space-y-4">
            <div className="flex items-center gap-3">
              <div className="w-8 h-8 bg-gray-100 dark:bg-gray-800 rounded-lg flex items-center justify-center">
                {section.icon}
              </div>
              <h2 className="text-gray-800 dark:text-gray-100 font-semibold text-lg">
                {section.title}
              </h2>
              <div className="flex-1 h-px bg-gray-200 dark:bg-gray-700" />
            </div>
            <div
              className={`grid gap-4 ${
                section.className || "grid-cols-1 md:grid-cols-2"
              }`}
            >
              {section.fields.map((field, fieldIndex) => (
                <React.Fragment
                  key={`${section.title}-${field.name}-${fieldIndex}`}
                >
                  {renderField(field)}
                </React.Fragment>
              ))}
            </div>
          </div>
        ))}
      </div>
    </form>
  );
};

export default DynamicForm;
