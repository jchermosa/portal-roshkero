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
        <h2 className="text-xl font-bold mb-4">
          {isEditing ? "Editar tipo de dispositivo" : "Nuevo tipo de dispositivo"}
        </h2>

        <DynamicForm
          sections={sections}
          initialData={data}
          onSubmit={async (formData) => {
            await handleSubmit(formData);
            if (onSaved) onSaved();
            onClose();
          }}
          onChange={setData}
          submitLabel={isEditing ? "Guardar cambios" : "Crear"}
          cancelLabel="Cancelar"
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
          <button
            onClick={onClose}
            className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 ml-auto"
          >
            Cerrar
          </button>
        </div>
      </div>
    </div>
  );
}
