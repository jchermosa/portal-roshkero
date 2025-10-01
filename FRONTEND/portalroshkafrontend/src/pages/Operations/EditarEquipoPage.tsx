// src/pages/EditarEquipoPage.tsx
import { useEffect, useMemo, useState, useCallback } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Select from "react-select";
import DynamicForm from "../../components/DynamicForm";
import DataTable from "../../components/DataTable";
import { useAuth } from "../../context/AuthContext";

type IMiembrosEquipo = {
  id: number;
  nombre: string;
  idCargo: number;
  disponibilidad?: number; // int 1..100
  fechaEntrada?: string;
  fechaFin?: string;
};
type Tecnologia = { idTecnologia: number; nombre: string };

type ITeam = {
  idEquipo: number;
  nombre: string;
  clienteId?: number;
  clienteNombre?: string;
  fechaInicio: string;
  fechaFin: string;
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
  idUbicacion?: number | null;
  nombreUbicacion?: string | null;
};

type MemberOpt = {
  value: number;
  label: string;
  idCargo: number;
  dispRestante: number; // int 0..100
};

const METADATAS_PATH = "/api/v1/admin/operations/metadatas";
const TEAM_PATH = "/api/v1/admin/operations/team";
const USERS_PATH = "/api/v1/admin/operations/users";
const DIAS_PATH = "/api/v1/admin/operations/diaslaborales";
const LIBRES_PATH = "/api/v1/admin/operations/asignacion/libres";

function useIsDark() {
  const [isDark, setIsDark] = useState(
    typeof document !== "undefined" &&
      document.documentElement.classList.contains("dark")
  );
  useEffect(() => {
    const el = document.documentElement;
    const obs = new MutationObserver(() =>
      setIsDark(el.classList.contains("dark"))
    );
    obs.observe(el, { attributes: true, attributeFilter: ["class"] });
    return () => obs.disconnect();
  }, []);
  return isDark;
}
// --- Date helpers ---
const d = (s?: string | null) => (s ? new Date(s) : null);
const lt = (a?: string | null, b?: string | null) =>
  d(a) && d(b) ? d(a)!.getTime() < d(b)!.getTime() : false;
const lte = (a?: string | null, b?: string | null) =>
  d(a) && d(b) ? d(a)!.getTime() <= d(b)!.getTime() : false;
const gt = (a?: string | null, b?: string | null) =>
  d(a) && d(b) ? d(a)!.getTime() > d(b)!.getTime() : false;
const gte = (a?: string | null, b?: string | null) =>
  d(a) && d(b) ? d(a)!.getTime() >= d(b)!.getTime() : false;

const validateMemberDates = (
  teamStart: string,
  teamEnd: string | null | undefined,
  start?: string | null,
  end?: string | null
): string | null => {
  // start must be >= teamStart
  if (start && lt(start, teamStart)) return "La entrada debe ser posterior al inicio del equipo.";
  // if teamEnd exists, start must be <= teamEnd
  if (teamEnd && start && gt(start, teamEnd)) return "La entrada debe ser anterior o igual a la fecha límite del equipo.";
  // end must be > start (if both present)
  if (start && end && !gt(end, start)) return "La fecha de fin debe ser posterior a la de entrada.";
  // if teamEnd exists, end must be <= teamEnd
  if (teamEnd && end && gt(end, teamEnd)) return "La fecha de fin debe ser anterior o igual a la fecha límite del equipo.";
  return null;
};

export default function EditarEquipoPage() {
  const { token } = useAuth();
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const isDark = useIsDark();

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [externalErrors, setExternalErrors] = useState<Record<string, string>>(
    {}
  );
  const [team, setTeam] = useState<ITeam | null>(null);

  const [formData, setFormData] = useState<{
    nombre: string;
    fechaInicio: string;
    fechaFin: string;
    estado: boolean;
  }>({ nombre: "", fechaInicio: "", fechaFin: "", estado: true });

  // Cliente / Lead / Tec
  const [clienteOptions, setClienteOptions] = useState<
    Array<{ value: number; label: string }>
  >([]);
  const [clienteSel, setClienteSel] = useState<{
    value: number;
    label: string;
  } | null>(null);

  const [leadOptions, setLeadOptions] = useState<
    Array<{ value: number; label: string }>
  >([]);
  const [leadSel, setLeadSel] = useState<{
    value: number;
    label: string;
  } | null>(null);
  const [leadId, setLeadId] = useState<number | null>(null);
  const [leadSaving, setLeadSaving] = useState(false);

  const [tecnologiaOptions, setTecnologiaOptions] = useState<
    Array<{ value: number; label: string }>
  >([]);
  const [tecnologiasSel, setTecnologiasSel] = useState<
    Array<{ value: number; label: string }>
  >([]);
  const [tecPick, setTecPick] = useState<number | "">("");

  // Miembros
  const [miembros, setMiembros] = useState<IMiembrosEquipo[]>([]);
  const [memberOptions, setMemberOptions] = useState<MemberOpt[]>([]);
  const [selectedMember, setSelectedMember] = useState<MemberOpt | null>(null);
  const [newMemberMax, setNewMemberMax] = useState<number>(100);
  const [newMemberDisp, setNewMemberDisp] = useState<number>(100);
  const [newMemberStart, setNewMemberStart] = useState<string>("");
  const [newMemberEnd, setNewMemberEnd] = useState<string>("");
  // idUsuario -> disponibilidad restante global (0..100)
const [dispCaps, setDispCaps] = useState<Map<number, number>>(new Map());

  // Días / Ubicaciones / Asignaciones
  const [diasOptions, setDiasOptions] = useState<
    Array<{ value: number; label: string }>
  >([]);
  const [libresByDay, setLibresByDay] = useState<Map<number, Ubicacion[]>>(
    new Map()
  );
  const [asignaciones, setAsignaciones] = useState<AsignDU[]>([]);

  const firstArray = (...cands: any[][]) =>
    cands.find((a) => Array.isArray(a) && a.length) ?? [];

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
      menu: (p: any) => ({
        ...p,
        zIndex: 9999,
        backgroundColor: isDark ? "#111827" : "#ffffff",
      }),
      input: (p: any) => ({ ...p, color: isDark ? "#ffffff" : "#111827" }),
      singleValue: (p: any) => ({ ...p, color: isDark ? "#ffffff" : "#111827" }),
      placeholder: (p: any) => ({
        ...p,
        color: isDark ? "#d1d5db" : "#6b7280",
      }),
      multiValueLabel: (p: any) => ({
        ...p,
        color: isDark ? "#0f172a" : "#1f2937",
      }),
      option: (p: any, s: any) => ({
        ...p,
        backgroundColor: s.isSelected
          ? isDark
            ? "#1f2937"
            : "#dbeafe"
          : s.isFocused
          ? isDark
            ? "#374151"
            : "#e5e7eb"
          : "transparent",
        color: isDark ? "#ffffff" : "#111827",
      }),
    }),
    [isDark]
  );

  // Nombre: verificación en caliente con debounce
  const originalName = team?.nombre ?? "";
  useEffect(() => {
    if (!team) return;
    const nombre = (formData.nombre || "").trim();

    if (!nombre) {
      setExternalErrors((p) => ({ ...p, nombre: "Ingresá un nombre" }));
      return;
    }
    if (nombre === originalName) {
      setExternalErrors((p) => {
        const n = { ...p };
        delete n.nombre;
        return n;
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
          setExternalErrors((p) => {
            const n = { ...p };
            delete n.nombre;
            return n;
          });
        }
      } catch {
        if (!ac.signal.aborted)
          setExternalErrors((p) => ({
            ...p,
            nombre: "No se pudo verificar el nombre",
          }));
      }
    }, 500);

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

        const md = await fetch(METADATAS_PATH, {
          headers: {
            Accept: "application/json",
            ...(token ? { Authorization: `Bearer ${token}` } : {}),
          },
          credentials: "include",
          signal: ac.signal,
        });
        if (!md.ok) throw new Error(`Metadatas ${md.status} ${md.statusText}`);
        const meta = await md.json();

        setClienteOptions(
          (meta.clientes ?? []).map((c: any) => ({
            value: c.idCliente,
            label: c.nombre,
          }))
        );

        const rawLeads = firstArray(
          meta.teamleaders,
          meta.teamLeaders,
          meta.team_leaders,
          meta.lideres,
          meta.leaders,
          meta.usuariosLideres,
          meta.usuarios
        );
        setLeadOptions(
          rawLeads.map((u: any) => ({
            value: u.idUsuario ?? u.id ?? u.usuarioId,
            label:
              [u.nombre, u.apellido].filter(Boolean).join(" ") ||
              u.nombreCompleto ||
              "Sin nombre",
          }))
        );
        setTecnologiaOptions(
          (meta.tecnologias ?? []).map((t: any) => ({
            value: t.idTecnologia,
            label: t.nombre,
          }))
        );

        const tr = await fetch(`${TEAM_PATH}/${id}`, {
          headers: {
            Accept: "application/json",
            ...(token ? { Authorization: `Bearer ${token}` } : {}),
          },
          credentials: "include",
          signal: ac.signal,
        });
        if (!tr.ok) throw new Error(`Team ${tr.status} ${tr.statusText}`);
        const t = await tr.json();

        const notIn = Array.isArray(t.usuariosNoEnEquipo)
          ? t.usuariosNoEnEquipo
          : [];
        if (notIn.length) {
  setMemberOptions(
    notIn.map((u: any) => ({
      value: u.idUsuario ?? u.id,
      label: [u.nombre, u.apellido].filter(Boolean).join(" ") || u.nombreCompleto || "Sin nombre",
      idCargo: u.idCargo ?? 0,
      dispRestante: Math.max(0, Math.min(100, u.disponibilidadRestante ?? u.disponibilidad ?? u.dispRestante ?? 100)),
    }))
  );
  // además: traer caps para todos los usuarios para poder limitar filas existentes
  try {
    const urAll = await fetch(USERS_PATH, {
      headers: { Accept: "application/json", ...(token ? { Authorization: `Bearer ${token}` } : {}) },
      credentials: "include",
      signal: ac.signal,
    });
    if (urAll.ok) {
      const usersData = await urAll.json();
      const usersArr = Array.isArray(usersData?.content) ? usersData.content : Array.isArray(usersData) ? usersData : [];
      const m = new Map<number, number>();
      usersArr.forEach((u: any) => {
        const id = u.id ?? u.idUsuario ?? u.usuarioId;
        const rest = Math.max(0, Math.min(100, u.disponibilidadRestante ?? u.disponibilidad ?? u.dispRestante ?? 100));
        if (id != null) m.set(Number(id), rest);
      });
      setDispCaps(m);
    }
  } catch {}
} else {
          const ur = await fetch(USERS_PATH, {
            headers: {
              Accept: "application/json",
              ...(token ? { Authorization: `Bearer ${token}` } : {}),
            },
            credentials: "include",
            signal: ac.signal,
          });
          if (ur.ok) {
  const usersData = await ur.json();
  const usersArr = Array.isArray(usersData?.content) ? usersData.content : Array.isArray(usersData) ? usersData : [];
  setMemberOptions(
    usersArr.map((u: any) => ({
      value: u.id ?? u.idUsuario ?? u.usuarioId,
      label: [u.nombre, u.apellido].filter(Boolean).join(" ") || u.nombreCompleto || "Sin nombre",
      idCargo: u.idCargo ?? u.cargo?.idCargo ?? 0,
      dispRestante: Math.max(0, Math.min(100, u.disponibilidadRestante ?? u.disponibilidad ?? u.dispRestante ?? 100)),
    }))
  );
  // llenar caps
  const m = new Map<number, number>();
  usersArr.forEach((u: any) => {
    const id = u.id ?? u.idUsuario ?? u.usuarioId;
    const rest = Math.max(0, Math.min(100, u.disponibilidadRestante ?? u.disponibilidad ?? u.dispRestante ?? 100));
    if (id != null) m.set(Number(id), rest);
  });
  setDispCaps(m);
}
        }

        const estadoBool =
          typeof t.estado === "boolean" ? t.estado : t.estado === "A";
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
  disponibilidad: Math.max(
    1,
    Math.min(100, u.disponibilidad ?? u.porcentajeTrabajo ?? 100)
  ),
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
          leadNombre:
            [t.lider?.nombre, t.lider?.apellido].filter(Boolean).join(" ") ||
            null,
        });

        setFormData({
          nombre: t.nombre ?? "",
          fechaInicio: t.fechaInicio ?? "",
          fechaFin,
          estado: estadoBool,
        });
        setMiembros(miembrosInicial);
        setTecnologiasSel(
          (t.tecnologias ?? []).map((x: any) => ({
            value: x.idTecnologia,
            label: x.nombre,
          }))
        );

        const lId = t.lider?.idUsuario ?? null;
        setLeadId(lId);
        setClienteSel(
          t.cliente?.idCliente
            ? { value: t.cliente.idCliente, label: t.cliente.nombre }
            : null
        );
        setLeadSel(
          lId
            ? {
                value: lId,
                label: [t.lider?.nombre, t.lider?.apellido]
                  .filter(Boolean)
                  .join(" "),
              }
            : null
        );

        // DU inicial: soporta plano y anidado
        const duSrc = Array.isArray(t.equipoDiaUbicacion)
          ? t.equipoDiaUbicacion
          : [];
        const inicialDU: AsignDU[] = duSrc
          .map((e: any) => {
            const idDia = e.idDiaLaboral ?? e?.diaLaboral?.idDiaLaboral;
            const nomDia = e.nombreDia ?? e?.diaLaboral?.nombreDia ?? "—";
            const idUbi = e.idUbicacion ?? e?.ubicacion?.idUbicacion ?? null;
            const nomUbi = e.nombreUbicacion ?? e?.ubicacion?.nombre ?? null;
            return idDia != null
              ? {
                  idDiaLaboral: idDia,
                  nombreDia: nomDia,
                  idUbicacion: idUbi,
                  nombreUbicacion: nomUbi,
                }
              : null;
          })
          .filter(Boolean) as AsignDU[];
        setAsignaciones(inicialDU);

        // Días
        const dr = await fetch(DIAS_PATH, {
          headers: {
            Accept: "application/json",
            ...(token ? { Authorization: `Bearer ${token}` } : {}),
          },
          credentials: "include",
          signal: ac.signal,
        });
        if (dr.ok) {
          const dias: DiaLaboral[] = await dr.json();
          setDiasOptions(
            dias.map((d) => ({ value: d.idDiaLaboral, label: d.nombreDia }))
          );
        }

        // Libres por día
        const lr = await fetch(LIBRES_PATH, {
          headers: {
            Accept: "application/json",
            ...(token ? { Authorization: `Bearer ${token}` } : {}),
          },
          credentials: "include",
          signal: ac.signal,
        });
        if (lr.ok) {
          const libres = await lr.json();
          const map = new Map<number, Ubicacion[]>();
          libres.forEach((entry: any) => {
            const idDia = entry.idDiaLaboral;
            const arr: Ubicacion[] = Array.isArray(entry.ubicacionesLibres)
              ? entry.ubicacionesLibres
              : [];
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

  const onClearExternalError = (name: string) =>
    setExternalErrors((prev) => {
      const next = { ...prev };
      delete next[name];
      return next;
    });

  const sections = useMemo(
    () => [
      {
        title: "Editar Equipo",
        icon: "🛠️",
        fields: [
          {
            name: "nombre",
            label: "Nombre del equipo",
            type: "text" as const,
            required: true,
          },
          {
            name: "fechaInicio",
            label: "Fecha de inicio",
            type: "date" as const,
            required: true,
          },
          {
            name: "fechaFin",
            label: "Fecha límite",
            type: "date" as const,
            required: false,
          },
          {
            name: "estado",
            label: "Activo",
            type: "checkbox" as const,
            required: true,
          },
        ],
      },
    ],
    []
  );

  // Handler seguro para DynamicForm
  const handleFormChangeDeferred = useCallback((updated: Record<string, any>) => {
    Promise.resolve().then(() =>
      setFormData((prev) => ({ ...prev, ...updated }))
    );
  }, []);

  // ====== TECNOLOGÍAS / MIEMBROS ======
  const handleAddTec = () => {
    if (tecPick === "") return;
    const opt = tecnologiaOptions.find((o) => o.value === Number(tecPick));
    if (!opt) return;
    if (tecnologiasSel.some((t) => t.value === opt.value)) return;
    setTecnologiasSel((prev) => [...prev, opt]);
    setTecPick("");
  };
  const handleRemoveTec = (idTec: number) =>
    setTecnologiasSel((prev) => prev.filter((t) => t.value !== idTec));

  const handleAddMember = () => {
    if (!selectedMember) return;
    if (miembros.some((m) => m.id === selectedMember.value)) return;

    const d = Math.max(1, Math.min(newMemberMax, newMemberDisp));
    const err = validateMemberDates(
  formData.fechaInicio,
  formData.fechaFin || null,
  newMemberStart || null,
  newMemberEnd || null
);
if (err) {
  alert(err);
  return;
}
    setMiembros((ms) => [
      ...ms,
      {
        id: selectedMember.value,
        nombre: selectedMember.label,
        idCargo: selectedMember.idCargo,
        disponibilidad: d,
        fechaEntrada: newMemberStart || "",
        fechaFin: newMemberEnd || "",
      },
    ]);
    setMemberOptions((opts) =>
      opts.filter((o) => o.value !== selectedMember.value)
    );
    setSelectedMember(null);
    setNewMemberStart("");
    setNewMemberEnd("");
    setNewMemberMax(100);
    setNewMemberDisp(100);
  };

  const updateMemberDisp = (id: number, val: number) =>
    
    setMiembros((ms) =>
      ms.map((m) =>
        m.id === id
          ? { ...m, disponibilidad: Math.max(1, Math.min(100, val)) }
          : m
      )
    );
  const updateMemberStart = (id: number, v: string) =>
    setMiembros((ms) => ms.map((m) => (m.id === id ? { ...m, fechaEntrada: v } : m)));
  const updateMemberEnd = (id: number, v: string) =>
    setMiembros((ms) => ms.map((m) => (m.id === id ? { ...m, fechaFin: v } : m)));

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
    {
  key: "disponibilidad",
  label: "Disp. (%)",
  render: (s: IMiembrosEquipo) => {
    const restante = dispCaps.get(s.id) ?? 100;            // lo que le queda libre fuera de este equipo
    const actual = Number(s.disponibilidad ?? 0);          // lo que ya tiene en este equipo
    const cap = Math.max(1, Math.min(100, restante + actual)); // máximo permitido para esta celda

    return (
      <input
        type="number"
        min={1}
        max={cap}
        value={Number(s.disponibilidad ?? 100)}
        onChange={(e) => {
          const v = Number(e.target.value) || 1;
          updateMemberDisp(s.id, Math.max(1, Math.min(cap, v)));
        }}
        className="w-20 px-2 py-1 rounded border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-800 text-sm"
        title={`Máximo permitido: ${cap}%`}
      />
    );
  },
},

    
    {
      key: "fechaEntrada",
      label: "Entrada",
      render: (s: IMiembrosEquipo) => (
        <input
  type="date"
  value={s.fechaEntrada ?? ""}
  onChange={(e) => {
    const v = e.target.value;
    const err = validateMemberDates(formData.fechaInicio, formData.fechaFin || null, v, s.fechaFin || null);
    if (err) return alert(err);
    updateMemberStart(s.id, v);
  }}
  min={formData.fechaInicio || undefined}
  max={(s.fechaFin || formData.fechaFin) || undefined}
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
  onChange={(e) => {
    const v = e.target.value;
    const err = validateMemberDates(formData.fechaInicio, formData.fechaFin || null, s.fechaEntrada || null, v);
    if (err) return alert(err);
    updateMemberEnd(s.id, v);
  }}
  min={(s.fechaEntrada || formData.fechaInicio) || undefined}
  max={formData.fechaFin || undefined}
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
              {
                value: s.id,
                label: s.nombre,
                idCargo: s.idCargo,
                dispRestante: 100,
              },
            ]);
          }}
          className="px-3 py-1 text-sm text-white bg-blue-600 rounded hover:bg-blue-700 focus:outline-none"
        >
          Eliminar
        </button>
      ),
    },
  ];

  // Días
  const dayName = useMemo(() => {
    const m = new Map<number, string>();
    diasOptions.forEach((d) => m.set(d.value, d.label));
    return m;
  }, [diasOptions]);

  const setUbicacionForDay = (
    dayId: number,
    opt: { value: number; label: string } | null
  ) => {
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

  const optionsForDay = (
    dayId: number,
    currentId?: number | null,
    currentName?: string | null
  ) => {
    let opts = (libresByDay.get(dayId) ?? []).map((u) => ({
      value: u.idUbicacion,
      label: u.nombre,
    }));
    if (currentId != null && !opts.some((o) => o.value === currentId)) {
      opts = [{ value: currentId, label: currentName ?? "—" }, ...opts];
    }
    return opts;
  };

  const duColumns = [
    { key: "nombreDia", label: "Día" },
    {
      key: "nombreUbicacion",
      label: "Ubicación",
      render: (r: AsignDU) => (
        <Select
          options={optionsForDay(
            r.idDiaLaboral,
            r.idUbicacion ?? null,
            r.nombreUbicacion ?? null
          )}
          value={
            r.idUbicacion != null
              ? { value: r.idUbicacion, label: r.nombreUbicacion ?? "—" }
              : null
          }
          onChange={(opt) => setUbicacionForDay(r.idDiaLaboral, opt as any)}
          isClearable
          placeholder="Elegir ubicación…"
          theme={(base) => ({
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
          })}
          styles={selectStyles}
          menuPortalTarget={document.body}
          menuPosition="fixed"
        />
      ),
    },
    {
      key: "acciones",
      label: "Acciones",
      render: (r: AsignDU) => (
        <button
          onClick={() => setUbicacionForDay(r.idDiaLaboral, null)}
          className="px-3 py-1 text-sm text-white bg-blue-600 rounded hover:bg-blue-700"
        >
          Eliminar
        </button>
      ),
    },
  ];

// Submit
  const onSubmit = async (_data: Record<string, any>) => {if (formData.fechaFin && !lt(formData.fechaInicio, formData.fechaFin)) {
  setError("La fecha de inicio del equipo debe ser anterior a la fecha límite.");
  setSaving(false);
  return;
}

// Validar fechas de todos los miembros
for (const m of miembros) {
  const err = validateMemberDates(
    formData.fechaInicio,
    formData.fechaFin || null,
    m.fechaEntrada || null,
    m.fechaFin || null
  );
  if (err) {
    setError(`Miembro ${m.nombre}: ${err}`);
    setSaving(false);
    return;
  }
}
    try {
      if (!team) return;
      setSaving(true);
      setError(null);
      setExternalErrors({});

      // DU plano
      const duPayload = asignaciones.map((a) => ({
        idDiaLaboral: a.idDiaLaboral,
        idUbicacion: a.idUbicacion ?? null,
      }));

      const payload: any = {
        nombre: formData.nombre,
        idCliente: clienteSel ? Number(clienteSel.value) : null,
        fechaInicio: formData.fechaInicio,
        fechaLimite: formData.fechaFin,
        estado: formData.estado ? "A" : "I",
        idLider: leadSel ? Number(leadSel.value) : null,
        idTecnologias: tecnologiasSel.map((t) => Number(t.value)),
        usuarios: miembros.map((m) => ({
  idUsuario: m.id,
  idCargo: m.idCargo,
  porcentajeTrabajo: Math.max(1, Math.min(100, Number(m.disponibilidad) || 1)),
  fechaEntrada: m.fechaEntrada || null,
  fechaFin: m.fechaFin || null,
  estado: "A",
})),
        equipoDiaUbicacion: duPayload,
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
        throw new Error(`${r.status} ${r.statusText} — ${txt.slice(0, 160)}`);
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

      <div className="relative z-10 h-full p-4">
        <div className="mx-auto w-full max-w-5xl bg-white/45 dark:bg-gray-900/80 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col max-h-[calc(100vh-2rem)] text-gray-900 dark:text-gray-100">
          <div className="p-6 border-b border-gray-200 dark:border-gray-700">
            <h2 className="text-xl font-bold">Editar Equipo: {team.nombre}</h2>
            <p className="text-sm text-gray-700 dark:text-gray-300">
              Cliente actual: {team.clienteNombre ?? "—"}
            </p>
          </div>

          <div className="flex-1 overflow-auto p-6 space-y-6">
            <DynamicForm
              id="editar-equipo-form"
              sections={sections}
              initialData={formData}
              onChange={handleFormChangeDeferred}
              onSubmit={onSubmit}
              externalErrors={externalErrors}
              onClearExternalError={onClearExternalError}
            />

            <div>
              <h3 className="text-sm font-semibold mb-2">
                Días y ubicaciones asignadas
              </h3>
              <DataTable
                data={rowsDU}
                columns={duColumns}
                rowKey={(r: AsignDU) => String(r.idDiaLaboral)}
                scrollable={false}
                enableSearch={false}
              />
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 items-end">
              <div>
                <label className="block text-sm mb-2">Cliente</label>
                <Select
                  options={clienteOptions}
                  value={clienteSel}
                  onChange={(opt) => setClienteSel(opt as any)}
                  placeholder="Seleccionar cliente…"
                  theme={(base) => ({
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
                  })}
                  styles={selectStyles}
                  menuPortalTarget={document.body}
                  menuPosition="fixed"
                />
              </div>

              <div className="grid grid-cols-3 gap-2 items-end">
                <div className="col-span-2">
                  <label className="block text-sm mb-2">
                    Team Lead (metadatas)
                  </label>
                  <Select
                    options={leadOptions}
                    value={leadSel}
                    onChange={(opt) => setLeadSel(opt as any)}
                    isClearable
                    placeholder="Seleccionar team lead…"
                    theme={(base) => ({
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
                    })}
                    styles={selectStyles}
                    menuPortalTarget={document.body}
                    menuPosition="fixed"
                  />
                </div>
                <button
                  type="button"
                  onClick={async () => {
                    if (!team) return;
                    try {
                      setLeadSaving(true);
                      const payload: any = {
                        idLider: leadSel ? Number(leadSel.value) : null,
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
                      if (!r.ok) throw new Error(await r.text());
                      setLeadId(leadSel ? leadSel.value : null);
                    } catch (e: any) {
                      alert(e.message || "No se pudo actualizar el Team Lead");
                    } finally {
                      setLeadSaving(false);
                    }
                  }}
                  disabled={
                    leadSaving ||
                    (leadSel?.value ?? null) === (team.leadId ?? null)
                  }
                  className="h-10 px-3 rounded bg-green-600 text-white text-sm disabled:opacity-60"
                  title="Aplicar cambio de Team Lead ahora"
                >
                  {leadSaving ? "Guardando…" : "Cambiar"}
                </button>
              </div>
            </div>

            <div>
              <label className="block text-sm mb-2">Tecnologías</label>
              <div className="flex gap-2">
                <select
                  className="w-full h-10 rounded border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100 px-3"
                  value={tecPick}
                  onChange={(e) =>
                    setTecPick(
                      e.target.value === "" ? "" : Number(e.target.value)
                    )
                  }
                >
                  <option value="">Seleccionar tecnología…</option>
                  {tecnologiaOptions.map((t) => (
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

            <div className="grid grid-cols-1 md:grid-cols-5 gap-3 items-end">
              <div className="md:col-span-2">
                <label className="block text-sm mb-2">Agregar miembro</label>
                <Select
                  options={memberOptions}
                  value={selectedMember}
                  onChange={(opt) => {
                    const m = opt as MemberOpt | null;
                    setSelectedMember(m);
                    const max = Math.max(
                      0,
                      Math.min(100, m?.dispRestante ?? 100)
                    );
                    setNewMemberMax(max);
                    setNewMemberDisp(max);
                  }}
                  isClearable
                  isSearchable
                  placeholder="Buscar miembro…"
                  theme={(base) => ({
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
                  })}
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
  min={formData.fechaInicio || undefined}
  max={formData.fechaFin || undefined}
  className="w-full px-3 py-2 rounded border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-800 text-sm"
/>
              </div>
              <div>
                <label className="block text-sm mb-2">Fin</label>
                <input
  type="date"
  value={newMemberEnd}
  onChange={(e) => setNewMemberEnd(e.target.value)}
  min={(newMemberStart || formData.fechaInicio) || undefined}
  max={formData.fechaFin || undefined}
  className="w-full px-3 py-2 rounded border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-800 text-sm"
/>
              </div>
              <div className="flex items-end gap-2">
                <input
                  type="number"
                  min={1}
                  max={newMemberMax}
                  value={newMemberDisp}
                  onChange={(e) =>
                    setNewMemberDisp(
                      Math.max(
                        1,
                        Math.min(newMemberMax, Number(e.target.value) || 1)
                      )
                    )
                  }
                  className="w-full px-3 py-2 rounded border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-800 text-sm"
                  placeholder="Disp. %"
                  title={
                    selectedMember
                      ? `Disponible máx: ${newMemberMax}%`
                      : "Elegí un miembro primero"
                  }
                />
                <button
                  type="button"
                  onClick={handleAddMember}
                  disabled={!selectedMember || newMemberMax <= 0}
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

          <div className="p-4 md:p-6 border-t border-gray-200 dark:border-gray-700 flex-shrink-0 flex items-center justify-between">
            <p className="text-sm text-gray-700 dark:text-gray-300">
              Revisá y guardá los cambios.
            </p>
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
