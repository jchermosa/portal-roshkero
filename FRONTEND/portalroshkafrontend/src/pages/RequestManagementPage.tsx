import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useSolicitudesTH } from "../hooks/solicitudes/useSolicitudesTH";
import { useCatalogos } from "../hooks/catalogos/useCatalogos";
import { tieneRol } from "../utils/permisos";
import { Roles } from "../types/roles";

import type { SolicitudItem } from "../types";
import DataTable from "../components/DataTable";
import PaginationFooter from "../components/PaginationFooter";
import SelectDropdown from "../components/SelectDropdown";
import IconButton from "../components/IconButton";
import PageLayout from "../layouts/PageLayout";

export default function SolicitudesTHPage() {
  const { token, user } = useAuth();
  const navigate = useNavigate();

  // Filtros
  const [tipoId, setTipoId] = useState("");
  const [usuarioNombre, setUsuarioNombre] = useState("");
  const [estado, setEstado] = useState("");
  const [page, setPage] = useState(0);

  // Permisos - Solo accesible para TH
  const puedeVerSolicitudes = tieneRol(user, Roles.TH, Roles.GTH);

  // Catálogos (solo tipos de solicitud si están disponibles)
  const { tiposSolicitud, loading: loadingCatalogos } = useCatalogos(token);

  // Solicitudes (hook especializado)
  const {
    data: solicitudes,
    totalPages,
    loading: loadingSolicitudes,
    error,
  } = useSolicitudesTH(
    token,
    { tipoId, usuarioNombre, estado }, // filtros
    page,
    10
  );

  const limpiarFiltros = () => {
    setTipoId("");
    setUsuarioNombre("");
    setEstado("");
    setPage(0);
  };

  const columns = [
    { key: "id", label: "ID" },
    {
      key: "usuario",
      label: "Usuario",
      render: (s: SolicitudItem) => 
        `${s.nombre || ''} ${s.apellido || ''}`.trim() || "—",
    },
    {
      key: "tipo",
      label: "Tipo",
      render: (s: SolicitudItem) => s.tipo?.nombre ?? "—",
    },
    {
      key: "fechaInicio",
      label: "Inicio",
      render: (s: SolicitudItem) => s.fecha_inicio 
        ? new Date(s.fecha_inicio).toLocaleDateString()
        : "—",
    },
    {
      key: "dias",
      label: "Días",
      render: (s: SolicitudItem) => (
        <span className="font-medium text-sm">
          {s.cantidad_dias ?? "—"}
        </span>
      ),
    },
    {
      key: "aprobaciones",
      label: "Aprobaciones",
      render: (s: SolicitudItem) => {
        const total = s.lideres?.length || 0;
        const aprobaciones = s.numero_aprobaciones || 0;
        return (
          <span className="font-medium text-sm">
            {aprobaciones}/{total}
          </span>
        );
      },
    },
    {
      key: "estado",
      label: "Estado",
      render: (s: SolicitudItem) => {
        const estados = { P: "Pendiente", A: "Aprobada", R: "Rechazada" };
        const colores = {
          P: "bg-yellow-100 text-yellow-700",
          A: "bg-green-100 text-green-700",
          R: "bg-red-100 text-red-700",
        };
        return (
          <span
            className={`px-2 py-1 text-xs font-medium rounded-full ${
              colores[s.estado] || "bg-gray-100 text-gray-700"
            }`}
          >
            {estados[s.estado] || s.estado}
          </span>
        );
      },
    },
  ];

  const renderActions = (s: SolicitudItem) => (
    <button
      onClick={() => navigate(`/solicitudes/${s.id}/evaluar`)}
      className="px-3 py-1 bg-blue-600 text-white rounded-lg text-xs hover:bg-blue-700 transition"
    >
      Evaluar
    </button>
  );

  // Control de permisos
  if (!puedeVerSolicitudes) {
    return <p>No tenés permisos para ver esta página.</p>;
  }

  if (loadingSolicitudes || loadingCatalogos) {
    return <p>Cargando solicitudes...</p>;
  }

  if (error) {
    return <p>{error}</p>;
  }

  // Opciones para filtros
  const tiposUnicos = Array.from(
    new Set(
      solicitudes
        .map((s) => s.tipo?.nombre)
        .filter((nombre): nombre is string => typeof nombre === "string")
    )
  ).map((nombre) => ({ value: nombre, label: nombre }));

  const estadosOptions = [
    { value: "P", label: "Pendiente" },
    { value: "A", label: "Aprobada" },
    { value: "R", label: "Rechazada" },
  ];

  return (
    <PageLayout
      title="Gestión de Solicitudes"
    >
      <div className="flex items-center gap-4 mb-4">
        <div className="flex-1">
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Usuario
          </label>
          <input
            type="text"
            name="usuario"
            value={usuarioNombre}
            onChange={(e) => setUsuarioNombre(e.target.value)}
            placeholder="Buscar por nombre..."
            className="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          />
        </div>

        <SelectDropdown
          name="tipo"
          label="Tipo"
          value={tipoId}
          onChange={(e) => setTipoId(e.target.value)}
          options={tiposUnicos}
          placeholder="Filtrar por Tipo"
        />

        <SelectDropdown
          name="estado"
          label="Estado"
          value={estado}
          onChange={(e) => setEstado(e.target.value)}
          options={estadosOptions}
          placeholder="Filtrar por Estado"
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
        data={solicitudes}
        columns={columns}
        rowKey={(s) => s.id}
        actions={renderActions}
        scrollable={false}
      />

      <PaginationFooter
        currentPage={page}
        totalPages={totalPages}
        onPageChange={setPage}
        onCancel={() => navigate(-1)}
      />
    </PageLayout>
  );
}