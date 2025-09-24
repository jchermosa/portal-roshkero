import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import SelectDropdown from "../components/SelectDropdown";
import DataTable from "../components/DataTable";
import PaginationFooter from "../components/PaginationFooter";
import IconButton from "../components/IconButton";
import rawSolicitudes from "../data/mockSolicitudes.json";
import type { SolicitudItem } from "../types";



export default function RequestPage() {
  const { token, user } = useAuth();
  const navigate = useNavigate();

  const [solicitudes, setSolicitudes] = useState<SolicitudItem[]>([]);
  const [tipo, setTipo] = useState<string>("");
  const [page, setPage] = useState<number>(0);
  const [totalPages, setTotalPages] = useState<number>(1);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const modoDesarrollo = true;
  const mockSolicitudes: SolicitudItem[] = rawSolicitudes.map((s) => ({
    ...s,
    estado: s.estado as "P" | "A" | "R",
    lideres: [
    { id: 1, nombre: "Ana", aprobado: true },
    { id: 2, nombre: "Carlos", aprobado: false },],
  }));


  useEffect(() => {
  if (modoDesarrollo) {
    const solicitudesFiltradas = mockSolicitudes.filter(
      (s) => !tipo || s.tipo?.nombre === tipo
    );

    setSolicitudes(solicitudesFiltradas);
    setTotalPages(1);
    setLoading(false);
    return;
  }

  if (!token || !user?.id) return;

  setLoading(true);
  setSolicitudes([]);

  const params = new URLSearchParams();
  params.append("usuarioId", user.id.toString());
  if (tipo) params.append("tipo", tipo);
  params.append("page", page.toString());
  params.append("size", "10");

  fetch(`/api/solicitudes?${params.toString()}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    .then(async (res) => {
      if (!res.ok) throw new Error(await res.text());
      return res.json();
    })
    .then((data) => {
      setSolicitudes(data.content);
      setTotalPages(data.totalPages);
    })
    .catch((err) => {
      setError("Error al cargar solicitudes: " + err.message);
    })
    .finally(() => setLoading(false));
  }, [token, user?.id, tipo, page]);

  const limpiarFiltros = () => {
    setTipo("");
    setPage(0);
  };

  const columns = [
  {
    key: "id",
    label: "ID",
  },
  {
    key: "tipo",
    label: "Tipo",
    render: (s: SolicitudItem) => s.tipo?.nombre ?? "—",
  },
  {
    key: "comentario",
    label: "Comentario",
  },
  {
    key: "numero_aprobaciones",
    label: "Aprobaciones",
    render: (s: SolicitudItem) => {
      const total = s.lideres.length;
      return(
      <span className="font-medium text-sm">
        {s.numero_aprobaciones}/{total}
      </span>
      )
    },
  },
  {
    key: "estado",
    label: "Estado",
    render: (s: SolicitudItem) => {
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
  {
    key: "acciones",
    label: "Acciones",
    render: (s: SolicitudItem) => (
      <button
        onClick={() => navigate(`/requests/${s.id}`)}
        className="px-3 py-1 text-sm text-white bg-blue-600 rounded hover:bg-blue-700"
      >
        Editar
      </button>
    ),
  },
];

  if (loading) return <p>Cargando solicitudes...</p>;
  if (error) return <p>{error}</p>;

  const tiposUnicos: { value: string; label: string }[] =
  solicitudes.length > 0
    ? Array.from(
        new Set(
          solicitudes
            .map((s) => s.tipo?.nombre)
            .filter((nombre): nombre is string => typeof nombre === "string")
        )
      ).map((nombre) => ({
        value: nombre,
        label: nombre,
      }))
    : [];

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
                Mis Solicitudes
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
                label="Tipo"
                name="tipo"
                value={tipo}
                onChange={(e) => setTipo(e.target.value)}
                options={tiposUnicos}
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