// src/pages/dispositivos/TipoDispositivoPage.tsx
import { useMemo, useState } from "react";
import { useAuth } from "../../context/AuthContext";

import PageLayout from "../../layouts/PageLayout";
import DataTable, { type RowAction } from "../../components/DataTable";
import { MsIcon } from "../../components/MsIcon";
import PaginationFooter from "../../components/PaginationFooter";
import IconButton from "../../components/IconButton";
import { Alert } from "../../components/Alert";

import { useTiposDispositivo } from "../../hooks/dispositivos/useTiposDispositivo";
import { deleteTipoDispositivo } from "../../services/TipoDispositivoService";
import TipoDispositivoModal from "./TipoDispositivoModal";

import { tipoDispositivoColumns } from "../../config/tables/tipoDispositivoTableConfig";
import type { TipoDispositivoItem } from "../../types";

interface Props {
  embedded?: boolean;
}

export default function TipoDispositivoPage({ embedded = false }: Props) {
  const { token } = useAuth();

  const [page, setPage] = useState(0);
  const [selected, setSelected] = useState<TipoDispositivoItem | null>(null);
  const [showModal, setShowModal] = useState(false);
  const [deletingId, setDeletingId] = useState<number | null>(null);

  const params = useMemo(() => ({ page, size: 10 }), [page]);

  // listado + paginación (tu hook ya pagina en memoria)
  const {
    data: dispositivos,
    totalPages,
    loading,
    error,
    refresh,
  } = useTiposDispositivo(token, params.page, params.size);

  // abrir modal en edición
  const onEdit = (row: TipoDispositivoItem) => {
    setSelected(row);
    setShowModal(true);
  };

  // eliminar con confirm
  const onDelete = async (row: TipoDispositivoItem) => {
    if (!token) return;
    const ok = window.confirm(`¿Eliminar el tipo "${row.nombre}"?`);
    if (!ok) return;
    try {
      setDeletingId(row.idTipoDispositivo);
      await deleteTipoDispositivo(token, row.idTipoDispositivo);
      await refresh();
    } finally {
      setDeletingId(null);
    }
  };

  // acciones por fila con iconos (como Roles)
  const rowActions: RowAction<TipoDispositivoItem>[] = [
    { key: "edit", label: "Editar", icon: <MsIcon name="edit" />, onClick: onEdit, variant: "primary" },
    {
      key: "delete",
      label: "Eliminar",
      icon: <MsIcon name="delete" />,
      onClick: onDelete,
      variant: "danger",
      disabled: (row) => deletingId === row.idTipoDispositivo,
    },
  ];

  // botón "Nuevo tipo"
  const newButton = (
    <IconButton
      label="Nuevo Tipo"
      icon={<span>➕</span>}
      variant="primary"
      onClick={() => {
        setSelected(null);
        setShowModal(true);
      }}
      className="h-10 text-sm px-4 flex items-center"
    />
  );

  // cuerpo reutilizable (para modo embebido o página)
  const body = (
    <>
      {error && <Alert kind="error">{error}</Alert>}

      <DataTable<TipoDispositivoItem>
        data={dispositivos}
        columns={tipoDispositivoColumns}
        rowKey={(d) => d.idTipoDispositivo}
        rowActions={rowActions}
        scrollable={false}
      />

      <PaginationFooter
        currentPage={page}
        totalPages={totalPages}
        onPageChange={setPage}
      />

      {loading && <Alert kind="info">Cargando tipos de dispositivo…</Alert>}

      {showModal && (
        <TipoDispositivoModal
          token={token}
          id={selected?.idTipoDispositivo}
          onClose={() => setShowModal(false)}
          onSaved={refresh}
        />
      )}
    </>
  );

  if (embedded) {
    // versión empotrada dentro de otra página (como hicimos en Roles)
    return (
      <div>
        <div className="mb-3 flex justify-end">{newButton}</div>
        {body}
      </div>
    );
  }

  // versión página completa
  return (
    <PageLayout title="Tipos de Dispositivo" actions={newButton}>
      {body}
    </PageLayout>
  );
}
