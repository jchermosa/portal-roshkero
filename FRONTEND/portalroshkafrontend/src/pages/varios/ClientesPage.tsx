import { useState, useMemo } from "react";
import { useAuth } from "../../context/AuthContext";
import { tieneRol } from "../../utils/permisos";
import { Roles } from "../../types/roles";

import PageLayout from "../../layouts/PageLayout";
import DataTable, { type RowAction } from "../../components/DataTable";
import { MsIcon } from "../../components/MsIcon";
import PaginationFooter from "../../components/PaginationFooter";
import IconButton from "../../components/IconButton";
import { Alert } from "../../components/Alert";

import { useClientesList } from "../../hooks/clientes/useClientesList";
import { useClientesForm } from "../../hooks/clientes/useClienteForm";
import ClientesModal from "./ClientesModal";

import type { ClienteResponse } from "../../types";
import { clientesColumns } from "../../config/tables/clientesTableConfig";

interface Props {
  embedded?: boolean;
}

export default function ClientesPage({ embedded = false }: Props) {
  const { token, user } = useAuth();

  const [page, setPage] = useState(0);
  const [selected, setSelected] = useState<ClienteResponse | null>(null);
  const [showModal, setShowModal] = useState(false);

  // Reglas de permisos básicas
  const canEdit = !tieneRol(user, Roles.OPERACIONES);

  // Listado paginado
  const { data, loading, error, refresh } = useClientesList(token, {
    page,
    size: 10,
    sortBy: "default",
  });

  // Hook para eliminar
  const { remove, deleting, error: deleteError } = useClientesForm(token, {
    onDeleted: () => refresh(),
  });

  const onEdit = (row: ClienteResponse) => {
    setSelected(row);
    setShowModal(true);
  };

  const onDelete = async (row: ClienteResponse) => {
    const ok = window.confirm(`¿Eliminar el cliente "${row.nombre}"?`);
    if (!ok) return;
    await remove(row.idCliente);
  };

  const rowActions: RowAction<ClienteResponse>[] = canEdit
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
          onClick: onDelete,
          variant: "danger",
          disabled: deleting,
        },
      ]
    : [];

  const newClienteButton = canEdit && (
    <IconButton
      label="Nuevo Cliente"
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

      <DataTable<ClienteResponse>
        data={data?.content ?? []}
        columns={clientesColumns}
        rowKey={(c) => c.idCliente}
        rowActions={rowActions}
        scrollable={false}
      />

      <PaginationFooter
        currentPage={page}
        totalPages={data?.totalPages ?? 0}
        onPageChange={setPage}
      />

      {/* Modal CRUD */}
      {showModal && (
        <ClientesModal
          token={token}
          id={selected?.idCliente}
          onClose={() => setShowModal(false)}
          onSaved={refresh}
        />
      )}

      {loading && <Alert kind="info">Cargando clientes…</Alert>}
      {error && <Alert kind="error">{error}</Alert>}
    </>
  );

  if (embedded) {
    return (
      <div>
        <div className="mb-3 flex justify-end">{newClienteButton}</div>
        {body}
      </div>
    );
  }

  return (
    <PageLayout title="Clientes" actions={newClienteButton}>
      {body}
    </PageLayout>
  );
}
