import { useMemo } from "react";
import { useSearchParams } from "react-router-dom";
import PageLayout from "../../layouts/PageLayout";
import SolicitudDispositivoPage from "./SolicitudDispositivoPage";
import DeviceAssignmentsPage from "./DeviceAssignmentsPage";

type TabKey = "solicitudes" | "asignaciones";

export default function GestionDispositivosPage() {
  const [searchParams, setSearchParams] = useSearchParams();

  const rawTab = searchParams.get("tab");
  const activeTab: TabKey = useMemo(() => {
    return rawTab === "asignaciones" ? "asignaciones" : "solicitudes";
  }, [rawTab]);

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
      <div className="mt-4">
        {activeTab === "solicitudes" && <SolicitudDispositivoPage embedded />}
        {activeTab === "asignaciones" && <DeviceAssignmentsPage embedded />}
      </div>
    </PageLayout>
  );
}
