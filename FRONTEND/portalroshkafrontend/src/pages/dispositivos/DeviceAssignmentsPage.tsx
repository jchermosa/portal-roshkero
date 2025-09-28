// src/pages/dispositivos/DeviceAssignmentsPage.tsx
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

  const [page, setPage] = useState(0);

  // const puedeVerAsignaciones = tieneRol(user, Roles.SYSADMIN, Roles.OPERACIONES);

  // ✅ Hook especializado con paginación real
  const { data: asignaciones, totalPages, loading, error } =
    useDispositivosAsignados(token, page, 10);

  const renderActions = (d: DispositivoAsignadoItem) => {
    if (tieneRol(user, Roles.OPERACIONES)) {
      return (
        <button
          onClick={() =>
            navigate(`/dispositivos-asignados/${d.idDispositivoAsignado}?readonly=true`)
          }
          className="px-3 py-1 bg-gray-500 text-white rounded-lg text-xs hover:bg-gray-600 transition"
        >
          Ver
        </button>
      );
    }
    return (
      <button
        onClick={() => navigate(`/dispositivos-asignados/${d.idDispositivoAsignado}`)}
        className="px-3 py-1 bg-blue-600 text-white rounded-lg text-xs hover:bg-blue-700 transition"
      >
        Editar
      </button>
    );
  };

  // if (!puedeVerAsignaciones) return <p>No tenés permisos para ver esta página.</p>;
  if (loading) return <p>Cargando asignaciones...</p>;
  if (error) return <p>{error}</p>;

  const body = (
    <>
      <DataTable
        data={asignaciones}
        columns={dispositivosAsignadosColumns}
        rowKey={(d) => d.idDispositivoAsignado}
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

  // Modo página completa
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
