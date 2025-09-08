import { useState } from "react";
import { Outlet, useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function DashboardLayout() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  // El activeSection ahora se sincroniza con la ruta
  const currentPath = location.pathname;
  const [activeSection, setActiveSection] = useState(currentPath);

  const isThOrGth = user?.rol?.nombre === "TH" || user?.rol?.nombre === "GTH";
  const isFuncionario = [
    "FUNCIONARIO_FABRICA",
    "FUNCIONARIO_TERCERIZADO",
    "LIDER",
    "OPERACIONES",
    "DIRECTORIO",
  ].includes(user?.rol?.nombre || "");

  const menuOptions = [
    { id: "/", label: "Inicio", icon: "🏠", available: true },
    { id: "/perfil", label: "Mi Perfil", icon: "👤", available: true },
    { id: "/usuarios", label: "Gestión de Usuarios", icon: "👥", available: isThOrGth },
    { id: "/reportes", label: "Reportes", icon: "📊", available: isThOrGth },
    { id: "/vacaciones", label: "Vacaciones", icon: "🏖️", available: isFuncionario },
    { id: "/configuracion", label: "Configuración", icon: "⚙️", available: true },
  ].filter(option => option.available);

  return (
    <div className="min-h-screen bg-gray-50 flex">
      {/* Menú lateral */}
      <div className="w-64 bg-white shadow-xl border-r border-gray-200 relative">
        <div className="p-6 border-b border-gray-200">
          <div className="flex items-center space-x-3">
            <div className="w-12 h-12 bg-gradient-to-r from-blue-600 to-blue-800 rounded-full flex items-center justify-center text-white font-bold text-lg">
              {user?.nombre?.[0]}{user?.apellido?.[0]}
            </div>
            <div>
              <p className="font-semibold text-gray-800">{user?.nombre}</p>
              <p className="text-sm text-gray-600">{user?.rol?.nombre}</p>
            </div>
          </div>
        </div>

        <nav className="mt-6">
          {menuOptions.map((option) => (
            <button
              key={option.id}
              onClick={() => {
                setActiveSection(option.id);
                navigate(option.id);
              }}
              className={`w-full text-left px-6 py-3 flex items-center space-x-3 hover:bg-gray-50 transition-colors ${
                currentPath === option.id
                  ? "bg-blue-50 border-r-2 border-blue-600 text-blue-700"
                  : "text-gray-700"
              }`}
            >
              <span className="text-xl">{option.icon}</span>
              <span className="font-medium">{option.label}</span>
            </button>
          ))}
        </nav>

        <div className="absolute bottom-0 w-64 p-6 border-t border-gray-200">
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
        <div className="p-8">
          <Outlet /> {/* 👈 Aquí se renderizan las páginas hijas */}
        </div>
      </div>
    </div>
  );
}
