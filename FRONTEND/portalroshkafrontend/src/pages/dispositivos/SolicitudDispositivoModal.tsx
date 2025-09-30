import { useState, useMemo } from "react";
import { useNavigate } from "react-router-dom";

import BaseModal from "../../components/BaseModal";
import DynamicModalForm from "../../components/DynamicModalForm";
import { Alert } from "../../components/Alert";

import { useSolicitudDispositivoForm } from "../../hooks/deviceRequest/useSolicitudDispositivoForm";
import { useGetDeviceTypes } from "../../hooks/dispositivos/useGetDeviceTypes";
import { buildSolicitudDispositivoSections } from "../../config/forms/solicitudDispositivoFormFields";
import { mapFormToUserSolicitudDto } from "../../mappers/solicitudDispositivoMapper";

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

  // Hook principal
  const {
    data,            // detalle de la solicitud (o {} si es nueva)
    create,
    accept,
    reject,
    loading,         // carga del detalle / acción interna de hook si la tuviera
    error,
    isEditing,
  } = useSolicitudDispositivoForm(
    token,
    typeof id === "string" ? parseInt(id, 10) : id,
    gestion
  );

  // Catálogo de tipos de dispositivo
  const { data: deviceTypes, loading: loadingTypes } = useGetDeviceTypes(token);

  const tipoDispositivoOptions = Array.isArray(deviceTypes)
    ? deviceTypes.map((t) => ({
        value: t.idTipoDispositivo,
        label: t.nombre,
      }))
    : [];

  const sections = buildSolicitudDispositivoSections(tipoDispositivoOptions);

  // Solo bloquea inputs mientras no hay datos todavía
  const hasData = useMemo(() => !!data && Object.keys(data ?? {}).length > 0, [data]);
  const formLoading = !readonly && (loading || loadingTypes) && !hasData;

  // initialData mapeada (si tu form requiere nombres específicos, mapealos aquí)
  const initial = data ?? {};

  // submitting local para botones (evita re-clicks)
  const [submitting, setSubmitting] = useState(false);

  // Gestión
  const handleAprobar = async () => {
    const solicitudId =
      typeof id === "string" ? parseInt(id, 10) : (id as number | undefined);
    if (!solicitudId) return;

    try {
      setSubmitting(true);
      await accept();
      onSaved?.();
      navigate(`/dispositivos-asignados/nuevo?solicitudId=${solicitudId}`);
      onClose();
    } finally {
      setSubmitting(false);
    }
  };

  const handleRechazar = async () => {
    if (!id) return;
    try {
      setSubmitting(true);
      await reject();
      onSaved?.();
      onClose();
    } finally {
      setSubmitting(false);
    }
  };

  const handleCreate = async (formData: Record<string, any>) => {
    if (readonly || gestion) return;
    setSubmitting(true);
    try {
      const dto = mapFormToUserSolicitudDto(formData);
      await create(dto);
      onSaved?.();
      onClose();
    } finally {
      setSubmitting(false);
    }
  };

  const title = isEditing
    ? readonly
      ? "Detalle de solicitud de dispositivo"
      : gestion
      ? "Gestionar solicitud de dispositivo"
      : "Editar solicitud de dispositivo"
    : "Nueva solicitud de dispositivo";

  const busy = formLoading || submitting;

  return (
    <BaseModal show onClose={onClose}>
      <h2 className="text-xl font-bold mb-4 text-gray-900 dark:text-gray-100">
        {title}
      </h2>

      <DynamicModalForm
        id="solicitud-dispositivo-form"
        key={isEditing ? `sol-${id}` : "sol-new"}
        sections={sections}
        initialData={initial}
        readonly={readonly || gestion}
        loading={formLoading}
        onSubmit={handleCreate}
      />

      {error && <Alert kind="error">{error}</Alert>}

      <div className="flex justify-end gap-2 mt-4">
        {gestion && !readonly ? (
          <>
            <button
              onClick={handleRechazar}
              disabled={busy}
              className="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 disabled:opacity-50"
            >
              {busy ? "Procesando..." : "Rechazar"}
            </button>
            <button
              onClick={handleAprobar}
              disabled={busy || !id}
              className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 disabled:opacity-50"
            >
              {busy ? "Procesando..." : "Aprobar y asignar"}
            </button>
          </>
        ) : (
          !readonly && (
            <button
              form="solicitud-dispositivo-form"
              type="submit"
              disabled={busy}
              className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
            >
              {busy ? "Procesando..." : isEditing ? "Guardar cambios" : "Crear"}
            </button>
          )
        )}

        <button
          onClick={onClose}
          className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600"
        >
          Cerrar
        </button>
      </div>
    </BaseModal>
  );
}
