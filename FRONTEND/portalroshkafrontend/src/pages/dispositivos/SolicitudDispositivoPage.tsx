import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { tieneRol } from "../../utils/permisos";
import { Roles } from "../../types/roles";

import type { SolicitudDispositivoItem } from "../../types";
import DataTable from "../../components/DataTable";
import PaginationFooter from "../../components/PaginationFooter";
import IconButton from "../../components/IconButton";
import PageLayout from "../../layouts/PageLayout";
import { solicitudDispositivoColumns } from "../../config/tables/solicitudDispositivoTableConfig";
import { useSolicitudesDispositivo } from "../../hooks/deviceRequest/useSolicitudesDispositivo";

export default function SolicitudDispositivoPage() {
  const { token, user } = useAuth();
  const navigate = useNavigate();

  // Paginación
  const [page, setPage] = useState(0);

  // Permisos
  const puedeVerSolicitudes = tieneRol(user, Roles.SYSADMIN, Roles.TH, Roles.GTH);

  // Hook especializado: solo solicitudes de tipo "Dispositivo"
  const {
    data: solicitudes,
    totalPages,
    loading,
    error,
  } = useSolicitudesDispositivo(token, {}, page, 10);

  // Render de acciones por fila
  const renderActions = (s: SolicitudDispositivoItem) => {
    return (
      <>
        <button
          onClick={() => navigate(`/solicitudes-dispositivo/${s.id_solicitud}?readonly=true`)}
          className="px-3 py-1 bg-gray-500 text-white rounded-lg text-xs hover:bg-gray-600 transition"
        >
          Ver
        </button>
        {!tieneRol(user, Roles.OPERACIONES) && (
          <button
            onClick={() => navigate(`/solicitudes-dispositivo/${s.id_solicitud}`)}
            className="ml-2 px-3 py-1 bg-blue-600 text-white rounded-lg text-xs hover:bg-blue-700 transition"
          >
            Editar
          </button>
        )}
      </>
    );
  };

  if (!puedeVerSolicitudes) return <p>No tenés permisos para ver esta página.</p>;
  if (loading) return <p>Cargando solicitudes...</p>;
  if (error) return <p>{error}</p>;

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
        onCancel={() => navigate("/home")}
      />
    </PageLayout>
  );
}
