import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

export default function Dashboard() {
  const { user } = useAuth();
  const navigate = useNavigate();

  const isThOrGth = user?.rol?.nombre === "TH" || user?.rol?.nombre === "GTH";

  const isFuncionario = [
    "FUNCIONARIO_FABRICA",
    "FUNCIONARIO_TERCERIZADO",
    "LIDER",
    "OPERACIONES",
    "DIRECTORIO",
  ].includes(user?.rol?.nombre || "");

  if (!user) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-100">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-700 mx-auto mb-4"></div>
          <p className="text-lg text-gray-700">
            Cargando información del usuario...
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="bg-gradient-to-r from-blue-600 to-blue-800 text-white p-8 rounded-2xl">
        <h2 className="text-3xl font-bold mb-2">¡Bienvenido de nuevo! 👋</h2>
        <p className="text-blue-100 text-lg">
          {user?.nombre} {user?.apellido}
        </p>
        <p className="text-blue-200 text-sm mt-1">{user?.correo}</p>
      </div>

      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
        {/* Acceso rápido para TH/GTH */}
        {isThOrGth && (
          <div className="bg-white p-6 rounded-xl shadow-md border-l-4 border-blue-600 hover:shadow-lg transition-shadow">
            <div className="flex items-center mb-3">
              <span className="text-2xl mr-3">👥</span>
              <h3 className="text-lg font-semibold text-gray-800">
                Gestión de Usuarios
              </h3>
            </div>
            <p className="text-gray-600 text-sm mb-4">
              Administra perfiles y usuarios del sistema
            </p>
            <button
              onClick={() => navigate("/usuarios")}
              className="w-full bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700 transition-colors"
            >
              Ir a usuarios
            </button>
          </div>
        )}

        {/* Acceso rápido para Funcionarios */}
        {isFuncionario && (
          <div className="bg-white p-6 rounded-xl shadow-md border-l-4 border-yellow-500 hover:shadow-lg transition-shadow">
            <div className="flex items-center mb-3">
              <span className="text-2xl mr-3">🏖️</span>
              <h3 className="text-lg font-semibold text-gray-800">
                Mis Vacaciones
              </h3>
            </div>
            <p className="text-gray-600 text-sm mb-2">
              Disponibles:{" "}
              <span className="font-semibold text-yellow-600">
                {user?.dias_vacaciones_restante ?? 0} días
              </span>
            </p>
            <p className="text-gray-600 text-sm mb-4">
              Total asignados: {user?.dias_vacaciones ?? 0} días
            </p>
            <button
              onClick={() => navigate("/vacaciones")}
              className="w-full bg-yellow-500 text-white py-2 px-4 rounded-lg hover:bg-yellow-600 transition-colors"
            >
              Ver detalles
            </button>
          </div>
        )}

        {/* Tarjeta de perfil */}
        <div className="bg-white p-6 rounded-xl shadow-md border-l-4 border-blue-500 hover:shadow-lg transition-shadow">
          <div className="flex items-center mb-3">
            <span className="text-2xl mr-3">👤</span>
            <h3 className="text-lg font-semibold text-gray-800">Mi Perfil</h3>
          </div>
          <p className="text-gray-600 text-sm mb-4">
            Rol: <span className="font-semibold">{user?.rol?.nombre}</span>
          </p>
          <button
            onClick={() => navigate("/perfil")}
            className="w-full bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600 transition-colors"
          >
            Ver perfil
          </button>
        </div>
      </div>
    </div>
  );
}
