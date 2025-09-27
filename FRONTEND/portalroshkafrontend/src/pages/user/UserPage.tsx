import { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { useUsuarios } from "../../hooks/usuarios/useUsuarios";
import { useCatalogosUsuarios } from "../../hooks/catalogos/useCatalogosUsuarios";
import { tieneRol } from "../../utils/permisos";
import { Roles } from "../../types/roles";
import { EstadoLabels } from "../../types";

import type { UsuarioItem } from "../../types";
import DataTable from "../../components/DataTable";
import PaginationFooter from "../../components/PaginationFooter";
import IconButton from "../../components/IconButton";
import PageLayout from "../../layouts/PageLayout";
import { usuariosColumns } from "../../config/tables/usuariosTableConfig";
import Toast from "../../components/Toast";
import SelectDropdown from "../../components/SelectDropdown";

export default function UserPage() {
  const { token, user } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const [filtros, setFiltros] = useState<{ idRol?: number; idCargo?: number; estado?: "A" | "I" }>({});
  const [page, setPage] = useState(0);

  // ✅ Toast con tipo
  const [toastMessage, setToastMessage] = useState<string | null>(null);
  const [toastType, setToastType] = useState<"success" | "error" | "info" | "warning">("info");

  if (!token) return <p>No autorizado</p>;

  // Catálogos
  const { roles, cargos, loading: loadingCatalogos } = useCatalogosUsuarios(token);

  // Usuarios
  const {
    data: usuarios,
    totalPages,
    loading: loadingUsuarios,
    error,
  } = useUsuarios(token, filtros, page, 10);

  const limpiarFiltros = () => {
    setFiltros({});
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
      setToastType("success");
    } else if (success === "updated") {
      setToastMessage("✅ Usuario actualizado con éxito");
      setToastType("success");
    }
  }, [location.search]);

  if (loadingUsuarios || loadingCatalogos) return <p>Cargando usuarios...</p>;
  if (error) {
    return (
      <>
        <p>{error}</p>
        <Toast message="❌ Error al cargar usuarios" type="error" onClose={() => {}} />
      </>
    );
  }

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
      {/* 🔽 Filtros */}
      <div className="grid grid-cols-1 sm:grid-cols-4 gap-4 mb-4">
        <SelectDropdown
          label="Rol"
          name="idRol"
          value={filtros.idRol ?? ""}
          onChange={(e) =>
            setFiltros((prev) => ({
              ...prev,
              idRol: e.target.value ? Number(e.target.value) : undefined,
            }))
          }
          options={roles.map((r) => ({ value: r.idRol, label: r.nombre }))}
          placeholder="Todos"
        />

        <SelectDropdown
          label="Cargo"
          name="idCargo"
          value={filtros.idCargo ?? ""}
          onChange={(e) =>
            setFiltros((prev) => ({
              ...prev,
              idCargo: e.target.value ? Number(e.target.value) : undefined,
            }))
          }
          options={cargos.map((c) => ({ value: c.idCargo, label: c.nombre }))}
          placeholder="Todos"
        />

        <SelectDropdown
          label="Estado"
          name="estado"
          value={filtros.estado ?? ""}
          onChange={(e) =>
            setFiltros((prev) => ({
              ...prev,
              estado: e.target.value as "A" | "I" | undefined,
            }))
          }
          options={Object.entries(EstadoLabels).map(([value, label]) => ({
            value,
            label,
          }))}
          placeholder="Todos"
        />

        <div className="flex items-end">
          <IconButton
            label="Limpiar filtros"
            icon={<span>🧹</span>}
            variant="secondary"
            onClick={limpiarFiltros}
            className="h-10 text-sm px-4 flex items-center w-full"
          />
        </div>
      </div>

      {/* 🔽 Tabla */}
      <DataTable
        data={usuarios}
        columns={usuariosColumns}
        rowKey={(u) => u.idUsuario}
        actions={renderActions}
        scrollable={false}
      />

      {/* 🔽 Paginación */}
      <PaginationFooter
        currentPage={page}
        totalPages={totalPages}
        onPageChange={setPage}
        onCancel={() => navigate("/home")}
      />

      {/* 🔽 Toast flotante */}
      {toastMessage && (
        <Toast
          message={toastMessage}
          type={toastType}
          onClose={() => setToastMessage(null)}
        />
      )}
    </PageLayout>
  );
}
