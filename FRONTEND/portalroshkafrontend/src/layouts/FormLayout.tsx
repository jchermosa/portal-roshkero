import type { ReactNode } from "react";

interface FormLayoutProps {
  title?: string;
  subtitle?: string;
  icon?: ReactNode;
  children: ReactNode;
  onCancel?: () => void;
  onSubmitLabel?: string;
  onCancelLabel?: string;
  loading?: boolean;
}

export default function FormLayout({
  title,
  subtitle,
  icon,
  children,
  onCancel,
  onSubmitLabel = "Guardar cambios",
  onCancelLabel = "Cancelar",
  loading = false,
}: FormLayoutProps) {
  return (
    <div className="min-h-screen flex flex-col overflow-hidden">
      {/* Fondo ilustrativo */}
      <div
        className="absolute inset-0 bg-brand-blue"
        style={{
          backgroundImage: "url('/src/assets/ilustracion-herov3.svg')",
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
        }}
      >
        <div className="absolute inset-0 bg-brand-blue/40"></div>
      </div>

      {/* Contenedor principal */}
      <div className="relative z-10 flex flex-col h-full p-4">
        <div className="max-w-3xl w-full mx-auto flex flex-col h-full">
          {/* Tarjeta translúcida */}
          <div className="bg-white/45 dark:bg-gray-900/80 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col w-full max-h-[96vh] overflow-hidden">
            {/* Header del formulario */}
            <div className="p-6 border-b border-gray-200 dark:border-gray-700 flex items-center gap-3">
              {icon && <span className="text-2xl">{icon}</span>}
              <div>
                <h2 className="text-xl font-bold text-gray-800 dark:text-white">
                  {title}
                </h2>
                {subtitle && (
                  <p className="text-sm text-gray-500 dark:text-gray-300">
                    {subtitle}
                  </p>
                )}
              </div>
            </div>

            {/* Contenido del formulario */}
            <div className="flex-1 overflow-auto p-6">{children}</div>

            {/* Footer con botones */}
            <div className="p-6 border-t border-gray-200 dark:border-gray-700 flex flex-col sm:flex-row gap-4">
              <button
                type="submit"
                form="dynamic-form" // <- asocia con el <form id="dynamic-form"> de DynamicForm
                disabled={loading}
                className={`flex-1 py-3 px-6 rounded-xl font-semibold transition-all duration-200
                  ${loading
                    ? "bg-gray-400 cursor-not-allowed text-white"
                    : "bg-gradient-to-r from-blue-600 to-blue-700 text-white hover:from-blue-700 hover:to-blue-800 hover:scale-105 active:scale-95"
                  }`}
              >
                💾 {onSubmitLabel}
              </button>

              {onCancel && (
                <button
                  type="button"
                  onClick={onCancel}
                  disabled={loading}
                  className="flex-1 py-3 px-6 rounded-xl transition-all duration-200
                    bg-white border border-gray-300 text-gray-700 hover:bg-gray-50
                    dark:bg-gray-800 dark:text-gray-100 dark:border-gray-700 dark:hover:bg-gray-700 hover:scale-105 active:scale-95"
                >
                  ↩️ {onCancelLabel}
                </button>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
