import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import DataTable from "../components/DataTable";
import PaginationFooter from "../components/PaginationFooter";
import SelectDropdown from "../components/SelectDropdown";
import IconButton from "../components/IconButton";
import PageLayout from "../layouts/PageLayout";
import mockSolicitudes from "../data/mockSolicitudes.json";
import mockBeneficios from "../data/mockBeneficios.json";

// Tipos separados
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

// Hook de mock
function useMockSolicitudes(
  tipoVista: "beneficios" | "permisos",
  { page, size, estado }: { page: number; size: number; estado: string }
) {
  const data =
    tipoVista === "beneficios"
      ? (mockBeneficios as SolicitudBeneficio[])
      : (mockSolicitudes as SolicitudPermiso[]);

  // Filtrar por estado
  let filtered = estado ? data.filter((s: any) => s.estado === estado) : data;

  // Paginación
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

  // Mock fetch según vista
  const { data: solicitudes, totalPages, loading, error } = useMockSolicitudes(
    tipoVista,
    { page, size: 10, estado }
  );

  const limpiarFiltros = () => {
    setEstado("");
    setPage(0);
  };

  // Columnas dinámicas
  const columns =
    tipoVista === "beneficios"
      ? [
          { key: "id", label: "ID" },
          { key: "usuario", label: "Usuario" },
          { key: "tipo", label: "Tipo Beneficio" },
          { key: "comentario", label: "Comentario" },
          {
            key: "estado",
            label: "Estado",
            render: (s: SolicitudBeneficio) => renderEstado(s.estado),
          },
        ]
      : [
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
        ];

  // Estado con estilos
  function renderEstado(estado: "Pendiente" | "Aprobada" | "Rechazada") {
    const base = "px-2 py-1 text-xs font-medium rounded-full";
    if (estado === "Pendiente") {
      return <span className={`${base} bg-yellow-100 text-yellow-700`}>Pendiente</span>;
    } else if (estado === "Aprobada") {
      return <span className={`${base} bg-green-100 text-green-700`}>Aprobada</span>;
    } else {
      return <span className={`${base} bg-red-100 text-red-700`}>Rechazada</span>;
    }
  }

  // Botones de acción
  const renderActions = (s: any) => {
    if (s.estado === "Pendiente") {
      return (
        <button
          onClick={() => navigate(`/solicitudes/${tipoVista}/${s.id}/evaluar`)}
          className="px-3 py-1 bg-blue-600 text-white rounded-lg text-xs hover:bg-blue-700 transition"
        >
          Evaluar
        </button>
      );
    }
    return (
      <button
        onClick={() => navigate(`/solicitudes/${tipoVista}/${s.id}`)}
        className="px-3 py-1 bg-gray-400 text-white rounded-lg text-xs cursor-pointer"
      >
        Ver
      </button>
    );
  };

  if (loading) return <p>Cargando solicitudes...</p>;
  if (error) return <p>{error}</p>;

  return (
    <PageLayout title={`Solicitudes de ${tipoVista}`}>
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

      {/* Footer con paginación y botón atrás */}
      <div className="flex justify-between items-center mt-4">
        <PaginationFooter
          currentPage={page}
          totalPages={totalPages}
          onPageChange={setPage}
          onCancel={() => navigate(-1)}
        />
      </div>
    </PageLayout>
  );
}
