import { useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { tieneRol } from "../../utils/permisos";
import { Roles } from "../../types/roles";
import { useSolicitudesDispositivo } from "../../hooks/deviceRequest/useSolicitudesDispositivo";
import { buildSolicitudDispositivoColumns } from "../../config/tables/solicitudDispositivoTableConfig";

import type { SolicitudDispositivoUI } from "../../types";
import DataTable from "../../components/DataTable";
import PaginationFooter from "../../components/PaginationFooter";
import IconButton from "../../components/IconButton";
import PageLayout from "../../layouts/PageLayout";

import SolicitudDispositivoModal from "./SolicitudDispositivoModal";

interface Props {
  embedded?: boolean;
}

export default function SolicitudDispositivoPage({ embedded = false }: Props) {
  const { token, user } = useAuth();

  const [page, setPage] = useState(0);
  const [selected, setSelected] = useState<SolicitudDispositivoUI | null>(null);
  const [showModal, setShowModal] = useState(false);

  const isSysAdmin = tieneRol(user, Roles.ADMINISTRADOR_DEL_SISTEMA);

  // Hook listado
  const {
    data: solicitudes,
    totalPages,
    loading,
    error,
    refresh,
  } = useSolicitudesDispositivo(token, page, 10, isSysAdmin);

  const canEdit = !tieneRol(user, Roles.OPERACIONES);

  const renderActions = (s: SolicitudDispositivoUI) => {
    if (s.estado === "A" || s.estado === "R") return null;

    return (
      <div className="flex gap-2">
        <button
          onClick={() => {
            setSelected(s);
            setShowModal(true);
          }}
          className="px-3 py-1 bg-blue-600 text-white rounded-lg text-xs hover:bg-blue-700 transition"
        >
          {isSysAdmin ? "Gestionar" : "Editar"}
        </button>
      </div>
    );
  };

  const newSolicitudButton = !embedded && !isSysAdmin && (
    <IconButton
      label="Nueva Solicitud"
      icon={<span>➕</span>}
      variant="primary"
      onClick={() => {
        setSelected(null);
        setShowModal(true);
      }}
      className="h-10 text-sm px-4 flex items-center"
    />
  );

  const body = (
    <>
      <DataTable<SolicitudDispositivoUI>
        data={solicitudes}
        columns={buildSolicitudDispositivoColumns(isSysAdmin)}
        rowKey={(s) => s.idSolicitud}
        actions={canEdit ? renderActions : undefined}
        scrollable={false}
      />

      {/* Solo admins tienen paginación */}
      {isSysAdmin && (
        <PaginationFooter
          currentPage={page}
          totalPages={totalPages}
          onPageChange={setPage}
        />
      )}

      {/* Modal Form */}
      {showModal && (
        <SolicitudDispositivoModal
          token={token}
          id={selected?.idSolicitud}
          gestion={isSysAdmin} // 🔹 ahora usamos el rol real, no el campo fuente
          onClose={() => setShowModal(false)}
          onSaved={refresh}
        />
      )}

      {loading && <p>Cargando...</p>}
      {error && <p className="text-red-500">{error}</p>}
    </>
  );

  if (embedded) {
    return (
      <div>
        <div className="mb-3 flex justify-end">{newSolicitudButton}</div>
        {body}
      </div>
    );
  }

  return (
    <PageLayout title="Solicitudes de Dispositivo" actions={newSolicitudButton}>
      {body}
    </PageLayout>
  );
}
