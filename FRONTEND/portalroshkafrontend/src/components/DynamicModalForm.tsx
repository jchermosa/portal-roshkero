import React, { useState } from "react";
import DynamicForm from "./DynamicForm"; 
import type { FormSection } from "./DynamicForm";

export interface DynamicModalFormProps {
  id: string;
  title: string;
  sections: FormSection[];
  initialData: Record<string, any>;
  onSubmit: (data: Record<string, any>) => Promise<void>;
  onCancel: () => void;
  loading?: boolean;
  readonly?: boolean;
  submitLabel?: string;
  cancelLabel?: string;
  show?: boolean;
  children?: React.ReactNode; // 👈 soporte para contenido extra
}

const DynamicModalForm: React.FC<DynamicModalFormProps> = ({
  id,
  title,
  sections,
  initialData,
  onSubmit,
  onCancel,
  loading = false,
  readonly = false,
  submitLabel = "Guardar",
  cancelLabel = "Cancelar",
  show = true,
  children,
}) => {
  const [error, setError] = useState<string | null>(null);

  if (!show) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white dark:bg-gray-900 rounded-lg shadow-lg w-full max-w-lg p-6">
        <h2 className="text-xl font-bold mb-4 text-gray-900 dark:text-gray-100">
          {title}
        </h2>

        <DynamicForm
          id={id}
          sections={sections}
          initialData={initialData}
          onSubmit={async (formData) => {
            try {
              await onSubmit(formData);
            } catch (err: any) {
              setError(err.message || "Error al guardar los datos");
            }
          }}
          loading={loading}
          readonly={readonly}
        />

        {error && <p className="text-red-500 text-sm mt-2">{error}</p>}

        <div className="flex justify-between mt-4">
          <button
            form={id}
            type="submit"
            disabled={loading || readonly}
            className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
          >
            {loading ? "Procesando..." : submitLabel}
          </button>
          <button
            onClick={onCancel}
            className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600"
          >
            {cancelLabel}
          </button>
        </div>

        {/* 👇 botones extra (ej: aprobar/rechazar) */}
        {children}
      </div>
    </div>
  );
};

export default DynamicModalForm;
