import { useMemo } from "react";
import { useSearchParams } from "react-router-dom";
import PageLayout from "../../layouts/PageLayout";
import TipoDispositivoPage from "../dispositivos/TipoDispositivoPage";
import UbicacionPage from "./UbicacionPage";
import { useAuth } from "../../context/AuthContext";
import { tieneRol } from "../../utils/permisos";
import { Roles } from "../../types/roles";

type TabKey = "tipos" | "ubicaciones";

export default function CatalogoSysPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const { user } = useAuth();

  // permisos (solo ADMIN DEL SISTEMA)
  const puedeVer = tieneRol(user, Roles.ADMINISTRADOR_DEL_SISTEMA);

  const rawTab = searchParams.get("tab");
  const activeTab: TabKey = useMemo<TabKey>(() => {
    return rawTab === "ubicaciones" ? "ubicaciones" : "tipos";
  }, [rawTab]);

  const switchTab = (next: TabKey) => {
    setSearchParams({ tab: next }, { replace: true });
  };

  // sin permisos
  if (!puedeVer) {
    return <p>No tenés permisos para ver esta página.</p>;
  }

  return (
    <PageLayout
      title="Catálogo de Administrador de Sistemas"
      actions={
        <div className="flex gap-2">
          <button
            onClick={() => switchTab("tipos")}
            className={`px-4 py-2 rounded-lg text-sm font-medium ${
              activeTab === "tipos"
                ? "bg-blue-600 text-white"
                : "bg-gray-200 text-gray-800 hover:bg-gray-300"
            }`}
          >
            Tipos de dispositivo
          </button>
          <button
            onClick={() => switchTab("ubicaciones")}
            className={`px-4 py-2 rounded-lg text-sm font-medium ${
              activeTab === "ubicaciones"
                ? "bg-blue-600 text-white"
                : "bg-gray-200 text-gray-800 hover:bg-gray-300"
            }`}
          >
            Ubicaciones
          </button>
        </div>
      }
    >
      <div className="mt-4">
        {activeTab === "tipos" && <TipoDispositivoPage embedded />}
        {activeTab === "ubicaciones" && <UbicacionPage embedded />}
      </div>
    </PageLayout>
  );
}
