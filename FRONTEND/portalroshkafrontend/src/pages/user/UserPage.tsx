import { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { useUsuarios } from "../../hooks/usuarios/useUsuarios";
import { useCatalogosUsuarios } from "../../hooks/catalogos/useCatalogosUsuarios";
import { tieneRol } from "../../utils/permisos";
import { Roles } from "../../types/roles";

import type { UsuarioItem } from "../../types";
import DataTable from "../../components/DataTable";
import PaginationFooter from "../../components/PaginationFooter";
import IconButton from "../../components/IconButton";
import PageLayout from "../../layouts/PageLayout";
import { usuariosColumns } from "../../config/tables/usuariosTableConfig";
import Toast from "../../components/Toast";

export default function UserPage() {
  const { token, user } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const [sortBy, setSortBy] = useState<"active" | "inactive" | "rol" | "cargo" | "">("");
  const [page, setPage] = useState(0);
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  if (!token) return <p>No autorizado</p>;

  // Catálogos
  const { roles, cargos, loading: loadingCatalogos } = useCatalogosUsuarios(token);

  // Usuarios
  const {
    data: usuarios,
    totalPages,
    loading: loadingUsuarios,
    error,
  } = useUsuarios(token, sortBy ? { sortBy } : {}, page, 10);

  const limpiarFiltros = () => {
    setSortBy("");
    setPage(0);
  };

  const renderActions = (u: UsuarioItem) => {
    if (tieneRol(user, Roles.OPERACIONES)) {
      return (
        <button
          onClick={() => navigate(`/usuarios/${u.idUsuario}?readonly=true`)}
          className="px-3 py-1 bg-gray-500 text-white rounded-lg text-xs hover:bg-gray-600 transition"
        >
          Ver
        </button>
      );
    }

    return (
      <button
        onClick={() => navigate(`/usuarios/${u.idUsuario}`)}
        className="px-3 py-1 bg-blue-600 text-white rounded-lg text-xs hover:bg-blue-700 transition"
      >
        Editar
      </button>
    );
  };

  // ✅ Mostrar toast en base a query param success
  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const success = params.get("success");

    if (success === "created") {
      setToastMessage("✅ Usuario creado con éxito");
    } else if (success === "updated") {
      setToastMessage("✅ Usuario actualizado con éxito");
    }
  }, [location.search]);

  if (loadingUsuarios || loadingCatalogos) return <p>Cargando usuarios...</p>;
  if (error) return <p>{error}</p>;

  return (
    <PageLayout
      title="Listado de usuarios"
      actions={
        <IconButton
          label="Crear Usuario"
          icon={<span>➕</span>}
          variant="primary"
          onClick={() => navigate("/usuarios/buscar")}
          className="h-10 text-sm px-4 flex items-center"
        />
      }
    >
      <div className="flex items-center gap-4 mb-4">
        <IconButton
          label="Limpiar filtros"
          icon={<span>🧹</span>}
          variant="secondary"
          onClick={limpiarFiltros}
          className="h-10 text-sm px-4 flex items-center"
        />
      </div>

      <DataTable
        data={usuarios}
        columns={usuariosColumns}
        rowKey={(u) => u.idUsuario}
        actions={renderActions}
        scrollable={false}
      />

      <PaginationFooter
        currentPage={page}
        totalPages={totalPages}
        onPageChange={setPage}
        onCancel={() => navigate("/home")}
      />

      {/* ✅ Toast flotante */}
      {toastMessage && (
        <Toast message={toastMessage} onClose={() => setToastMessage(null)} />
      )}
    </PageLayout>
  );
}
