// src/pages/solicitudes/SolicitudDispositivoPage.tsx
import { useState } from "react";
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

import SolicitudDispositivoModal from "./SolicitudDispositivoModal";

export default function SolicitudDispositivoPage() {
  const { token, user } = useAuth();

  const [page, setPage] = useState(0);
  const [selected, setSelected] = useState<SolicitudDispositivoItem | null>(null);
  const [showModal, setShowModal] = useState(false);

  // ✅ Hook listado
  const {
    data: solicitudes,
    totalPages,
    loading,
    error,
    refresh,
  } = useSolicitudesDispositivo(token, {}, page, 10);

  const canEdit = !tieneRol(user, Roles.OPERACIONES);

  const renderActions = (s: SolicitudDispositivoItem) => (
    <div className="flex gap-2">
      <button
        onClick={() => {
          setSelected(s);
          setShowModal(true);
        }}
        className="px-3 py-1 bg-blue-600 text-white rounded-lg text-xs hover:bg-blue-700 transition"
      >
        Editar
      </button>
    </div>
  );

  return (
    <PageLayout
      title="Solicitudes de Dispositivo"
      actions={
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
      }
    >
      {/* Tabla */}
      <DataTable
        data={solicitudes}
        columns={solicitudDispositivoColumns}
        rowKey={(s) => s.idSolicitud}
        actions={canEdit ? renderActions : undefined}
        scrollable={false}
      />

      <PaginationFooter
        currentPage={page}
        totalPages={totalPages}
        onPageChange={setPage}
      />

      {/* Modal Form */}
      {showModal && (
        <SolicitudDispositivoModal
          token={token}
          userId={user?.id}
          id={selected?.idSolicitud}
          onClose={() => setShowModal(false)}
          onSaved={refresh}
        />
      )}

      {loading && <p>Cargando...</p>}
      {error && <p className="text-red-500">{error}</p>}
    </PageLayout>
  );
}
