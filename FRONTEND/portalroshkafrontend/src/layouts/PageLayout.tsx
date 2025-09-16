import type { ReactNode } from "react";

interface PageLayoutProps {
  title?: string;
  actions?: ReactNode;
  children: ReactNode;
}

export default function PageLayout({ title, actions, children }: PageLayoutProps) {
  return (
    <div className="h-full flex flex-col overflow-hidden">
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

      {/* Contenido */}
      <div className="relative z-10 flex flex-col h-full p-4">
        <div className="bg-white/45 dark:bg-gray-900/70 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col h-full overflow-hidden">
          {/* Header */}
          <div className="p-6 border-b border-gray-200 flex items-center justify-between">
            <h2 className="text-2xl font-bold text-brand-blue dark:text-white">{title}</h2>
            {actions}
          </div>

          {/* Body */}
          <div className="flex-1 overflow-auto p-6">{children}</div>
        </div>
      </div>
    </div>
  );
}
