import React from "react";

interface PaginatedTableProps {
  headers: string[];
  rows: Array<Record<string, any>>;
  page: number;
  totalPages: number;
  onPageChange: (page: number) => void;
  onEdit?: (row: Record<string, any>) => void;
  rowKey: (row: Record<string, any>) => string | number;
}

const PaginatedTable: React.FC<PaginatedTableProps> = ({
  headers,
  rows,
  page,
  totalPages,
  onPageChange,
  onEdit,
  rowKey,
}) => {
  return (
    <div>
      <table className="min-w-full border border-gray-300 dark:border-gray-700 rounded-lg bg-white dark:bg-gray-800">
        <thead className="bg-gray-100 dark:bg-blue-900">
          <tr>
            {headers.map((header) => (
              <th
                key={header}
                className="px-4 py-2 border-b border-gray-300 dark:border-gray-700 text-left text-sm font-medium text-gray-700 dark:text-blue-100"
              >
                {header}
              </th>
            ))}
            {onEdit && (
              <th className="px-4 py-2 border-b border-gray-300 dark:border-gray-700 text-gray-700 dark:text-blue-100">
                Acciones
              </th>
            )}
          </tr>
        </thead>
        <tbody className="text-gray-800 dark:text-gray-200">
          {rows.map((row) => (
            <tr
              key={rowKey(row)}
              className="hover:bg-gray-50 dark:hover:bg-gray-700/40"
            >
              {headers.map((header) => (
                <td
                  key={header}
                  className="px-4 py-2 border-b border-gray-300 dark:border-gray-700"
                >
                  {row[header]}
                </td>
              ))}
              {onEdit && (
                <td className="px-4 py-2 border-b border-gray-300 dark:border-gray-700">
                  <button
                    onClick={() => onEdit(row)}
                    className="text-blue-600 dark:text-blue-400 hover:underline"
                  >
                    Editar
                  </button>
                </td>
              )}
            </tr>
          ))}
        </tbody>
      </table>

      <div className="flex justify-between items-center mt-4">
        <button
          onClick={() => onPageChange(page - 1)}
          disabled={page === 1}
          className="px-3 py-1 bg-gray-200 dark:bg-gray-700 text-gray-900 dark:text-gray-100 rounded disabled:opacity-50"
        >
          Anterior
        </button>
        <span className="text-sm text-gray-700 dark:text-gray-300">
          Página {page} de {totalPages}
        </span>
        <button
          onClick={() => onPageChange(page + 1)}
          disabled={page === totalPages}
          className="px-3 py-1 bg-gray-200 dark:bg-gray-700 text-gray-900 dark:text-gray-100 rounded disabled:opacity-50"
        >
          Siguiente
        </button>
      </div>
    </div>
  );
};

export default PaginatedTable;
