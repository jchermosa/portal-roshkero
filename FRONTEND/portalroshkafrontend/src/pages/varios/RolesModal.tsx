import { useEffect, useMemo, useState } from "react";
import BaseModal from "../../components/BaseModal";
import DynamicModalForm from "../../components/DynamicModalForm";
import { Alert } from "../../components/Alert";

import { buildRolSections } from "../../config/forms/rolesFormFields";
import { useRolForm } from "../../hooks/roles/useRolForm";
import { mapFormToRolInsert } from "../../mappers/rolMapper";
import { getRolById } from "../../services/RolesService"; // ⬅️ import directo

interface RolesModalProps {
  token: string | null;
  id?: number | string;
  readonly?: boolean;
  onClose: () => void;
  onSaved?: () => void;
}

export default function RolesModal({
  token,
  id,
  readonly = false,
  onClose,
  onSaved,
}: RolesModalProps) {
  const numericId = useMemo(() => {
    if (id === null || id === undefined) return undefined;
    const n = Number(id);
    return Number.isFinite(n) ? n : undefined;
  }, [id]);

  // Hook para create/update/delete/mensajes
  const { saving, error, success, create, update, clearMessages } =
    useRolForm(token, { onSaved: () => onSaved?.() });

  // 🔑 Estado LOCAL para el detalle (kill switch)
  const [detail, setDetail] = useState<{ idRol: number; nombre: string } | null>(null);
  const [detailLoading, setDetailLoading] = useState(false);
  const [detailError, setDetailError] = useState<string | undefined>(undefined);

  const isEditing = numericId !== undefined;
  const sections = buildRolSections();

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
        const res = await getRolById(token, numericId);
        if (!cancelled) {
          setDetail(res); // { idRol, nombre, ... }
        }
      } catch (e: any) {
        if (!cancelled) setDetailError(e?.message || "Error al cargar el rol");
      } finally {
        if (!cancelled) setDetailLoading(false);
      }
    }
    run();
    return () => { cancelled = true; };
  }, [isEditing, token, numericId]);

  useEffect(() => {
    return () => clearMessages();
  }, [clearMessages]);

  const initial = detail ? { nombre: detail.nombre ?? "" } : {};

  return (
    <BaseModal show onClose={onClose}>
      <h2 className="text-xl font-bold mb-4 text-gray-900 dark:text-gray-100">
        {isEditing ? (readonly ? "Detalle de rol" : "Editar rol") : "Nuevo rol"}
      </h2>

      <DynamicModalForm
        id="rol-form"
        key={isEditing ? `rol-${numericId}` : "rol-new"}
        sections={sections}
        initialData={initial}
        readonly={readonly}
        loading={detailLoading}
        onSubmit={async (formData) => {
          if (readonly) return;
          const dto = mapFormToRolInsert(formData);
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

      {(detailError || error) && <Alert kind="error">{detailError || error}</Alert>}
      {success && <Alert kind="success">{success}</Alert>}

      <div className="flex justify-end gap-2 mt-4">
        {!readonly && (
          <button
            form="rol-form"
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
