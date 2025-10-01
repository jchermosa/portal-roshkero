import React from "react";

interface BaseModalProps {
  show: boolean;
  title?: string;
  onClose: () => void;
  children: React.ReactNode;
  width?: string;
  footer?: React.ReactNode;
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
        className={`relative bg-white dark:bg-gray-900 rounded-lg shadow-lg w-full ${width} p-6`}
      >
        {/* Botón de cerrar flotante */}
        <button
          onClick={onClose}
          className="absolute top-3 right-3 text-gray-400 hover:text-gray-600 dark:text-gray-500 dark:hover:text-gray-300 transition-colors"
          aria-label="Cerrar"
        >
          ✖
        </button>

        {/* Header */}
        {title && (
          <h2 className="text-xl font-bold text-gray-900 dark:text-gray-100 mb-4">
            {title}
          </h2>
        )}

        {/* Body */}
        <div className="overflow-y-auto max-h-[70vh]">{children}</div>

        {/* Footer */}
        {footer && <div className="mt-4 flex justify-end gap-2">{footer}</div>}
      </div>
    </div>
  );
};

export default BaseModal;
