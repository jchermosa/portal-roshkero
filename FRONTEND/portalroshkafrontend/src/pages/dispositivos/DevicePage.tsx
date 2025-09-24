import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { tieneRol } from "../../utils/permisos";
import { Roles } from "../../types/roles";

import type { DispositivoItem } from "../../types";
import { useDispositivos } from "../../hooks/dispositivos/useDispositivos";
import DataTable from "../../components/DataTable";
import PaginationFooter from "../../components/PaginationFooter";
import SelectDropdown from "../../components/SelectDropdown";
import IconButton from "../../components/IconButton";
import PageLayout from "../../layouts/PageLayout";
import { dispositivosColumns } from "../../config/tables/dispositivoTableConfig";

export default function DevicePage() {
  const { token, user } = useAuth();
  const navigate = useNavigate();

  // Filtros
  const [categoria, setCategoria] = useState("");
  const [estado, setEstado] = useState("");
  const [encargado, setEncargado] = useState("");
  const [page, setPage] = useState(0);

  // Permisos
  const puedeVerDispositivos = tieneRol(user, Roles.SYSADMIN );

  // Dispositivos (hook especializado)
  const {
    data: dispositivos,
    totalPages,
    loading,
    error,
  } = useDispositivos(
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

  const renderActions = (d: DispositivoItem) => {
    if (tieneRol(user, Roles.OPERACIONES)) {
      // Solo ver
      return (
        <button
          onClick={() =>
            navigate(`/dispositivos/${d.id_dispositivo}?readonly=true`)
          }
          className="px-3 py-1 bg-gray-500 text-white rounded-lg text-xs hover:bg-gray-600 transition"
        >
          Ver
        </button>
      );
    }

    return (
      <button
        onClick={() => navigate(`/dispositivos/${d.id_dispositivo}`)}
        className="px-3 py-1 bg-blue-600 text-white rounded-lg text-xs hover:bg-blue-700 transition"
      >
        Editar
      </button>
    );
  };

  if (!puedeVerDispositivos)
    return <p>No tenés permisos para ver esta página.</p>;
  if (loading) return <p>Cargando dispositivos...</p>;
  if (error) return <p>{error}</p>;

  return (
    <PageLayout
      title="Listado de dispositivos"
      actions={
        <IconButton
          label="Registrar Dispositivo"
          icon={<span>➕</span>}
          variant="primary"
          onClick={() => navigate("/dispositivos/nuevo")}
          className="h-10 text-sm px-4 flex items-center"
        />
      }
    >
      <div className="flex items-center gap-4 mb-4">
        <SelectDropdown
          name="categoria"
          label="Categoría"
          value={categoria}
          onChange={(e) => setCategoria(e.target.value)}
          options={[
            { value: "Laptop", label: "Laptop" },
            { value: "Impresora", label: "Impresora" },
            { value: "Servidor", label: "Servidor" },
          ]}
          placeholder="Filtrar por Categoría"
        />
        <SelectDropdown
          name="estado"
          label="Estado"
          value={estado}
          onChange={(e) => setEstado(e.target.value)}
          options={[
            { value: "Activo", label: "Activo" },
            { value: "En reparación", label: "En reparación" },
            { value: "Inactivo", label: "Inactivo" },
          ]}
          placeholder="Filtrar por Estado"
        />
        <SelectDropdown
          name="encargado"
          label="Encargado"
          value={encargado}
          onChange={(e) => setEncargado(e.target.value)}
          options={[
            { value: "Juan Pérez", label: "Juan Pérez" },
            { value: "María López", label: "María López" },
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

      <DataTable
        data={dispositivos}
        columns={columns}
        rowKey={(d) => d.id_dispositivo}
        actions={renderActions}
        scrollable={false}
      />

      <PaginationFooter
        currentPage={page}
        totalPages={totalPages}
        onPageChange={setPage}
        onCancel={() => navigate("/home")}
      />
    </PageLayout>
  );
}
