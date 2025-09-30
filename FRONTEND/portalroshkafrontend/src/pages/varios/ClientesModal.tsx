import { useEffect } from "react";
import BaseModal from "../../components/BaseModal";
import DynamicForm from "../../components/DynamicForm";
import { Alert } from "../../components/Alert";

import { buildClienteSections } from "../../config/forms/clientesFormFields";
import { mapFormToClienteRequest } from "../../mappers/clientesMapper";
import { useClientesForm } from "../../hooks/clientes/useClienteForm";

interface ClientesModalProps {
  token: string | null;
  id?: number | string;
  readonly?: boolean;
  onClose: () => void;
  onSaved?: () => void; // refrescar lista afuera
}

export default function ClientesModal({
  token,
  id,
  readonly = false,
  onClose,
  onSaved,
}: ClientesModalProps) {
  const numericId = typeof id === "string" ? parseInt(id, 10) : id;

  // Hook CRUD unificado
  const {
    item,
    loading,    // loading de loadById
    saving,     // estado de create/update
    error,
    loadById,
    create,
    update,
  } = useClientesForm(token, {
    onSaved: () => onSaved?.(),
  });

  const isEditing = !!numericId;

  useEffect(() => {
    if (numericId) loadById(numericId);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [numericId]);

  const sections = buildClienteSections();

  return (
    <BaseModal show onClose={onClose}>
      <h2 className="text-xl font-bold mb-4 text-gray-900 dark:text-gray-100">
        {isEditing
          ? readonly
            ? "Detalle de cliente"
            : "Editar cliente"
          : "Nuevo cliente"}
      </h2>

      <DynamicForm
        id="cliente-form"
        sections={sections}
        initialData={item ?? {}}
        readonly={readonly}
        loading={loading || saving}
        onSubmit={async (formData) => {
          if (readonly) return;
          const dto = mapFormToClienteRequest(formData);
          if (isEditing && numericId) {
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

      {error && <Alert kind="error">{error}</Alert>}

      <div className="flex justify-end gap-2 mt-4">
        {!readonly && (
          <button
            form="cliente-form"
            type="submit"
            disabled={loading || saving}
            className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
          >
            {saving
              ? "Procesando..."
              : isEditing
              ? "Guardar cambios"
              : "Crear"}
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
