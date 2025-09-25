// src/layouts/DashboardLayout.tsx
import { NavLink, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { tieneRol } from "../utils/permisos";
import { Roles } from "../types/roles";

export default function DashboardLayout() {
  const { user, logout } = useAuth();

  // Permisos usando tieneRol
  const puedeGestionarUsuarios = tieneRol(user, Roles.TH, Roles.GTH, Roles.OPERACIONES);
  const puedeGestionarSolicitudes = tieneRol(user, Roles.TH, Roles.GTH, Roles.OPERACIONES);
  const puedeGestionarDispositivos = tieneRol(user, Roles.SYSADMIN);

  const menuOptions = [
    { id: "/", label: "Inicio", icon: "🏠", available: true, end: true as const },
    { id: "/profile", label: "Mi Perfil", icon: "👤", available: true },
    { id: "/usuarios", label: "Gestión de Usuarios", icon: "👥", available: puedeGestionarUsuarios },
    { id: "/gestion-dispositivos", label: "Gestión de Dispositivos", icon: "💻", available: puedeGestionarDispositivos },
    { id: "/dispositivos", label: "Dispositivos", icon: "🖥️", available: puedeGestionarDispositivos},
    { id: "/tipo-dispositivo", label: "Tipos Dispositivos", icon: "🛠️", available: puedeGestionarDispositivos },
    { id: "/seleccion-solicitudesTH", label: "Gestión de Solicitudes", icon: "📤", available: puedeGestionarSolicitudes },
    { id: "/vacaciones", label: "Vacaciones", icon: "🏖️", available: true },
    { id: "/requests", label: "Solicitudes", icon: "📩", available: true },
    { id: "/solicitud-dispositivo", label: "Solicitud Dispositivo", icon: "📱", available: true },
    { id: "/benefits", label: "Beneficios", icon: "🎁", available: true },
    { id: "/configuracion", label: "Configuración", icon: "⚙️", available: true },
   
  ].filter((o) => o.available);

  const initials =
    `${user?.nombre?.[0] ?? ""}${user?.apellido?.[0] ?? ""}`.trim() || "👤";

  return (
    <div className="h-screen bg-gray-50 dark:bg-gray-950 flex overflow-hidden">
      {/* Sidebar */}
      <aside className="w-64 bg-white dark:bg-gray-900 shadow-xl border-r border-gray-200 dark:border-gray-800 flex flex-col">
        {/* Perfil compacto */}
        <div className="p-6 border-b border-gray-200 dark:border-gray-800 flex-shrink-0">
          <div className="flex items-center space-x-3">
            <div className="w-12 h-12 bg-gradient-to-r from-blue-600 to-blue-800 rounded-full flex items-center justify-center text-white font-bold text-lg">
              {initials}
            </div>
            <div>
              <p className="font-semibold text-gray-800 dark:text-gray-100 line-clamp-1">
                {user?.nombre}
              </p>
              <p className="text-sm text-gray-600 dark:text-gray-400">
                {user?.rol?.nombre}
              </p>
            </div>
          </div>
        </div>

        {/* Menú con scroll personalizado */}
        <nav className="flex-1 overflow-y-auto mt-6 scrollbar-thin scrollbar-track-transparent scrollbar-thumb-gray-300 dark:scrollbar-thumb-gray-600 hover:scrollbar-thumb-gray-400 dark:hover:scrollbar-thumb-gray-500">
          <style jsx>{`
            /* Estilos personalizados para el scrollbar */
            nav::-webkit-scrollbar {
              width: 6px;
            }
            
            nav::-webkit-scrollbar-track {
              background: transparent;
            }
            
            nav::-webkit-scrollbar-thumb {
              background-color: rgb(209 213 219); /* gray-300 */
              border-radius: 3px;
              transition: background-color 0.2s ease;
            }
            
            nav::-webkit-scrollbar-thumb:hover {
              background-color: rgb(156 163 175); /* gray-400 */
            }
            
            /* Estilos para modo oscuro */
            .dark nav::-webkit-scrollbar-thumb {
              background-color: rgb(75 85 99); /* gray-600 */
            }
            
            .dark nav::-webkit-scrollbar-thumb:hover {
              background-color: rgb(107 114 128); /* gray-500 */
            }
            
            /* Para Firefox */
            nav {
              scrollbar-width: thin;
              scrollbar-color: rgb(209 213 219) transparent;
            }
            
            .dark nav {
              scrollbar-color: rgb(75 85 99) transparent;
            }
          `}</style>
          
          {menuOptions.map((opt) => (
            <NavLink
              key={opt.id}
              to={opt.id}
              end={opt.end}
              className={({ isActive }) =>
                [
                  "w-full text-left px-6 py-3 flex items-center space-x-3 transition-colors",
                  isActive
                    ? "bg-blue-50 dark:bg-blue-900/30 border-r-2 border-blue-600 text-blue-700 dark:text-blue-200"
                    : "text-gray-700 dark:text-gray-200 hover:bg-gray-50 dark:hover:bg-gray-800",
                ].join(" ")
              }
            >
              <span className="text-xl">{opt.icon}</span>
              <span className="font-medium">{opt.label}</span>
            </NavLink>
          ))}
        </nav>

        {/* Logout */}
        <div className="p-6 border-t border-gray-200 dark:border-gray-800 bg-white dark:bg-gray-900 flex-shrink-0">
          <button
            onClick={logout}
            className="w-full flex items-center space-x-3 px-4 py-2 text-red-600 dark:text-red-400 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-lg transition-colors"
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