import { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { useUsuarios } from "../../hooks/usuarios/useUsuarios";
import { useCatalogosUsuarios } from "../../hooks/catalogos/useCatalogosUsuarios";
import { tieneRol } from "../../utils/permisos";
import { Roles } from "../../types/roles";
import { EstadoLabels } from "../../types";

import type { UsuarioItem } from "../../types";
import DataTable, { type RowAction } from "../../components/DataTable";
import PaginationFooter from "../../components/PaginationFooter";
import IconButton from "../../components/IconButton";
import PageLayout from "../../layouts/PageLayout";
import { usuariosColumns } from "../../config/tables/usuariosTableConfig";
import Toast from "../../components/Toast";
import SelectDropdown from "../../components/SelectDropdown";
import { MsIcon } from "../../components/MsIcon";

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

  // Acciones con íconos
  const canEdit = !tieneRol(user, Roles.OPERACIONES);

  const onEdit = (u: UsuarioItem) => navigate(`/usuarios/${u.idUsuario}`);
  const onView = (u: UsuarioItem) => navigate(`/usuarios/${u.idUsuario}?readonly=true`);

  const rowActions: RowAction<UsuarioItem>[] = canEdit
    ? [
        {
          key: "edit",
          label: "Editar",
          icon: <MsIcon name="edit" />,
          onClick: onEdit,
          variant: "primary",
        },
      ]
    : [
        {
          key: "view",
          label: "Ver",
          icon: <MsIcon name="visibility" />,
          onClick: onView,
          variant: "secondary",
        },
      ];

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
        canEdit && (
          <IconButton
            label="Crear Usuario"
            icon={<span>➕</span>}
            variant="primary"
            onClick={() => navigate("/usuarios/buscar")}
            className="h-10 text-sm px-4 flex items-center"
          />
        )
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
          noMargin
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
          noMargin
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
          noMargin
        />

       <div className="h-full flex items-end">
          <IconButton
            label="Limpiar filtros"
            icon={<span>🧹</span>}
            variant="secondary"
            onClick={limpiarFiltros}
            className="h-10 text-sm px-4 flex items-center"
          />
        </div>
      </div>

      {/* 🔽 Tabla */}
      <DataTable<UsuarioItem>
        data={usuarios}
        columns={usuariosColumns}
        rowKey={(u) => u.idUsuario}
        rowActions={rowActions}
        scrollable={false}
      />

      {/* 🔽 Paginación */}
      <PaginationFooter
        currentPage={page}
        totalPages={totalPages}
        onPageChange={setPage}
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
