import React from "react";

interface BaseModalProps {
  show: boolean;
  title?: string;
  onClose: () => void;
  children: React.ReactNode;
  width?: string; // para controlar el tamaño (ej: "max-w-lg", "max-w-2xl")
  footer?: React.ReactNode; // para pasar botones personalizados
}

const BaseModal: React.FC<BaseModalProps> = ({
  show,
  title,
  onClose,
  children,
  width = "max-w-lg",
  footer,
}) => {
  if (!show) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div
        className={`bg-white dark:bg-gray-900 rounded-lg shadow-lg w-full ${width} p-6`}
      >
        {/* Header */}
        <div className="flex justify-between items-center mb-4">
          {title && (
            <h2 className="text-xl font-bold text-gray-900 dark:text-gray-100">
              {title}
            </h2>
          )}
          <button
            onClick={onClose}
            className="text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200"
          >
            ✖
          </button>
        </div>

        {/* Body */}
        <div className="overflow-y-auto max-h-[70vh]">{children}</div>

        {/* Footer */}
        {footer && <div className="mt-4 flex justify-end gap-2">{footer}</div>}
      </div>
    </div>
  );
};

export default BaseModal;
