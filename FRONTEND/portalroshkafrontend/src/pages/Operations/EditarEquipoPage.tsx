// src/pages/EditarEquipoPage.tsx
import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Select from "react-select";
import DynamicForm from "../../components/DynamicForm";
import DataTable from "../../components/DataTable";
import { useAuth } from "../../context/AuthContext";

type IMiembrosEquipo = {
  id: number;
  nombre: string;
  idCargo: number;
  disponibilidad?: number;   // 0..100 en UI
  fechaEntrada?: string;     // YYYY-MM-DD
  fechaFin?: string;         // YYYY-MM-DD
};
type Tecnologia = { idTecnologia: number; nombre: string };

type ITeam = {
  idEquipo: number;
  nombre: string;
  clienteId?: number;
  clienteNombre?: string;
  fechaInicio: string;
  fechaFin: string; // o fechaLimite
  estado: boolean | "A" | "I";
  tecnologias: Tecnologia[];
  miembros: IMiembrosEquipo[];
  leadId?: number | null;
  leadNombre?: string | null;
};

const METADATAS_PATH = "/api/v1/admin/operations/metadatas";
const TEAM_PATH = "/api/v1/admin/operations/team";   // /:id
const USERS_PATH = "/api/v1/admin/operations/users"; // fallback listado

function useIsDark() {
  const [isDark, setIsDark] = useState(
    typeof document !== "undefined" && document.documentElement.classList.contains("dark")
  );
  useEffect(() => {
    const el = document.documentElement;
    const obs = new MutationObserver(() => setIsDark(el.classList.contains("dark")));
    obs.observe(el, { attributes: true, attributeFilter: ["class"] });
    return () => obs.disconnect();
  }, []);
  return isDark;
}

/** Normaliza valores de disponibilidad:
 * - Si viene 0..1 => lo convierte a porcentaje (0..100)
 * - Si viene 1..100 => asume porcentaje y lo limita a 0..100
 */
const toPercent = (v: any) => {
  const n = Number(v);
  if (!isFinite(n)) return 100;
  if (n <= 1) return Math.round(n * 100);
  return Math.max(0, Math.min(100, Math.round(n)));
};
/** Convierte el valor de UI (0..100 o 0..1) a fracción para el backend */
const toFraction = (p: any) => {
  const n = Number(p);
  if (!isFinite(n)) return 1;
  return n > 1 ? +(n / 100).toFixed(4) : n;
};

export default function EditarEquipoPage() {
  const { token } = useAuth();
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const isDark = useIsDark();

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // errores externos para campos (p.ej. nombre duplicado)
  const [externalErrors, setExternalErrors] = useState<Record<string, string>>({});

  // datos base del equipo
  const [team, setTeam] = useState<ITeam | null>(null);

  // form editable
  const [formData, setFormData] = useState<{ nombre: string; fechaInicio: string; fechaFin: string; estado: boolean; }>(
    { nombre: "", fechaInicio: "", fechaFin: "", estado: true }
  );

  // Selects: cliente, lead, tecnologías
  const [clienteOptions, setClienteOptions] = useState<Array<{ value: number; label: string }>>([]);
  const [clienteSel, setClienteSel] = useState<{ value: number; label: string } | null>(null);

  const [leadOptions, setLeadOptions] = useState<Array<{ value: number; label: string }>>([]);
  const [leadSel, setLeadSel] = useState<{ value: number; label: string } | null>(null);
  const [leadId, setLeadId] = useState<number | null>(null);
  const [leadSaving, setLeadSaving] = useState(false);

  const [tecnologiaOptions, setTecnologiaOptions] = useState<Array<{ value: number; label: string }>>([]);
  const [tecnologiasSel, setTecnologiasSel] = useState<Array<{ value: number; label: string }>>([]);
  const [tecPick, setTecPick] = useState<number | "">("");

  // miembros
  const [miembros, setMiembros] = useState<IMiembrosEquipo[]>([]);
  const [memberOptions, setMemberOptions] = useState<Array<{ value: number; label: string; idCargo: number }>>([]);
  const [selectedMember, setSelectedMember] = useState<{ value: number; label: string; idCargo: number } | null>(null);
  const [newMemberDisp, setNewMemberDisp] = useState<number>(100);
  const [newMemberStart, setNewMemberStart] = useState<string>("");
  const [newMemberEnd, setNewMemberEnd] = useState<string>("");

  const firstArray = (...cands: any[][]) => cands.find(a => Array.isArray(a) && a.length) ?? [];

  // cargar metadatas + team (+ fallback users)
  useEffect(() => {
    const ac = new AbortController();
    (async () => {
      try {
        setLoading(true);
        setError(null);

        // METADATAS
        const md = await fetch(METADATAS_PATH, {
          headers: { Accept: "application/json", ...(token ? { Authorization: `Bearer ${token}` } : {}) },
          credentials: "include",
          signal: ac.signal,
        });
        if (!md.ok) throw new Error(`Metadatas ${md.status} ${md.statusText}`);
        const meta = await md.json();

        setClienteOptions((meta.clientes ?? []).map((c: any) => ({ value: c.idCliente, label: c.nombre })));

        const rawLeads = firstArray(
          meta.teamleaders,
          meta.teamLeaders,
          meta.team_leaders,
          meta.lideres,
          meta.leaders,
          meta.usuariosLideres,
          meta.usuarios // fallback
        );
        setLeadOptions(
          rawLeads.map((u: any) => ({
            value: u.idUsuario ?? u.id ?? u.usuarioId,
            label: [u.nombre, u.apellido].filter(Boolean).join(" ") || u.nombreCompleto || "Sin nombre",
          }))
        );

        setTecnologiaOptions((meta.tecnologias ?? []).map((t: any) => ({ value: t.idTecnologia, label: t.nombre })));

        // TEAM (usamos usuariosNoEnEquipo si viene para el selector)
        const tr = await fetch(`${TEAM_PATH}/${id}`, {
          headers: { Accept: "application/json", ...(token ? { Authorization: `Bearer ${token}` } : {}) },
          credentials: "include",
          signal: ac.signal,
        });
        if (!tr.ok) throw new Error(`Team ${tr.status} ${tr.statusText}`);
        const t = await tr.json();

        const notIn = Array.isArray(t.usuariosNoEnEquipo) ? t.usuariosNoEnEquipo : [];
        if (notIn.length) {
          setMemberOptions(
            notIn.map((u: any) => ({
              value: u.idUsuario ?? u.id,
              label: [u.nombre, u.apellido].filter(Boolean).join(" "),
              idCargo: u.idCargo ?? 0,
            }))
          );
        } else {
          // Fallback users (por si la API no envía usuariosNoEnEquipo)
          const ur = await fetch(USERS_PATH, {
            headers: { Accept: "application/json", ...(token ? { Authorization: `Bearer ${token}` } : {}) },
            credentials: "include",
            signal: ac.signal,
          });
          if (ur.ok) {
            const usersData = await ur.json();
            const usersArr = Array.isArray(usersData?.content) ? usersData.content : (Array.isArray(usersData) ? usersData : []);
            setMemberOptions(
              usersArr.map((u: any) => ({
                value: u.id ?? u.idUsuario ?? u.usuarioId,
                label: [u.nombre, u.apellido].filter(Boolean).join(" ") || u.nombreCompleto || "Sin nombre",
                idCargo: u.idCargo ?? u.cargo?.idCargo ?? 0,
              }))
            );
          }
        }

        const estadoBool = typeof t.estado === "boolean" ? t.estado : t.estado === "A";
        const fechaFin = t.fechaLimite ?? t.fechaFin ?? "";

        const miembrosFuente = firstArray(
          t.usuariosAsignacion,
          t.usuariosEnEquipo,
          t.usuarios,
          t.miembros,
          t.integrantes,
          t.miembrosEquipo
        );

        const miembrosInicial: IMiembrosEquipo[] = miembrosFuente.map((u: any) => ({
          id: u.idUsuario ?? u.id ?? u.usuarioId,
          nombre:
            u.nombreCompleto ??
            [u.nombre, u.apellido].filter(Boolean).join(" ") ??
            String(u.nombre ?? u.apellido ?? "Sin nombre"),
          idCargo: u.idCargo ?? u.cargo?.idCargo ?? 0,
          disponibilidad: toPercent(u.disponibilidad ?? u.porcentajeTrabajo ?? 100),
          fechaEntrada: u.fechaEntrada ?? "",
          fechaFin: u.fechaFin ?? "",
        }));

        setTeam({
          idEquipo: t.idEquipo,
          nombre: t.nombre,
          clienteId: t.cliente?.idCliente,
          clienteNombre: t.cliente?.nombre,
          fechaInicio: t.fechaInicio ?? "",
          fechaFin,
          estado: estadoBool,
          tecnologias: t.tecnologias ?? [],
          miembros: miembrosInicial,
          leadId: t.lider?.idUsuario ?? null,
          leadNombre: [t.lider?.nombre, t.lider?.apellido].filter(Boolean).join(" ") || null,
        });

        setFormData({
          nombre: t.nombre ?? "",
          fechaInicio: t.fechaInicio ?? "",
          fechaFin,
          estado: estadoBool,
        });

        setMiembros(miembrosInicial);
        setTecnologiasSel((t.tecnologias ?? []).map((x: any) => ({ value: x.idTecnologia, label: x.nombre })));

        const lId = t.lider?.idUsuario ?? null;
        setLeadId(lId);
        setClienteSel(
          t.cliente?.idCliente
            ? { value: t.cliente.idCliente, label: t.cliente.nombre }
            : null
        );
        if (lId && !rawLeads.find((u: any) => (u.idUsuario ?? u.id ?? u.usuarioId) === lId)) {
          const phantom = { value: lId, label: [t.lider?.nombre, t.lider?.apellido].filter(Boolean).join(" ") || "Team Lead" };
          setLeadOptions(prev => [...prev, phantom]);
          setLeadSel(phantom);
        } else {
          setLeadSel(lId ? { value: lId, label: [t.lider?.nombre, t.lider?.apellido].filter(Boolean).join(" ") } : null);
        }
      } catch (e: any) {
        if (e?.name !== "AbortError") setError(e.message || "Error");
      } finally {
        setLoading(false);
      }
    })();
    return () => ac.abort();
  }, [id, token]);

  // limpiar error externo por campo
  const onClearExternalError = (name: string) =>
    setExternalErrors((prev) => {
      const next = { ...prev };
      delete next[name];
      return next;
    });

  // secciones base con botón de verificación de nombre
  const getSections = () => [
    {
      title: "Editar Equipo",
      icon: "🛠️",
      fields: [
        {
          name: "asyncCheck",
          label: " ",
          type: "custom" as const,
          fullWidth: true,
          render: () => (
            <div className="flex items-center justify-end">
              <button
                type="button"
                onClick={handleCheckNombre}
                className="px-3 py-1.5 text-sm rounded bg-amber-500 hover:bg-amber-600 text-white"
                title="Verificar si el nombre ya existe sin guardar"
              >
                Verificar nombre
              </button>
            </div>
          ),
        },
        { name: "nombre", label: "Nombre del equipo", type: "text" as const, required: true },
        { name: "fechaInicio", label: "Fecha de inicio", type: "date" as const, required: true },
        { name: "fechaFin", label: "Fecha límite", type: "date" as const, required: true },
        { name: "estado", label: "Activo", type: "checkbox" as const, required: true },
      ],
    },
  ];

  const handleFormChange = (updated: Record<string, any>) =>
    setFormData(prev => ({ ...prev, ...updated }));

  // Verificar nombre llamando PATCH /team/:id solo con {nombre}
  const handleCheckNombre = async () => {
    if (!team) return;
    const nombre = (formData.nombre || "").trim();
    if (!nombre) {
      setExternalErrors((p) => ({ ...p, nombre: "Ingresá un nombre para verificar." }));
      return;
    }
    onClearExternalError("nombre");
    try {
      const r = await fetch(`${TEAM_PATH}/${team.idEquipo}`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          ...(token ? { Authorization: `Bearer ${token}` } : {}),
        },
        credentials: "include",
        body: JSON.stringify({ nombre }),
      });
      if (!r.ok) {
        const txt = await r.text();
        setExternalErrors((p) => ({
          ...p,
          nombre:
            /existe|duplic/i.test(txt) || r.status === 409
              ? "El nombre ya existe"
              : "No se pudo verificar el nombre",
        }));
      } else {
        setExternalErrors((p) => {
          const n = { ...p };
          delete n.nombre;
          return n;
        });
        alert("Nombre disponible");
      }
    } catch {
      setExternalErrors((p) => ({ ...p, nombre: "No se pudo verificar el nombre" }));
    }
  };

  // tecnologías
  const handleAddTec = () => {
    if (tecPick === "") return;
    const opt = tecnologiaOptions.find(o => o.value === Number(tecPick));
    if (!opt) return;
    if (tecnologiasSel.some(t => t.value === opt.value)) return;
    setTecnologiasSel(prev => [...prev, opt]);
    setTecPick("");
  };
  const handleRemoveTec = (idTec: number) =>
    setTecnologiasSel(prev => prev.filter(t => t.value !== idTec));

  // miembros
  const handleAddMember = () => {
    if (!selectedMember) return;
    if (miembros.some(m => m.id === selectedMember.value)) return;

    setMiembros(ms => [
      ...ms,
      {
        id: selectedMember.value,
        nombre: selectedMember.label,
        idCargo: selectedMember.idCargo,
        disponibilidad: Math.max(0, Math.min(100, newMemberDisp)),
        fechaEntrada: newMemberStart || "",
        fechaFin: newMemberEnd || "",
      },
    ]);
    setMemberOptions(opts => opts.filter(o => o.value !== selectedMember.value));
    setSelectedMember(null);
    setNewMemberDisp(100);
    setNewMemberStart("");
    setNewMemberEnd("");
  };

  const updateMemberDisp = (id: number, val: number) =>
    setMiembros(ms => ms.map(m => (m.id === id ? { ...m, disponibilidad: Math.max(0, Math.min(100, val)) } : m)));
  const updateMemberStart = (id: number, v: string) =>
    setMiembros(ms => ms.map(m => (m.id === id ? { ...m, fechaEntrada: v } : m)));
  const updateMemberEnd = (id: number, v: string) =>
    setMiembros(ms => ms.map(m => (m.id === id ? { ...m, fechaFin: v } : m)));

  const columns = [
    { key: "id", label: "ID" },
    {
      key: "nombre",
      label: "Nombre",
      render: (s: IMiembrosEquipo) => (
        <div className="flex items-center gap-2">
          <span className="text-gray-900 dark:text-gray-100">{s.nombre}</span>
          {leadId === s.id && (
            <span className="px-2 py-0.5 text-[10px] rounded-full bg-blue-100 text-blue-700 dark:bg-blue-900 dark:text-blue-100">
              Lead
            </span>
          )}
        </div>
      ),
    },
    { key: "idCargo", label: "Cargo" },
    {
      key: "disponibilidad",
      label: "Disp. (%)",
      render: (s: IMiembrosEquipo) => (
        <input
          type="number"
          min={0}
          max={100}
          value={s.disponibilidad ?? 100}
          onChange={(e) =>
            updateMemberDisp(s.id, Number.isNaN(+e.target.value) ? 0 : +e.target.value)
          }
          className="w-20 px-2 py-1 rounded border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-800 text-sm"
        />
      ),
    },
    {
      key: "fechaEntrada",
      label: "Entrada",
      render: (s: IMiembrosEquipo) => (
        <input
          type="date"
          value={s.fechaEntrada ?? ""}
          onChange={(e) => updateMemberStart(s.id, e.target.value)}
          className="px-2 py-1 rounded border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-800 text-sm"
        />
      ),
    },
    {
      key: "fechaFin",
      label: "Fin",
      render: (s: IMiembrosEquipo) => (
        <input
          type="date"
          value={s.fechaFin ?? ""}
          onChange={(e) => updateMemberEnd(s.id, e.target.value)}
          className="px-2 py-1 rounded border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-800 text-sm"
        />
      ),
    },
    {
      key: "acciones",
      label: "Acciones",
      render: (s: IMiembrosEquipo) => (
        <button
          onClick={() => {
            setMiembros(ms => ms.filter(m => m.id !== s.id));
            setMemberOptions(opts => [...opts, { value: s.id, label: s.nombre, idCargo: s.idCargo }]);
          }}
          className="px-3 py-1 text-sm text-white bg-blue-600 rounded hover:bg-blue-700 focus:outline-none"
        >
          Eliminar
        </button>
      ),
    },
  ];

  // theme select
  const selectTheme = useMemo(
    () => (base: any) => ({
      ...base,
      colors: {
        ...base.colors,
        neutral0: isDark ? "#111827" : "#ffffff",
        neutral20: isDark ? "#374151" : "#d1d5db",
        neutral80: isDark ? "#f9fafb" : "#111827",
        primary: "#3b82f6",
        primary25: isDark ? "#374151" : "#e5e7eb",
        primary50: isDark ? "#1f2937" : "#dbeafe",
      },
    }),
    [isDark]
  );
  const selectStyles = useMemo(
    () => ({
      control: (p: any, s: any) => ({
        ...p,
        backgroundColor: isDark ? "#111827" : "#ffffff",
        borderColor: s.isFocused ? "#3b82f6" : isDark ? "#374151" : "#d1d5db",
        boxShadow: s.isFocused ? "0 0 0 2px rgba(59,130,246,.2)" : "none",
        ":hover": { borderColor: "#3b82f6" },
        minHeight: 40,
      }),
      menuPortal: (b: any) => ({ ...b, zIndex: 9999 }),
      menu: (p: any) => ({ ...p, zIndex: 9999, backgroundColor: isDark ? "#111827" : "#ffffff" }),
      input: (p: any) => ({ ...p, color: isDark ? "#f9fafb" : "#111827" }),
      singleValue: (p: any) => ({ ...p, color: isDark ? "#f9fafb" : "#111827" }),
      placeholder: (p: any) => ({ ...p, color: isDark ? "#9ca3af" : "#6b7280" }),
      option: (p: any, s: any) => ({
        ...p,
        backgroundColor: s.isSelected ? (isDark ? "#1f2937" : "#dbeafe") : s.isFocused ? (isDark ? "#374151" : "#e5e7eb") : "transparent",
        color: isDark ? "#f9fafb" : "#111827",
      }),
    }),
    [isDark]
  );

  // PATCH inmediato para cambiar Team Lead (botón "Cambiar")
  const patchLeadNow = async () => {
    if (!team) return;
    try {
      setLeadSaving(true);
      const payload: any = { idLider: leadSel ? Number(leadSel.value) : null };
      const r = await fetch(`${TEAM_PATH}/${team.idEquipo}`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          ...(token ? { Authorization: `Bearer ${token}` } : {}),
        },
        credentials: "include",
        body: JSON.stringify(payload),
      });
      if (!r.ok) {
        const txt = await r.text();
        throw new Error(`${r.status} ${r.statusText} — ${txt.slice(0,160)}`);
      }
      setLeadId(leadSel ? leadSel.value : null); // éxito
    } catch (e: any) {
      alert(e.message || "No se pudo actualizar el Team Lead");
    } finally {
      setLeadSaving(false);
    }
  };

  // submit: payload completo (convierte disponibilidad a fracción 0..1)
  const onSubmit = async (_data: Record<string, any>) => {
    try {
      if (!team) return;
      if (!clienteSel) throw new Error("Seleccioná un cliente.");
      setSaving(true);
      setError(null);
      setExternalErrors({});

      const payload: any = {
        nombre: formData.nombre,
        idCliente: Number(clienteSel.value),
        fechaInicio: formData.fechaInicio,
        fechaLimite: formData.fechaFin,
        estado: formData.estado ? "A" : "I",
        idLider: leadSel ? Number(leadSel.value) : null,
        idTecnologias: tecnologiasSel.map(t => Number(t.value)),
        usuarios: miembros.map(m => ({
          idUsuario: m.id,
          idCargo: m.idCargo,
          porcentajeTrabajo: toFraction(m.disponibilidad ?? 100),
          fechaEntrada: m.fechaEntrada || null,
          fechaFin: m.fechaFin || null,
          estado: "A",
        })),
      };

      const r = await fetch(`${TEAM_PATH}/${team.idEquipo}`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          ...(token ? { Authorization: `Bearer ${token}` } : {}),
        },
        credentials: "include",
        body: JSON.stringify(payload),
      });
      if (!r.ok) {
        const txt = await r.text();
        if (/existe|duplic/i.test(txt) || r.status === 409) {
          setExternalErrors({ nombre: "El nombre ya existe" });
          throw new Error("Nombre duplicado");
        }
        throw new Error(`${r.status} ${r.statusText} — ${txt.slice(0,160)}`);
      }
      navigate("/operations");
    } catch (e: any) {
      setError(e.message || "Error al guardar");
    } finally {
      setSaving(false);
    }
  };

  if (loading) return <div className="p-4 text-sm">Cargando…</div>;
  if (error) return <div className="p-4 text-sm text-red-600">Error: {error}</div>;
  if (!team) return <div className="p-4 text-sm">No se encontró el equipo.</div>;

  return (
    <div className="h-full flex flex-col overflow-hidden">
      {/* Fondo */}
      <div
        className="absolute inset-0 bg-brand-blue"
        style={{ backgroundImage: "url('/src/assets/ilustracion-herov3.svg')", backgroundSize: "cover", backgroundPosition: "center", backgroundRepeat: "no-repeat" }}
      >
        <div className="absolute inset-0 bg-brand-blue/40" />
      </div>

      <div className="relative z-10 h-full p-4">
        <div className="mx-auto w-full max-w-5xl bg-white/45 dark:bg-gray-900/80 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col max-h-[calc(100vh-2rem)] text-gray-900 dark:text-gray-100">
          {/* Header */}
          <div className="p-6 border-b border-gray-200 dark:border-gray-700">
            <h2 className="text-xl font-bold">Editar Equipo: {team.nombre}</h2>
            <p className="text-sm text-gray-700 dark:text-gray-300">Cliente actual: {team.clienteNombre ?? "—"}</p>
          </div>

          {/* Body */}
          <div className="flex-1 overflow-auto p-6 space-y-6">
            <DynamicForm
              id="editar-equipo-form"
              sections={getSections()}
              initialData={formData}
              onChange={handleFormChange}
              onSubmit={onSubmit}
              externalErrors={externalErrors}
              onClearExternalError={onClearExternalError}
            />

            {/* Cliente y Team Lead + botón Cambiar */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 items-end">
              <div>
                <label className="block text-sm mb-2">Cliente</label>
                <Select
                  options={clienteOptions}
                  value={clienteSel}
                  onChange={(opt) => setClienteSel(opt as any)}
                  placeholder="Seleccionar cliente…"
                  theme={selectTheme}
                  styles={selectStyles}
                  menuPortalTarget={document.body}
                  menuPosition="fixed"
                />
              </div>

              <div className="grid grid-cols-3 gap-2 items-end">
                <div className="col-span-2">
                  <label className="block text-sm mb-2">Team Lead (metadatas)</label>
                  <Select
                    options={leadOptions}
                    value={leadSel}
                    onChange={(opt) => setLeadSel(opt as any)}
                    isClearable
                    placeholder="Seleccionar team lead…"
                    theme={selectTheme}
                    styles={selectStyles}
                    menuPortalTarget={document.body}
                    menuPosition="fixed"
                  />
                </div>
                <button
                  type="button"
                  onClick={patchLeadNow}
                  disabled={leadSaving || (leadSel?.value ?? null) === (team.leadId ?? null)}
                  className="h-10 px-3 rounded bg-green-600 text-white text-sm disabled:opacity-60"
                  title="Aplicar cambio de Team Lead ahora"
                >
                  {leadSaving ? "Guardando…" : "Cambiar"}
                </button>
              </div>
            </div>

            {/* Tecnologías */}
            <div>
              <label className="block text-sm mb-2">Tecnologías</label>
              <div className="flex gap-2">
                <select
                  className="w-full h-10 rounded border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100 px-3"
                  value={tecPick}
                  onChange={(e) => setTecPick(e.target.value === "" ? "" : Number(e.target.value))}
                >
                  <option value="">Seleccionar tecnología…</option>
                  {tecnologiaOptions.map((t) => (
                    <option key={t.value} value={t.value}>{t.label}</option>
                  ))}
                </select>
                <button
                  type="button"
                  onClick={handleAddTec}
                  disabled={tecPick === ""}
                  className="px-4 h-10 rounded bg-blue-600 text-white text-sm disabled:opacity-60"
                >
                  Agregar
                </button>
              </div>
              {tecnologiasSel.length > 0 && (
                <div className="mt-3 flex flex-wrap gap-2">
                  {tecnologiasSel.map((t) => (
                    <span
                      key={t.value}
                      className="inline-flex items-center gap-1 px-2 py-0.5 text-xs rounded-full bg-blue-100 text-blue-700 dark:bg-blue-900 dark:text-blue-100"
                    >
                      {t.label}
                      <button type="button" onClick={() => handleRemoveTec(t.value)} className="ml-1 hover:opacity-80">×</button>
                    </span>
                  ))}
                </div>
              )}
            </div>

            {/* Agregar miembro con fecha inicio/fin y disponibilidad */}
            <div className="grid grid-cols-1 md:grid-cols-5 gap-3 items-end">
              <div className="md:col-span-2">
                <label className="block text-sm mb-2">Agregar miembro</label>
                <Select
                  options={memberOptions}
                  value={selectedMember}
                  onChange={(opt) => setSelectedMember(opt as any)}
                  isClearable
                  isSearchable
                  placeholder="Buscar miembro…"
                  theme={selectTheme}
                  styles={selectStyles}
                  menuPortalTarget={document.body}
                  menuPosition="fixed"
                />
              </div>
              <div>
                <label className="block text-sm mb-2">Entrada</label>
                <input
                  type="date"
                  value={newMemberStart}
                  onChange={(e) => setNewMemberStart(e.target.value)}
                  className="w-full px-3 py-2 rounded border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-800 text-sm"
                />
              </div>
              <div>
                <label className="block text-sm mb-2">Fin</label>
                <input
                  type="date"
                  value={newMemberEnd}
                  onChange={(e) => setNewMemberEnd(e.target.value)}
                  className="w-full px-3 py-2 rounded border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-800 text-sm"
                />
              </div>
              <div className="flex items-end gap-2">
                <input
                  type="number"
                  min={0}
                  max={100}
                  value={newMemberDisp}
                  onChange={(e) => setNewMemberDisp(Math.max(0, Math.min(100, Number(e.target.value) || 0)))}
                  className="w-full px-3 py-2 rounded border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-800 text-sm"
                  placeholder="Disp. %"
                />
                <button
                  type="button"
                  onClick={handleAddMember}
                  disabled={!selectedMember}
                  className="px-4 h-10 rounded bg-blue-600 text-white text-sm disabled:opacity-60"
                >
                  Agregar
                </button>
              </div>
            </div>

            <DataTable
              data={miembros}
              columns={columns}
              rowKey={(s) => s.id}
              scrollable={false}
              enableSearch={false}
            />
          </div>

          {/* Footer */}
          <div className="p-4 md:p-6 border-t border-gray-200 dark:border-gray-700 flex-shrink-0 flex items-center justify-between">
            <p className="text-sm text-gray-700 dark:text-gray-300">Revisá y guardá los cambios.</p>
            <div className="flex items-center gap-3">
              <button
                type="button"
                onClick={() => navigate("/operations")}
                className="px-4 py-2 rounded border border-gray-300 dark:border-gray-600 text-sm"
              >
                Cancelar
              </button>
              <button
                type="submit"
                form="editar-equipo-form"
                disabled={saving}
                className="px-4 py-2 rounded bg-blue-600 hover:bg-blue-700 text-white text-sm disabled:opacity-60"
              >
                {saving ? "Guardando…" : "Guardar"}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
