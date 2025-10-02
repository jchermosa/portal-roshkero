import { useMemo } from "react";
import { useSearchParams } from "react-router-dom";
import PageLayout from "../../layouts/PageLayout";

import ClientesPage from "../varios/ClientesPage";
import TecnologiasPage from "../varios/TecnologiasPage";
import { useAuth } from "../../context/AuthContext";
import { tieneRol } from "../../utils/permisos";
import { Roles } from "../../types/roles";

type TabKey = "clientes" | "tecnologias";

export default function CatalogoOpPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const { user } = useAuth();

  //  permisos
  const puedeVer = tieneRol(user, Roles.OPERACIONES);

  const rawTab = searchParams.get("tab");
  const activeTab: TabKey = useMemo<TabKey>(() => {
    return rawTab === "tecnologias" ? "tecnologias" : "clientes";
  }, [rawTab]);

  const switchTab = (next: TabKey) => {
    setSearchParams({ tab: next }, { replace: true });
  };

  //  sin permisos
  if (!puedeVer) {
    return <p>No tenés permisos para ver esta página.</p>;
  }

  return (
    <PageLayout
      title="Catálogo de Operaciones"
      actions={
        <div className="flex gap-2">
          <button
            onClick={() => switchTab("clientes")}
            className={`px-4 py-2 rounded-lg text-sm font-medium ${
              activeTab === "clientes"
                ? "bg-blue-600 text-white"
                : "bg-gray-200 text-gray-800 hover:bg-gray-300"
            }`}
          >
            Clientes
          </button>
          <button
            onClick={() => switchTab("tecnologias")}
            className={`px-4 py-2 rounded-lg text-sm font-medium ${
              activeTab === "tecnologias"
                ? "bg-blue-600 text-white"
                : "bg-gray-200 text-gray-800 hover:bg-gray-300"
            }`}
          >
            Tecnologías
          </button>
        </div>
      }
    >
      <div className="mt-4">
        {activeTab === "clientes" && <ClientesPage embedded />}
        {activeTab === "tecnologias" && <TecnologiasPage embedded />}
      </div>
    </PageLayout>
  );
}
