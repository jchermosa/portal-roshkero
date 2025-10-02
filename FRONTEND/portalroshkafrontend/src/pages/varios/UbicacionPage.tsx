// src/pages/ubicaciones/UbicacionPage.tsx
import { useState, useMemo } from "react";
import { useAuth } from "../../context/AuthContext";
import PageLayout from "../../layouts/PageLayout";
import DataTable, { type RowAction } from "../../components/DataTable";
import PaginationFooter from "../../components/PaginationFooter";
import IconButton from "../../components/IconButton";
import { MsIcon } from "../../components/MsIcon";
import { Alert } from "../../components/Alert";

import { ubicacionColumns } from "../../config/tables/ubicacionTableConfig";
import UbicacionModal from "./UbicacionModal";
import { useUbicaciones } from "../../hooks/ubicaciones/useUbicaciones";
import { deleteUbicacion } from "../../services/UbicacionService";

import type { UbicacionItem } from "../../types";
// Si querés controlar permisos como en Roles, podés importar:
// import { tieneRol } from "../../utils/permisos";
// import { Roles as RolesEnum } from "../../types/roles";

interface Props {
  embedded?: boolean;
}

export default function UbicacionPage({ embedded = false }: Props) {
  const { token /*, user*/ } = useAuth();

  // const canEdit = !tieneRol(user, RolesEnum.OPERACIONES);
  const canEdit = true; // ajusta si aplican permisos

  const [page, setPage] = useState(0);
  const [selected, setSelected] = useState<UbicacionItem | null>(null);
  const [showModal, setShowModal] = useState(false);

  const { data, totalPages, loading, error, refresh } = useUbicaciones(token, page, 10);

  // estado para feedback de eliminación (toggle A/I en backend)
  const [deletingId, setDeletingId] = useState<number | null>(null);
  const [deleteError, setDeleteError] = useState<string | null>(null);
  const [deleteSuccess, setDeleteSuccess] = useState<string | null>(null);

  const columns = ubicacionColumns;

  const onEdit = (row: UbicacionItem) => {
    setSelected(row);
    setShowModal(true);
  };

  const onDelete = async (row: UbicacionItem) => {
    if (!token) return;
    const ok = window.confirm(
      `¿${row.estado === "A" ? "Desactivar" : "Activar"} la ubicación "${row.nombre}"?`
    );
    if (!ok) return;
    try {
      setDeleteError(null);
      setDeleteSuccess(null);
      setDeletingId(row.idUbicacion);
      await deleteUbicacion(token, row.idUbicacion); // backend hace toggle A/I
      setDeleteSuccess(`Ubicación "${row.nombre}" actualizada correctamente.`);
      await refresh();
    } catch (e: any) {
      setDeleteError(e?.message || "No se pudo actualizar el estado de la ubicación.");
    } finally {
      setDeletingId(null);
    }
  };

  const rowActions: RowAction<UbicacionItem>[] = useMemo(() => {
    if (!canEdit) return [];
    return [
      {
        key: "edit",
        label: "Editar",
        icon: <MsIcon name="edit" />,
        onClick: onEdit,
        variant: "primary",
      },
      {
        key: "delete",
        label: "Eliminar",
        icon: <MsIcon name="delete" />,
        onClick: onDelete,
        variant: "danger",
        // deshabilita mientras se procesa
        disabled: (row) => deletingId === row.idUbicacion,
      },
    ];
  }, [canEdit, deletingId]);

  const newUbicacionButton = canEdit && (
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
  );

  const body = (
    <>
      {deleteError && <Alert kind="error">{deleteError}</Alert>}
      {deleteSuccess && <Alert kind="success">{deleteSuccess}</Alert>}

      <DataTable<UbicacionItem>
        data={data}
        columns={columns}
        rowKey={(u) => u.idUbicacion}
        rowActions={rowActions}
        scrollable={false}
      />

      <PaginationFooter
        currentPage={page}
        totalPages={totalPages}
        onPageChange={(p) => {
          setDeleteError(null);
          setDeleteSuccess(null);
          setPage(p);
        }}
      />

      {showModal && (
        <UbicacionModal
          token={token}
          id={selected?.idUbicacion}
          onClose={() => setShowModal(false)}
          onSaved={refresh}
        />
      )}

      {loading && <Alert kind="info">Cargando ubicaciones…</Alert>}
      {error && <Alert kind="error">{error}</Alert>}
    </>
  );

  if (embedded) {
    return (
      <div>
        <div className="mb-3 flex justify-end">{newUbicacionButton}</div>
        {body}
      </div>
    );
  }

  return (
    <PageLayout title="Ubicaciones" actions={newUbicacionButton}>
      {body}
    </PageLayout>
  );
}
