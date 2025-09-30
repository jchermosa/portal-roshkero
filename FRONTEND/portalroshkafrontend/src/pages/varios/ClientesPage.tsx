// src/pages/clientes/ClientesPage.tsx
import { useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { tieneRol } from "../../utils/permisos";
import { Roles } from "../../types/roles";

import PageLayout from "../../layouts/PageLayout";
import DataTable from "../../components/DataTable";
import PaginationFooter from "../../components/PaginationFooter";
import IconButton from "../../components/IconButton";
import { Alert } from "../../components/Alert";

import { useClientesList } from "../../hooks/clientes/useClientesList";
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

  // Reglas de permisos básicas (ajustá a tus necesidades):
  const canEdit = !tieneRol(user, Roles.OPERACIONES); 

  // Listado paginado
  const { data, loading, error, refresh } = useClientesList(token, {
    page,
    size: 10,
    sortBy: "default",
  });

  const renderActions = (c: ClienteResponse) => {
    if (!canEdit) return null;
    return (
      <div className="flex gap-2">
        <button
          onClick={() => {
            setSelected(c);
            setShowModal(true);
          }}
          className="px-3 py-1 bg-blue-600 text-white rounded-lg text-xs hover:bg-blue-700 transition"
        >
          Editar
        </button>
      </div>
    );
  };

  const newClienteButton = canEdit && !embedded && (
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

  const tableBody = (
    <>
      <DataTable<ClienteResponse>
        data={data?.content ?? []}
        columns={clientesColumns}
        rowKey={(c) => c.idCliente}
        actions={canEdit ? renderActions : undefined}
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
        {tableBody}
      </div>
    );
  }

  return (
    <PageLayout title="Clientes" actions={newClienteButton}>
      {tableBody}
    </PageLayout>
  );
}
