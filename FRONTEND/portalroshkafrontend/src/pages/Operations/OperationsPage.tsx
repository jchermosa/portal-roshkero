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

type Tecnologia = { idTecnologia: number; nombre: string };

type EquipoDiaUbicacion = {
  diaLaboral?: { idDiaLaboral?: number; nombreDia?: string };
  ubicacion?: { idUbicacion?: number; nombre?: string };
};

type ApiTeam = {
  idEquipo: number;
  nombre: string;
  fechaInicio?: string;
  fechaLimite?: string;
  estado?: string | boolean | number;
  lider?: { idUsuario: number; nombre: string; apellido: string; correo?: string };
  cliente?: { idCliente: number; nombre: string };
  tecnologias?: Tecnologia[];
  equipoDiaUbicacion?: EquipoDiaUbicacion[];
};

type Paged<T> = { content: T[]; totalPages: number } | T[];

type Pair = { dia: string; ubicacion: string };

type IListTeams = {
  idTeam: number;
  nombre: string;
  estado: boolean;
  liderNombre: string;
  liderCorreo?: string;
  clienteNombre: string;
  tecnologias: Tecnologia[];
  duPairs: Pair[];
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

  const [equipoConfirm, setEquipoConfirm] = useState<IListTeams | null>(null);
  const [changingEstado, setChangingEstado] = useState(false);

  const [viewId, setViewId] = useState<number | null>(null);

  const tiposUnicos = useMemo(
    () => [
      { value: "Cliente", label: "Cliente" },
      { value: "Team Leader", label: "Team Leader" },
      { value: "Tecnologías", label: "Tecnologías" },
    ],
    []
  );

  const toBoolEstado = (v: ApiTeam["estado"]) =>
    typeof v === "boolean"
      ? v
      : v === "A" || v === "ACTIVO" || v === 1 || v === "1" || v === "true";

  useEffect(() => {
    const ac = new AbortController();

    const parseJsonSafe = async (r: Response) => {
      if (r.status === 204) return null;
      const ct = r.headers.get("content-type") || "";
      const raw = await r.text();
      if (!ct.includes("application/json"))
        throw new Error(`Respuesta no-JSON (${r.status}). Body: ${raw.slice(0, 120)}`);
      return JSON.parse(raw);
    };

    (async () => {
      try {
        setLoading(true);
        setError(null);

        const url = `${LIST_PATH}?page=${page}&size=${PAGE_SIZE}`;
        const r = await fetch(url, {
          headers: {
            Accept: "application/json",
            ...(token ? { Authorization: `Bearer ${token}` } : {}),
          },
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

        const needDetails = items.filter(
          (t) => !Array.isArray(t.tecnologias) || t.tecnologias.length === 0
        );
        const techById = new Map<number, Tecnologia[]>();

        if (needDetails.length) {
          const details = await Promise.all(
            needDetails.map((t) =>
              fetch(`${TEAM_PATH}/${t.idEquipo}`, {
                headers: {
                  Accept: "application/json",
                  ...(token ? { Authorization: `Bearer ${token}` } : {}),
                },
                credentials: "include",
                signal: ac.signal,
              })
                .then((res) => (res.ok ? res.json() : null))
                .catch(() => null)
            )
          );
          details
            .filter(Boolean)
            .forEach((d: any) => {
              techById.set(d.idEquipo, Array.isArray(d.tecnologias) ? d.tecnologias : []);
            });
        }

        const parsed: IListTeams[] = items.map((t) => {
          const duPairs: Pair[] = (t.equipoDiaUbicacion ?? [])
            .map((e) => ({
              dia: e?.diaLaboral?.nombreDia?.trim() ?? "",
              ubicacion: e?.ubicacion?.nombre?.trim() ?? "",
            }))
            .filter((p) => p.dia && p.ubicacion);

          return {
            idTeam: t.idEquipo,
            nombre: t.nombre ?? "",
            estado: toBoolEstado(t.estado),
            liderNombre: [t.lider?.nombre, t.lider?.apellido].filter(Boolean).join(" "),
            liderCorreo: t.lider?.correo,
            clienteNombre: t.cliente?.nombre ?? "",
            tecnologias: techById.get(t.idEquipo) ?? (t.tecnologias ?? []),
            duPairs,
          };
        });

        setEquipos(parsed);
        setTotalPages(Array.isArray(body) ? 1 : body?.totalPages ?? 1);
      } catch (e: any) {
        if (e?.name !== "AbortError") setError(e.message || "Error");
      } finally {
        setLoading(false);
      }
    })();

    return () => ac.abort();
  }, [page, token]);

  const confirmToggleEstado = (row: IListTeams) => setEquipoConfirm(row);

  const handleConfirmToggle = async () => {
    if (!equipoConfirm) return;
    setChangingEstado(true);

    const row = equipoConfirm;
    const nuevoEstado = row.estado ? "I" : "A";

    try {
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
        idCliente: det?.cliente?.idCliente,
        fechaInicio: det?.fechaInicio,
        fechaLimite: det?.fechaLimite,
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

      setEquipos((prev) =>
        prev.map((e) => (e.idTeam === row.idTeam ? { ...e, estado: !row.estado } : e))
      );
      setEquipoConfirm(null);
    } catch (err: any) {
      setError(err.message || "Error al cambiar estado");
    } finally {
      setChangingEstado(false);
    }
  };

  const columns = [
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
    {
      key: "duPairs",
      label: "Día / Ubicación",
      render: (row: IListTeams) => <PairGridWithOverflow pairs={row.duPairs} maxRows={2} />,
    },
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
      render: (row: IListTeams) => (
        <PillsWithOverflow items={row.tecnologias.map((t) => t.nombre)} maxToShow={2} color="blue" />
      ),
    },
    {
      key: "acciones",
      label: "Acciones",
      render: (row: IListTeams) => (
        <div className="flex gap-1">
          <button type="button" onClick={() => setViewId(row.idTeam)} aria-label="Show" title="Ver Equipo">
            <link
              rel="stylesheet"
              href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"
            />
            <span className="material-symbols-outlined text-gray-500">visibility</span>
          </button>
          <button
            type="button"
            onClick={() => navigate(`/equipo/${row.idTeam}/edit`)}
            aria-label="Editar"
            title="Editar"
          >
            <span className="material-symbols-outlined text-gray-500">edit</span>
          </button>
          <button
            type="button"
            onClick={() => confirmToggleEstado(row)}
            aria-label="Cambiar estado"
            title="Cambiar estado"
          >
            <span className="material-symbols-outlined text-gray-500">mode_off_on</span>
          </button>
        </div>
      ),
    },
  ];

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
          </div>

          <div className="flex-1 overflow-auto p-6">
            {loading && <div className="text-sm text-gray-600 dark:text-gray-300">Cargando…</div>}
            {error && <div className="text-sm text-red-600">Error: {error}</div>}
            {!loading && !error && (
              <DataTable data={equipos} columns={columns} rowKey={(s: IListTeams) => s.idTeam} scrollable={false} />
            )}
          </div>

          <PaginationFooter currentPage={page} totalPages={totalPages} onPageChange={setPage} onCancel={() => navigate(-1)} />
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
                  equipoConfirm.estado ? "bg-red-600 hover:bg-red-700" : "bg-green-600 hover:bg-green-700"
                }`}
                disabled={changingEstado}
              >
                {changingEstado ? "Guardando..." : "Confirmar"}
              </button>
            </div>
          </div>
        </div>
      )}

      {viewId && <TeamDetailsModal id={viewId} token={token} onClose={() => setViewId(null)} />}
    </div>
  );
}

/* =======================
   Modal Ver Equipo
   ======================= */

type TeamDetailsModalProps = {
  id: number;
  token?: string | null;
  onClose: () => void;
};

function TeamDetailsModal({ id, token, onClose }: TeamDetailsModalProps) {
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState<string | null>(null);
  const [det, setDet] = useState<any>(null);
  const [memberNamesNoLead, setMemberNamesNoLead] = useState<string[]>([]);

  function extractMembersFromTeamDetail(d: any): string[] {
    const sources = [
      d?.miembros,
      d?.asignacionesUsuarioEquipo,
      d?.equipoUsuarios,
      d?.usuarios,
    ].filter(Array.isArray) as any[][];
    const fullName = (x: any) => {
      const u = x?.usuario ?? x;
      const fn = u?.nombre ?? u?.nombres ?? "";
      const ln = u?.apellido ?? u?.apellidos ?? "";
      const n = `${fn} ${ln}`.trim() || u?.nombreCompleto || u?.fullName || u?.displayName || "";
      return n.trim();
    };
    const set = new Set<string>();
    for (const arr of sources) for (const it of arr) {
      const n = fullName(it);
      if (n) set.add(n);
    }
    return Array.from(set);
  }

  useEffect(() => {
    const ac = new AbortController();
    (async () => {
      try {
        setLoading(true);
        setErr(null);
        const r = await fetch(`${TEAM_PATH}/${id}`, {
          headers: {
            Accept: "application/json",
            ...(token ? { Authorization: `Bearer ${token}` } : {}),
          },
          credentials: "include",
          signal: ac.signal,
        });
        if (!r.ok) throw new Error(`Error ${r.status}`);
        const data = await r.json();

        const baseMembers = extractMembersFromTeamDetail(data);
        const lead = [data?.lider?.nombre, data?.lider?.apellido].filter(Boolean).join(" ").trim().toLowerCase();
        const noLead = lead ? baseMembers.filter((n) => n.toLowerCase() !== lead) : baseMembers;
        setMemberNamesNoLead(noLead);

        setDet(data);
      } catch (e: any) {
        if (e?.name !== "AbortError") setErr(e.message || "Error");
      } finally {
        setLoading(false);
      }
    })();
    return () => ac.abort();
  }, [id, token]);

  const estadoActivo =
    typeof det?.estado === "boolean"
      ? det.estado
      : det?.estado === "A" || det?.estado === "ACTIVO" || det?.estado === 1 || det?.estado === "1" || det?.estado === "true";

  const duPairs: Pair[] = (det?.equipoDiaUbicacion ?? [])
    .map((e: any) => ({
      dia: e?.diaLaboral?.nombreDia?.trim() ?? "",
      ubicacion: e?.ubicacion?.nombre?.trim() ?? "",
    }))
    .filter((p) => p.dia && p.ubicacion);

  const tecnologias: { idTecnologia: number; nombre: string }[] = det?.tecnologias ?? [];

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div className="absolute inset-0 bg-black/50" onClick={onClose} />
      <div className="relative z-10 w-full max-w-3xl rounded-2xl shadow-xl overflow-hidden">
        <div className="relative bg-white/70 dark:bg-gray-900/70 backdrop-blur-md">
          <div className="flex items-start justify-between p-6 border-b border-gray-200 dark:border-gray-700">
            <div>
              <h3 className="text-xl font-bold text-brand-blue dark:text-blue-200">{det?.nombre || "Equipo"}</h3>
              <div className="mt-2 flex flex-wrap items-center gap-2">
                <span
                  className={`px-2 py-0.5 text-xs rounded-full ${
                    estadoActivo
                      ? "bg-green-100 text-green-700 dark:bg-green-900/60 dark:text-green-100"
                      : "bg-red-100 text-red-700 dark:bg-red-900/60 dark:text-red-100"
                  }`}
                >
                  {estadoActivo ? "Activo" : "Inactivo"}
                </span>
                {det?.fechaInicio && (
                  <span className="px-2 py-0.5 text-xs rounded-full bg-gray-100 text-gray-700 dark:bg-gray-700 dark:text-gray-100">
                    Inicio: {det.fechaInicio}
                  </span>
                )}
                {det?.fechaLimite && (
                  <span className="px-2 py-0.5 text-xs rounded-full bg-gray-100 text-gray-700 dark:bg-gray-700 dark:text-gray-100">
                    Límite: {det.fechaLimite}
                  </span>
                )}
              </div>
            </div>
            <button
              onClick={onClose}
              className="p-2 rounded-lg text-gray-600 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-800"
              aria-label="Cerrar"
              title="Cerrar"
            >
              ✕
            </button>
          </div>

          <div className="p-6">
            {loading && <div className="text-sm text-gray-600 dark:text-gray-300">Cargando…</div>}
            {err && <div className="text-sm text-red-600">Error: {err}</div>}
            {!loading && !err && (
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="space-y-3">
                  <Section label="Cliente">
                    <span>{det?.cliente?.nombre || "—"}</span>
                  </Section>
                  <Section label="Líder">
                    <div className="flex flex-col">
                      <span>{[det?.lider?.nombre, det?.lider?.apellido].filter(Boolean).join(" ") || "—"}</span>
                      {det?.lider?.correo && (
                        <span className="text-xs text-gray-600 dark:text-gray-300">{det.lider.correo}</span>
                      )}
                    </div>
                  </Section>
                  <Section label="Día / Ubicación">
                    <PairGridWithOverflow pairs={duPairs} maxRows={2} />
                  </Section>
                </div>

                <div className="space-y-3">
                  <Section label="Tecnologías">
                    <PillsWithOverflow items={(tecnologias ?? []).map((t: any) => t.nombre)} maxToShow={2} color="blue" />
                  </Section>
                </div>

                <div className="md:col-span-2">
                  <Section label="Miembros del Equipo">
                    <PillsWithOverflow items={memberNamesNoLead} maxToShow={8} color="blue" />
                  </Section>
                </div>
              </div>
            )}
          </div>

          <div className="p-4 border-t border-gray-200 dark:border-gray-700 flex justify-end">
            <button
              onClick={onClose}
              className="px-4 py-2 rounded border border-gray-300 dark:border-gray-600 text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-800"
            >
              Cerrar
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

/* =======================
   Helpers
   ======================= */

function Section({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div>
      <p className="text-xs font-semibold text-gray-700 dark:text-gray-200 mb-1">{label}</p>
      <div className="text-sm text-gray-800 dark:text-gray-100">{children}</div>
    </div>
  );
}

function PillsWithOverflow({
  items,
  maxToShow = 2,
  color,
}: {
  items: string[];
  maxToShow?: number;
  color: "blue" | "gray";
}) {
  const base = "px-2 py-0.5 text-xs rounded-full";
  const theme =
    color === "blue"
      ? "bg-blue-100 text-blue-700 dark:bg-blue-900/60 dark:text-blue-100"
      : "bg-gray-200 text-gray-700 dark:bg-gray-700 dark:text-gray-100";

  const visibles = items.slice(0, maxToShow);
  const extras = items.slice(maxToShow);
  const extraCount = Math.max(0, items.length - maxToShow);

  if (items.length === 0) return <span className="text-gray-500 dark:text-gray-400">—</span>;

  return (
    <div className="relative group flex flex-wrap gap-1 max-w-[240px]">
      {visibles.map((t, i) => (
        <span key={`${t}-${i}`} className={`${base} ${theme}`}>
          {t}
        </span>
      ))}
      {extraCount > 0 && <span className={`${base} ${theme}`}>+{extraCount}</span>}
      {extras.length > 0 && (
        <div className="pointer-events-none absolute left-0 top-full mt-1 hidden w-max max-w-[320px] rounded-md border border-gray-200 bg-white p-2 text-xs text-gray-800 shadow-lg group-hover:block dark:border-gray-700 dark:bg-gray-900 dark:text-gray-100 z-10">
          <div className="flex flex-wrap gap-1">
            {extras.map((t, i) => (
              <span key={`extra-${t}-${i}`} className={`${base} ${theme}`}>
                {t}
              </span>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

function PairGridWithOverflow({ pairs, maxRows = 2 }: { pairs: Pair[]; maxRows?: number }) {
  if (!pairs.length) return <span className="text-gray-500 dark:text-gray-400">—</span>;

  const baseCell =
    "px-2 py-0.5 text-xs rounded-full bg-blue-100 text-blue-700 dark:bg-blue-900/60 dark:text-blue-100";
  const visibles = pairs.slice(0, maxRows);
  const extras = pairs.slice(maxRows);
  const extraCount = Math.max(0, pairs.length - maxRows);

  return (
    <div className="relative group">
      <div className="grid grid-cols-2 gap-1 mb-1">
        <span className="text-[10px] uppercase tracking-wide text-gray-500 dark:text-gray-400">Día</span>
        <span className="text-[10px] uppercase tracking-wide text-gray-500 dark:text-gray-400">Ubicación</span>
      </div>

      <div className="grid grid-cols-2 gap-1 max-w=[340px]">
        {visibles.map((p, i) => (
          <FragmentRow key={`${p.dia}-${p.ubicacion}-${i}`} p={p} baseCell={baseCell} />
        ))}
        {extraCount > 0 && <span className={`${baseCell} col-span-2 w-min`}>+{extraCount}</span>}
      </div>

      {extras.length > 0 && (
        <div className="pointer-events-none absolute left-0 top-full mt-1 hidden w-max max-w-[420px] rounded-md border border-gray-200 bg-white p-2 text-xs text-gray-800 shadow-lg group-hover:block dark:border-gray-700 dark:bg-gray-900 dark:text-gray-100 z-10">
          <div className="grid grid-cols-2 gap-1">
            {pairs.map((p, i) => (
              <FragmentRow key={`all-${p.dia}-${p.ubicacion}-${i}`} p={p} baseCell={baseCell} />
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

function FragmentRow({ p, baseCell }: { p: Pair; baseCell: string }) {
  return (
    <>
      <span className={baseCell}>{p.dia}</span>
      <span className={baseCell}>{p.ubicacion}</span>
    </>
  );
}
