import React, { useEffect, useState } from "react";

export interface FormField {
  name: string;
  label: string;
  type: "text" | "email" | "password" | "number" | "date" | "select" | "checkbox" | "textarea"| "custom";
  required?: boolean;
  placeholder?: string;
  options?: Array<{ value: string | number; label: string }>;
  helperText?: string;
  disabled?: boolean;
  validation?: (value: any) => string | null;
  //Para campo de request
  value?: any;
  onChange?: (e: React.ChangeEvent<any>) => void;
  render?: () => React.ReactNode;
  className?: string;
}


export interface FormSection {
  title: string;
  icon: string | React.ReactNode;
  fields: FormField[];
  className?: string;
}

export interface DynamicFormProps {
  title: string;
  subtitle?: string;
  headerIcon: string | React.ReactNode;
  sections: FormSection[];
  initialData?: Record<string, any>;
  onSubmit: (data: Record<string, any>) => Promise<void>;
  onCancel?: () => void;
  onChange?: (data: Record<string, any>) => void;
  submitLabel?: string;
  cancelLabel?: string;
  loading?: boolean;
  className?: string;
}

export interface FormMessage {
  type: "success" | "error";
  text: string;
}

const DynamicForm: React.FC<DynamicFormProps> = ({
  title,
  subtitle,
  headerIcon,
  sections,
  initialData = {},
  onSubmit,
  onCancel,
  onChange, // <-- incluido
  submitLabel = "Guardar cambios",
  cancelLabel = "Cancelar",
  loading = false,
  className = "",
}) => {
  const [formData, setFormData] = useState<Record<string, any>>(initialData);
  const [message, setMessage] = useState<FormMessage | null>(null);
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    setFormData(initialData);
  }, [initialData]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, type, value } = e.target;
    let processedValue: any = value;

    if (type === "checkbox") processedValue = (e.target as HTMLInputElement).checked;
    else if (type === "number") processedValue = value === "" ? "" : Number(value);

    setFormData((prev) => {
      const next = { ...prev, [name]: processedValue };
      if (onChange) onChange(next); // <-- notifico al padre con el nuevo estado
      return next;
    });

    if (errors[name]) setErrors((p) => ({ ...p, [name]: "" }));
    if (message) setMessage(null);
  };

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {};
    sections.forEach((section) => {
      section.fields.forEach((field) => {
        const value = formData[field.name];
        if (field.required) {
          if (field.type !== "checkbox" && (value === undefined || value === "" || value === null)) {
            newErrors[field.name] = `${field.label} es requerido`;
            return;
          }
        }
        if (field.validation && value !== undefined && value !== "" && value !== null) {
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
      setMessage({ type: "error", text: "Por favor corrige los errores en el formulario" });
      return;
    }
    setSubmitting(true);
    try {
      await onSubmit(formData);
      setMessage({ type: "success", text: "Datos guardados correctamente" });
    } catch (error: any) {
      setMessage({ type: "error", text: error.message || "Error al guardar los datos" });
    } finally {
      setSubmitting(false);
    }
  };

  const renderField = (field: FormField) => {
    const value = formData[field.name] ?? "";
    const error = errors[field.name];
    const isFieldDisabled = field.disabled || loading;

    //Cambios para que funione cantidad_dias en RequestFormPage
    const baseProps = {
      name: field.name,
      value: field.value !== undefined ? field.value : field.type === "checkbox" ? undefined : value,
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
                          ${error ? "border-red-500 focus:ring-red-500" : ""}
                          ${isFieldDisabled ? "bg-gray-100 dark:bg-gray-800 cursor-not-allowed" : ""}`}
            >
              <option value="" disabled>
                {field.placeholder ?? "Seleccionar..."}
              </option>
              {field.options?.map((opt) => (
                <option key={opt.value} value={opt.value}>
                  {opt.label}
                </option>
              ))}
            </select>
            {error && <p className="text-red-500 dark:text-red-400 text-xs">⚠️ {error}</p>}
          </div>
        );

      case "checkbox":
        return (
          <label
            key={field.name}
            className={`group flex items-center gap-3 p-3 rounded-xl border
                        bg-gray-50 border-gray-200 hover:bg-gray-100
                        dark:bg-gray-800 dark:border-gray-700 dark:hover:bg-gray-700
                        ${isFieldDisabled ? "opacity-60 cursor-not-allowed" : "cursor-pointer"}`}
          >
            <input
              type="checkbox"
              name={field.name}
              checked={formData[field.name] || false}
              onChange={handleChange}
              disabled={isFieldDisabled}
              className="w-5 h-5 text-blue-600 border-gray-300 rounded focus:ring-blue-500 dark:bg-gray-800 dark:border-gray-600"
            />
            <span className="text-gray-800 dark:text-gray-200 flex-1">{field.label}</span>
            {error && <span className="text-red-500 dark:text-red-400 text-sm">⚠️ {error}</span>}
          </label>
        );

      default:
        if (field.type === "custom" && field.render) {
        return (
          <div key={field.name} className="mb-4">
            {field.render()}
          </div>
        );
      }
        return (
          <div key={field.name} className="space-y-2">
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
    <form onSubmit={handleSubmit} className={`flex flex-col h-full ${className}`}>
      {/* Header */}
      <div className="p-6 border-b border-gray-200 dark:border-gray-700 flex items-center gap-4">
        <div className="w-12 h-12 bg-gray-100 dark:bg-gray-800 rounded-xl flex items-center justify-center text-2xl">
          {headerIcon}
        </div>
        <div>
          <h1 className="text-2xl font-bold text-gray-800 dark:text-gray-100">{title}</h1>
          {subtitle && <p className="text-gray-500 dark:text-gray-400">{subtitle}</p>}
        </div>
        {loading && (
          <div className="ml-auto">
            <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-blue-600"></div>
          </div>
        )}
      </div>

      {/* Body */}
      <div className="flex-1 overflow-y-auto p-6 space-y-10">
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
            <div className={`grid gap-4 ${section.className || "grid-cols-1 md:grid-cols-2"}`}>
              {section.fields.map(renderField)}
            </div>
          </div>
        ))}
      </div>

      {/* Footer */}
      <div className="p-6 border-t border-gray-200 dark:border-gray-700 flex flex-col sm:flex-row gap-4">
        <button
          type="submit"
          disabled={submitting || loading}
          className={`flex-1 py-3 px-6 rounded-xl font-semibold transition-all duration-200
            ${submitting || loading
              ? "bg-gray-400 dark:bg-gray-600 cursor-not-allowed text-white"
              : "bg-gradient-to-r from-blue-600 to-blue-700 text-white hover:from-blue-700 hover:to-blue-800 hover:scale-105 active:scale-95"}`}
        >
          {submitting ? (
            <span className="flex items-center justify-center gap-2">
              <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
              Guardando...
            </span>
          ) : (
            `💾 ${submitLabel}`
          )}
        </button>

        {onCancel && (
          <button
            type="button"
            onClick={onCancel}
            disabled={submitting}
            className={`flex-1 py-3 px-6 rounded-xl transition-all duration-200
              ${submitting
                ? "bg-gray-200 dark:bg-gray-800 text-gray-400 cursor-not-allowed border border-gray-200 dark:border-gray-700"
                : "bg-white border border-gray-300 text-gray-700 hover:bg-gray-50 dark:bg-gray-800 dark:text-gray-100 dark:border-gray-700 dark:hover:bg-gray-700 hover:scale-105 active:scale-95"}`}
          >
            ↩️ {cancelLabel}
          </button>
        )}
      </div>
    </form>
  );
};

export default DynamicForm;
