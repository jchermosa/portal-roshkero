// src/pages/ubicaciones/UbicacionModal.tsx
import { useMemo, useState } from "react";
import BaseModal from "../../components/BaseModal";
import DynamicModalForm from "../../components/DynamicModalForm";
import { Alert } from "../../components/Alert";

import { buildUbicacionSections } from "../../config/forms/ubicacionFormField"; 
import { useUbicacionForm } from "../../hooks/ubicaciones/useUbicacionForm";
import type { UbicacionItem } from "../../types";

interface Props {
  token: string | null;
  id?: number | string;
  readonly?: boolean;
  onClose: () => void;
  onSaved?: () => void;
}

export default function UbicacionModal({
  token,
  id,
  readonly = false,
  onClose,
  onSaved,
}: Props) {
  const {
    data,
    setData,
    loading,    
    saving,     
    toggling,   
    error,
    isEditing,
    handleSubmit,
    handleDelete, 
  } = useUbicacionForm(token, id);

  const sections = buildUbicacionSections();

  const hasData = useMemo(
    () => !!data && Object.keys(data as UbicacionItem | object).length > 0,
    [data]
  );
  const formLoading = !readonly && loading && !hasData;

  const initial = (data ?? { estado: "A" }) as Partial<UbicacionItem>;

  const [submitting, setSubmitting] = useState(false);
  const busy = formLoading || saving || toggling || submitting;

  const title = isEditing
    ? readonly
      ? "Detalle de ubicación"
      : "Editar ubicación"
    : "Nueva ubicación";

  return (
    <BaseModal show onClose={onClose}>
      <h2 className="text-xl font-bold mb-4 text-gray-900 dark:text-gray-100">
        {title}
      </h2>

      <DynamicModalForm
        id="ubicacion-form"
        key={isEditing ? `ubi-${id}` : "ubi-new"}  
        sections={sections}
        initialData={initial}
        readonly={readonly}
        loading={formLoading}
        onChange={(next) => setData(next)}
        onSubmit={async (formData) => {
          if (readonly) return;
          try {
            setSubmitting(true);
            const payload: Partial<UbicacionItem> = {
              nombre: (formData.nombre ?? "").toString().trim(),
              estado: (formData.estado ?? "A") as "A" | "I",
            };
            await handleSubmit(payload);
            onSaved?.();
            onClose();
          } finally {
            setSubmitting(false);
          }
        }}
      />

      {error && <Alert kind="error">{error}</Alert>}

      <div className="flex justify-between mt-4">
        <div className="flex gap-2 ml-auto">
          {!readonly && (
            <button
              form="ubicacion-form"
              type="submit"
              disabled={busy}
              className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
            >
              {busy ? "Procesando..." : isEditing ? "Guardar cambios" : "Crear"}
            </button>
          )}
          <button
            onClick={onClose}
            className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600"
          >
            Cerrar
          </button>
        </div>
      </div>
    </BaseModal>
  );
}
