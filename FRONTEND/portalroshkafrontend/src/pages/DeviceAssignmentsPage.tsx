import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useDispositivosAsignados } from "../hooks/dispositivosAsignados/useDispositivosAsignados";
import { tieneRol } from "../utils/permisos";
import { Roles } from "../types/roles";

import type { DispositivoAsignadoItem } from "../types";
import DataTable from "../components/DataTable";
import PaginationFooter from "../components/PaginationFooter";
import IconButton from "../components/IconButton";
import PageLayout from "../layouts/PageLayout";
import { dispositivosAsignadosColumns } from "../config/tables/dispositivosAsignadosTableConfig";

export default function DeviceAssignmentsPage() {
  const { token, user } = useAuth();
  const navigate = useNavigate();

  // Filtros (ejemplo: estado y encargado)
  const [estado, setEstado] = useState("");
  const [encargado, setEncargado] = useState("");
  const [page, setPage] = useState(0);

  // Permisos
  const puedeVerAsignaciones = tieneRol(user, Roles.SYSADMIN, Roles.OPERACIONES);

  // Hook especializado
  const {
    data: asignaciones,
    totalPages,
    loading,
    error,
  } = useDispositivosAsignados(
    token,
    { estado, encargado },
    page,
    10
  );

  const limpiarFiltros = () => {
    setEstado("");
    setEncargado("");
    setPage(0);
  };

  const columns = dispositivosAsignadosColumns;

  const renderActions = (d: DispositivoAsignadoItem) => {
    // SYSADMIN puede editar, OPERACIONES solo ver
    if (tieneRol(user, Roles.OPERACIONES)) {
      return (
        <button
          onClick={() => navigate(`/dispositivos-asignados/${d.id_dispositivo_asignado}?readonly=true`)}
          className="px-3 py-1 bg-gray-500 text-white rounded-lg text-xs hover:bg-gray-600 transition"
        >
          Ver
        </button>
      );
    }

    return (
      <button
        onClick={() => navigate(`/dispositivos-asignados/${d.id_dispositivo_asignado}`)}
        className="px-3 py-1 bg-blue-600 text-white rounded-lg text-xs hover:bg-blue-700 transition"
      >
        Editar
      </button>
    );
  };

  if (!puedeVerAsignaciones) return <p>No tenés permisos para ver esta página.</p>;
  if (loading) return <p>Cargando asignaciones...</p>;
  if (error) return <p>{error}</p>;

  return (
    <PageLayout
      title="Gestión de Dispositivos Asignados"
      actions={
        <IconButton
          label="Asignar Dispositivo"
          icon={<span>➕</span>}
          variant="primary"
          onClick={() => navigate("/dispositivos-asignados/nuevo")}
          className="h-10 text-sm px-4 flex items-center"
        />
      }
    >
      {/* Filtros simples */}
      <div className="flex items-center gap-4 mb-4">
        <input
          type="text"
          value={estado}
          onChange={(e) => setEstado(e.target.value)}
          placeholder="Filtrar por estado"
          className="px-3 py-2 border rounded-lg text-sm"
        />
        <input
          type="text"
          value={encargado}
          onChange={(e) => setEncargado(e.target.value)}
          placeholder="Filtrar por encargado"
          className="px-3 py-2 border rounded-lg text-sm"
        />
        <IconButton
          label="Limpiar filtros"
          icon={<span>🧹</span>}
          variant="secondary"
          onClick={limpiarFiltros}
          className="h-10 text-sm px-4 flex items-center"
        />
      </div>

      {/* Tabla */}
      <DataTable
        data={asignaciones}
        columns={columns}
        rowKey={(d) => d.id_dispositivo_asignado}
        actions={renderActions}
        scrollable={false}
      />

      {/* Paginación */}
      <PaginationFooter
        currentPage={page}
        totalPages={totalPages}
        onPageChange={setPage}
        onCancel={() => navigate("/home")}
      />
    </PageLayout>
  );
}
