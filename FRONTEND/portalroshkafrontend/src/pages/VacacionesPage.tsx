import React from "react";
import { useNavigate } from "react-router-dom";

const Vacaciones: React.FC = () => {
  const navigate = useNavigate();

  const diasDisponibles: number = 15;
  const diasTotales: number = 30;

  const handleSolicitudClick = () => {
    navigate("/solicitud-vacaciones");
  };

  return (
    <div className="h-full flex flex-col overflow-hidden">
      {/* Fondo con imagen y overlay */}
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
        <div className="bg-white/45 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col h-full overflow-hidden">
          
          {/* Header fijo */}
          <div className="p-6 border-b border-gray-200 flex-shrink-0">
            <h1 className="text-2xl font-bold text-brand-blue">
              Gestión de Vacaciones
            </h1>
          </div>

          {/* Contenido */}
          <div className="flex-1 overflow-auto p-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
              <div className="rounded-2xl border border-green-300 bg-green-100/60 backdrop-blur-sm p-6 shadow">
                <h2 className="text-xl font-semibold text-green-700 mb-2">
                  Días disponibles
                </h2>
                <p className="text-green-800 text-lg">
                  {diasDisponibles} días restantes
                </p>
              </div>

              <div className="rounded-2xl border border-blue-300 bg-blue-100/60 backdrop-blur-sm p-6 shadow">
                <h2 className="text-xl font-semibold text-blue-700 mb-2">
                  Días totales asignados
                </h2>
                <p className="text-blue-800 text-lg">
                  {diasTotales} días por año
                </p>
              </div>
            </div>

            <button
              onClick={handleSolicitudClick}
              className="px-6 py-2 bg-blue-600 text-white rounded-lg text-sm font-medium shadow hover:bg-blue-700 transition"
            >
              Solicitar vacaciones
            </button>
          </div>

          {/* Footer */}
          <div className="p-6 border-t border-gray-200 flex-shrink-0">
            <p className="text-sm text-gray-600">
              Recordá que las solicitudes deben ser aprobadas por tu supervisor.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Vacaciones;
