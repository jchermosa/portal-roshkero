import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function DashboardLayout() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const isThOrGth = user?.rol?.nombre === "TH" || user?.rol?.nombre === "GTH";
  const isFuncionario = [
    "FUNCIONARIO_FABRICA",
    "FUNCIONARIO_TERCERIZADO",
    "LIDER",
    "OPERACIONES",
    "DIRECTORIO",
  ].includes(user?.rol?.nombre || "");

  const menuOptions = [
    { id: "/", label: "Inicio", icon: "🏠", available: true, end: true as const },
    { id: "/perfil", label: "Mi Perfil", icon: "👤", available: true },
    { id: "/usuarios", label: "Gestión de Usuarios", icon: "👥", available: isThOrGth },
    { id: "/usuarios", label: "Gestión de Solicitudes", icon:  "📤", available: isThOrGth },
    { id: "/reportes", label: "Reportes", icon: "📊", available: isThOrGth },
    { id: "/vacaciones", label: "Vacaciones", icon: "🏖️", available: true },
    { id: "/solicitudes", label: "Solicitudes", icon: "📩", available: true},
    { id: "/configuracion", label: "Configuración", icon: "⚙️", available: true },
  ].filter(o => o.available);

  const initials =
    `${user?.nombre?.[0] ?? ""}${user?.apellido?.[0] ?? ""}`.trim() || "👤";

  return (
    <div className="min-h-screen bg-gray-50 flex">
      {/* Sidebar */}
      <aside className="w-64 bg-white shadow-xl border-r border-gray-200 relative pb-24">
        {/* Perfil compacto */}
        <div className="p-6 border-b border-gray-200">
          <div className="flex items-center space-x-3">
            <div className="w-12 h-12 bg-gradient-to-r from-blue-600 to-blue-800 rounded-full flex items-center justify-center text-white font-bold text-lg">
              {initials}
            </div>
            <div>
              <p className="font-semibold text-gray-800 line-clamp-1">{user?.nombre}</p>
              <p className="text-sm text-gray-600">{user?.rol?.nombre}</p>
            </div>
          </div>
        </div>

        {/* Menú */}
        <nav className="mt-6">
          {menuOptions.map((opt) => (
            <NavLink
              key={opt.id}
              to={opt.id}
              // Para "/" usamos end para que no quede activo en todas las rutas
              end={opt.end}
              className={({ isActive }) =>
                `block w-full text-left px-6 py-3 flex items-center space-x-3 hover:bg-gray-50 transition-colors ${
                  isActive
                    ? "bg-blue-50 border-r-2 border-blue-600 text-blue-700"
                    : "text-gray-700"
                }`
              }
            >
              <span className="text-xl">{opt.icon}</span>
              <span className="font-medium">{opt.label}</span>
            </NavLink>
          ))}
        </nav>

        {/* Logout */}
        <div className="absolute bottom-0 w-full p-6 border-t border-gray-200 bg-white">
          <button
            onClick={() => {
              logout();
              // Si tu logout NO navega, descomenta la siguiente línea:
              // navigate("/login", { replace: true });
            }}
            className="w-full flex items-center space-x-3 px-4 py-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
          >
            <span className="text-xl">🚪</span>
            <span className="font-medium">Cerrar sesión</span>
          </button>
        </div>
      </aside>

      {/* Contenido principal */}
      <main className="flex-1 overflow-auto">
        <div className="p-8">
          <Outlet />
        </div>
      </main>
    </div>
  );
}
