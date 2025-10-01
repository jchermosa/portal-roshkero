import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { tieneRol } from "../../utils/permisos";
import { Roles } from "../../types/roles";

import type { DispositivoItem } from "../../types";
import { useDispositivos } from "../../hooks/dispositivos/useDispositivos";
import DataTable, { type RowAction } from "../../components/DataTable";
import PaginationFooter from "../../components/PaginationFooter";
import SelectDropdown from "../../components/SelectDropdown";
import IconButton from "../../components/IconButton";
import PageLayout from "../../layouts/PageLayout";
import { dispositivosColumns } from "../../config/tables/dispositivoTableConfig";
import { MsIcon } from "../../components/MsIcon";
import { CategoriaEnum, CategoriaLabels, EstadoInventarioEnum, EstadoInventarioLabels } from "../../types";

export default function DevicePage() {
  const { token, user } = useAuth();
  const navigate = useNavigate();

  const canEdit = !tieneRol(user, Roles.OPERACIONES);

  // Filtros
  const [categoria, setCategoria] = useState("");
  const [estado, setEstado] = useState("");
  const [encargado, setEncargado] = useState("");
  const [page, setPage] = useState(0);

  // Resetear página cuando cambian filtros
  useEffect(() => {
    setPage(0);
  }, [categoria, estado, encargado]);

  // Hook de listado
  const { data: dispositivos, loading, error, totalPages } = useDispositivos(
    token,
    { categoria, estado, encargado },
    page,
    10
  );

  const limpiarFiltros = () => {
    setCategoria("");
    setEstado("");
    setEncargado("");
    setPage(0);
  };

  const columns = dispositivosColumns;

  // Acciones por fila (con íconos, como en Roles)
  const rowActions: RowAction<DispositivoItem>[] = useMemo(() => {
    if (!canEdit) {
      return [
        {
          key: "view",
          label: "Ver",
          icon: <MsIcon name="visibility" />,
          onClick: (d) => navigate(`/dispositivos/${d.idDispositivo}?readonly=true`),
          variant: "secondary",
        },
      ];
    }
    return [
      {
        key: "edit",
        label: "Editar",
        icon: <MsIcon name="edit" />,
        onClick: (d) => navigate(`/dispositivos/${d.idDispositivo}`),
        variant: "primary",
      },
      // Si luego habilitas eliminar, puedes agregar aquí un action "delete"
    ];
  }, [canEdit, navigate]);

  return (
    <PageLayout
      title="Listado de dispositivos"
      actions={
        canEdit && (
          <IconButton
            label="Registrar Dispositivo"
            icon={<span>➕</span>}
            variant="primary"
            onClick={() => navigate("/dispositivos/nuevo")}
            className="h-10 text-sm px-4 flex items-center"
          />
        )
      }
    >
      <div className="flex items-end gap-4 mb-4">
        <SelectDropdown
          name="categoria"
          label="Categoría"
          value={categoria}
          onChange={(e) => setCategoria(e.target.value)}
          options={Object.values(CategoriaEnum).map((value) => ({
            value,
            label: CategoriaLabels[value],
          }))}
          placeholder="Filtrar por Categoría"
        />
        <SelectDropdown
          name="estado"
          label="Estado"
          value={estado}
          onChange={(e) => setEstado(e.target.value)}
          options={Object.values(EstadoInventarioEnum).map((value) => ({
            value,
            label: EstadoInventarioLabels[value],
          }))}
          placeholder="Filtrar por Estado"
        />
        <SelectDropdown
          name="encargado"
          label="Encargado"
          value={encargado}
          onChange={(e) => setEncargado(e.target.value)}
          options={[
            { value: "1", label: "Juan Pérez" },
            { value: "2", label: "María López" },
          ]}
          placeholder="Filtrar por Encargado"
        />
        <IconButton
          label="Limpiar filtros"
          icon={<span>🧹</span>}
          variant="secondary"
          onClick={limpiarFiltros}
          className="h-10 text-sm px-4 flex items-center"
        />
      </div>

      {loading && <p>Cargando dispositivos...</p>}
      {error && <p className="text-red-600">{error}</p>}

      <DataTable<DispositivoItem>
        data={dispositivos}
        columns={columns}
        rowKey={(d) => d.idDispositivo!}
        rowActions={rowActions}          
        scrollable={false}
      />

      <PaginationFooter
        currentPage={page}
        totalPages={totalPages || 1}
        onPageChange={setPage}
        onCancel={() => navigate("/home")}
      />
    </PageLayout>
  );
}
