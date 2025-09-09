import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import SelectDropdown from "../components/SelectDropdown";

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

  const puedeVer = user?.rol?.nombre === "TH" || user?.rol?.nombre === "GTH";

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
    setUsuarios([]);
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
        <div className="bg-white/95 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col h-full overflow-hidden">
          
          {/* Header fijo */}
          <div className="p-6 border-b border-gray-200 flex-shrink-0">
            <h2 className="text-2xl font-bold text-brand-blue mb-4">
              Listado de usuarios
            </h2>

            {error && (
              <p className="text-red-600 mb-4">{error}</p>
            )}

            <div className="mb-4">
          <button
            onClick={() => navigate("/usuarios/nuevo")}
            className="px-4 py-2 bg-green-600 text-white rounded-lg font-medium hover:bg-green-700 transition"
          >
            ➕ Crear Usuario
          </button>
        </div>

            {/* Filtros */}
            <div className="flex flex-wrap gap-4">
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
                  onChange={(e) => setRolId(e.target.value)}
                  options={roles.map((r) => ({ value: r.id, label: r.nombre }))}
                  placeholder="Filtrar por Cargo"
                />

              <SelectDropdown
                  label="Equipo"
                  name="equipo"
                  value={equipoId}
                  onChange={(e) => setRolId(e.target.value)}
                  options={roles.map((r) => ({ value: r.id, label: r.nombre }))}
                  placeholder="Filtrar por Equipo"
                />

              <button
                onClick={limpiarFiltros}
                className="px-4 py-2 bg-gray-200 rounded-lg hover:bg-gray-300 transition text-sm"
              >
                Limpiar filtros
              </button>
            </div>
          </div>

          {/* Tabla con scroll interno */}
          <div className="flex-1 overflow-auto p-6 pt-0">
            <div className="mt-6">
              <table className="w-full border-collapse rounded-lg overflow-hidden">
                <thead className="bg-blue-100 text-blue-800 sticky top-0 z-10">
                  <tr>
                    <th className="px-4 py-3 text-left text-sm font-semibold">ID</th>
                    <th className="px-4 py-3 text-left text-sm font-semibold">Nombre</th>
                    <th className="px-4 py-3 text-left text-sm font-semibold">Correo</th>
                    <th className="px-4 py-3 text-left text-sm font-semibold">Antigüedad</th>
                    <th className="px-4 py-3 text-left text-sm font-semibold">Estado</th>
                    <th className="px-4 py-3 text-left text-sm font-semibold">Acciones</th>
                  </tr>
                </thead>
                <tbody className="bg-white">
                  {usuarios.map((u) => (
                    <tr key={u.id} className="border-b last:border-0 hover:bg-gray-50">
                      <td className="px-4 py-3 text-sm">{u.id}</td>
                      <td className="px-4 py-3 text-sm font-medium">{u.nombre} {u.apellido}</td>
                      <td className="px-4 py-3 text-sm text-gray-600">{u.correo}</td>
                      <td className="px-4 py-3 text-sm">{u.antiguedadPretty ?? "-"}</td>
                      <td className="px-4 py-3">
                        {u.estado === true ? (
                          <span className="px-2 py-1 text-xs font-medium bg-green-100 text-green-700 rounded-full">
                            Activo
                          </span>
                        ) : (
                          <span className="px-2 py-1 text-xs font-medium bg-red-100 text-red-700 rounded-full">
                            Inactivo
                          </span>
                        )}
                      </td>
                      <td className="px-4 py-3">
                        <button
                          onClick={() => navigate(`/usuarios/${u.id}`)}
                          className="px-3 py-1 bg-blue-600 text-white rounded-lg text-xs hover:bg-blue-700 transition"
                        >
                          Editar
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          {/* Footer fijo con paginación */}
          <div className="p-6 border-t border-gray-200 flex-shrink-0">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                {Array.from({ length: totalPages }, (_, i) => (
                  <button
                    key={i}
                    onClick={() => setPage(i)}
                    disabled={i === page}
                    className={`px-3 py-1 rounded-lg text-sm ${
                      i === page
                        ? "bg-blue-600 text-white font-semibold"
                        : "bg-gray-200 hover:bg-gray-300"
                    }`}
                  >
                    {i + 1}
                  </button>
                ))}
              </div>
              
              <button
                type="button"
                onClick={() => navigate(-1)}
                className="px-4 py-2 bg-gray-400 text-white rounded-lg hover:bg-gray-500 transition text-sm"
              >
                Cancelar
              </button>
            </div>
          </div>

        </div>
      </div>
    </div>
  );
}