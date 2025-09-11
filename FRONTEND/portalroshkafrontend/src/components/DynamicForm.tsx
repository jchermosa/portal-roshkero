import React, { useEffect, useState } from "react";

export interface FormField {
  name: string;
  label: string;
  type: "text" | "email" | "password" | "number" | "date" | "select" | "checkbox";
  required?: boolean;
  placeholder?: string;
  options?: Array<{ value: string | number; label: string }>;
  helperText?: string;
  disabled?: boolean;
  validation?: (value: any) => string | null;
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

    if (type === "checkbox") {
      processedValue = (e.target as HTMLInputElement).checked;
    } else if (type === "number") {
      // ✅ Mejorar el manejo de números
      processedValue = value === "" ? "" : Number(value);
    }

    setFormData((prev) => ({
      ...prev,
      [name]: processedValue,
    }));

    // ✅ Limpiar errores cuando el usuario empieza a escribir
    if (errors[name]) {
      setErrors((prev) => ({
        ...prev,
        [name]: "",
      }));
    }

    // ✅ Limpiar mensajes cuando el usuario hace cambios
    if (message) {
      setMessage(null);
    }
  };

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {};

    sections.forEach((section) => {
      section.fields.forEach((field) => {
        const value = formData[field.name];

        // ✅ Mejorar validación de campos requeridos
        if (field.required) {
          if (field.type === "checkbox") {
            // Para checkboxes, no validar como requerido ya que pueden ser false
            // Si necesitas que un checkbox sea obligatorio, usa validación personalizada
          } else if (value === undefined || value === "" || value === null) {
            newErrors[field.name] = `${field.label} es requerido`;
            return;
          }
        }

        // ✅ Solo validar si hay valor (no vacío)
        if (field.validation && value !== undefined && value !== "" && value !== null) {
          const validationError = field.validation(value);
          if (validationError) {
            newErrors[field.name] = validationError;
          }
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
      console.error('Submit error:', error);
      setMessage({ type: "error", text: error.message || "Error al guardar los datos" });
    } finally {
      setSubmitting(false);
    }
  };

  const renderField = (field: FormField) => {
    const value = formData[field.name] ?? "";
    const error = errors[field.name];
    const isFieldDisabled = field.disabled || loading;

    const baseProps = {
      name: field.name,
      value: field.type === "checkbox" ? undefined : value,
      onChange: handleChange,
      required: field.required,
      placeholder: field.placeholder,
      disabled: isFieldDisabled,
    };

    switch (field.type) {
      case "select":
        return (
          <div key={field.name} className="space-y-2">
            <label className="text-sm font-semibold text-gray-700">
              {field.label}
              {field.required && <span className="text-red-500 ml-1">*</span>}
            </label>
            <select
              {...baseProps}
              className={`w-full bg-white text-gray-800 border rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring focus:ring-blue-300 ${
                error ? 'border-red-500' : 'border-gray-300'
              } ${isFieldDisabled ? 'bg-gray-100 cursor-not-allowed' : ''}`}
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
            {error && <p className="text-red-500 text-xs">⚠️ {error}</p>}
          </div>
        );

      case "checkbox":
        return (
          <label
            key={field.name}
            className={`group flex items-center gap-3 p-3 bg-gray-50 rounded-xl border border-gray-200 ${
              isFieldDisabled ? 'opacity-60 cursor-not-allowed' : 'cursor-pointer hover:bg-gray-100'
            }`}
          >
            <input
              type="checkbox"
              name={field.name}
              checked={formData[field.name] || false}
              onChange={handleChange}
              disabled={isFieldDisabled}
              className="w-5 h-5 text-blue-600 border-gray-300 rounded focus:ring-blue-500"
            />
            <span className="text-gray-800 flex-1">{field.label}</span>
            {error && <span className="text-red-400 text-sm">⚠️ {error}</span>}
          </label>
        );

      default:
        return (
          <div key={field.name} className="space-y-2">
            <label className="text-sm font-semibold text-gray-700">
              {field.label}
              {field.required && <span className="text-red-500 ml-1">*</span>}
            </label>
            <input
              type={field.type}
              {...baseProps}
              className={`w-full bg-white text-gray-800 border rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring focus:ring-blue-300 ${
                error ? 'border-red-500' : 'border-gray-300'
              } ${isFieldDisabled ? 'bg-gray-100 cursor-not-allowed' : ''}`}
            />
            {field.helperText && (
              <p className="text-gray-500 text-xs">{field.helperText}</p>
            )}
            {error && <p className="text-red-500 text-xs">⚠️ {error}</p>}
          </div>
        );
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      className={`flex flex-col h-full ${className}`}
    >
      {/* Header */}
      <div className="p-6 border-b border-gray-200 flex items-center gap-4">
        <div className="w-12 h-12 bg-gray-100 rounded-xl flex items-center justify-center text-2xl">
          {headerIcon}
        </div>
        <div>
          <h1 className="text-2xl font-bold text-gray-800">{title}</h1>
          {subtitle && <p className="text-gray-500">{subtitle}</p>}
        </div>
        {loading && (
          <div className="ml-auto">
            <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-blue-600"></div>
          </div>
        )}
      </div>

      {/* Formulario scrollable */}
      <div className="flex-1 overflow-y-auto p-6 space-y-10">
        {message && (
          <div
            className={`p-4 rounded-lg text-sm border ${
              message.type === "error"
                ? "bg-red-50 text-red-700 border-red-200"
                : "bg-green-50 text-green-700 border-green-200"
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
              <div className="w-8 h-8 bg-gray-100 rounded-lg flex items-center justify-center">
                {section.icon}
              </div>
              <h2 className="text-gray-800 font-semibold text-lg">{section.title}</h2>
              <div className="flex-1 h-px bg-gray-200" />
            </div>
            <div
              className={`grid gap-4 ${section.className || "grid-cols-1 md:grid-cols-2"}`}
            >
              {section.fields.map(renderField)}
            </div>
          </div>
        ))}
      </div>

      {/* Footer con botones */}
      <div className="p-6 border-t border-gray-200 flex flex-col sm:flex-row gap-4">
        <button
          type="submit"
          disabled={submitting || loading}
          className={`flex-1 py-3 px-6 rounded-xl font-semibold transition-all duration-200 ${
            submitting || loading
              ? 'bg-gray-400 cursor-not-allowed text-white'
              : 'bg-gradient-to-r from-blue-600 to-blue-700 text-white hover:from-blue-700 hover:to-blue-800 hover:scale-105 active:scale-95'
          }`}
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
            className={`flex-1 py-3 px-6 rounded-xl transition-all duration-200 ${
              submitting
                ? 'bg-gray-200 text-gray-400 cursor-not-allowed border border-gray-200'
                : 'bg-white border border-gray-300 text-gray-700 hover:bg-gray-50 hover:scale-105 active:scale-95'
            }`}
          >
            ↩️ {cancelLabel}
          </button>
        )}
      </div>
    </form>
  );
};

export default DynamicForm;