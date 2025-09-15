import { useState } from "react";
interface Entidad {
  id: number;
  nombre: string;
}

interface AsignadorEntidadProps<T extends Entidad> {
  disponibles: T[];
  asignados: T[];
  setAsignados: React.Dispatch<React.SetStateAction<T[]>>;
  label: string;
  max?: number;
}

export function AsignadorEntidad<T extends { id: number; nombre: string }>({
  disponibles,
  asignados,
  setAsignados,
  label,
  max,
}: AsignadorEntidadProps<T>) {
  const [seleccionado, setSeleccionado] = useState<string>("");

  const agregar = () => {
    const entidad = disponibles.find((e) => String(e.id) === seleccionado);
    if (!entidad) return;
    if (asignados.some((e) => e.id === entidad.id)) return;
    if (max && asignados.length >= max) return;

    setAsignados([...asignados, entidad]);
    setSeleccionado("");
  };

  const eliminar = (id: number) => {
    setAsignados(asignados.filter((e) => e.id !== id));
  };

  return (
    <div className="bg-gray-50 p-4 rounded-lg shadow-sm">
      <label className="block font-semibold mb-2">{label}</label>

      <div className="flex gap-4 items-center mb-3">
        <select
          value={seleccionado}
          onChange={(e) => setSeleccionado(e.target.value)}
          className="w-full rounded-lg px-3 py-2 border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-700 text-sm text-gray-800 dark:text-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          <option value="">Seleccionar...</option>
          {disponibles
            .filter((e) => !asignados.some((a) => a.id === e.id))
            .map((e) => (
              <option key={e.id} value={e.id}>
                {e.nombre}
              </option>
            ))}
        </select>

        <button
          onClick={agregar}
          className="bg-blue-600 text-white px-4 py-2 rounded disabled:opacity-50"
         disabled={!seleccionado || !!(max && asignados.length >= max)}
        >
          Agregar
        </button>
      </div>

      <div className="flex flex-wrap gap-2">
        {asignados.map((e) => (
          <span
            key={e.id}
            className="bg-blue-100 text-blue-800 px-3 py-1 rounded-full text-sm flex items-center gap-2"
          >
            {e.nombre}
            <button
              onClick={() => eliminar(e.id)}
              className="text-blue-500 hover:text-blue-700"
            >
              ✕
            </button>
          </span>
        ))}
      </div>
    </div>
  );
}