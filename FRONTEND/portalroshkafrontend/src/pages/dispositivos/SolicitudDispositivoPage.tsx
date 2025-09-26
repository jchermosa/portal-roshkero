import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { tieneRol } from "../../utils/permisos";
import { Roles } from "../../types/roles";
import { useSolicitudesDispositivo } from "../../hooks/deviceRequest/useSolicitudesDispositivo";
import { solicitudDispositivoColumns } from "../../config/tables/solicitudDispositivoTableConfig";

import type { SolicitudDispositivoItem } from "../../types";
import DataTable from "../../components/DataTable";
import PaginationFooter from "../../components/PaginationFooter";
import IconButton from "../../components/IconButton";
import PageLayout from "../../layouts/PageLayout";

interface Props {
  embedded?: boolean;
}

export default function SolicitudDispositivoPage({ embedded = false }: Props) {
  const { token, user } = useAuth();
  const navigate = useNavigate();

  const [page, setPage] = useState(0);

  const { data: solicitudes, totalPages, loading, error } =
    useSolicitudesDispositivo(token, {}, page, 10);

  const canSee = true; // cualquier usuario puede ver sus solicitudes
  const canEdit = !tieneRol(user, Roles.OPERACIONES); // ejemplo

  const renderActions = (s: SolicitudDispositivoItem) => (
    <>
      <button
        onClick={() =>
          navigate(`/solicitud-dispositivo/${s.id_solicitud}?readonly=true`)
        }
        className="px-3 py-1 bg-gray-500 text-white rounded-lg text-xs hover:bg-gray-600 transition"
      >
        Ver
      </button>
      {canEdit && (
        <button
          onClick={() => navigate(`/solicitud-dispositivo/${s.id_solicitud}`)}
          className="ml-2 px-3 py-1 bg-blue-600 text-white rounded-lg text-xs hover:bg-blue-700 transition"
        >
          Editar
        </button>
      )}
    </>
  );

  if (!canSee) return <p>No tenés permisos para ver esta página.</p>;
  if (loading) return <p>Cargando solicitudes...</p>;
  if (error) return <p>{error}</p>;

  const body = (
    <>
      <DataTable
        data={solicitudes}
        columns={solicitudDispositivoColumns}
        rowKey={(s) => s.id_solicitud}
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

  // 👉 Modo embebido: sin botón "Nueva Solicitud"
  if (embedded) {
    return <div>{body}</div>;
  }

  // 👉 Modo página completa: con botón "Nueva Solicitud"
  return (
    <PageLayout
      title="Solicitudes de Dispositivo"
      actions={
        <IconButton
          label="Nueva Solicitud"
          icon={<span>➕</span>}
          variant="primary"
          onClick={() => navigate("/solicitud-dispositivo/nuevo")}
          className="h-10 text-sm px-4 flex items-center"
        />
      }
    >
      {body}
    </PageLayout>
  );
}
