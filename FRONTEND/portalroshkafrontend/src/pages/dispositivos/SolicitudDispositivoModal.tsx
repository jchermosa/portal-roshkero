import { useMemo, useState } from "react";
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
  gestion?: boolean;          // true = flujo de administración (aprobar/rechazar)
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

  // Normalizar id
  const numericId = useMemo(() => {
    if (id === null || id === undefined) return undefined;
    const n = Number(id);
    return Number.isFinite(n) ? n : undefined;
  }, [id]);

  // Hook principal (carga/acciones de la solicitud)
  const {
    data,          // detalle mapeado a UI desde el hook (si editing)
    create,
    accept,
    reject,
    loading,       // loading del detalle / acciones del hook
    error,
    isEditing,
  } = useSolicitudDispositivoForm(token, numericId, gestion);

  // Catálogo de tipos de dispositivo
  const { data: deviceTypes, loading: loadingTypes } = useGetDeviceTypes(token);
  const tipoDispositivoOptions = Array.isArray(deviceTypes)
    ? deviceTypes.map((t) => ({ value: t.idTipoDispositivo, label: t.nombre }))
    : [];

  // Secciones de formulario (select + comentario)
  const sections = useMemo(
    () => buildSolicitudDispositivoSections(tipoDispositivoOptions),
    [tipoDispositivoOptions]
  );

  // Si hay data, no bloquees inputs aunque aún esté “loading” algún fetch secundario
  const hasData = !!data && Object.keys(data ?? {}).length > 0;
  const formLoading = !readonly && (loading || (loadingTypes && isEditing)) && !hasData;

  // Mapear initialData a los nombres que el form espera
  const initial = useMemo(() => {
    // Ajusta estos paths según el DTO real de detalle (admin/usuario).
    const idTipo =
      (data as any)?.idTipoDispositivo ??
      (data as any)?.tipoDispositivo?.idTipoDispositivo ??
      "";

    return {
      idTipoDispositivo: idTipo,
      comentario: (data as any)?.comentario ?? "",
    };
  }, [data]);

  // submitting local para evitar dobles clics en botones
  const [submitting, setSubmitting] = useState(false);
  const busy = formLoading || submitting;

  // Gestión (admin)
  const handleAprobar = async () => {
    if (!numericId) return;
    try {
      setSubmitting(true);
      await accept();
      onSaved?.();
      navigate(`/dispositivos-asignados/nuevo?solicitudId=${numericId}`);
      onClose();
    } finally {
      setSubmitting(false);
    }
  };

  const handleRechazar = async () => {
    if (!numericId) return;
    try {
      setSubmitting(true);
      await reject();
      onSaved?.();
      onClose();
    } finally {
      setSubmitting(false);
    }
  };

  // Crear (usuario)
  const handleCreate = async (formData: Record<string, any>) => {
    if (readonly || gestion) return;
    try {
      setSubmitting(true);
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

  return (
    <BaseModal show onClose={onClose}>
      <h2 className="text-xl font-bold mb-4 text-gray-900 dark:text-gray-100">
        {title}
      </h2>

      <DynamicModalForm
        id="solicitud-dispositivo-form"
        key={isEditing ? `sol-${numericId}` : "sol-new"}
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
              disabled={busy || !numericId}
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
