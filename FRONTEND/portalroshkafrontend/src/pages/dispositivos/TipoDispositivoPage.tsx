// src/pages/dispositivos/TipoDispositivoPage.tsx
import { useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { useTiposDispositivo } from "../../hooks/dispositivos/useTiposDispositivo";

import PageLayout from "../../layouts/PageLayout";
import DataTable from "../../components/DataTable";
import PaginationFooter from "../../components/PaginationFooter";
import IconButton from "../../components/IconButton";
import { tipoDispositivoColumns } from "../../config/tables/tipoDispositivoTableConfig";

import TipoDispositivoModal from "./TipoDispositivoModal";

import type { TipoDispositivoItem } from "../../types";

export default function TipoDispositivoPage() {
  const { token } = useAuth();
  const [page, setPage] = useState(0);
  const [selected, setSelected] = useState<TipoDispositivoItem | null>(null);
  const [showModal, setShowModal] = useState(false);

  // ✅ Hook listado
  const {
    data: dispositivos,
    totalPages,
    loading,
    error,
    refresh,
  } = useTiposDispositivo(token, page, 10);

  const columns = tipoDispositivoColumns;

  const renderActions = (d: TipoDispositivoItem) => (
    <div className="flex gap-2">
      <button
        onClick={() => {
          setSelected(d);
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
      title="Tipos de Dispositivo"
      actions={
        <IconButton
          label="Nuevo tipo"
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
        data={dispositivos}
        columns={columns}
        rowKey={(d) => d.id_tipo_dispositivo}
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
        <TipoDispositivoModal
          token={token}
          id={selected?.id_tipo_dispositivo}
          onClose={() => setShowModal(false)}
          onSaved={refresh}
        />
      )}

      {loading && <p>Cargando...</p>}
      {error && <p className="text-red-500">{error}</p>}
    </PageLayout>
  );
}
