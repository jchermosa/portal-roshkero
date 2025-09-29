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

type DiaLaboral = { idDiaLaboral: number; nombreDia: string };
type Ubicacion = { idUbicacion: number; nombre: string };

type AsignDU = {
  idDiaLaboral: number;
  nombreDia: string;
  idUbicacion: number;
  nombreUbicacion: string;
};

const METADATAS_PATH = "/api/v1/admin/operations/metadatas";
const TEAM_PATH = "/api/v1/admin/operations/team";
const USERS_PATH = "/api/v1/admin/operations/users";
const DIAS_PATH = "/api/v1/admin/operations/diaslaborales";
const LIBRES_PATH = "/api/v1/admin/operations/asignacion/libres";

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

/** Normaliza disponibilidad a 0..100 */
const toPercent = (v: any) => {
  const n = Number(v);
  if (!isFinite(n)) return 100;
  if (n <= 1) return Math.round(n * 100);
  return Math.max(0, Math.min(100, Math.round(n)));
};
/** Convierte UI (0..100 o 0..1) a fracción 0..1 */
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

  const [externalErrors, setExternalErrors] = useState<Record<string, string>>({});
  const [team, setTeam] = useState<ITeam | null>(null);

  const [formData, setFormData] = useState<{ nombre: string; fechaInicio: string; fechaFin: string; estado: boolean; }>(
    { nombre: "", fechaInicio: "", fechaFin: "", estado: true }
  );

  // Cliente / Lead / Tec
  const [clienteOptions, setClienteOptions] = useState<Array<{ value: number; label: string }>>([]);
  const [clienteSel, setClienteSel] = useState<{ value: number; label: string } | null>(null);

  const [leadOptions, setLeadOptions] = useState<Array<{ value: number; label: string }>>([]);
  const [leadSel, setLeadSel] = useState<{ value: number; label: string } | null>(null);
  const [leadId, setLeadId] = useState<number | null>(null);
  const [leadSaving, setLeadSaving] = useState(false);

  const [tecnologiaOptions, setTecnologiaOptions] = useState<Array<{ value: number; label: string }>>([]);
  const [tecnologiasSel, setTecnologiasSel] = useState<Array<{ value: number; label: string }>>([]);
  const [tecPick, setTecPick] = useState<number | "">("");

  // Miembros
  const [miembros, setMiembros] = useState<IMiembrosEquipo[]>([]);
  const [memberOptions, setMemberOptions] = useState<Array<{ value: number; label: string; idCargo: number }>>([]);
  const [selectedMember, setSelectedMember] = useState<{ value: number; label: string; idCargo: number } | null>(null);
  const [newMemberDisp, setNewMemberDisp] = useState<number>(100);
  const [newMemberStart, setNewMemberStart] = useState<string>("");
  const [newMemberEnd, setNewMemberEnd] = useState<string>("");

  // Días / Ubicaciones libres / Asignaciones
  const [diasOptions, setDiasOptions] = useState<Array<{ value: number; label: string }>>([]);
  const [libresByDay, setLibresByDay] = useState<Map<number, Ubicacion[]>>(new Map());
  const [diaSel, setDiaSel] = useState<{ value: number; label: string } | null>(null);
  const [ubicSel, setUbicSel] = useState<{ value: number; label: string } | null>(null);
  const [asignaciones, setAsignaciones] = useState<AsignDU[]>([]);

  const firstArray = (...cands: any[][]) => cands.find(a => Array.isArray(a) && a.length) ?? [];
  // Guarda el nombre original para saber si cambió
const originalName = team?.nombre ?? "";

// Valida nombre en caliente con debounce
useEffect(() => {
  if (!team) return;
  const nombre = (formData.nombre || "").trim();

  // si está vacío: muestra error local y no llama al backend
  if (!nombre) {
    setExternalErrors((p) => ({ ...p, nombre: "Ingresá un nombre" }));
    return;
  }

  // si no cambió respecto al original: limpia error y no llama
  if (nombre === originalName) {
    setExternalErrors((p) => {
      const n = { ...p }; delete n.nombre; return n;
    });
    return;
  }

  const ac = new AbortController();
  const t = setTimeout(async () => {
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
        signal: ac.signal,
      });

      if (!r.ok) {
        const txt = await r.text();
        const msg =
          /existe|duplic/i.test(txt) || r.status === 409
            ? "El nombre ya existe"
            : "No se pudo verificar el nombre";
        setExternalErrors((p) => ({ ...p, nombre: msg }));
      } else {
        // disponible
        setExternalErrors((p) => {
          const n = { ...p }; delete n.nombre; return n;
        });
      }
    } catch {
      if (!ac.signal.aborted) {
        setExternalErrors((p) => ({ ...p, nombre: "No se pudo verificar el nombre" }));
      }
    }
  }, 500); // debounce 500ms

  return () => {
    clearTimeout(t);
    ac.abort();
  };
}, [formData.nombre, team?.idEquipo, token, originalName]);


  // Carga inicial
  useEffect(() => {
    const ac = new AbortController();
    (async () => {
      try {
        setLoading(true);
        setError(null);

        // Metadatas
        const md = await fetch(METADATAS_PATH, {
          headers: { Accept: "application/json", ...(token ? { Authorization: `Bearer ${token}` } : {}) },
          credentials: "include",
          signal: ac.signal,
        });
        if (!md.ok) throw new Error(`Metadatas ${md.status} ${md.statusText}`);
        const meta = await md.json();

        setClienteOptions((meta.clientes ?? []).map((c: any) => ({ value: c.idCliente, label: c.nombre })));

        const rawLeads = firstArray(
          meta.teamleaders, meta.teamLeaders, meta.team_leaders,
          meta.lideres, meta.leaders, meta.usuariosLideres, meta.usuarios
        );
        setLeadOptions(
          rawLeads.map((u: any) => ({
            value: u.idUsuario ?? u.id ?? u.usuarioId,
            label: [u.nombre, u.apellido].filter(Boolean).join(" ") || u.nombreCompleto || "Sin nombre",
          }))
        );

        setTecnologiaOptions((meta.tecnologias ?? []).map((t: any) => ({ value: t.idTecnologia, label: t.nombre })));

        // Team
        const tr = await fetch(`${TEAM_PATH}/${id}`, {
          headers: { Accept: "application/json", ...(token ? { Authorization: `Bearer ${token}` } : {}) },
          credentials: "include",
          signal: ac.signal,
        });
        if (!tr.ok) throw new Error(`Team ${tr.status} ${tr.statusText}`);
        const t = await tr.json();

        // Usuarios no en equipo (selector agregar miembro)
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
          t.usuariosAsignacion, t.usuariosEnEquipo, t.usuarios,
          t.miembros, t.integrantes, t.miembrosEquipo
        );

        const miembrosInicial: IMiembrosEquipo[] = miembrosFuente.map((u: any) => ({
          id: u.idUsuario ?? u.id ?? u.usuarioId,
          nombre: u.nombreCompleto ?? [u.nombre, u.apellido].filter(Boolean).join(" ") ?? String(u.nombre ?? u.apellido ?? "Sin nombre"),
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
        setClienteSel(t.cliente?.idCliente ? { value: t.cliente.idCliente, label: t.cliente.nombre } : null);
        setLeadSel(lId ? { value: lId, label: [t.lider?.nombre, t.lider?.apellido].filter(Boolean).join(" ") } : null);

        // Precargar asignaciones con ubicación
        const duSrc = Array.isArray(t.equipoDiaUbicacion) ? t.equipoDiaUbicacion : [];
        const inicialDU: AsignDU[] = duSrc
          .filter((e: any) => e?.diaLaboral?.idDiaLaboral && e?.ubicacion?.idUbicacion)
          .map((e: any) => ({
            idDiaLaboral: e.diaLaboral.idDiaLaboral,
            nombreDia: e.diaLaboral.nombreDia ?? "—",
            idUbicacion: e.ubicacion.idUbicacion,
            nombreUbicacion: e.ubicacion.nombre ?? "—",
          }));
        setAsignaciones(inicialDU);

        // Días
        const dr = await fetch(DIAS_PATH, {
          headers: { Accept: "application/json", ...(token ? { Authorization: `Bearer ${token}` } : {}) },
          credentials: "include",
          signal: ac.signal,
        });
        if (dr.ok) {
          const dias: DiaLaboral[] = await dr.json();
          setDiasOptions(dias.map(d => ({ value: d.idDiaLaboral, label: d.nombreDia })));
        }

        // Libres por día
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
      } catch (e: any) {
        if (e?.name !== "AbortError") setError(e.message || "Error");
      } finally {
        setLoading(false);
      }
    })();
    return () => ac.abort();
  }, [id, token]);

  // Limpiar error externo por campo
  const onClearExternalError = (name: string) =>
    setExternalErrors((prev) => {
      const next = { ...prev };
      delete next[name];
      return next;
    });

  // Secciones formulario principal
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

  // Verificar nombre con PATCH parcial
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

  // Tecnologías
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

  // Miembros
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

  // Columnas miembros
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

  // Tema de react-select
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

  // PATCH inmediato para cambiar Team Lead
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
      setLeadId(leadSel ? leadSel.value : null);
    } catch (e: any) {
      alert(e.message || "No se pudo actualizar el Team Lead");
    } finally {
      setLeadSaving(false);
    }
  };

  // Submit (si querés persistir DU: descomenta en payload)
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
        // equipoDiaUbicacion: asignaciones.map(a => ({ idDiaLaboral: a.idDiaLaboral, idUbicacion: a.idUbicacion })),
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

  // ===== LÓGICA SELECTORES DÍA/UBICACIÓN =====

  // Días disponibles: excluir los ya agregados a la tabla
  const availableDaysOptions = useMemo(() => {
    const tomados = new Set(asignaciones.map(a => a.idDiaLaboral));
    return diasOptions.filter(d => !tomados.has(d.value));
  }, [diasOptions, asignaciones]);

  // Ubicaciones del día seleccionado, excluyendo las ya usadas ese día
  const ubicacionesOptions = useMemo(() => {
    if (!diaSel) return [];
    const yaUsadas = new Set(
      asignaciones.filter(a => a.idDiaLaboral === diaSel.value).map(a => a.idUbicacion)
    );
    const arr = (libresByDay.get(diaSel.value) ?? []).filter(u => !yaUsadas.has(u.idUbicacion));
    return arr.map(u => ({ value: u.idUbicacion, label: u.nombre }));
  }, [diaSel, libresByDay, asignaciones]);

  // Agregar asignación
  const addAsignDU = () => {
    if (!diaSel || !ubicSel) return;
    const exists = asignaciones.some(a => a.idDiaLaboral === diaSel.value && a.idUbicacion === ubicSel.value);
    if (exists) return;
    setAsignaciones(prev => [
      ...prev,
      {
        idDiaLaboral: diaSel.value,
        nombreDia: diaSel.label,
        idUbicacion: ubicSel.value,
        nombreUbicacion: ubicSel.label,
      },
    ]);
    setUbicSel(null);
  };

  // Columnas tabla Día–Ubicación
  const duColumns = [
    { key: "nombreDia", label: "Día" },
    { key: "nombreUbicacion", label: "Ubicación" },
    {
      key: "acciones",
      label: "Acciones",
      render: (row: AsignDU) => (
        <button
          onClick={() =>
            setAsignaciones(prev =>
              prev.filter(a => !(a.idDiaLaboral === row.idDiaLaboral && a.idUbicacion === row.idUbicacion))
            )
          }
          className="px-3 py-1 text-sm text-white bg-blue-600 rounded hover:bg-blue-700"
        >
          Eliminar
        </button>
      ),
    },
  ];

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

            {/* === Día + Ubicación (arriba de Cliente/Lead) === */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-3 items-end">
              <div className="md:col-span-1">
                <label className="block text-sm mb-2">Día laboral</label>
                <Select
                  options={availableDaysOptions}
                  value={diaSel}
                  onChange={(opt) => { setDiaSel(opt as any); setUbicSel(null); }}
                  isClearable
                  placeholder="Seleccionar día…"
                  theme={selectTheme}
                  styles={selectStyles}
                  menuPortalTarget={document.body}
                  menuPosition="fixed"
                />
              </div>
              <div className="md:col-span-1">
                <label className="block text-sm mb-2">Ubicación libre</label>
                <Select
                  options={ubicacionesOptions}
                  value={ubicSel}
                  onChange={(opt) => setUbicSel(opt as any)}
                  isClearable
                  isDisabled={!diaSel}
                  placeholder={diaSel ? "Seleccionar ubicación…" : "Elegí un día primero"}
                  theme={selectTheme}
                  styles={selectStyles}
                  menuPortalTarget={document.body}
                  menuPosition="fixed"
                />
              </div>
              <div className="flex md:justify-start">
                <button
                  type="button"
                  onClick={addAsignDU}
                  disabled={!diaSel || !ubicSel}
                  className="self-end h-10 px-4 rounded bg-blue-600 text-white text-sm disabled:opacity-60"
                >
                  Agregar
                </button>
              </div>
            </div>

            {/* Tabla Día–Ubicación */}
            <div>
              <h3 className="text-sm font-semibold mb-2">Días y ubicaciones asignadas</h3>
              <DataTable
                data={asignaciones}
                columns={duColumns}
                rowKey={(r: AsignDU) => `${r.idDiaLaboral}-${r.idUbicacion}`}
                scrollable={false}
                enableSearch={false}
              />
            </div>

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

            {/* Agregar miembro */}
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
