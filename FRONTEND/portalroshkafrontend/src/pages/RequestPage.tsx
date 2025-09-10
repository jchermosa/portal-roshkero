import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import SelectDropdown from "../components/SelectDropdown";
import DataTable from "../components/DataTable";
import PaginationFooter from "../components/PaginationFooter";
import IconButton from "../components/IconButton";

interface SolicitudItem {
  id_solicitud: number;
  tipo: {
    id: number;
    nombre: string;
  };
  comentario: string;
  estado: "P" | "A" | "R";
}


export default function SolicitudesPage() {
  const { token, user } = useAuth();
  const navigate = useNavigate();

  const [solicitudes, setSolicitudes] = useState<SolicitudItem[]>([]);
  const [tipo, setTipo] = useState<string>("");
  const [page, setPage] = useState<number>(0);
  const [totalPages, setTotalPages] = useState<number>(1);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const modoDesarrollo = true;

  useEffect(() => {
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
    { key: "id", label: "ID" },
    { key: "tipo", label: "Tipo" },
    { key: "descripcion", label: "Descripción" },
    { key: "estado", label: "Estado" },
    { key: "fechaCreacion", label: "Fecha de creación" },
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
        <div className="bg-white/45 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col h-full overflow-hidden">
          <div className="p-6 border-b border-gray-200 flex-shrink-0">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-2xl font-bold text-brand-blue">
                Mis Solicitudes
              </h2>
              <IconButton
                label="Nueva Solicitud"
                icon={<span>➕</span>}
                variant="primary"
                onClick={() => navigate("/solicitudes/nueva")}
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
              rowKey={(s) => s.id_solicitud}
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