import { useMemo, useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { tieneRol } from "../../utils/permisos";
import { Roles } from "../../types/roles";

import PageLayout from "../../layouts/PageLayout";
import DataTable from "../../components/DataTable";
import PaginationFooter from "../../components/PaginationFooter";
import IconButton from "../../components/IconButton";
import { Alert } from "../../components/Alert";

import { useCargosList } from "../../hooks/cargos/useCargosList";
import { useCargoForm } from "../../hooks/cargos/useCargoForm";
import CargosModal from "./CargosModal";

import type { CargoListItem } from "../../types";
import { cargosColumns } from "../../config/tables/cargosTableConfig";

interface Props {
  embedded?: boolean;
}

export default function CargosPage({ embedded = false }: Props) {
  const { token, user } = useAuth();

  // permisos básicos (ajustá a tu criterio)
  const canEdit = !tieneRol(user, Roles.OPERACIONES);

  const [page, setPage] = useState(0);
  const [selected, setSelected] = useState<CargoListItem | null>(null);
  const [showModal, setShowModal] = useState(false);

  const params = useMemo(() => ({ page, size: 10 }), [page]);
  const { data, loading, error, refresh } = useCargosList(token, params);

  // Hook para delete (aprovechamos success/error de acá también si querés mostrar debajo de la tabla)
  const { remove, deleting, error: deleteError, success: deleteSuccess, clearMessages } =
    useCargoForm(token, {
      onDeleted: () => refresh(),
    });

  const handleDelete = async (row: CargoListItem) => {
    // confirm simple; podés reemplazar con tu modal de confirmación
    const ok = window.confirm(`¿Eliminar el cargo "${row.nombre}"?`);
    if (!ok) return;
    await remove(row.idCargo);
  };

  const renderActions = (row: CargoListItem) => {
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

  const newCargoButton = canEdit && (
    <IconButton
      label="Nuevo Cargo"
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
      {/* feedback de delete u otras acciones globales de la page */}
      {deleteError && <Alert kind="error">{deleteError}</Alert>}
      {deleteSuccess && <Alert kind="success">{deleteSuccess}</Alert>}

      <DataTable<CargoListItem>
        data={data?.content ?? []}
        columns={cargosColumns}
        rowKey={(c) => c.idCargo}
        actions={canEdit ? renderActions : undefined}
        scrollable={false}
      />

      <PaginationFooter
        currentPage={page}
        totalPages={data?.totalPages ?? 0}
        onPageChange={(p) => {
          clearMessages(); // limpia mensajes al cambiar de página
          setPage(p);
        }}
      />

      {/* Modal CRUD */}
      {showModal && (
        <CargosModal
          token={token}
          id={selected?.idCargo}
          onClose={() => setShowModal(false)}
          onSaved={refresh}
        />
      )}

      {loading && <Alert kind="info">Cargando cargos…</Alert>}
      {error && <Alert kind="error">{error}</Alert>}
    </>
  );

  if (embedded) {
    return (
      <div>
        <div className="mb-3 flex justify-end">{newCargoButton}</div>
        {body}
      </div>
    );
  }

  return (
    <PageLayout title="Cargos" actions={newCargoButton}>
      {body}
    </PageLayout>
  );
}
