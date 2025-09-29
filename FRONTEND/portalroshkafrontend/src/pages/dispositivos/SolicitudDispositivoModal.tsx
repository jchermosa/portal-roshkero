import { useSolicitudDispositivoForm } from "../../hooks/deviceRequest/useSolicitudDispositivoForm";
import { useGetDeviceTypes } from "../../hooks/dispositivos/useGetDeviceTypes";
import { buildSolicitudDispositivoSections } from "../../config/forms/solicitudDispositivoFormFields";
import BaseModal from "../../components/BaseModal";
import DynamicForm from "../../components/DynamicForm";
import { mapFormToUserSolicitudDto } from "../../mappers/solicitudDispositivoMapper";
import { useNavigate } from "react-router-dom";
import { Alert } from "../../components/Alert";

interface SolicitudDispositivoModalProps {
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
}: SolicitudDispositivoModalProps) {
  const navigate = useNavigate(); 

  const { data, create, accept, reject, loading, error, isEditing } =
    useSolicitudDispositivoForm(
      token,
      typeof id === "string" ? parseInt(id) : id,
      gestion
    );

  const { data: deviceTypes, loading: loadingTypes } = useGetDeviceTypes(token);

  const tipoDispositivoOptions = Array.isArray(deviceTypes)
    ? deviceTypes.map((tipo) => ({
        value: tipo.idTipoDispositivo,
        label: tipo.nombre,
      }))
    : [];

  const sections = buildSolicitudDispositivoSections(tipoDispositivoOptions);

  // Handlers de gestión
  const handleAprobar = async () => {
    const solicitudId =
      typeof id === "string" ? parseInt(id, 10) : (id as number | undefined);

    if (!solicitudId) return;
    try {
      await accept();
      onSaved?.();
      navigate(`/dispositivos-asignados/nuevo?solicitudId=${solicitudId}`);
      onClose();
    } catch {
    }
  };

  const handleRechazar = async () => {
    if (!id) return;
    try {
      await reject();
      onSaved?.();
      onClose();
    } catch {}
  };

  return (
    <BaseModal show onClose={onClose}>
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
            const dto = mapFormToUserSolicitudDto(formData);
            await create(dto);
            onSaved?.();
            onClose();
          }
        }}
        loading={loading || loadingTypes}
        readonly={readonly || gestion}
      />

     {error && <Alert kind="error">{error}</Alert>}

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
              disabled={loading || !id}
              className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 disabled:opacity-50"
            >
              {loading ? "Procesando..." : "Aprobar y asignar"}
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
    </BaseModal>
  );
}
