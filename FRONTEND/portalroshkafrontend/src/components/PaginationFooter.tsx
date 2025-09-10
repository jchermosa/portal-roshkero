interface PaginationFooterProps {
  currentPage: number; 
  totalPages: number;
  onPageChange: (page: number) => void;
  onCancel?: () => void;
}

export default function PaginationFooter({
  currentPage,
  totalPages,
  onPageChange,
  onCancel,
}: PaginationFooterProps) {
  return (
    <div className="p-6 border-t border-gray-200 flex-shrink-0">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-2">
          {Array.from({ length: totalPages }, (_, i) => (
            <button
              key={i}
              onClick={() => onPageChange(i)}
              disabled={i === currentPage}
              className={`px-3 py-1 rounded-lg text-sm ${
                i === currentPage
                  ? "bg-blue-600 text-white font-semibold"
                  : "bg-gray-200 hover:bg-gray-300"
              }`}
            >
              {i + 1}
            </button>
          ))}
        </div>

        {onCancel && (
          <button
            type="button"
            onClick={onCancel}
            className="px-4 py-2 bg-gray-400 text-white rounded-lg hover:bg-gray-500 transition text-sm"
          >
            Cancelar
          </button>
        )}
      </div>
    </div>
  );
}
