import { useMemo } from "react";
import { useSearchParams } from "react-router-dom";
import PageLayout from "../../layouts/PageLayout";
import SolicitudDispositivoPage from "./SolicitudDispositivoPage";
import DeviceAssignmentsPage from "./DeviceAssignmentsPage";
import { tieneRol } from "../../utils/permisos";
import { Roles } from "../../types/roles";
import { useAuth } from "../../context/AuthContext";

type TabKey = "solicitudes" | "asignaciones";

export default function GestionDispositivosPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const { user } = useAuth();

  // permisos
  const puedeVerSolicitudes = tieneRol(user, Roles.ADMINISTRADOR_DEL_SISTEMA);
  const puedeVerAsignaciones = tieneRol(
    user,
    Roles.ADMINISTRADOR_DEL_SISTEMA,
    Roles.OPERACIONES
  );

  // Tab activa, forzando a la primera disponible según permisos
  const rawTab = searchParams.get("tab");
  const activeTab: TabKey = useMemo(() => {
    if (rawTab === "asignaciones" && puedeVerAsignaciones) return "asignaciones";
    if (rawTab === "solicitudes" && puedeVerSolicitudes) return "solicitudes";

    // fallback: si no hay permisos, null
    if (puedeVerSolicitudes) return "solicitudes";
    if (puedeVerAsignaciones) return "asignaciones";
    return null as unknown as TabKey;
  }, [rawTab, puedeVerSolicitudes, puedeVerAsignaciones]);

  const switchTab = (next: TabKey) => {
    setSearchParams({ tab: next }, { replace: true });
  };

  //  sin permisos
  if (!puedeVerSolicitudes && !puedeVerAsignaciones) {
    return <p>No tenés permisos para ver esta página.</p>;
  }

  return (
    <PageLayout
      title="Gestión de Dispositivos"
      actions={
        <div className="flex gap-2">
          {puedeVerSolicitudes && (
            <button
              onClick={() => switchTab("solicitudes")}
              className={`px-4 py-2 rounded-lg text-sm font-medium ${
                activeTab === "solicitudes"
                  ? "bg-blue-600 text-white"
                  : "bg-gray-200 text-gray-800 hover:bg-gray-300"
              }`}
            >
              Solicitudes
            </button>
          )}
          {puedeVerAsignaciones && (
            <button
              onClick={() => switchTab("asignaciones")}
              className={`px-4 py-2 rounded-lg text-sm font-medium ${
                activeTab === "asignaciones"
                  ? "bg-blue-600 text-white"
                  : "bg-gray-200 text-gray-800 hover:bg-gray-300"
              }`}
            >
              Asignaciones
            </button>
          )}
        </div>
      }
    >
      <div className="mt-4">
        {activeTab === "solicitudes" && (
          <SolicitudDispositivoPage embedded forceSysAdmin />
        )}
        {activeTab === "asignaciones" && <DeviceAssignmentsPage embedded />}
      </div>
    </PageLayout>
  );
}
