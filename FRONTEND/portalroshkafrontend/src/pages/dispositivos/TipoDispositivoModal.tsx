import DynamicForm from "../../components/DynamicForm";
import { buildTipoDispositivoSections } from "../../config/forms/tipoDispositivoFormFields";
import { useTipoDispositivoForm } from "../../hooks/dispositivos/useTipoDispositivoForm";

interface Props {
  token: string;
  id?: string; // si existe → edición
  onClose: () => void;
  onSaved: () => void;
}

export default function TipoDispositivoModal({ token, id, onClose, onSaved }: Props) {
  const { data, handleSubmit, loading, error, isEditing } = useTipoDispositivoForm(token, id);
  const sections = buildTipoDispositivoSections();

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black/50 z-50">
      <div className="bg-white dark:bg-gray-900 rounded-lg shadow-xl p-6 w-full max-w-md">
        <h2 className="text-lg font-semibold mb-4 text-gray-800 dark:text-gray-100">
          {isEditing ? "Editar Tipo de Dispositivo" : "Nuevo Tipo de Dispositivo"}
        </h2>

        <DynamicForm
          sections={sections}
          initialData={data}
          loading={loading}
          onSubmit={async (formData) => {
            await handleSubmit(formData);
            onSaved();
            onClose();
          }}
        />

        {error && <p className="text-red-500 text-sm mt-2">{error}</p>}

        <div className="mt-4 flex justify-end gap-2">
          <button
            onClick={onClose}
            className="px-4 py-2 rounded bg-gray-200 dark:bg-gray-700 text-gray-800 dark:text-gray-200 hover:bg-gray-300 dark:hover:bg-gray-600"
          >
            Cancelar
          </button>
        </div>
      </div>
    </div>
  );
}
