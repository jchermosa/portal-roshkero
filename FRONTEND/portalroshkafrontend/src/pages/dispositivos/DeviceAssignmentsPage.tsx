import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { useDispositivosAsignados } from "../../hooks/dispositivosAsignados/useDispositivosAsignados";
import { tieneRol } from "../../utils/permisos";
import { Roles } from "../../types/roles";
import { dispositivosAsignadosColumns } from "../../config/tables/dispositivosAsignadosTableConfig";

import type { DispositivoAsignadoItem } from "../../types";
import DataTable from "../../components/DataTable";
import PaginationFooter from "../../components/PaginationFooter";
import IconButton from "../../components/IconButton";
import PageLayout from "../../layouts/PageLayout";

interface Props {
  embedded?: boolean;
}

export default function DeviceAssignmentsPage({ embedded = false }: Props) {
  const { token, user } = useAuth();
  const navigate = useNavigate();

  const [estado, setEstado] = useState("");
  const [encargado, setEncargado] = useState("");
  const [page, setPage] = useState(0);

  const puedeVerAsignaciones = tieneRol(user, Roles.SYSADMIN, Roles.OPERACIONES);

  const { data: asignaciones, totalPages, loading, error } =
    useDispositivosAsignados(token, { estado, encargado }, page, 10);

  const renderActions = (d: DispositivoAsignadoItem) => {
    if (tieneRol(user, Roles.OPERACIONES)) {
      return (
        <button
          onClick={() =>
            navigate(`/dispositivos-asignados/${d.id_dispositivo_asignado}?readonly=true`)
          }
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

  const limpiarFiltros = () => {
    setEstado("");
    setEncargado("");
    setPage(0);
  };

  const body = (
    <>
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

      <DataTable
        data={asignaciones}
        columns={dispositivosAsignadosColumns}
        rowKey={(d) => d.id_dispositivo_asignado}
        actions={renderActions}
        scrollable={false}
      />

      <PaginationFooter
        currentPage={page}
        totalPages={totalPages}
        onPageChange={setPage}
      />
    </>
  );

  if (embedded) {
    return (
      <div>
        <div className="mb-3 flex justify-end">
          <IconButton
            label="Asignar Dispositivo"
            icon={<span>➕</span>}
            variant="primary"
            onClick={() => navigate("/dispositivos-asignados/nuevo")}
            className="h-10 text-sm px-4 flex items-center"
          />
        </div>
        {body}
      </div>
    );
  }

  // Modo página tradicional
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
      {body}
    </PageLayout>
  );
}
