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
      <table className="min-w-full border border-gray-300 rounded-lg">
        <thead className="bg-gray-100">
          <tr>
            {headers.map((header) => (
              <th key={header} className="px-4 py-2 border-b text-left text-sm font-medium text-gray-700">
                {header}
              </th>
            ))}
            {onEdit && <th className="px-4 py-2 border-b">Acciones</th>}
          </tr>
        </thead>
        <tbody>
          {rows.map((row) => (
            <tr key={rowKey(row)} className="hover:bg-gray-50">
              {headers.map((header) => (
                <td key={header} className="px-4 py-2 border-b">
                  {row[header]}
                </td>
              ))}
              {onEdit && (
                <td className="px-4 py-2 border-b">
                  <button
                    onClick={() => onEdit(row)}
                    className="text-blue-600 hover:underline"
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
          className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
        >
          Anterior
        </button>
        <span>Página {page} de {totalPages}</span>
        <button
          onClick={() => onPageChange(page + 1)}
          disabled={page === totalPages}
          className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
        >
          Siguiente
        </button>
      </div>
    </div>
  );
};

export default PaginatedTable;
