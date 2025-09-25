// src/pages/OperationsPage.tsx
import { useEffect, useMemo, useState } from "react";
import DataTable from "../../components/DataTable";
import SelectDropdown from "../../components/SelectDropdown";
import { useNavigate } from "react-router-dom";
import IconButton from "../../components/IconButton";
import { useAuth } from "../../context/AuthContext";
import PaginationFooter from "../../components/PaginationFooter";

const PAGE_SIZE = 10;
const LIST_PATH = "/api/v1/admin/operations/teams";
const TEAM_PATH = "/api/v1/admin/operations/team";
const DELETE_PATH = "/api/v1/admin/operations/team";

type Tecnologia = { idTecnologia: number; nombre: string };
type ApiTeam = {
  idEquipo: number;
  nombre: string;
  fechaInicio?: string;
  fechaLimite?: string;
  estado?: string | boolean | number;
  lider?: { idUsuario: number; nombre: string; apellido: string; correo?: string };
  cliente?: { idCliente: number; nombre: string };
  tecnologias?: Tecnologia[];
};
type Paged<T> = { content: T[]; totalPages: number } | T[];

type IListTeams = {
  idTeam: number;
  nombre: string;
  fechaInicio?: string;
  fechaLimite?: string;
  estado: boolean;
  liderNombre: string;
  liderCorreo?: string;
  clienteNombre: string;
  tecnologias: Tecnologia[];
};

export default function OperationsPage() {
  const navigate = useNavigate();
  const { token } = useAuth();

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [equipos, setEquipos] = useState<IListTeams[]>([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [tipo, setTipo] = useState<string>("");
  const [deleting, setDeleting] = useState<number | null>(null);

  const tiposUnicos = useMemo(
    () => [
      { value: "Cliente", label: "Cliente" },
      { value: "Team Leader", label: "Team Leader" },
      { value: "Tecnologías", label: "Tecnologías" },
    ],
    []
  );

  const fmtDate = (s?: string) => (s ? s.slice(0, 10) : "—");
  const toBoolEstado = (v: ApiTeam["estado"]) =>
    typeof v === "boolean" ? v : v === "A" || v === "ACTIVO" || v === 1 || v === "1" || v === "true";

  useEffect(() => {
    const ac = new AbortController();

    const parseJsonSafe = async (r: Response) => {
      if (r.status === 204) return null;
      const ct = r.headers.get("content-type") || "";
      const raw = await r.text();
      if (!ct.includes("application/json")) throw new Error(`Respuesta no-JSON (${r.status}). Body: ${raw.slice(0,120)}`);
      return JSON.parse(raw);
    };

    (async () => {
      try {
        setLoading(true);
        setError(null);

        const url = `${LIST_PATH}?page=${page}&size=${PAGE_SIZE}`;
        const r = await fetch(url, {
          headers: { Accept: "application/json", ...(token ? { Authorization: `Bearer ${token}` } : {}) },
          credentials: "include",
          signal: ac.signal,
        });
        if (!r.ok) {
          const body = await r.text();
          throw new Error(`${r.status} ${r.statusText} — ${body.slice(0, 160)}`);
        }

        const body: Paged<ApiTeam> | null = await parseJsonSafe(r);
        const items = Array.isArray(body) ? body : body?.content;
        if (!Array.isArray(items)) {
          setEquipos([]);
          setTotalPages(1);
          return;
        }

        const needDetails = items.filter(t => !Array.isArray(t.tecnologias) || t.tecnologias.length === 0);
        const techById = new Map<number, Tecnologia[]>();

        if (needDetails.length) {
          const details = await Promise.all(
            needDetails.map(t =>
              fetch(`${TEAM_PATH}/${t.idEquipo}`, {
                headers: { Accept: "application/json", ...(token ? { Authorization: `Bearer ${token}` } : {}) },
                credentials: "include",
                signal: ac.signal,
              })
              .then(res => (res.ok ? res.json() : null))
              .catch(() => null)
            )
          );
          details.filter(Boolean).forEach((d: any) => {
            techById.set(d.idEquipo, Array.isArray(d.tecnologias) ? d.tecnologias : []);
          });
        }

        const parsed: IListTeams[] = items.map((t) => ({
          idTeam: t.idEquipo,
          nombre: t.nombre ?? "",
          fechaInicio: t.fechaInicio,
          fechaLimite: t.fechaLimite,
          estado: toBoolEstado(t.estado),
          liderNombre: [t.lider?.nombre, t.lider?.apellido].filter(Boolean).join(" "),
          liderCorreo: t.lider?.correo,
          clienteNombre: t.cliente?.nombre ?? "",
          tecnologias: techById.get(t.idEquipo) ?? (t.tecnologias ?? []),
        }));

        setEquipos(parsed);
        setTotalPages(Array.isArray(body) ? 1 : (body?.totalPages ?? 1));
      } catch (e: any) {
        if (e?.name !== "AbortError") setError(e.message || "Error");
      } finally {
        setLoading(false);
      }
    })();

    return () => ac.abort();
  }, [page, token]);

  // Dentro del componente OperationsPage
const [equipoConfirm, setEquipoConfirm] = useState<IListTeams | null>(null);
const [changingEstado, setChangingEstado] = useState(false);

const confirmToggleEstado = (row: IListTeams) => {
  setEquipoConfirm(row);
};

const handleConfirmToggle = async () => {
  if (!equipoConfirm) return;
  setChangingEstado(true);

  const row = equipoConfirm;
  const nuevoEstado = row.estado ? "I" : "A";

  try {
    // Traer detalle para conservar cliente y fechas
    const detRes = await fetch(`${TEAM_PATH}/${row.idTeam}`, {
      headers: {
        Accept: "application/json",
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
      },
      credentials: "include",
    });
    if (!detRes.ok) throw new Error("Error al cargar detalle");
    const det = await detRes.json();

    const payload: any = {
      estado: nuevoEstado,
      idCliente: det?.cliente?.idCliente ?? row.clienteNombre,
      fechaInicio: det?.fechaInicio ?? row.fechaInicio,
      fechaLimite: det?.fechaLimite ?? row.fechaLimite,
    };

    const r = await fetch(`${TEAM_PATH}/${row.idTeam}`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
      },
      credentials: "include",
      body: JSON.stringify(payload),
    });
    if (!r.ok) throw new Error("Error al actualizar estado");

    setEquipos(prev =>
      prev.map(e =>
        e.idTeam === row.idTeam ? { ...e, estado: !row.estado } : e
      )
    );
    setEquipoConfirm(null); // cerrar modal
  } catch (err: any) {
    setError(err.message || "Error al cambiar estado");
  } finally {
    setChangingEstado(false);
  }
};




  const columns = [
    { key: "idTeam", label: "ID" },
    { key: "nombre", label: "Proyecto/Equipo" },
    { key: "clienteNombre", label: "Cliente" },
    {
      key: "liderNombre",
      label: "Líder",
      render: (row: IListTeams) => (
        <div className="flex flex-col">
          <span>{row.liderNombre || "-"}</span>
          {row.liderCorreo ? (
            <span className="text-xs text-gray-600 dark:text-gray-300">{row.liderCorreo}</span>
          ) : null}
        </div>
      ),
    },
    { key: "fechaInicio", label: "Inicio", render: (row: IListTeams) => <span>{fmtDate(row.fechaInicio)}</span> },
    { key: "fechaLimite", label: "Límite", render: (row: IListTeams) => <span>{fmtDate(row.fechaLimite)}</span> },
    {
      key: "estado",
      label: "Estado",
      render: (row: IListTeams) => (
        <span className={row.estado ? "text-green-600" : "text-red-500"}>
          {row.estado ? "Activo" : "Inactivo"}
        </span>
      ),
    },
    {
  key: "tecnologias",
  label: "Tecnologías",
  render: (row: IListTeams) => {
    const maxToShow = 2;
    const extra = row.tecnologias.length - maxToShow;
    const visibles = row.tecnologias.slice(0, maxToShow);

    if (!extra) return <span className="text-gray-500 dark:text-gray-400">—</span>;

    return (
      <div className="relative group flex flex-wrap gap-1 max-w-[220px]">
        {visibles.map((tec) => (
          <span
            key={tec.idTecnologia}
            className="px-2 py-0.5 text-xs rounded-full bg-blue-100 text-blue-700 dark:bg-blue-900/60 dark:text-blue-100"
          >
            {tec.nombre}
          </span>
        ))}

        {/* pill de conteo con tooltip */}
       {extra > 0 && (
          <span className="px-2 py-0.5 text-xs rounded-full bg-gray-200 text-gray-700 dark:bg-blue-900/60 dark:text-blue-100">
            +{extra}
          </span>)}

        {/* tooltip */}
        <div
          className="pointer-events-none absolute z-20 hidden group-hover:block top-full left-0 mt-2 w-64 rounded-lg border border-gray-200 dark:border-gray-700
                     bg-white dark:bg-gray-800 shadow-lg p-3"
          role="tooltip"
        >
          <p className="text-xs font-semibold text-gray-700 dark:text-gray-200 mb-2">
            Tecnologías
          </p>
          <div className="flex flex-wrap gap-1">
            {row.tecnologias.map((t) => (
              <span
                key={t.idTecnologia}
                className="px-2 py-0.5 text-[11px] rounded-full bg-blue-100 text-blue-700 dark:bg-blue-900/60 dark:text-blue-100"
              >
                {t.nombre}
              </span>
            ))}
          </div>
        </div>
      </div>
    );
  },
},

    {
      key: "acciones",
      label: "Acciones",
      render: (row: IListTeams) => (
        <div className="flex gap-1">
          <button type="button" onClick={() => navigate(`/equipo/${row.idTeam}/edit`)}>
            <link
              rel="stylesheet"
              href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"
            />
            <span className="material-symbols-outlined text-gray-500">edit</span>
          </button>
          <button
  type="button"
  onClick={() => confirmToggleEstado(row)}
  aria-label="Cambiar estado"
  title="Cambiar estado"
>
  <span className="material-symbols-outlined text-gray-500">
    mode_off_on
  </span>
</button>
    </div>
  ),
},
  ];

  const limpiarFiltros = () => setTipo("");

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
        <div className="absolute inset-0 bg-brand-blue/40" />
      </div>

      <div className="relative z-10 flex flex-col h-full p-4">
        <div className="bg-white/45 dark:bg-gray-900/70 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col h-full overflow-hidden">
          <div className="p-6 border-b border-gray-200 dark:border-gray-700 flex-shrink-0">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-2xl font-bold text-brand-blue dark:text-blue-200">Equipos</h2>
              <IconButton
                label="Nuevo Equipo"
                icon={<span>➕</span>}
                variant="primary"
                onClick={() => navigate("/equipo/nuevo")}
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
                placeholder="Filtrar por"
              />
              <div className="mb-4">
                <label className="block text-sm font-medium text-transparent mb-1 select-none">&nbsp;</label>
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
            {loading && <div className="text-sm text-gray-600 dark:text-gray-300">Cargando…</div>}
            {error && <div className="text-sm text-red-600">Error: {error}</div>}
            {!loading && !error && (
              <DataTable
                data={equipos}
                columns={columns}
                rowKey={(s: IListTeams) => s.idTeam}
                scrollable={false}
              />
            )}
          </div>

          {/* Footer de paginación */}
          <PaginationFooter
            currentPage={page}
            totalPages={totalPages}
            onPageChange={setPage}
            onCancel={() => navigate(-1)}
          />
        </div>
      </div>
      {equipoConfirm && (
  <div className="fixed inset-0 flex items-center justify-center z-50 bg-black/50">
    <div className="bg-white dark:bg-gray-700 rounded-lg shadow-lg p-6 w-full max-w-md">
      <h3 className="text-lg text-gray-800 dark:text-gray-100 mb-4">
        {equipoConfirm.estado
          ? `¿Seguro que quieres desactivar "${equipoConfirm.nombre}"?`
          : `¿Seguro que quieres activar "${equipoConfirm.nombre}"?`}
      </h3>
      <div className="flex justify-end gap-3">
        <button
          onClick={() => setEquipoConfirm(null)}
          className="px-4 py-2 rounded border border-gray-300 dark:border-gray-600 text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700"
          disabled={changingEstado}
        >
          Cancelar
        </button>
        <button
          onClick={handleConfirmToggle}
          className={`px-4 py-2 rounded text-white ${
            equipoConfirm.estado
              ? "bg-red-600 hover:bg-red-700"
              : "bg-green-600 hover:bg-green-700"
          }`}
          disabled={changingEstado}
        >
          {changingEstado ? "Guardando..." : "Confirmar"}
        </button>
      </div>
    </div>
  </div>
)}

    </div>
  );
}
