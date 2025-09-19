import React from "react";
import { useNavigate } from "react-router-dom";
import IconButton from "../components/IconButton";
import DataTable from "../components/DataTable";

interface SolicitudVacaciones {
  id: number;
  fechaInicio: string;
  fechaFin: string;
  estado: "Aprobada" | "Pendiente" | "Rechazada";
}

const Vacaciones: React.FC = () => {
  const navigate = useNavigate();

  const diasDisponibles: number = 15;
  const diasTotales: number = 30;

  const handleSolicitudClick = () => {
    navigate("/solicitud-vacaciones");
  };

  const historialSolicitudes: SolicitudVacaciones[] = [
    { id: 1, fechaInicio: "2025-06-10", fechaFin: "2025-06-15", estado: "Aprobada" },
    { id: 2, fechaInicio: "2025-08-01", fechaFin: "2025-08-05", estado: "Pendiente" },
    { id: 3, fechaInicio: "2025-09-20", fechaFin: "2025-09-25", estado: "Rechazada" },
  ];

  const columns = [
    { key: "fechaInicio", label: "Fecha Inicio" },
    { key: "fechaFin", label: "Fecha Fin" },
    {
      key: "estado",
      label: "Estado",
      render: (row: SolicitudVacaciones) => {
        const color =
          row.estado === "Aprobada"
            ? "text-green-700 dark:text-green-400"
            : row.estado === "Pendiente"
            ? "text-yellow-700 dark:text-yellow-300"
            : "text-red-700 dark:text-red-400";
        return <span className={`font-medium ${color}`}>{row.estado}</span>;
      },
    },
  ];

  return (
    <div className="h-full flex flex-col overflow-hidden">
      {/* Fondo */}
      <div
        className="absolute inset-0 bg-brand-blue"
        style={{
          backgroundImage: "url('/src/assets/ilustracion-herov3.svg')",
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
        }}
      >
        <div className="absolute inset-0 bg-brand-blue/40" />
      </div>

      {/* Contenedor principal */}
      <div className="relative z-10 flex flex-col h-full p-4">
        <div className="bg-white/45 dark:bg-gray-900/70 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col h-full overflow-hidden">
          {/* Header */}
          <div className="p-6 border-b border-gray-200 dark:border-gray-700 flex-shrink-0 flex items-center justify-between">
            <h1 className="text-2xl font-bold text-brand-blue dark:text-white">
              Gestión de Vacaciones
            </h1>
            <IconButton
              label="Solicitar vacaciones"
              icon={<span>➕</span>}
              variant="primary"
              onClick={handleSolicitudClick}
            />
          </div>

          {/* Contenido */}
          <div className="flex-1 overflow-auto p-6">
            {/* KPIs */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
              <div className="rounded-2xl border border-green-300 dark:border-green-700 bg-green-100/60 dark:bg-green-900/30 backdrop-blur-sm p-6 shadow">
                <h2 className="text-xl font-semibold text-green-700 dark:text-green-300 mb-2">
                  Días disponibles
                </h2>
                <p className="text-green-800 dark:text-green-200 text-lg">
                  {diasDisponibles} días restantes
                </p>
              </div>

              <div className="rounded-2xl border border-blue-300 dark:border-blue-700 bg-blue-100/60 dark:bg-blue-900/30 backdrop-blur-sm p-6 shadow">
                <h2 className="text-xl font-semibold text-blue-700 dark:text-blue-200 mb-2">
                  Días totales asignados
                </h2>
                <p className="text-blue-800 dark:text-blue-100 text-lg">
                  {diasTotales} días por año
                </p>
              </div>
            </div>

            {/* Historial */}
            <div className="bg-white/60 dark:bg-gray-800/70 rounded-2xl p-6 shadow backdrop-blur-sm">
              <h2 className="text-xl font-semibold text-gray-800 dark:text-gray-100 mb-4">
                Historial de Solicitudes
              </h2>
              <DataTable<SolicitudVacaciones>
                data={historialSolicitudes}
                columns={columns}
                rowKey={(row) => row.id}
                scrollable={false}
              />
            </div>
          </div>

          {/* Footer */}
          <div className="p-6 border-t border-gray-200 dark:border-gray-700 flex-shrink-0 flex justify-end">
            <p className="text-sm text-gray-600 dark:text-gray-400">
              Recordá que las solicitudes deben ser aprobadas por tu supervisor.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Vacaciones;
