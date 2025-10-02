// src/pages/tecnologias/TecnologiasPage.tsx
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

import { useTecnologiasList } from "../../hooks/tecnologias/useTecnologiasList";
import { useTecnologiasForm } from "../../hooks/tecnologias/useTecnologiasForm";
import TecnologiasModal from "./TecnologiasModal";

import type { TecnologiaResponse } from "../../types";
import { tecnologiasColumns } from "../../config/tables/tecnologiasTableConfig";

interface Props {
  embedded?: boolean;
}

export default function TecnologiasPage({ embedded = false }: Props) {
  const { token, user } = useAuth();
  const canEdit = tieneRol(user, RolesEnum.OPERACIONES);

  const [page, setPage] = useState(0);
  const [selected, setSelected] = useState<TecnologiaResponse | null>(null);
  const [showModal, setShowModal] = useState(false);

  const params = useMemo(() => ({ page, size: 10 }), [page]);
  const { data, loading, error, refresh } = useTecnologiasList(token, params);

  // Hook CRUD (para eliminar)
  const {
    remove,
    deleting,
    error: deleteError,
  } = useTecnologiasForm(token, { onDeleted: () => refresh() });

  const onEdit = (row: TecnologiaResponse) => {
    setSelected(row);
    setShowModal(true);
  };

  const onDelete = async (row: TecnologiaResponse) => {
    const ok = window.confirm(`¿Eliminar la tecnología "${row.nombre}"?`);
    if (!ok) return;
    await remove(row.idTecnologia);
  };

  const rowActions: RowAction<TecnologiaResponse>[] = [
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
  ];

  const newTecnologiaButton = canEdit &&  (
    <IconButton
      label="Nueva Tecnología"
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

      <DataTable<TecnologiaResponse>
        data={data?.content ?? []}
        columns={tecnologiasColumns}
        rowKey={(t) => t.idTecnologia}
        rowActions={canEdit ? rowActions : undefined}
        scrollable={false}
      />

      <PaginationFooter
        currentPage={page}
        totalPages={data?.totalPages ?? 0}
        onPageChange={setPage}
      />

      {showModal && (
        <TecnologiasModal
          token={token}
          id={selected?.idTecnologia}
          onClose={() => setShowModal(false)}
          onSaved={refresh}
        />
      )}

      {loading && <Alert kind="info">Cargando tecnologías…</Alert>}
      {error && <Alert kind="error">{error}</Alert>}
    </>
  );

  if (embedded) {
    return (
      <div>
        <div className="mb-3 flex justify-end">{newTecnologiaButton}</div>
        {body}
      </div>
    );
  }

  return (
    <PageLayout title="Tecnologías" actions={newTecnologiaButton}>
      {body}
    </PageLayout>
  );
}
