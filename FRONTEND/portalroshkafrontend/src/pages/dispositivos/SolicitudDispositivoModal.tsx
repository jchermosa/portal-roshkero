import DynamicForm from "../../components/DynamicForm";
import { useSolicitudDispositivoForm } from "../../hooks/deviceRequest/useSolicitudDispositivoForm";
import { useGetDeviceTypes } from "../../hooks/dispositivos/useGetDeviceTypes";
import { buildSolicitudDispositivoSections } from "../../config/forms/solicitudDispositivoFormFields";

interface Props {
  token: string | null;
  id?: number | string;
  readonly?: boolean;
  gestion?: boolean;
  onClose: () => void;
  onSaved?: () => void;
}

export default function SolicitudDispositivoModal({
  token,
  id,
  readonly = false,
  gestion = false,
  onClose,
  onSaved,
}: Props) {
  const {
    data,
    setData,
    create,
    accept,
    reject,
    loading,
    error,
    isEditing,
  } = useSolicitudDispositivoForm(
    token,
    typeof id === "string" ? parseInt(id) : id,
    gestion 
  );

  // Obtener tipos de dispositivos desde la API
  const { data: deviceTypes, loading: loadingTypes } = useGetDeviceTypes(token);

  // Transformar tipos de dispositivos para el formulario
  const tipoDispositivoOptions = deviceTypes.map((tipo) => ({
    value: tipo.idTipoDispositivo,
    label: tipo.nombre,
  }));

  const sections = buildSolicitudDispositivoSections(tipoDispositivoOptions);

  const handleAprobar = async () => {
    if (!id) return;
    try {
      await accept();
      if (onSaved) onSaved();
      onClose();
    } catch {
      // El error ya se maneja en el hook
    }
  };

  const handleRechazar = async () => {
    if (!id) return;
    try {
      await reject();
      if (onSaved) onSaved();
      onClose();
    } catch {
      // El error ya se maneja en el hook
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white dark:bg-gray-900 rounded-lg shadow-lg w-full max-w-lg p-6">
        <h2 className="text-xl font-bold mb-4 text-gray-900 dark:text-gray-100">
          {isEditing
            ? readonly
              ? "Detalle de solicitud de dispositivo"
              : gestion
              ? "Gestionar solicitud de dispositivo"
              : "Editar solicitud de dispositivo"
            : "Nueva solicitud de dispositivo"}
        </h2>

        <DynamicForm
          id="solicitud-dispositivo-form"
          sections={sections}
          initialData={data ?? {}}   
          onSubmit={async (formData) => {
            if (!readonly && !gestion) {
              try {
                await create(formData as any); 
                if (onSaved) onSaved();
                onClose();
              } catch {
                // error ya se maneja en el hook
              }
            }
          }}
          onChange={(formData) => setData(formData as any)} 
          loading={loading || loadingTypes}
          readonly={readonly || gestion}
        />

        {error && <p className="text-red-500 text-sm mt-2">{error}</p>}

        <div className="flex justify-between mt-4">
          {gestion && !readonly ? (
            <div className="flex gap-2 ml-auto">
              <button
                onClick={handleRechazar}
                disabled={loading}
                className="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 disabled:opacity-50"
              >
                {loading ? "Procesando..." : "Rechazar"}
              </button>
              <button
                onClick={handleAprobar}
                disabled={loading}
                className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 disabled:opacity-50"
              >
                {loading ? "Procesando..." : "Aprobar"}
              </button>
            </div>
          ) : (
            <div className="flex gap-2 ml-auto">
              <button
                form="solicitud-dispositivo-form"
                type="submit"
                disabled={loading || readonly}
                className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
              >
                {loading
                  ? "Procesando..."
                  : isEditing
                  ? "Guardar cambios"
                  : "Crear"}
              </button>
              <button
                onClick={onClose}
                className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600"
              >
                Cerrar
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
