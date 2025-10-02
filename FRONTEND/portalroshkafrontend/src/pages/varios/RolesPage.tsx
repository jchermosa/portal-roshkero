import { useMemo, useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { tieneRol } from "../../utils/permisos";
import { Roles as RolesEnum } from "../../types/roles";

import PageLayout from "../../layouts/PageLayout";
import DataTable, { type RowAction } from "../../components/DataTable";
import { MsIcon } from "../../components/MsIcon";
import PaginationFooter from "../../components/PaginationFooter";
import IconButton from "../../components/IconButton";
import { Alert } from "../../components/Alert";
import ConfirmModal from "../../components/ConfirmModal"; 

import { useRolesList } from "../../hooks/roles/useRolesList";
import { useRolForm } from "../../hooks/roles/useRolForm";
import RolesModal from "./RolesModal";

import type { RolListItem } from "../../types";
import { rolesColumns } from "../../config/tables/rolesTableConfig";

interface Props {
  embedded?: boolean;
}

export default function RolesPage({ embedded = false }: Props) {
  const { token, user } = useAuth();

  const canEdit = !tieneRol(user, RolesEnum.OPERACIONES);

  const [page, setPage] = useState(0);
  const [selected, setSelected] = useState<RolListItem | null>(null);
  const [showModal, setShowModal] = useState(false);

  // estados para confirmación
  const [confirmOpen, setConfirmOpen] = useState(false);
  const [rowToDelete, setRowToDelete] = useState<RolListItem | null>(null);

  const params = useMemo(() => ({ page, size: 10 }), [page]);
  const { data, loading, error, refresh } = useRolesList(token, params);

  const {
    remove,
    deleting,
    error: deleteError,
    success: deleteSuccess,
    clearMessages,
  } = useRolForm(token, { onDeleted: () => refresh() });

  const onEdit = (row: RolListItem) => {
    setSelected(row);
    setShowModal(true);
  };

  // en vez de window.confirm abrimos modal
  const askDelete = (row: RolListItem) => {
    setRowToDelete(row);
    setConfirmOpen(true);
  };

  const handleDelete = async () => {
    if (rowToDelete) {
      await remove(rowToDelete.idRol);
      setRowToDelete(null);
      setConfirmOpen(false);
    }
  };

  const rowActions: RowAction<RolListItem>[] = canEdit
    ? [
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
          onClick: askDelete, 
          variant: "danger",
          disabled: deleting,
        },
      ]
    : [];

  const newRolButton = canEdit && (
    <IconButton
      label="Nuevo Rol"
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

      <DataTable<RolListItem>
        data={data?.content ?? []}
        columns={rolesColumns}
        rowKey={(r) => r.idRol}
        rowActions={rowActions}
        scrollable={false}
      />

      <PaginationFooter
        currentPage={page}
        totalPages={data?.totalPages ?? 0}
        onPageChange={(p) => {
          clearMessages();
          setPage(p);
        }}
      />

      {showModal && (
        <RolesModal
          token={token}
          id={selected?.idRol}
          onClose={() => setShowModal(false)}
          onSaved={refresh}
        />
      )}

      {/* Modal de confirmación */}
      <ConfirmModal
        show={confirmOpen}
        title="Eliminar Rol"
        message={`¿Estás seguro de eliminar el rol "${rowToDelete?.nombre}"?`}
        confirmText="Eliminar"
        cancelText="Cancelar"
        onConfirm={handleDelete}
        onCancel={() => setConfirmOpen(false)}
        loading={deleting}
      />

      {loading && <Alert kind="info">Cargando roles…</Alert>}
      {error && <Alert kind="error">{error}</Alert>}
    </>
  );

  if (embedded) {
    return (
      <div>
        <div className="mb-3 flex justify-end">{newRolButton}</div>
        {body}
      </div>
    );
  }

  return <PageLayout title="Roles" actions={newRolButton}>{body}</PageLayout>;
}
