import { useMemo, useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { tieneRol } from "../../utils/permisos";
import { Roles as RolesEnum } from "../../types/roles";

import PageLayout from "../../layouts/PageLayout";
import DataTable from "../../components/DataTable";
import type { RowAction } from "../../components/DataTable";
import { MsIcon } from "../../components/MsIcon";
import PaginationFooter from "../../components/PaginationFooter";
import IconButton from "../../components/IconButton";
import { Alert } from "../../components/Alert";

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

  const params = useMemo(() => ({ page, size: 10 }), [page]);
  const { data, loading, error, refresh } = useRolesList(token, params);

  const onEdit = (row: RolListItem) => { setSelected(row); setShowModal(true); };
  const onDelete = (row: RolListItem) => handleDelete(row);



  const {
    remove,
    deleting,
    error: deleteError,
    success: deleteSuccess,
    clearMessages,
  } = useRolForm(token, { onDeleted: () => refresh() });

  const handleDelete = async (row: RolListItem) => {
    const ok = window.confirm(`¿Eliminar el rol "${row.nombre}"?`);
    if (!ok) return;
    await remove(row.idRol);
  };

  const renderActions = (row: RolListItem) => {
    if (!canEdit) return null;
    return (
      <div className="flex gap-2">
        <button
          onClick={() => {
            setSelected(row);
            setShowModal(true);
          }}
          className="px-3 py-1 bg-blue-600 text-white rounded-lg text-xs hover:bg-blue-700 transition"
        >
          Editar
        </button>
        <button
          onClick={() => handleDelete(row)}
          disabled={deleting}
          className="px-3 py-1 bg-red-600 text-white rounded-lg text-xs hover:bg-red-700 transition disabled:opacity-50"
        >
          {deleting ? "Eliminando..." : "Eliminar"}
        </button>
      </div>
    );
  };

  
    const rowActions: RowAction<RolListItem>[] = [
    { key: "edit", label: "Editar", icon: <MsIcon name="edit" />, onClick: onEdit, variant: "primary" },
    { key: "delete", label: "Eliminar", icon: <MsIcon name="delete" />, onClick: onDelete, variant: "danger", disabled: deleting },
  ];

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

  return (
    <PageLayout title="Roles" actions={newRolButton}>
      {body}
    </PageLayout>
  );
}
