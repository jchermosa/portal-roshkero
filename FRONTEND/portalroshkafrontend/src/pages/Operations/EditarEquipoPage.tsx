// src/pages/EditarEquipoPage.tsx
import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Select from "react-select";
import DynamicForm from "../../components/DynamicForm";
import DataTable from "../../components/DataTable";
import { useAuth } from "../../context/AuthContext";

type IMiembrosEquipo = { id: number; nombre: string; idCargo: number };
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
const TEAM_PATH = "/api/v1/admin/operations/team"; // /:id

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

export default function EditarEquipoPage() {
  const { token } = useAuth();
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const isDark = useIsDark();

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // datos base del equipo
  const [team, setTeam] = useState<ITeam | null>(null);

  // form editable
  const [formData, setFormData] = useState<{
    nombre: string;
    fechaInicio: string;
    fechaFin: string;
    estado: boolean;
  }>({ nombre: "", fechaInicio: "", fechaFin: "", estado: true });

  // Selects: cliente, lead, tecnologías
  const [clienteOptions, setClienteOptions] = useState<Array<{ value: number; label: string }>>([]);
  const [clienteSel, setClienteSel] = useState<{ value: number; label: string } | null>(null);

  const [leadOptions, setLeadOptions] = useState<Array<{ value: number; label: string }>>([]);
  const [leadSel, setLeadSel] = useState<{ value: number; label: string } | null>(null);

  const [tecnologiaOptions, setTecnologiaOptions] = useState<Array<{ value: number; label: string }>>([]);
  const [tecnologiasSel, setTecnologiasSel] = useState<Array<{ value: number; label: string }>>([]);
  const [tecPick, setTecPick] = useState<number | "">("");

  // miembros
  const [miembros, setMiembros] = useState<IMiembrosEquipo[]>([]);
  const [memberOptions, setMemberOptions] = useState<Array<{ value: number; label: string; idCargo: number }>>([]);
  const [selectedMember, setSelectedMember] = useState<{ value: number; label: string; idCargo: number } | null>(null);
  const [leadId, setLeadId] = useState<number | null>(null); // para badge en tabla

  // cargar metadatas + equipo
  useEffect(() => {
    const ac = new AbortController();
    (async () => {
      try {
        setLoading(true);
        setError(null);

        // metadatas
        const md = await fetch(METADATAS_PATH, {
          headers: { Accept: "application/json", ...(token ? { Authorization: `Bearer ${token}` } : {}) },
          credentials: "include",
          signal: ac.signal,
        });
        if (!md.ok) throw new Error(`Metadatas ${md.status} ${md.statusText}`);
        const meta = await md.json();

        setClienteOptions(
          (meta.clientes ?? []).map((c: any) => ({ value: c.idCliente, label: c.nombre }))
        );

        setLeadOptions(
          (meta.teamleaders ?? meta.lideres ?? meta.usuarios ?? []).map((u: any) => ({
            value: u.idUsuario ?? u.id,
            label: [u.nombre, u.apellido].filter(Boolean).join(" "),
          }))
        );

        setTecnologiaOptions(
          (meta.tecnologias ?? []).map((t: any) => ({ value: t.idTecnologia, label: t.nombre }))
        );

        const allUsers = meta.AllUsersDesarrollos ?? meta.usuarios ?? [];
        setMemberOptions(
          allUsers.map((u: any) => ({
            value: u.id ?? u.idUsuario,
            label: [u.nombre, u.apellido].filter(Boolean).join(" "),
            idCargo: u.idCargo ?? 0,
          }))
        );

        // equipo
        const tr = await fetch(`${TEAM_PATH}/${id}`, {
          headers: { Accept: "application/json", ...(token ? { Authorization: `Bearer ${token}` } : {}) },
          credentials: "include",
          signal: ac.signal,
        });
        if (!tr.ok) throw new Error(`Team ${tr.status} ${tr.statusText}`);
        const t = await tr.json();

        const estadoBool = typeof t.estado === "boolean" ? t.estado : t.estado === "A";
        const fechaFin = t.fechaLimite ?? t.fechaFin ?? "";

        const miembrosInicial: IMiembrosEquipo[] =
          t.usuarios?.map((u: any) => ({
            id: u.idUsuario ?? u.id,
            nombre: [u.nombre, u.apellido].filter(Boolean).join(" ") || u.nombre,
            idCargo: u.idCargo ?? 0,
          })) ??
          t.miembros ??
          [];

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

        setClienteSel(
          t.cliente?.idCliente
            ? { value: t.cliente.idCliente, label: t.cliente.nombre }
            : null
        );

        const lId = t.lider?.idUsuario ?? null;
        setLeadId(lId);
        setLeadSel(
          lId
            ? { value: lId, label: [t.lider?.nombre, t.lider?.apellido].filter(Boolean).join(" ") }
            : null
        );

        setMiembros(miembrosInicial);

        setTecnologiasSel(
          (t.tecnologias ?? []).map((x: any) => ({ value: x.idTecnologia, label: x.nombre }))
        );
      } catch (e: any) {
        if (e?.name !== "AbortError") setError(e.message || "Error");
      } finally {
        setLoading(false);
      }
    })();
    return () => ac.abort();
  }, [id, token]);

  // secciones base
  const getSections = () => [
    {
      title: "Editar Equipo",
      icon: "🛠️",
      fields: [
        { name: "nombre", label: "Nombre del equipo", type: "text" as const, required: true },
        { name: "fechaInicio", label: "Fecha de inicio", type: "date" as const, required: true },
        { name: "fechaFin", label: "Fecha límite", type: "date" as const, required: true },
        { name: "estado", label: "Activo", type: "checkbox" as const, required: true },
      ],
    },
  ];

  const handleFormChange = (updated: Record<string, any>) =>
    setFormData(prev => ({ ...prev, ...updated }));

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
    setMiembros(ms => [...ms, { id: selectedMember.value, nombre: selectedMember.label, idCargo: selectedMember.idCargo }]);
    setSelectedMember(null);
  };

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
      key: "acciones",
      label: "Acciones",
      render: (s: IMiembrosEquipo) => (
        <button
          onClick={() => setMiembros(ms => ms.filter(m => m.id !== s.id))}
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

  // submit con dirty check y shape de tu API (idTecnologias como IDs)
  const onSubmit = async (_data: Record<string, any>) => {
    try {
      if (!team) return;
      if (!clienteSel) throw new Error("Seleccioná un cliente.");
      setSaving(true);
      setError(null);

      const payload: any = {};
      const estadoActual = formData.estado ? "A" : "I";
      const estadoOriginal = team.estado === true || team.estado === "A" ? "A" : "I";

      // nombre
      if (formData.nombre !== team.nombre) payload.nombre = formData.nombre;

      // cliente
      if (clienteSel?.value !== team.clienteId) payload.idCliente = Number(clienteSel.value);

      // fechas
      if (formData.fechaInicio !== team.fechaInicio) payload.fechaInicio = formData.fechaInicio;
      if (formData.fechaFin !== team.fechaFin) payload.fechaLimite = formData.fechaFin;

      // estado
      if (estadoActual !== estadoOriginal) payload.estado = estadoActual;

      // líder
      if ((leadSel?.value ?? null) !== (team.leadId ?? null)) {
        payload.idLider = leadSel ? Number(leadSel.value) : null;
      }

      // tecnologías (IDs)
      const tecIds = tecnologiasSel.map(t => Number(t.value)).sort((a,b)=>a-b);
      const tecOriginal = (team.tecnologias ?? []).map(t => t.idTecnologia).sort((a,b)=>a-b);
      const tecChanged =
        tecIds.length !== tecOriginal.length ||
        tecIds.some((id, i) => id !== tecOriginal[i]);
      if (tecChanged) payload.idTecnologias = tecIds;

      // miembros (solo si cambia la lista de IDs; estructura mínima)
      const miembrosIds = miembros.map(m => m.id).sort((a,b)=>a-b);
      const miembrosOriginal = (team.miembros ?? []).map(m => m.id).sort((a,b)=>a-b);
      const miembrosChanged =
        miembrosIds.length !== miembrosOriginal.length ||
        miembrosIds.some((id, i) => id !== miembrosOriginal[i]);
      if (miembrosChanged) {
        // si tu backend exige más campos, agregalos aquí
        payload.usuarios = miembros.map(m => ({
          idUsuario: m.id,
          idCargo: m.idCargo,
        }));
      }

      if (Object.keys(payload).length === 0) {
        alert("No hay cambios para guardar.");
        return;
      }

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
    <div className="h-full flex flex-col overflow-hidden">
      <div className="absolute inset-0 bg-brand-blue">
        <div className="absolute inset-0 bg-brand-blue/40" />
      </div>

      <div className="relative z-10 h-full p-4">
        <div className="mx-auto w-full max-w-5xl bg-white/45 dark:bg-gray-900/80 backdrop-blur-sm
                        rounded-2xl shadow-lg flex flex-col 
                        max-h-[calc(100vh-2rem)] text-gray-900 dark:text-gray-100">

          {/* Header fijo */}
          <div className="p-6 border-b border-gray-200 dark:border-gray-700 flex-shrink-0">
            <h2 className="text-xl font-bold">Editar Equipo: {team.nombre}</h2>
            <p className="text-sm text-gray-700 dark:text-gray-300">
              Cliente actual: {team.clienteNombre ?? "—"}
            </p>
          </div>

          {/* Body scrolleable */}
          <div className="flex-1 min-h-0 overflow-auto p-6 space-y-6">
            <DynamicForm
              id="editar-equipo-form"
              sections={getSections()}
              initialData={formData}
              onChange={handleFormChange}
              onSubmit={onSubmit}
            />

            {/* Cliente y Team Lead */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
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
              <div>
                <label className="block text-sm mb-2">Team Lead (opcional)</label>
                <Select
                  options={leadOptions}
                  value={leadSel}
                  onChange={(opt) => {
                    const v = opt ? (opt as any) : null;
                    setLeadSel(v);
                    setLeadId(v ? v.value : null);
                  }}
                  isClearable
                  placeholder="Seleccionar team lead…"
                  theme={selectTheme}
                  styles={selectStyles}
                  menuPortalTarget={document.body}
                  menuPosition="fixed"
                />
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
                      <button
                        type="button"
                        onClick={() => handleRemoveTec(t.value)}
                        className="ml-1 hover:opacity-80"
                      >
                        ×
                      </button>
                    </span>
                  ))}
                </div>
              )}
            </div>

            {/* Miembros */}
            <div className="mt-2 flex flex-col gap-3 md:flex-row md:items-center">
              <div className="flex-1 min-w-[240px]">
                <Select
                  options={memberOptions}
                  value={selectedMember}
                  onChange={(opt) => setSelectedMember(opt as any)}
                  isClearable
                  isSearchable
                  placeholder="Buscar miembro para agregar…"
                  theme={selectTheme}
                  styles={selectStyles}
                  menuPortalTarget={document.body}
                  menuPosition="fixed"
                />
              </div>
              <button
                type="button"
                onClick={handleAddMember}
                disabled={!selectedMember}
                className="px-4 py-2 rounded bg-blue-600 text-white text-sm disabled:opacity-60"
              >
                Agregar al equipo
              </button>
            </div>

            <DataTable
              data={miembros}
              columns={columns}
              rowKey={(s) => s.id}
              scrollable={false}
              enableSearch={false}
            />
          </div>

          {/* Footer fijo */}
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
    </div>
  );
}
