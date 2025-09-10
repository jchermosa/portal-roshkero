import React, { useState } from "react";
import { useEffect } from "react";

type EditableFieldProps = {
  label: string;
  value?: string;
  placeholder?: string;
  onSave: (newValue: string) => Promise<void> | void;
};

export default function EditableField({ label, value, placeholder, onSave }: EditableFieldProps) {
  const [editing, setEditing] = useState(false);
  const [tempValue, setTempValue] = useState(value ?? "");
  useEffect(() => {
  setTempValue(value ?? "");
  }, [value]);
  const handleSave = async () => {
    await onSave(tempValue);
    setEditing(false);
  };

  return (
    <div className="flex items-center justify-between rounded-xl border border-gray-200 bg-white p-3">
      <div className="flex items-center gap-3">
        <span className="text-sm font-medium">{label}</span>
      </div>

      {editing ? (
        <div className="flex items-center gap-2">
          <input
            type="text"
            value={tempValue}
            onChange={(e) => setTempValue(e.target.value)}
            placeholder={placeholder}
            className="rounded border px-2 py-1 text-sm"
          />
          <button
            onClick={handleSave}
            className="rounded bg-green-600 px-2 py-1 text-xs text-white hover:bg-green-700"
          >
            Guardar
          </button>
          <button
            onClick={() => {
              setEditing(false);
              setTempValue(value ?? "");
            }}
            className="rounded bg-gray-300 px-2 py-1 text-xs text-gray-700 hover:bg-gray-400"
          >
            Cancelar
          </button>
        </div>
      ) : (
        <div className="flex items-center gap-2">
          <span className="truncate text-sm text-gray-500">
            {value || placeholder || "—"}
          </span>
          <button
            onClick={() => setEditing(true)}
            className="text-xs text-blue-600 hover:underline"
          >
            Editar
          </button>
        </div>
      )}
    </div>
  );
}
