import { useMemo } from "react";
import { useSearchParams } from "react-router-dom";
import PageLayout from "../../layouts/PageLayout";
import CargosPage from "./CargosPage";
import RolesPage from "./RolesPage";

type TabKey = "cargos" | "roles";

export default function CatalogoTHPage() {
  const [searchParams, setSearchParams] = useSearchParams();

  const rawTab = (searchParams.get("tab") || "").toLowerCase();
  const activeTab: TabKey = useMemo<TabKey>(() => {
    return rawTab === "roles" ? "roles" : "cargos";
  }, [rawTab]);

  const switchTab = (next: TabKey) => {
    setSearchParams({ tab: next }, { replace: true });
  };

  return (
    <PageLayout
      title="Gestión TH"
      actions={
        <div className="flex gap-2">
          <button
            onClick={() => switchTab("cargos")}
            className={`px-4 py-2 rounded-lg text-sm font-medium ${
              activeTab === "cargos"
                ? "bg-blue-600 text-white"
                : "bg-gray-200 text-gray-800 hover:bg-gray-300"
            }`}
            aria-pressed={activeTab === "cargos"}
          >
            Cargos
          </button>
          <button
            onClick={() => switchTab("roles")}
            className={`px-4 py-2 rounded-lg text-sm font-medium ${
              activeTab === "roles"
                ? "bg-blue-600 text-white"
                : "bg-gray-200 text-gray-800 hover:bg-gray-300"
            }`}
            aria-pressed={activeTab === "roles"}
          >
            Roles
          </button>
        </div>
      }
    >
      <div className="mt-4">
        {activeTab === "cargos" && <CargosPage embedded />}
        {activeTab === "roles" && <RolesPage embedded />}
      </div>
    </PageLayout>
  );
}
