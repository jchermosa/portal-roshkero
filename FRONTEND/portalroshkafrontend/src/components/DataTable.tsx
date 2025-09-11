import React, { useMemo, useState } from "react";

interface Column<T> {
  key: keyof T | string;
  label: string;
  render?: (row: T) => React.ReactNode;
  className?: string;
}

interface DataTableProps<T> {
  data: T[];
  columns: Column<T>[];
  rowKey: (row: T) => string | number;
  onRowClick?: (row: T) => void;
  actions?: (row: T) => React.ReactNode;
  scrollable?: boolean;
  enableSearch?: boolean;
}

function DataTable<T>({
  data,
  columns,
  rowKey,
  onRowClick,
  actions,
  scrollable = true,
  enableSearch = true,
}: DataTableProps<T>) {
  const [searchTerm, setSearchTerm] = useState("");

  const filteredData = useMemo(() => {
    if (!searchTerm.trim()) return data;
    return data.filter((row) =>
      columns.some((col) => {
        const rawValue = col.render?.(row) ?? (row as any)[col.key];
        const text = typeof rawValue === "string" ? rawValue : String(rawValue);
        return text.toLowerCase().includes(searchTerm.toLowerCase());
      })
    );
  }, [data, columns, searchTerm]);

  const containerClass = scrollable ? "flex-1 overflow-auto p-6 pt-0" : "";

  return (
    <div className={containerClass}>
      {enableSearch && (
        <div className="mb-4">
          <input
            type="text"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder="Buscar..."
            className="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500
                       bg-white text-gray-900 border-gray-300
                       dark:bg-gray-700 dark:text-gray-100 dark:border-gray-600"
          />
        </div>
      )}

      <table className="w-full border-collapse rounded-lg overflow-hidden bg-white dark:bg-gray-700">
        <thead className="bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-100 sticky top-0 z-10">
          <tr>
            {columns.map((col) => (
              <th
                key={col.key as string}
                className={`px-4 py-3 text-left text-sm font-semibold border-b border-gray-200 dark:border-gray-700 ${col.className ?? ""}`}
              >
                {col.label}
              </th>
            ))}
            {actions && (
              <th className="px-4 py-3 text-left text-sm font-semibold border-b border-gray-200 dark:border-gray-700">
                Acciones
              </th>
            )}
          </tr>
        </thead>

        <tbody className="text-gray-800 dark:text-gray-200">
          {filteredData.length === 0 ? (
            <tr>
              <td
                colSpan={columns.length + (actions ? 1 : 0)}
                className="px-4 py-6 text-center text-gray-400 dark:text-gray-500"
              >
                No hay resultados.
              </td>
            </tr>
          ) : (
            filteredData.map((row) => (
              <tr
                key={rowKey(row)}
                className={`border-b last:border-0 border-gray-200 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-700/40 ${onRowClick ? "cursor-pointer" : ""}`}
                onClick={() => onRowClick?.(row)}
              >
                {columns.map((col) => (
                  <td
                    key={col.key as string}
                    className={`px-4 py-3 text-sm ${col.className ?? ""}`}
                  >
                    {col.render ? col.render(row) : String((row as any)[col.key])}
                  </td>
                ))}
                {actions && <td className="px-4 py-3 text-sm">{actions(row)}</td>}
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export default DataTable;
