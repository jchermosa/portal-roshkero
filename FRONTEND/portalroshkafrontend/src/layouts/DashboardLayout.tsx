import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function DashboardLayout() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const isThOrGth = user?.rol?.nombre === "TH" || user?.rol?.nombre === "GTH" || user?.rol?.nombre === "OPERACIONES";
  const isFuncionario = [
    "FUNCIONARIO_FABRICA",
    "FUNCIONARIO_TERCERIZADO",
    "LIDER",
    "DIRECTORIO",
  ].includes(user?.rol?.nombre || "");

  const menuOptions = [
    { id: "/", label: "Inicio", icon: "🏠", available: true, end: true as const },
    { id: "/profile", label: "Mi Perfil", icon: "👤", available: true },
    { id: "/usuarios", label: "Gestión de Usuarios", icon: "👥", available: isThOrGth },
    { id: "/gestionsolicitud", label: "Gestión de Solicitudes", icon:  "📤", available: isThOrGth },
    { id: "/vacaciones", label: "Vacaciones", icon: "🏖️", available: true },
    { id: "/requests", label: "Solicitudes", icon: "📩", available: true},
    { id: "/benefits", label: "Beneficios", icon: "🏆", available: true },
    { id: "/configuracion", label: "Configuración", icon: "⚙️", available: true },
  ].filter(o => o.available);

  const initials =
    `${user?.nombre?.[0] ?? ""}${user?.apellido?.[0] ?? ""}`.trim() || "👤";

  return (
    <div className="h-screen bg-gray-50 flex overflow-hidden">
      {/* Sidebar */}
      <aside className="w-64 bg-white shadow-xl border-r border-gray-200 flex flex-col">
        {/* Perfil compacto */}
        <div className="p-6 border-b border-gray-200 flex-shrink-0">
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

        {/* Menú - Área con scroll si es necesario */}
        <nav className="flex-1 overflow-y-auto mt-6">
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

        {/* Logout - Fijo en la parte inferior */}
        <div className="p-6 border-t border-gray-200 bg-white flex-shrink-0">
          <button
            onClick={() => {
              logout();
            }}
            className="w-full flex items-center space-x-3 px-4 py-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
          >
            <span className="text-xl">🚪</span>
            <span className="font-medium">Cerrar sesión</span>
          </button>
        </div>
      </aside>

      {/* Contenido principal */}
      <main className="flex-1 overflow-hidden relative">
        <Outlet />
      </main>
    </div>
  );
}