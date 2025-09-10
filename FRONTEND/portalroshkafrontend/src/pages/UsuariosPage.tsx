import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import SelectDropdown from "../components/SelectDropdown";
import DataTable from "../components/DataTable";
import PaginationFooter from "../components/PaginationFooter";
import IconButton from "../components/IconButton";

interface UsuarioItem {
  id: number;
  nombre: string;
  apellido: string;
  correo: string;
  rolId?: number;
  equipoId?: number;
  cargoId?: number;
  estado?: boolean;
  antiguedad?: string;
  antiguedadPretty?: string;
}

interface CatalogItem {
  id: number;
  nombre: string;
}

export default function UsuariosPage() {
  const { token, user } = useAuth();
  const navigate = useNavigate();

  const [usuarios, setUsuarios] = useState<UsuarioItem[]>([]);
  const [roles, setRoles] = useState<CatalogItem[]>([]);
  const [equipos, setEquipos] = useState<CatalogItem[]>([]);
  const [cargos, setCargos] = useState<CatalogItem[]>([]);

  const [rolId, setRolId] = useState<string>("");
  const [equipoId, setEquipoId] = useState<string>("");
  const [cargoId, setCargoId] = useState<string>("");
  const [page, setPage] = useState<number>(0);
  const [totalPages, setTotalPages] = useState<number>(1);

  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  const puedeVer = user?.rol?.nombre === "TH" || user?.rol?.nombre === "GTH" || user?.rol?.nombre === "OPERACIONES"

  const columns = [
  { key: "id", label: "ID" },
  {
    key: "nombre",
    label: "Nombre",
    render: (u: UsuarioItem) => `${u.nombre} ${u.apellido}`,
  },
  { key: "correo", label: "Correo" },
  {
    key: "antiguedadPretty",
    label: "Antigüedad",
    render: (u: UsuarioItem) => u.antiguedadPretty ?? "-",
  },
  {
    key: "estado",
    label: "Estado",
    render: (u: UsuarioItem) =>
      u.estado === true ? (
        <span className="px-2 py-1 text-xs font-medium bg-green-100 text-green-700 rounded-full">
          Activo
        </span>
      ) : (
        <span className="px-2 py-1 text-xs font-medium bg-red-100 text-red-700 rounded-full">
          Inactivo
        </span>
      ),
  },
];


const renderActions = (u: UsuarioItem) => (
  <button
    onClick={() => navigate(`/usuarios/${u.id}`)}
    className="px-3 py-1 bg-blue-600 text-white rounded-lg text-xs hover:bg-blue-700 transition"
  >
    Editar
  </button>
);


  useEffect(() => {
    console.log("Token:", token)
    if (!token || !puedeVer) return;

    Promise.all([
      fetch("/api/catalogos/roles").then((r) => r.json()),
      fetch("/api/catalogos/cargos").then((r) => r.json()),
      fetch("/api/catalogos/equipos").then((r) => r.json()),
    ]).then(([r, c, e]) => {
      setRoles(r);
      setCargos(c);
      setEquipos(e);
    });
  }, [token, puedeVer]);

  useEffect(() => {
    if (!token || !puedeVer) return;

    setLoading(true);
    setUsuarios([]);

    const params = new URLSearchParams();
    if (rolId) params.append("rolId", rolId);
    if (equipoId) params.append("equipoId", equipoId);
    if (cargoId) params.append("cargoId", cargoId);
    params.append("page", page.toString());
    params.append("size", "10");

    fetch(`/api/usuarios?${params.toString()}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then(async (res) => {
        if (!res.ok) throw new Error(await res.text());
        return res.json();
      })
      .then((data) => {
        setUsuarios(data.content);
        setTotalPages(data.totalPages);
      })
      .catch((err) => {
        setError("Error al cargar usuarios: " + err.message);
      })
      .finally(() => setLoading(false));
  }, [token, puedeVer, rolId, equipoId, cargoId, page]);

  const limpiarFiltros = () => {
    setRolId("");
    setEquipoId("");
    setCargoId("");
    setPage(0);
  };

  if (!puedeVer) return <p>No tenés permisos para ver esta página.</p>;
  if (loading) return <p>Cargando usuarios...</p>;

  return (
    <div className="h-full flex flex-col overflow-hidden">
      {/* Fondo con imagen - Solo visible en los bordes */}
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

      {/* Contenedor principal - Ocupa toda la altura disponible */}
      <div className="relative z-10 flex flex-col h-full p-4">
        <div className="bg-white/45 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col h-full overflow-hidden">
          
          {/* Header fijo */}
          <div className="p-6 border-b border-gray-200 flex-shrink-0">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-2xl font-bold text-brand-blue">
                Listado de usuarios
              </h2>
              <IconButton
                label="Crear Usuario"
                icon={<span>➕</span>}
                variant="primary"
                onClick={() => navigate("/usuarios/nuevo")}
                className="h-10 text-sm px-4 flex items-center"
              />
            </div>


            {/* Filtros y Boton de Limpieza */}
            <div className="flex items-center gap-4">
              <SelectDropdown
                  label="Rol"
                  name="rol"
                  value={rolId}
                  onChange={(e) => setRolId(e.target.value)}
                  options={roles.map((r) => ({ value: r.id, label: r.nombre }))}
                  placeholder="Filtrar por Rol"
                />

              <SelectDropdown
                  label="Cargo"
                  name="cargo"
                  value={cargoId}
                  onChange={(e) => setCargoId(e.target.value)}
                  options={cargos.map((c) => ({ value: c.id, label: c.nombre }))}
                  placeholder="Filtrar por Cargo"
                />

              <SelectDropdown
                  label="Equipo"
                  name="equipo"
                  value={equipoId}
                  onChange={(e) => setEquipoId(e.target.value)}
                  options={equipos.map((e) => ({ value: e.id, label: e.nombre }))}
                  placeholder="Filtrar por Equipo"
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

          {/* Tabla con scroll interno */}
          <div className="flex-1 overflow-auto p-6">
            <DataTable
                data={usuarios}
                columns={columns}
                rowKey={(u) => u.id}
                actions={renderActions}
                scrollable={false}
              />
          </div>

          {/* Footer fijo con paginación */}
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