import { useEffect, useMemo } from "react";
import { useSearchParams } from "react-router-dom";
import PageLayout from "../../layouts/PageLayout";
import SolicitudDispositivoPage from "./SolicitudDispositivoPage";
import DeviceAssignmentsPage from "./DeviceAssignmentsPage";

type TabKey = "solicitudes" | "asignaciones";

export default function GestionDispositivosPage() {
  const [searchParams, setSearchParams] = useSearchParams();

  const activeTab: TabKey = useMemo(() => {
    const t = (searchParams.get("tab") || "solicitudes") as TabKey;
    return (t === "asignaciones" || t === "solicitudes") ? t : "solicitudes";
  }, [searchParams]);

  // Si no hay tab en la URL, fijamos el default
  useEffect(() => {
    if (!searchParams.get("tab")) {
      setSearchParams({ tab: "solicitudes" }, { replace: true });
    }
  }, [searchParams, setSearchParams]);

  const switchTab = (next: TabKey) => {
    setSearchParams({ tab: next }, { replace: true });
  };

  return (
    <PageLayout
      title="Gestión de Dispositivos"
      actions={
        <div className="flex gap-2">
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
        </div>
      }
    >
      {/* Render embed para no duplicar PageLayout */}
      <div className="mt-4">
        {activeTab === "solicitudes" && <SolicitudDispositivoPage embedded />}
        {activeTab === "asignaciones" && <DeviceAssignmentsPage embedded />}
      </div>
    </PageLayout>
  );
}
