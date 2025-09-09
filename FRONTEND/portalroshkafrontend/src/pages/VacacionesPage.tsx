import React from 'react';
import { useNavigate } from 'react-router-dom';

const Vacaciones: React.FC = () => {
  const navigate = useNavigate();

  const diasDisponibles: number = 15;
  const diasTotales: number = 30;

  const handleSolicitudClick = () => {
    navigate('/solicitud-vacaciones'); // Ruta hacia el formulario
  };

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold mb-6">Gestión de Vacaciones</h1>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
        <div className="bg-green-100 border-l-4 border-green-500 p-4 rounded shadow">
          <h2 className="text-xl font-semibold text-green-700 mb-2">Días disponibles</h2>
          <p className="text-green-800 text-lg">{diasDisponibles} días restantes</p>
        </div>

        <div className="bg-blue-100 border-l-4 border-blue-500 p-4 rounded shadow">
          <h2 className="text-xl font-semibold text-blue-700 mb-2">Días totales asignados</h2>
          <p className="text-blue-800 text-lg">{diasTotales} días por año</p>
        </div>
      </div>

      <button
        onClick={handleSolicitudClick}
        className="btn btn-primary"
      >
        Solicitar vacaciones
      </button>
    </div>
  );
};

export default Vacaciones;
