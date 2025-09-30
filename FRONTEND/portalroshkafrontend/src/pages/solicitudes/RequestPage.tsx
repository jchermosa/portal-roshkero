import { useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";
import SelectDropdown from "../../components/SelectDropdown";
import DataTable from "../../components/DataTable";
import PaginationFooter from "../../components/PaginationFooter";
import IconButton from "../../components/IconButton";
import { useSolicitudesPermiso } from "../../hooks/solicitudes/useRequestPage";
import type { SolicitudPermiso } from "../../types";

export default function RequestPage() {
  const { user } = useAuth();
  const navigate = useNavigate();

  const [subtipo, setSubtipo] = useState<string>("");
  const [page, setPage] = useState<number>(0);

  // Hook para obtener solicitudes de permiso del usuario
  const { solicitudes, totalPages, loading, error } = useSolicitudesPermiso(
    page,
    subtipo || undefined
  );

  const limpiarFiltros = () => {
    setSubtipo("");
    setPage(0);
  };

  const columns = [
    { key: "id", label: "ID" },
    {
      key: "subtipo",
      label: "Tipo de Permiso",
      render: (s: SolicitudPermiso) => s.subtipo?.nombre ?? "—",
    },
    {
      key: "cantidad_dias",
      label: "Días",
      render: (s: SolicitudPermiso) => s.cantidad_dias,
    },
    {
      key: "fecha_inicio",
      label: "Fecha Inicio",
      render: (s: SolicitudPermiso) => s.fecha_inicio,
    },
    {
      key: "lider",
      label: "Líder Asignado",
      render: (s: SolicitudPermiso) => `${s.lider?.nombre} ${s.lider?.apellido}`.trim() || "—",
    },
    {
      key: "estado",
      label: "Estado",
      render: (s: SolicitudPermiso) => {
        const estados = { P: "Pendiente", A: "Aprobado", R: "Rechazado" };
        const colores = {
          P: "bg-yellow-100 text-yellow-700",
          A: "bg-green-100 text-green-700",
          R: "bg-red-100 text-red-700",
        };
        return (
          <span
            className={`px-2 py-1 text-xs font-medium rounded-full ${colores[s.estado]}`}
          >
            {estados[s.estado]}
          </span>
        );
      },
    },
  ];

  const renderActions = (s: SolicitudPermiso) => (
    <button
      onClick={() => navigate(`/requests/${s.id}`)}
      className="px-3 py-1 text-sm text-white bg-blue-600 rounded hover:bg-blue-700 disabled:opacity-50"
      disabled={loading}
    >
      {s.estado === "P" ? "Ver / Editar" : "Ver"}
    </button>
  );

  if (loading) return <p>Cargando solicitudes...</p>;
  if (error) return <p className="text-red-500">Error: {error}</p>;

  // Generar opciones de filtro de subtipo basado en los datos actuales
  const subtiposUnicos = Array.from(
    new Set(
      solicitudes
        .map((s) => s.subtipo?.nombre)
        .filter((nombre): nombre is string => typeof nombre === "string")
    )
  ).map((nombre) => ({
    value: nombre,
    label: nombre,
  }));

  return (
    <div className="h-full flex flex-col overflow-hidden">
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
          <div className="p-6 border-b border-gray-200 flex-shrink-0">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-2xl font-bold text-brand-blue">
                Mis Solicitudes de Permiso
              </h2>
              <IconButton
                label="Nueva Solicitud"
                icon={<span>➕</span>}
                variant="primary"
                onClick={() => navigate("/requests/nuevo")}
                className="h-10 text-sm px-4 flex items-center"
              />
            </div>

            <div className="flex items-center gap-4">
              <SelectDropdown
                label="Tipo de Permiso"
                name="subtipo"
                value={subtipo}
                onChange={(e) => setSubtipo(e.target.value)}
                options={subtiposUnicos}
                placeholder="Filtrar por tipo"
              />

              <div className="mb-4">
                <label className="block text-sm font-medium text-transparent mb-1 select-none">
                  &nbsp;
                </label>
                <IconButton
                  label="Limpiar filtros"
                  icon={<span>🧹</span>}
                  variant="secondary"
                  onClick={limpiarFiltros}
                  className="h-10 text-sm px-4 flex items-center"
                />
              </div>
            </div>
          </div>

          <div className="flex-1 overflow-auto p-6">
            <DataTable
              data={solicitudes}
              columns={columns}
              rowKey={(s) => s.id}
              actions={renderActions}
              scrollable={false}
            />
          </div>

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