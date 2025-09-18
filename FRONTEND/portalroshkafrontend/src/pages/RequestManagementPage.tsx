import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import DataTable from "../components/DataTable";
import PaginationFooter from "../components/PaginationFooter";
import SelectDropdown from "../components/SelectDropdown";
import IconButton from "../components/IconButton";
import PageLayout from "../layouts/PageLayout";

import rawSolicitudes from "../data/mockSolicitudes.json";
import rawBeneficios from "../data/mockBeneficios.json";

type SolicitudPermiso = {
  id: number;
  usuario: string;
  tipo: string;
  fechaInicio: string;
  dias: number;
  estado: "Pendiente" | "Aprobada" | "Rechazada";
};

type SolicitudBeneficio = {
  id: number;
  usuario: string;
  tipo: string;
  comentario: string;
  estado: "Pendiente" | "Aprobada" | "Rechazada";
};

// ✅ Mocks tipados
const mockSolicitudes = rawSolicitudes as SolicitudPermiso[];
const mockBeneficios = rawBeneficios as SolicitudBeneficio[];

// Hook mock
function useMockSolicitudes(
  tipoVista: "beneficios" | "permisos",
  { page, size, estado }: { page: number; size: number; estado: string }
) {
  const data = tipoVista === "beneficios" ? mockBeneficios : mockSolicitudes;

  const filtered = estado ? data.filter((s) => s.estado === estado) : data;

  const start = page * size;
  const paginated = filtered.slice(start, start + size);

  return {
    data: paginated,
    totalPages: Math.ceil(filtered.length / size),
    loading: false,
    error: null,
  };
}

export default function SolicitudesPage({
  tipoVista,
}: {
  tipoVista: "beneficios" | "permisos";
}) {
  const { token } = useAuth();
  const navigate = useNavigate();

  const [estado, setEstado] = useState("");
  const [page, setPage] = useState(0);

  const { data: solicitudes, totalPages, loading, error } = useMockSolicitudes(
    tipoVista,
    { page, size: 10, estado }
  );

  const limpiarFiltros = () => {
    setEstado("");
    setPage(0);
  };

  // Render del estado con badge
  const renderEstado = (estado: "Pendiente" | "Aprobada" | "Rechazada") => {
    const base = "px-2 py-1 text-xs font-medium rounded-full";
    switch (estado) {
      case "Pendiente":
        return (
          <span className={`${base} bg-yellow-100 text-yellow-700`}>
            Pendiente
          </span>
        );
      case "Aprobada":
        return (
          <span className={`${base} bg-green-100 text-green-700`}>
            Aprobada
          </span>
        );
      case "Rechazada":
        return (
          <span className={`${base} bg-red-100 text-red-700`}>Rechazada</span>
        );
    }
  };

  // ✅ Columnas tipadas
  const columns =
    tipoVista === "beneficios"
      ? ([
          { key: "id", label: "ID" },
          { key: "usuario", label: "Usuario" },
          { key: "tipo", label: "Tipo Beneficio" },
          { key: "comentario", label: "Comentario" },
          {
            key: "estado",
            label: "Estado",
            render: (s: SolicitudBeneficio) => renderEstado(s.estado),
          },
        ] as {
          key: keyof SolicitudBeneficio;
          label: string;
          render?: (s: SolicitudBeneficio) => React.ReactNode;
        }[])
      : ([
          { key: "id", label: "ID" },
          { key: "usuario", label: "Usuario" },
          { key: "tipo", label: "Tipo Permiso" },
          { key: "fechaInicio", label: "Fecha Inicio" },
          { key: "dias", label: "Días" },
          {
            key: "estado",
            label: "Estado",
            render: (s: SolicitudPermiso) => renderEstado(s.estado),
          },
        ] as {
          key: keyof SolicitudPermiso;
          label: string;
          render?: (s: SolicitudPermiso) => React.ReactNode;
        }[]);

  // Acciones por fila
  const renderActions = (s: SolicitudBeneficio | SolicitudPermiso) =>
    s.estado === "Pendiente" ? (
      <button
        onClick={() => navigate(`/solicitudes/${tipoVista}/${s.id}/evaluar`)}
        className="px-3 py-1 bg-blue-600 text-white rounded-lg text-xs hover:bg-blue-700 transition"
      >
        Evaluar
      </button>
    ) : (
      <button
        onClick={() => navigate(`/solicitudes/${tipoVista}/${s.id}`)}
        className="px-3 py-1 bg-gray-400 text-white rounded-lg text-xs cursor-pointer"
      >
        Ver
      </button>
    );

  if (loading) return <p>Cargando solicitudes...</p>;
  if (error) return <p>{error}</p>;

  return (
    <PageLayout
      title={`Solicitudes de ${tipoVista}`}
      actions={
        <IconButton
          label="Nueva Solicitud"
          icon={<span>➕</span>}
          variant="primary"
          onClick={() => navigate(`/solicitudes/${tipoVista}/crear`)}
          className="h-10 text-sm px-4 flex items-center"
        />
      }
    >
      {/* Filtros */}
      <div className="flex items-center gap-4 mb-4">
        <SelectDropdown
          name="estado"
          label="Estado"
          value={estado}
          onChange={(e) => setEstado(e.target.value)}
          options={[
            { value: "", label: "Todos" },
            { value: "Pendiente", label: "Pendiente" },
            { value: "Aprobada", label: "Aprobada" },
            { value: "Rechazada", label: "Rechazada" },
          ]}
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

      {/* Tabla */}
      <DataTable
        data={solicitudes}
        columns={columns}
        rowKey={(s) => s.id}
        actions={renderActions}
        scrollable={false}
      />

      {/* Footer */}
      <PaginationFooter
        currentPage={page}
        totalPages={totalPages}
        onPageChange={setPage}
        onCancel={() => navigate(-1)}
      />
    </PageLayout>
  );
}
