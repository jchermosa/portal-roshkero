// src/pages/tecnologias/TecnologiasModal.tsx
import { useEffect } from "react";
import BaseModal from "../../components/BaseModal";
import DynamicForm from "../../components/DynamicForm";
import { Alert } from "../../components/Alert";

import { buildTecnologiaSections } from "../../config/forms/tecnologiasFormFields";
import { mapFormToTecnologiaRequest } from "../../mappers/tecnologiasMapper";
import { useTecnologiasForm } from "../../hooks/tecnologias/useTecnologiasForm";

interface TecnologiasModalProps {
  token: string | null;
  id?: number | string;
  readonly?: boolean;
  onClose: () => void;
  onSaved?: () => void; 
}

export default function TecnologiasModal({
  token,
  id,
  readonly = false,
  onClose,
  onSaved,
}: TecnologiasModalProps) {
  const numericId = typeof id === "string" ? parseInt(id, 10) : id;

  
  const {
    item,
    loading,    
    saving,     
    error,
    loadById,
    create,
    update,
  } = useTecnologiasForm(token, {
    onSaved: () => onSaved?.(),
  });

  const isEditing = !!numericId;

  useEffect(() => {
    if (numericId) loadById(numericId);
  }, [numericId]);

  const sections = buildTecnologiaSections();

  return (
    <BaseModal show onClose={onClose}>
      <h2 className="text-xl font-bold mb-4 text-gray-900 dark:text-gray-100">
        {isEditing
          ? readonly
            ? "Detalle de tecnología"
            : "Editar tecnología"
          : "Nueva tecnología"}
      </h2>

      <DynamicForm
        id="tecnologia-form"
        sections={sections}
        initialData={item ?? {}}
        readonly={readonly}
        loading={loading || saving}
        onSubmit={async (formData) => {
          if (readonly) return;
          const dto = mapFormToTecnologiaRequest(formData);
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
            form="tecnologia-form"
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
