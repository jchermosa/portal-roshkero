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
import { usuariosColumns } from "../config/forms/usuariosTableConfig";


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

  const columns = usuariosColumns;

    const renderActions = (u: UsuarioItem) => {
    if (tieneRol(user, Roles.OPERACIONES)) {
      // Solo ver
      return (
        <button
          onClick={() => navigate(`/usuarios/${u.id}?readonly=true`)}
          className="px-3 py-1 bg-gray-500 text-white rounded-lg text-xs hover:bg-gray-600 transition"
        >
          Ver
        </button>
      );
    }

    // Editar para otros roles
    return (
      <button
        onClick={() => navigate(`/usuarios/${u.id}`)}
        className="px-3 py-1 bg-blue-600 text-white rounded-lg text-xs hover:bg-blue-700 transition"
      >
        Editar
      </button>
    );
  };


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
        onCancel={() => navigate("/home")}
      />
    </PageLayout>
  );
}
