import { useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";
import SelectDropdown from "../../components/SelectDropdown";
import DataTable from "../../components/DataTable";
import PaginationFooter from "../../components/PaginationFooter";
import IconButton from "../../components/IconButton";
import { useRequestTL } from "../../hooks/solicitudes/useRequestTL";
import type { SolicitudItem } from "../../types";

export default function RequestPage() {

  const navigate = useNavigate();

  const [subtipo, setSubtipo] = useState<string>("");
  const [page, setPage] = useState<number>(0);
  const [estadoFiltro, setEstadoFiltro] = useState<string>("");
  const [tipoFiltro, setTipoFiltro] = useState<string>("");

  const { solicitudes, totalPages, loading, error } = useRequestTL(
    page,
    subtipo || undefined
  );

  const solicitudesFiltradas = solicitudes.filter((s) => {
    const matchesEstado = estadoFiltro ? s.estado === estadoFiltro : true;
    const matchesTipo = tipoFiltro ? s.tipoSolicitud === tipoFiltro : true;
    return matchesEstado && matchesTipo;
  });

  const limpiarFiltros = () => {
    setEstadoFiltro("");
    setTipoFiltro("");
    setPage(0);
  };

  const columns = [
    { key: "idSolicitud", label: "ID" },
    {
      key: "tipoSolicitud",
      label: "Tipo de Solicitud",
      render: (s: SolicitudItem) => s.tipoSolicitud || "—",
    },
    {
      key: "subtipo",
      label: "Sub-Tipo",
      render: (s: SolicitudItem) => s.nombreSubTipoSolicitud || "—",
    },
    {
      key: "cantDias",
      label: "Días",
      render: (s: SolicitudItem) =>
        s.cantDias !== undefined && s.cantDias !== null ? s.cantDias : "—",
    },
    {
      key: "fechaInicio",
      label: "Fecha Inicio",
      render: (s: SolicitudItem) => s.fechaInicio || "—",
    },
    {
      key: "nombreLider",   // 
      label: "Líder",
      render: (s: SolicitudItem) => s.nombreLider || "—",
    },
    {
      key: "estado",
      label: "Estado",
      render: (s: SolicitudItem) => {
        const estados = { P: "Pendiente", A: "Aprobado", R: "Rechazado", RC: "Recalendarizado" };
        const colores = {
          P: "bg-yellow-100 text-yellow-700",
          A: "bg-green-100 text-green-700",
          R: "bg-red-100 text-red-700",
          RC:"bg-orange-100 text-orange-700",
        };
        return (
          <span className={`px-2 py-1 text-xs font-medium rounded-full ${colores[s.estado]}`}>
            {estados[s.estado]}
          </span>
        );
      },
    },
  ];

    const renderActions = (s: SolicitudItem) => {
        if (s.estado === "P") {
        return (
            <button
            onClick={() => navigate(`/solicitudesTL/${s.idSolicitud}/evaluar`)}
            className="w-16 px-3 py-1 bg-blue-600 text-white rounded-lg text-xs hover:bg-blue-700 transition-colors duration-200"
            >
            Evaluar
            </button>
        );
        } else {
        return (
            <button
            onClick={() => navigate(`/solicitudesTL/${s.idSolicitud}/ver`)}
            className="w-16 px-3 py-1 bg-gray-400 text-white rounded-lg text-xs hover:bg-gray-500 transition-colors duration-200"
            >
            Ver
            </button>
        );
        }
    };


  if (loading) return <p>Cargando solicitudes...</p>;
  if (error) return <p className="text-red-500">Error: {error}</p>;


  return (
    <div className="h-full flex flex-col overflow-hidden">
      {/* Fondo */}
      <div
        className="absolute inset-0 bg-brand-blue"
        style={{
          backgroundImage: "url('/src/assets/ilustracion-herov3.svg')",
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
        }}
      >
        <div className="absolute inset-0 bg-brand-blue/40"></div>
      </div>

      <div className="relative z-10 flex flex-col h-full p-4">
        <div className="bg-white/45 dark:bg-gray-900/70 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col h-full overflow-hidden">

          {/* Sección fija: botones + filtros + búsqueda */}
          <div className="flex flex-col flex-shrink-0 p-6 border-b border-gray-200 gap-4">
            {/* Botones */}
            <div className="flex items-center justify-between">
              <h2 className="text-2xl font-bold text-brand-blue">Gestiona Solicitudes</h2>
            </div>

            {/* Filtros */}
            <div className="flex items-center gap-4">
              <SelectDropdown
                label="Tipo de Solicitud"
                name="tipo"
                value={tipoFiltro}
                onChange={(e) => setTipoFiltro(e.target.value)}
                options={[
                  { value: "PERMISO", label: "Permiso" },
                  { value: "BENEFICIO", label: "Beneficio" },
                  { value: "VACACIONES", label: "Vacaciones" },
                ]}
                placeholder="Filtrar por tipo"
              />

              <SelectDropdown
                label="Estado"
                name="estado"
                value={estadoFiltro}
                onChange={(e) => setEstadoFiltro(e.target.value)}
                options={[
                  { value: "P", label: "Pendiente" },
                  { value: "A", label: "Aprobado" },
                  { value: "R", label: "Rechazado" },
                  { value: "RC", label: "Recalendarizado" },
                ]}
                placeholder="Filtrar por estado"
              />
              <IconButton
                label="Limpiar filtros"
                icon={<span>🧹</span>}
                variant="secondary"
                onClick={limpiarFiltros}
                className="h-10 text-sm px-4"
              />
            </div>
          </div>

          {/* Tabla scrollable */}
          <div className="flex-1 overflow-auto p-6">
            <DataTable
              data={solicitudesFiltradas}
              columns={columns}
              rowKey={(s) => s.idSolicitud}
              actions={renderActions}
              scrollable={false}
            />
          </div>

          {/* Paginación */}
          <div className="p-6 border-t border-gray-200 flex-shrink-0">
            <PaginationFooter
              currentPage={page}
              totalPages={totalPages}
              onPageChange={setPage}
              onCancel={() => navigate(-1)}
            />
          </div>

        </div>
      </div>
    </div>
  );
}
