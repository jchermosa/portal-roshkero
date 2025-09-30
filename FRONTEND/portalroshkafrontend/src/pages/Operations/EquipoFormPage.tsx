// src/pages/EquipoFormPage.tsx
import { useState, useMemo, useEffect } from "react";
import Select from "react-select";
import { useNavigate } from "react-router-dom";
import DynamicForm from "../../components/DynamicForm";
import DataTable from "../../components/DataTable";
import { useAuth } from "../../context/AuthContext";

const METADATAS_PATH   = "/api/v1/admin/operations/metadatas";
const CREATE_TEAM_PATH = "/api/v1/admin/operations/team";
const DIAS_PATH        = "/api/v1/admin/operations/diaslaborales";
const LIBRES_PATH      = "/api/v1/admin/operations/asignacion/libres";
const USERS_PATH       = "/api/v1/admin/operations/users";

type DiaLaboral = { idDiaLaboral: number; nombreDia: string };
type Ubicacion  = { idUbicacion: number; nombre: string };

type AsignDU = {
  idDiaLaboral: number;
  nombreDia: string;
  idUbicacion?: number | null;
  nombreUbicacion?: string | null;
};

type IMiembrosEquipo = {
  id: number;
  nombre: string;
  idCargo: number;
  disponibilidad?: number;
  fechaEntrada?: string;
  fechaFin?: string;
};

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

const toPercent = (v: any) => {
  const n = Number(v);
  if (!isFinite(n)) return 100;
  if (n <= 1) return Math.round(n * 100);
  return Math.max(0, Math.min(100, Math.round(n)));
};
const toFraction = (p: any) => {
  const n = Number(p);
  if (!isFinite(n)) return 1;
  return n > 1 ? +(n / 100).toFixed(4) : n;
};

export default function EquipoFormPage() {
  const { token } = useAuth();
  const navigate = useNavigate();
  const isDark = useIsDark();

  // ------ Form base ------
  const [formData, setFormData] = useState({
    nombre: "",
    fechaInicio: "",
    fechaFin: "",
    estado: true,
  });
  const [externalErrors, setExternalErrors] = useState<Record<string, string>>({});
  const onClearExternalError = (name: string) =>
    setExternalErrors((prev) => {
      const next = { ...prev };
      delete next[name];
      return next;
    });

  // ------ Metadatas ------
  const [clienteOptions, setClienteOptions] = useState<Array<{ value: number; label: string }>>([]);
  const [leadOptions, setLeadOptions] = useState<Array<{ value: number; label: string }>>([]);
  const [tecOptions, setTecOptions] = useState<Array<{ value: number; label: string }>>([]);

  // ------ Selecciones base ------
  const [clienteSel, setClienteSel] = useState<{ value: number; label: string } | null>(null);
  const [leadSel, setLeadSel]       = useState<{ value: number; label: string } | null>(null); // opcional
  const [tecSel, setTecSel]         = useState<Array<{ value: number; label: string }>>([]);
  const [tecPick, setTecPick]       = useState<number | "">("");

  // ------ Días / Ubicaciones ------
  const [diasOptions, setDiasOptions] = useState<Array<{ value: number; label: string }>>([]);
  const [libresByDay, setLibresByDay] = useState<Map<number, Ubicacion[]>>(new Map());
  const [asignaciones, setAsignaciones] = useState<AsignDU[]>([]); // sólo guardadas; se renderizan todas las filas

  // ------ Miembros ------
  const [miembros, setMiembros] = useState<IMiembrosEquipo[]>([]);
  const [memberOptions, setMemberOptions] = useState<
    Array<{ value: number; label: string; idCargo: number }>
  >([]);
  const [selectedMember, setSelectedMember] = useState<{ value: number; label: string; idCargo: number } | null>(null);
  const [selectedMemberMaxDisp, setSelectedMemberMaxDisp] = useState<number>(100);
  const [newMemberDisp, setNewMemberDisp] = useState<number>(100);
  const [newMemberStart, setNewMemberStart] = useState<string>("");
  const [newMemberEnd, setNewMemberEnd] = useState<string>("");

  // cache de disponibilidad restante por usuario
  const [availCache, setAvailCache] = useState<Map<number, number>>(new Map());

  // ------ THEME react-select ------
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
      input: (p: any) => ({ ...p, color: isDark ? "#ffffff" : "#111827" }),
      singleValue: (p: any) => ({ ...p, color: isDark ? "#ffffff" : "#111827" }),
      placeholder: (p: any) => ({ ...p, color: isDark ? "#d1d5db" : "#6b7280" }),
      option: (p: any, s: any) => ({
        ...p,
        backgroundColor: s.isSelected
          ? (isDark ? "#1f2937" : "#dbeafe")
          : s.isFocused
          ? (isDark ? "#374151" : "#e5e7eb")
          : "transparent",
        color: isDark ? "#ffffff" : "#111827",
      }),
    }),
    [isDark]
  );

  // ------ Carga metadatas + días/ubicaciones ------
  useEffect(() => {
    const ac = new AbortController();
    (async () => {
      try {
        const r = await fetch(METADATAS_PATH, {
          headers: { Accept: "application/json", ...(token ? { Authorization: `Bearer ${token}` } : {}) },
          credentials: "include",
          signal: ac.signal,
        });
        if (!r.ok) throw new Error(`Metadatas ${r.status}`);
        const data = await r.json();

        setClienteOptions((data.clientes ?? []).map((c: any) => ({ value: c.idCliente, label: c.nombre })));

        const leadsSrc = (data.teamleaders ?? data.lideres ?? data.usuarios ?? []) as any[];
        setLeadOptions(
          leadsSrc.map((u: any) => ({
            value: u.idUsuario ?? u.id,
            label: [u.nombre, u.apellido].filter(Boolean).join(" ") || u.nombreCompleto || "Sin nombre",
          }))
        );

        setTecOptions((data.tecnologias ?? []).map((t: any) => ({ value: t.idTecnologia, label: t.nombre })));

        // Si metadatas trae usuarios, tomalos para el selector base
        const usersSrc = (data.usuarios ?? data.users ?? []) as any[];
        if (usersSrc.length) {
          setMemberOptions(
            usersSrc.map((u: any) => ({
              value: u.id ?? u.idUsuario ?? u.usuarioId,
              label: [u.nombre, u.apellido].filter(Boolean).join(" ") || u.nombreCompleto || "Sin nombre",
              idCargo: u.idCargo ?? u.cargo?.idCargo ?? 0,
            }))
          );
        } else {
          // fallback al listado general
          const ur = await fetch(USERS_PATH, {
            headers: { Accept: "application/json", ...(token ? { Authorization: `Bearer ${token}` } : {}) },
            credentials: "include",
            signal: ac.signal,
          });
          if (ur.ok) {
            const usersData = await ur.json();
            const arr = Array.isArray(usersData?.content) ? usersData.content : (Array.isArray(usersData) ? usersData : []);
            setMemberOptions(
              arr.map((u: any) => ({
                value: u.id ?? u.idUsuario ?? u.usuarioId,
                label: [u.nombre, u.apellido].filter(Boolean).join(" ") || u.nombreCompleto || "Sin nombre",
                idCargo: u.idCargo ?? u.cargo?.idCargo ?? 0,
              }))
            );
          }
        }
      } catch (e) {
        console.error(e);
      }
    })();

    (async () => {
      try {
        const dr = await fetch(DIAS_PATH, {
          headers: { Accept: "application/json", ...(token ? { Authorization: `Bearer ${token}` } : {}) },
          credentials: "include",
          signal: ac.signal,
        });
        if (dr.ok) {
          const dias: DiaLaboral[] = await dr.json();
          setDiasOptions(dias.map((d) => ({ value: d.idDiaLaboral, label: d.nombreDia })));
        }
        const lr = await fetch(LIBRES_PATH, {
          headers: { Accept: "application/json", ...(token ? { Authorization: `Bearer ${token}` } : {}) },
          credentials: "include",
          signal: ac.signal,
        });
        if (lr.ok) {
          const libres = await lr.json();
          const map = new Map<number, Ubicacion[]>();
          libres.forEach((entry: any) => {
            const idDia = entry.idDiaLaboral;
            const arr: Ubicacion[] = Array.isArray(entry.ubicacionesLibres) ? entry.ubicacionesLibres : [];
            map.set(idDia, arr);
          });
          setLibresByDay(map);
        }
      } catch (e) {
        console.error(e);
      }
    })();

    return () => ac.abort();
  }, [token]);

  // ------ Validación simple de nombre ------
  useEffect(() => {
    const nombre = (formData.nombre || "").trim();
    if (!nombre) {
      setExternalErrors((p) => ({ ...p, nombre: "Ingresá un nombre" }));
    } else {
      onClearExternalError("nombre");
    }
  }, [formData.nombre]);

  // ------ Días/Ubicaciones helpers ------
  const dayName = useMemo(() => {
    const m = new Map<number, string>();
    diasOptions.forEach((d) => m.set(d.value, d.label));
    return m;
  }, [diasOptions]);

  const setUbicacionForDay = (dayId: number, opt: { value: number; label: string } | null) => {
    setAsignaciones((prev) => {
      const others = prev.filter((a) => a.idDiaLaboral !== dayId);
      return [
        ...others,
        {
          idDiaLaboral: dayId,
          nombreDia: dayName.get(dayId) ?? "—",
          idUbicacion: opt ? opt.value : null,
          nombreUbicacion: opt ? opt.label : null,
        },
      ];
    });
  };

  const rowsDU: AsignDU[] = useMemo(() => {
    const map = new Map<number, AsignDU>();
    asignaciones.forEach((a) => map.set(a.idDiaLaboral, a));
    return diasOptions.map((d) => {
      const a = map.get(d.value);
      return {
        idDiaLaboral: d.value,
        nombreDia: d.label,
        idUbicacion: a?.idUbicacion ?? null,
        nombreUbicacion: a?.nombreUbicacion ?? null,
      };
    });
  }, [diasOptions, asignaciones]);

  const optionsForDay = (dayId: number, currentId?: number | null, currentName?: string | null) => {
    let opts = (libresByDay.get(dayId) ?? []).map((u) => ({ value: u.idUbicacion, label: u.nombre }));
    if (currentId != null && !opts.some((o) => o.value === currentId)) {
      opts = [{ value: currentId, label: currentName ?? "—" }, ...opts];
    }
    return opts;
  };

  // ------ Miembros: disponibilidad restante al elegir (onChange) ------
  const fetchRemainingAvailability = async (userId: number): Promise<number> => {
    // cache hit
    if (availCache.has(userId)) return availCache.get(userId)!;

    // buscar en /users (lista) y tomar el campo más razonable
    const r = await fetch(USERS_PATH, {
      headers: { Accept: "application/json", ...(token ? { Authorization: `Bearer ${token}` } : {}) },
      credentials: "include",
    });
    if (!r.ok) throw new Error(`Users ${r.status}`);
    const data = await r.json();
    const arr = Array.isArray(data?.content) ? data.content : (Array.isArray(data) ? data : []);
    const u = arr.find(
      (x: any) => (x.id ?? x.idUsuario ?? x.usuarioId) === userId
    );

    // heurística: probar campos comunes
    const rest =
      toPercent(
        u?.disponibilidadRestante ??
          u?.porcentajeDisponible ??
          u?.disponibilidadDisponible ??
          u?.porcentajeTrabajoRestante ??
          u?.disponibilidad ??
          u?.porcentajeTrabajo ??
          100
      );

    setAvailCache((m) => {
      const nm = new Map(m);
      nm.set(userId, rest);
      return nm;
    });
    return rest;
  };

  const handleMemberSelect = async (opt: any) => {
    const sel = opt as { value: number; label: string; idCargo: number } | null;
    setSelectedMember(sel);
    if (!sel) {
      setSelectedMemberMaxDisp(100);
      setNewMemberDisp(100);
      return;
    }
    try {
      const remaining = await fetchRemainingAvailability(sel.value);
      setSelectedMemberMaxDisp(remaining);
      setNewMemberDisp(remaining); // <- cambia el input al lado del botón
    } catch {
      setSelectedMemberMaxDisp(100);
      setNewMemberDisp(100);
    }
  };

  const handleAddMember = () => {
    if (!selectedMember) return;
    if (miembros.some((m) => m.id === selectedMember.value)) return;

    const toAdd = Math.max(0, Math.min(selectedMemberMaxDisp, Number(newMemberDisp) || 0));

    setMiembros((ms) => [
      ...ms,
      {
        id: selectedMember.value,
        nombre: selectedMember.label,
        idCargo: selectedMember.idCargo,
        disponibilidad: toAdd,
        fechaEntrada: newMemberStart || "",
        fechaFin: newMemberEnd || "",
      },
    ]);
    setMemberOptions((opts) => opts.filter((o) => o.value !== selectedMember.value));
    setSelectedMember(null);
    setSelectedMemberMaxDisp(100);
    setNewMemberDisp(100);
    setNewMemberStart("");
    setNewMemberEnd("");
  };

  const updateMemberDisp = (id: number, val: number) =>
    setMiembros((ms) =>
      ms.map((m) => (m.id === id ? { ...m, disponibilidad: Math.max(0, Math.min(100, val)) } : m))
    );
  const updateMemberStart = (id: number, v: string) =>
    setMiembros((ms) => ms.map((m) => (m.id === id ? { ...m, fechaEntrada: v } : m)));
  const updateMemberEnd = (id: number, v: string) =>
    setMiembros((ms) => ms.map((m) => (m.id === id ? { ...m, fechaFin: v } : m)));

  const memberColumns = [
    { key: "id", label: "ID" },
    { key: "nombre", label: "Nombre" },
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
          onChange={(e) => updateMemberDisp(s.id, Number.isNaN(+e.target.value) ? 0 : +e.target.value)}
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
            setMiembros((ms) => ms.filter((m) => m.id !== s.id));
            setMemberOptions((opts) => [
              ...opts,
              { value: s.id, label: s.nombre, idCargo: s.idCargo },
            ]);
          }}
          className="px-3 py-1 text-sm text-white bg-blue-600 rounded hover:bg-blue-700 focus:outline-none"
        >
          Eliminar
        </button>
      ),
    },
  ];

  // ------ Tecnologías ------
  const handleAddTec = () => {
    if (tecPick === "") return;
    const opt = tecOptions.find((o) => o.value === Number(tecPick));
    if (!opt) return;
    if (tecSel.some((t) => t.value === opt.value)) return;
    setTecSel((prev) => [...prev, opt]);
    setTecPick("");
  };
  const handleRemoveTec = (id: number) =>
    setTecSel((prev) => prev.filter((t) => t.value !== id));

  // ------ Secciones del form base ------
  const getSections = () => [
    {
      title: "Crear Equipo",
      icon: "📅",
      fields: [
        { name: "nombre",      label: "Nombre del equipo", type: "text" as const,  required: true },
        { name: "fechaInicio", label: "Fecha de inicio",   type: "date" as const,  required: true },
        { name: "fechaFin",    label: "Fecha límite",      type: "date" as const,  required: true },
        { name: "estado",      label: "Activo",            type: "checkbox" as const, required: true },
      ],
    },
  ];

  const handleFormChange = (updatedData: any) => setFormData((prev) => ({ ...prev, ...updatedData }));

  // ------ Submit (POST) ------
  const handleSubmit = async (data: Record<string, any>) => {
    if (!clienteSel) {
      setExternalErrors((p) => ({ ...p, cliente: "Seleccioná un cliente." }));
      throw new Error("Seleccioná un cliente.");
    }

    const duPayload = rowsDU.map((a) => {
      const base: any = { diaLaboral: { idDiaLaboral: a.idDiaLaboral } };
      if (a.idUbicacion != null) base.ubicacion = { idUbicacion: a.idUbicacion };
      return base;
    });

    const payload = {
      nombre: data.nombre,
      idLider: leadSel ? Number(leadSel.value) : null,
      fechaInicio: data.fechaInicio,
      fechaLimite: data.fechaFin,
      idCliente: Number(clienteSel.value),
      estado: data.estado ? "A" : "I",
      idTecnologias: tecSel.map((t) => Number(t.value)),
      usuarios: miembros.map((m) => ({
        idUsuario: m.id,
        idCargo: m.idCargo,
        porcentajeTrabajo: toFraction(m.disponibilidad ?? 100),
        fechaEntrada: m.fechaEntrada || null,
        fechaFin: m.fechaFin || null,
        estado: "A",
      })),
      equipoDiaUbicacion: duPayload,
    };

    const r = await fetch(CREATE_TEAM_PATH, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
      },
      credentials: "include",
      body: JSON.stringify(payload),
    });
    if (!r.ok) {
      const msg = await r.text();
      if (/existe|duplic/i.test(msg) || r.status === 409) {
        setExternalErrors((p) => ({ ...p, nombre: "El nombre ya existe" }));
      }
      throw new Error(`${r.status} ${r.statusText} — ${msg}`);
    }
    navigate("/operations");
  };

  return (
    <div className="h-full flex flex-col overflow-hidden">
      <div className="absolute inset-0 bg-brand-blue">
        <div className="absolute inset-0 bg-brand-blue/40" />
      </div>

      <div className="relative z-10 h-full p-4">
        <div className="mx-auto w-full max-w-5xl bg-white/45 dark:bg-gray-900/80 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col max-h-[calc(100vh-2rem)] text-gray-900 dark:text-gray-100">
          {/* Header */}
          <div className="p-6 border-b border-gray-200 dark:border-gray-700">
            <h2 className="text-xl font-bold">Crear Equipo</h2>
          </div>

          {/* Body */}
          <div className="flex-1 overflow-auto p-6 space-y-6">
            <DynamicForm
              id="equipo-form"
              sections={getSections()}
              initialData={formData}
              onChange={handleFormChange}
              onSubmit={handleSubmit}
              externalErrors={externalErrors}
              onClearExternalError={onClearExternalError}
            />

            {/* Cliente y Team Lead */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm mb-2">Cliente</label>
                <Select
                  options={clienteOptions}
                  value={clienteSel}
                  onChange={(opt) => { setClienteSel(opt as any); onClearExternalError("cliente"); }}
                  placeholder="Seleccionar cliente…"
                  theme={selectTheme}
                  styles={selectStyles}
                  menuPortalTarget={document.body}
                  menuPosition="fixed"
                />
                {externalErrors.cliente && (
                  <p className="text-xs text-red-500 mt-1">{externalErrors.cliente}</p>
                )}
              </div>
              <div>
                <label className="block text-sm mb-2">Team Lead (opcional)</label>
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
            </div>

            {/* Tabla Día–Ubicación */}
            <div>
              <h3 className="text-sm font-semibold mb-2">Días y ubicaciones asignadas</h3>
              <DataTable
                data={rowsDU}
                columns={[
                  { key: "nombreDia", label: "Día" },
                  {
                    key: "nombreUbicacion",
                    label: "Ubicación",
                    render: (r: AsignDU) => (
                      <Select
                        options={optionsForDay(r.idDiaLaboral, r.idUbicacion ?? null, r.nombreUbicacion ?? null)}
                        value={r.idUbicacion != null ? { value: r.idUbicacion, label: r.nombreUbicacion ?? "—" } : null}
                        onChange={(opt) => setUbicacionForDay(r.idDiaLaboral, opt as any)}
                        isClearable
                        placeholder="Elegir ubicación…"
                        theme={selectTheme}
                        styles={selectStyles}
                        menuPortalTarget={document.body}
                        menuPosition="fixed"
                      />
                    ),
                  },
                ]}
                rowKey={(r: AsignDU) => String(r.idDiaLaboral)}
                scrollable={false}
                enableSearch={false}
              />
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
                  {tecOptions.map((t) => (
                    <option key={t.value} value={t.value}>
                      {t.label}
                    </option>
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
              {tecSel.length > 0 && (
                <div className="mt-3 flex flex-wrap gap-2">
                  {tecSel.map((t) => (
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

            {/* Agregar miembro (con disponibilidad restante dinámica) */}
            <div className="grid grid-cols-1 md:grid-cols-5 gap-3 items-end">
              <div className="md:col-span-2">
                <label className="block text-sm mb-2">Agregar miembro</label>
                <Select
                  options={memberOptions}
                  value={selectedMember}
                  onChange={handleMemberSelect}
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
                  max={selectedMemberMaxDisp}
                  value={newMemberDisp}
                  onChange={(e) => {
                    const v = Number(e.target.value) || 0;
                    const clamped = Math.max(0, Math.min(selectedMemberMaxDisp, v));
                    setNewMemberDisp(clamped);
                  }}
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
              columns={memberColumns}
              rowKey={(s) => s.id}
              scrollable={false}
              enableSearch={false}
            />
          </div>

          {/* Footer */}
          <div className="p-4 border-t border-gray-200 dark:border-gray-700 flex justify-end gap-3">
            <button
              type="button"
              onClick={() => navigate("/operations")}
              className="px-4 py-2 rounded border border-gray-300 dark:border-gray-600 text-sm"
            >
              Cancelar
            </button>
            <button
              type="submit"
              form="equipo-form"
              className="px-4 py-2 rounded bg-blue-600 text-white text-sm"
            >
              Guardar
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
