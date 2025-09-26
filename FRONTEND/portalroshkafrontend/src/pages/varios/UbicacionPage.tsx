import { useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { useUbicaciones } from "../../hooks/ubicaciones/useUbicaciones";

import PageLayout from "../../layouts/PageLayout";
import DataTable from "../../components/DataTable";
import PaginationFooter from "../../components/PaginationFooter";
import IconButton from "../../components/IconButton";
import { ubicacionColumns } from "../../config/tables/ubicacionTableConfig";

import UbicacionModal from "./UbicacionModal";

import type { UbicacionItem } from "../../types";

export default function UbicacionPage() {
  const { token } = useAuth();
  const [page, setPage] = useState(0);
  const [selected, setSelected] = useState<UbicacionItem | null>(null);
  const [showModal, setShowModal] = useState(false);

  // ✅ Hook listado
  const {
    data: ubicaciones,
    totalPages,
    loading,
    error,
    refresh,
  } = useUbicaciones(token, page, 10);

  const columns = ubicacionColumns;

  const renderActions = (u: UbicacionItem) => (
    <div className="flex gap-2">
      <button
        onClick={() => {
          setSelected(u);
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
      title="Ubicaciones"
      actions={
        <IconButton
          label="Nueva ubicación"
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
        data={ubicaciones}
        columns={columns}
        rowKey={(u) => u.id_ubicacion}
        actions={renderActions}
        scrollable={false}
      />

      <PaginationFooter
        currentPage={page}
        totalPages={totalPages}
        onPageChange={setPage}
      />

      {/* Modal Form */}
      {showModal && (
        <UbicacionModal
          token={token}
          id={selected?.id_ubicacion}
          onClose={() => setShowModal(false)}
          onSaved={refresh}
        />
      )}

      {loading && <p>Cargando...</p>}
      {error && <p className="text-red-500">{error}</p>}
    </PageLayout>
  );
}
