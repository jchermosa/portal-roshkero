import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { useState } from "react";

export default function Dashboard() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [activeSection, setActiveSection] = useState("inicio");

  const isThOrGth = user?.rol?.nombre === "TH" || user?.rol?.nombre === "GTH";

  const isFuncionario = [
    "FUNCIONARIO_FABRICA",
    "FUNCIONARIO_TERCERIZADO",
    "LIDER",
    "OPERACIONES",
    "DIRECTORIO",
  ].includes(user?.rol?.nombre || "");

  // Opciones del menú basadas en el rol
  const menuOptions = [
    { id: "inicio", label: "Inicio", icon: "🏠", available: true },
    { id: "perfil", label: "Mi Perfil", icon: "👤", available: true },
    { id: "usuarios", label: "Gestión de Usuarios", icon: "👥", available: isThOrGth },
    { id: "reportes", label: "Reportes", icon: "📊", available: isThOrGth },
    { id: "vacaciones", label: "Vacaciones", icon: "🏖️", available: isFuncionario },
    { id: "configuracion", label: "Configuración", icon: "⚙️", available: true },
  ].filter((option) => option.available);

  const renderContent = () => {
    switch (activeSection) {
      case "inicio":
        return (
          <div className="space-y-6">
            <div className="bg-gradient-to-r from-blue-600 to-blue-800 text-white p-8 rounded-2xl">
              <h2 className="text-3xl font-bold mb-2">¡Bienvenido de nuevo! 👋</h2>
              <p className="text-blue-100 text-lg">
                {user?.nombre} {user?.apellido}
              </p>
              <p className="text-blue-200 text-sm mt-1">{user?.correo}</p>
            </div>
          </div>
        );

      case "perfil":
        return (
          <div className="space-y-6">
            <div className="bg-white rounded-2xl shadow-lg p-8">
              <h2 className="text-2xl font-bold text-gray-800 mb-6 flex items-center">
                <span className="mr-3">👤</span>
                Información Personal
              </h2>
              <p className="text-lg text-gray-700">
                Nombre: {user?.nombre} {user?.apellido}
              </p>
              <p className="text-lg text-gray-700">Correo: {user?.correo}</p>
              <p className="text-lg text-gray-700">Rol: {user?.rol?.nombre}</p>
            </div>
          </div>
        );

      default:
        return (
          <div className="text-center py-12">
            <h2 className="text-xl text-gray-600">Sección en desarrollo</h2>
          </div>
        );
    }
  };

  if (!user) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-100">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-700 mx-auto mb-4"></div>
          <p className="text-lg text-gray-700">Cargando información del usuario...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 flex">
      {/* Menú lateral con flex-col */}
      <div className="w-64 bg-white shadow-xl border-r border-gray-200 flex flex-col justify-between min-h-screen">
        {/* Parte superior */}
        <div>
          <div className="p-6 border-b border-gray-200">
            <div className="flex items-center space-x-3">
              <div className="w-12 h-12 bg-gradient-to-r from-blue-600 to-blue-800 rounded-full flex items-center justify-center text-white font-bold text-lg">
                {user.nombre?.[0]}
                {user.apellido?.[0]}
              </div>
              <div>
                <p className="font-semibold text-gray-800">{user.nombre}</p>
                <p className="text-sm text-gray-600">{user.rol?.nombre}</p>
              </div>
            </div>
          </div>

          <nav className="mt-6">
            {menuOptions.map((option) => (
              <button
                key={option.id}
                onClick={() => setActiveSection(option.id)}
                className={`w-full text-left px-6 py-3 flex items-center space-x-3 hover:bg-gray-50 transition-colors ${
                  activeSection === option.id
                    ? "bg-blue-50 border-r-2 border-blue-600 text-blue-700"
                    : "text-gray-700"
                }`}
              >
                <span className="text-xl">{option.icon}</span>
                <span className="font-medium">{option.label}</span>
              </button>
            ))}
          </nav>
        </div>

        {/* Parte inferior: logout */}
        <div className="p-6 border-t border-gray-200">
          <button
            onClick={logout}
            className="w-full flex items-center space-x-3 px-4 py-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
          >
            <span className="text-xl">🚪</span>
            <span className="font-medium">Cerrar sesión</span>
          </button>
        </div>
      </div>

      {/* Contenido principal */}
      <div className="flex-1 overflow-auto">
        <div className="p-8">{renderContent()}</div>
      </div>
    </div>
  );
}
