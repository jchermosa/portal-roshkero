import { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { useSolicitudesTH } from "../../hooks/solicitudes/useSolicitudesTH";
import { useCatalogosSolicitudes } from "../../hooks/catalogos/useCatalogosSolicitudes";
import { tieneRol } from "../../utils/permisos";
import { Roles } from "../../types/roles";

import DataTable from "../../components/DataTable";
import PaginationFooter from "../../components/PaginationFooter";
import SelectDropdown from "../../components/SelectDropdown";
import IconButton from "../../components/IconButton";
import PageLayout from "../../layouts/PageLayout";
import type { SolicitudItem } from "../../types/";

export default function SolicitudesTHPage() {
  const { token, user } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  // Detectamos la URL para determinar la categoría
  const tipoSolicitud = location.pathname.includes("permisos") ? "PERMISO" : "BENEFICIO";

  // Filtros
  const [tipoId, setTipoId] = useState("");
  const [estado, setEstado] = useState("");
  const [page, setPage] = useState(0);

  // Permisos
  const puedeVerSolicitudes = tieneRol(user, Roles.TH, Roles.GTH);

  // Catálogos
  const { tiposPermiso, tiposBeneficio, loading: loadingCatalogos } = useCatalogosSolicitudes(token);

  const opcionesTipoEspecifico =
    tipoSolicitud === "PERMISO"
      ? tiposPermiso.map((t) => ({ value: t.id, label: t.nombre }))
      : tipoSolicitud === "BENEFICIO"
      ? tiposBeneficio.map((t) => ({ value: t.id, label: t.nombre }))
      : [];

  // Hook de solicitudes
  const { data: solicitudes, totalPages, loading, error } = useSolicitudesTH(
    token,
    { tipoSolicitud, tipoId, estado },
    page
  );

  const limpiarFiltros = () => {
    setTipoId("");
    setEstado("");
    setPage(0);
  };

  // Columnas
  const columns = [
    { key: "idSolicitud", label: "ID" },
    {
      key: "usuario",
      label: "Usuario",
      render: (s: SolicitudItem) => {
        const [nombre, ...apellidoArr] = s.usuario.split(" ");
        const apellido = apellidoArr.join(" ");
        return `${nombre} ${apellido}`.trim();
      },
    },
    {
      key: "tipoSolicitud",
      label: "Tipo",
      render: (s: SolicitudItem) => s.tipoSolicitud,
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

  const renderActions = (s: SolicitudItem) => {
    if (s.estado === "P") {
      return (
        <button
          onClick={() => navigate(`/solicitudesTH/${s.idSolicitud}/evaluar`)}
          className="w-16 px-3 py-1 bg-blue-600 text-white rounded-lg text-xs hover:bg-blue-700 transition-colors duration-200"
        >
          Evaluar
        </button>
      );
    } else {
      return (
        <button
          onClick={() => navigate(`/solicitudesTH/${s.idSolicitud}/ver`)}
          className="w-16 px-3 py-1 bg-gray-400 text-white rounded-lg text-xs hover:bg-gray-500 transition-colors duration-200"
        >
          Ver
        </button>
      );
    }
  };

  if (!puedeVerSolicitudes) return <p>No tenés permisos para ver esta página.</p>;
  if (loading || loadingCatalogos) return <p>Cargando solicitudes...</p>;
  if (error) return <p>{error}</p>;

  const estadosOptions = [
    { value: "P", label: "Pendiente" },
    { value: "A", label: "Aprobada" },
    { value: "R", label: "Rechazada" },
  ];

  return (
    <PageLayout title="Gestión de Solicitudes">
      <div className="flex items-center gap-4 mb-4">
        {tipoSolicitud && opcionesTipoEspecifico.length > 0 && (
          <SelectDropdown
            name="tipoEspecifico"
            label="Tipo"
            value={tipoId}
            onChange={(e) => setTipoId(e.target.value)}
            options={opcionesTipoEspecifico}
            placeholder={`Filtrar ${tipoSolicitud.toLowerCase()}`}
          />
        )}

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
        rowKey={(s) => s.idSolicitud}
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
