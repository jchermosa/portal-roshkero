// src/pages/cargos/CargosModal.tsx
import { useEffect, useMemo, useState } from "react";
import BaseModal from "../../components/BaseModal";
import DynamicModalForm from "../../components/DynamicModalForm";
import { Alert } from "../../components/Alert";

import { buildCargoSections } from "../../config/forms/cargosFormFields";
import { useCargoForm } from "../../hooks/cargos/useCargoForm";
import { mapFormToCargoInsert } from "../../mappers/cargoMapper";
import { getCargoById } from "../../services/CargosService"; // ⬅️ asegúrate que exista

interface CargosModalProps {
  token: string | null;
  id?: number | string;
  readonly?: boolean;
  onClose: () => void;
  onSaved?: () => void;
}

export default function CargosModal({
  token,
  id,
  readonly = false,
  onClose,
  onSaved,
}: CargosModalProps) {
  // Normalizar id
  const numericId = useMemo(() => {
    if (id === null || id === undefined) return undefined;
    const n = Number(id);
    return Number.isFinite(n) ? n : undefined;
  }, [id]);

  // Hook para create/update/delete (NO para detalle)
  const { saving, error, success, create, update, clearMessages } =
    useCargoForm(token, { onSaved: () => onSaved?.() });

  // Estado LOCAL del detalle (kill-switch pattern)
  const [detail, setDetail] = useState<any | null>(null);
  const [detailLoading, setDetailLoading] = useState(false);
  const [detailError, setDetailError] = useState<string | undefined>(undefined);

  const isEditing = numericId !== undefined;
  const sections = buildCargoSections();

  // Cargar detalle localmente solo cuando edito
  useEffect(() => {
    let cancelled = false;
    async function run() {
      if (!isEditing || !token || numericId === undefined) {
        setDetail(null);
        return;
      }
      setDetailLoading(true);
      setDetailError(undefined);
      try {
        const res = await getCargoById(token, numericId); // { idCargo, nombre, ... }
        if (!cancelled) setDetail(res);
      } catch (e: any) {
        if (!cancelled) setDetailError(e?.message || "Error al cargar el cargo");
      } finally {
        if (!cancelled) setDetailLoading(false);
      }
    }
    run();
    return () => { cancelled = true; };
  }, [isEditing, token, numericId]);

  useEffect(() => () => clearMessages(), [clearMessages]);

  // Mapeá las claves EXACTAS que tus fields esperan (ejemplo)
  const initial = detail
    ? {
        nombre: detail.nombre ?? "",
        // agrega otros campos si existen en buildCargoSections
      }
    : {};

  return (
    <BaseModal show onClose={onClose}>
      <h2 className="text-xl font-bold mb-4 text-gray-900 dark:text-gray-100">
        {isEditing ? (readonly ? "Detalle de cargo" : "Editar cargo") : "Nuevo cargo"}
      </h2>

      <DynamicModalForm
        id="cargo-form"
        key={isEditing ? `cargo-${numericId}` : "cargo-new"}
        sections={sections}
        initialData={initial}
        readonly={readonly}
        loading={detailLoading} // bloquea solo mientras NO hay detalle
        onSubmit={async (formData) => {
          if (readonly) return;
          const dto = mapFormToCargoInsert(formData);
          if (isEditing && numericId !== undefined) {
            const ok = await update(numericId, dto);
            if (ok) {
              onSaved?.();
              onClose();
            }
          } else {
            const ok = await create(dto);
            if (ok) {
              onSaved?.();
              onClose();
            }
          }
        }}
      />

      {detailError && <Alert kind="error">{detailError}</Alert>}
      {error && <Alert kind="error">{error}</Alert>}
      {success && <Alert kind="success">{success}</Alert>}

      <div className="flex justify-end gap-2 mt-4">
        {!readonly && (
          <button
            form="cargo-form"
            type="submit"
            disabled={saving || detailLoading}
            className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
          >
            {saving ? "Procesando..." : isEditing ? "Guardar cambios" : "Crear"}
          </button>
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
