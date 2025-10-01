// src/pages/UsuariosPage.tsx
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useUsuarios } from "../hooks/usuarios/useUsuarios";
import { useCatalogos } from "../hooks/catalogos/useCatalogos";
import { tieneRol } from "../utils/permisos";
import { Roles } from "../types/roles";

import type { UsuarioItem } from "../types"; 
import DataTable from "../components/DataTable";
import PaginationFooter from "../components/PaginationFooter";
import SelectDropdown from "../components/SelectDropdown";
import IconButton from "../components/IconButton";
import PageLayout from "../layouts/PageLayout";

export default function UsuariosPage() {
  const { token, user } = useAuth();
  const navigate = useNavigate();

  // Filtros
  const [rolId, setRolId] = useState("");
  const [equipoId, setEquipoId] = useState("");
  const [cargoId, setCargoId] = useState("");
  const [page, setPage] = useState(0);

  // Permisos
  const puedeVerUsuarios = tieneRol(user, Roles.TH, Roles.GTH, Roles.OPERACIONES);

  // Catálogos
  const { roles, cargos, equipos, loading: loadingCatalogos } = useCatalogos(token);

  // Usuarios (hook especializado)
  const {
    data: usuarios,
    totalPages,
    loading: loadingUsuarios,
    error,
  } = useUsuarios(
    token,
    { rolId, equipoId, cargoId }, // filtros
    page,
    10
  );

  const limpiarFiltros = () => {
    setRolId("");
    setEquipoId("");
    setCargoId("");
    setPage(0);
  };

  const columns = [
    { key: "id", label: "ID" },
    {
      key: "nombre",
      label: "Nombre",
      render: (u: UsuarioItem) => `${u.nombre} ${u.apellido}`,
    },
    { key: "correo", label: "Correo" },
    {
      key: "antiguedadPretty",
      label: "Antigüedad",
      render: (u: UsuarioItem) => u.antiguedadPretty ?? "-",
    },
    {
      key: "estado",
      label: "Estado",
      render: (u: UsuarioItem) =>
        u.estado ? (
          <span className="px-2 py-1 text-xs font-medium bg-green-100 text-green-700 rounded-full">
            Activo
          </span>
        ) : (
          <span className="px-2 py-1 text-xs font-medium bg-red-100 text-red-700 rounded-full">
            Inactivo
          </span>
        ),
    },
  ];

  const renderActions = (u: UsuarioItem) => (
    <button
      onClick={() => navigate(`/usuarios/${u.id}`)}
      className="px-3 py-1 bg-blue-600 text-white rounded-lg text-xs hover:bg-blue-700 transition"
    >
      Editar
    </button>
  );

  if (!puedeVerUsuarios) return <p>No tenés permisos para ver esta página.</p>;
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
        <SelectDropdown
          name="rol"
          label="Rol"
          value={rolId}
          onChange={(e) => setRolId(e.target.value)}
          options={roles.map((r) => ({ value: r.id, label: r.nombre }))}
          placeholder="Filtrar por Rol"
        />
        <SelectDropdown
          name="cargo"
          label="Cargo"
          value={cargoId}
          onChange={(e) => setCargoId(e.target.value)}
          options={cargos.map((c) => ({ value: c.id, label: c.nombre }))}
          placeholder="Filtrar por Cargo"
        />
        <SelectDropdown
          name="equipo"
          label="Equipo"
          value={equipoId}
          onChange={(e) => setEquipoId(e.target.value)}
          options={equipos.map((e) => ({ value: e.id, label: e.nombre }))}
          placeholder="Filtrar por Equipo"
        />
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
        columns={columns}
        rowKey={(u) => u.id}
        actions={renderActions}
        scrollable={false}
      />

      <PaginationFooter
        currentPage={page}
        totalPages={totalPages}
        onPageChange={setPage}
        onCancel={() => navigate(-1)}
      />
    </PageLayout>
  );
}
