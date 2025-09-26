import DynamicForm from "../../components/DynamicForm";
import { useTipoDispositivoForm } from "../../hooks/dispositivos/useTipoDispositivoForm";
import { buildTipoDispositivoSections } from "../../config/forms/tipoDispositivoFormFields";

interface Props {
  token: string | null;
  id?: number | string;
  onClose: () => void;
  onSaved?: () => void;
}

export default function TipoDispositivoModal({ token, id, onClose, onSaved }: Props) {
  const { data, setData, handleSubmit, handleDelete, loading, error, isEditing } =
    useTipoDispositivoForm(token, id);

  const sections = buildTipoDispositivoSections();

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white dark:bg-gray-900 rounded-lg shadow-lg w-full max-w-lg p-6">
        <h2 className="text-xl font-bold mb-4 text-gray-900 dark:text-gray-100">
          {isEditing ? "Editar tipo de dispositivo" : "Nuevo tipo de dispositivo"}
        </h2>

        <DynamicForm
          id="tipo-dispositivo-form"
          sections={sections}
          initialData={data}
          onSubmit={async (formData) => {
            await handleSubmit(formData);
            if (onSaved) onSaved();
            onClose();
          }}
          onChange={setData}
          loading={loading}
        />

        {error && <p className="text-red-500 text-sm mt-2">{error}</p>}

        <div className="flex justify-between mt-4">
          {isEditing && (
            <button
              onClick={async () => {
                await handleDelete();
                if (onSaved) onSaved();
                onClose();
              }}
              className="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700"
            >
              Eliminar
            </button>
          )}
          <div className="flex gap-2 ml-auto">
            <button
              form="tipo-dispositivo-form"
              type="submit"
              disabled={loading}
              className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
            >
              {isEditing ? "Guardar cambios" : "Crear"}
            </button>
            <button
              onClick={onClose}
              className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600"
            >
              Cerrar
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
