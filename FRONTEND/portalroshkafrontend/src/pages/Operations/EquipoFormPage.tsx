// src/pages/EquipoFormPage.tsx
import { useState, useMemo, useEffect } from "react";
import Select from "react-select";
import { useNavigate } from "react-router-dom";
import DynamicForm from "../../components/DynamicForm";
import { useAuth } from "../../context/AuthContext";
import DataTable from "../../components/DataTable";

type DiaLaboral = { idDiaLaboral: number; nombreDia: string };
type Ubicacion = { idUbicacion: number; nombre: string };

type Miembro = {
  id: number;
  nombre: string;
  idCargo: number;
  disponibilidad?: number; // int 1..100
  fechaEntrada?: string;
  fechaFin?: string;
};
type IMiembrosEquipo = {
  id: number;
  nombre: string;
  idCargo: number;
  disponibilidad?: number; // int 1..100
  fechaEntrada?: string;
  fechaFin?: string;
  estado: boolean | "A" | "I";
};
type MemberOpt = {
  value: number;
  label: string;
  idCargo: number;
  dispRestante: number; // int 0..100
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
const METADATAS_PATH = "http://localhost:8080/api/v1/admin/operations/metadatas";
const CREATE_TEAM_PATH = "http://localhost:8080/api/v1/admin/operations/team";
const DIAS_PATH = "http://localhost:8080/api/v1/admin/operations/diaslaborales";
const LIBRES_PATH = "http://localhost:8080/api/v1/admin/operations/asignacion/libres";
const USERS_PATH = "http://localhost:8080/api/v1/admin/operations/users";

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

function validateMemberDates(
  teamStart: string,
  teamEnd: string | null,
  entrada: string | null,
  fin: string | null
): string | null {
  const ts = teamStart ? Date.parse(teamStart) : NaN;
  const te = teamEnd ? Date.parse(teamEnd) : NaN;
  const es = entrada ? Date.parse(entrada) : NaN;
  const fe = fin ? Date.parse(fin) : NaN;

  if (entrada && ts && es < ts) return "La entrada no puede ser antes del inicio del equipo";
  if (teamEnd && entrada && es > te) return "La entrada no puede ser después del fin del equipo";
  if (fin && ts && fe < ts) return "La salida no puede ser antes del inicio del equipo";
  if (fin && teamEnd && fe > te) return "La salida no puede ser después del fin del equipo";
  if (entrada && fin && es > fe) return "La salida debe ser después de la entrada";
  return null;
}


export default function EquipoFormPage() {
  const { token } = useAuth();
  const navigate = useNavigate();
  const isDark = useIsDark();
  const [error, setError] = useState<string | null>(null);
  const [leadId, setLeadId] = useState<number | null>(null);
  const [dispCaps, setDispCaps] = useState<Map<number, number>>(new Map());
  const updateMemberStart = (id: number, v: string) =>
    setMiembros((ms) => ms.map((m) => (m.id === id ? { ...m, fechaEntrada: v } : m)));
  // ========== THEME + STYLES ==========
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
        borderColor: s.isFocused
          ? "#3b82f6"
          : isDark
            ? "#374151"
            : "#d1d5db",
        minHeight: 40,
        boxShadow: s.isFocused ? "0 0 0 2px rgba(59,130,246,.2)" : "none",
        ":hover": { borderColor: "#3b82f6" },
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

  // Form base
  const [formData, setFormData] = useState({
    nombre: "",
    fechaInicio: "",
    fechaFin: "",
    estado: true,
  });

  // Metadatas
  const [clienteOptions, setClienteOptions] = useState<
    Array<{ value: number; label: string }>
  >([]);
  const [leadOptions, setLeadOptions] = useState<
    Array<{ value: number; label: string }>
  >([]);
  const [tecOptions, setTecOptions] = useState<
    Array<{ value: number; label: string }>
  >([]);

  // Selecciones
  const [clienteSel, setClienteSel] = useState<
    { value: number; label: string } | null
  >(null);
  const [leadSel, setLeadSel] = useState<
    { value: number; label: string } | null
  >(null);
  const [tecSel, setTecSel] = useState<Array<{ value: number; label: string }>>(
    []
  );
  const [tecPick, setTecPick] = useState<number | "">("");

  // Días & Ubicaciones
  const [diasOptions, setDiasOptions] = useState<
    Array<{ value: number; label: string }>
  >([]);
  const [libresByDay, setLibresByDay] = useState<Map<number, Ubicacion[]>>(
    new Map()
  );
  const [asignaciones, setAsignaciones] = useState<
    Map<number, { idUbicacion: number | null; nombreUbicacion: string | null }>
  >(new Map());

  // Miembros
  const [memberOptions, setMemberOptions] = useState<
    Array<{
      value: number;
      label: string;
      idCargo: number;
      dispRestante: number; // 0..100
    }>
  >([]);
  const [selectedMember, setSelectedMember] = useState<{
    value: number;
    label: string;
    idCargo: number;
    dispRestante: number;
  } | null>(null);
  const [miembros, setMiembros] = useState<IMiembrosEquipo[]>([]);

  const updateMemberEnd = (id: number, v: string) =>
    setMiembros((ms) =>
      ms.map((m) =>
        m.id === id
          ? { ...m, fechaFin: v, estado: m.estado ?? "A" } // гарантируем поле estado
          : m
      )
    );
  const [newMemberStart, setNewMemberStart] = useState("");
  const [newMemberEnd, setNewMemberEnd] = useState("");
  const [newMemberMax, setNewMemberMax] = useState(100);
  const [newMemberDisp, setNewMemberDisp] = useState(100);
  const updateMemberDisp = (id: number, val: number) =>

    setMiembros((ms) =>
      ms.map((m) =>
        m.id === id
          ? { ...m, disponibilidad: Math.max(1, Math.min(100, val)) }
          : m
      )
    );
  // Cargar metadatas + días + libres + usuarios
  useEffect(() => {
    const ac = new AbortController();
    (async () => {
      try {
        // Metadatas
        const metaRes = await fetch(METADATAS_PATH, {
          headers: {
            Accept: "application/json",
            ...(token ? { Authorization: `Bearer ${token}` } : {}),
          },
          credentials: "include",
          signal: ac.signal,
        });
        if (metaRes.ok) {
          const meta = await metaRes.json();
          setClienteOptions(
            (meta.clientes ?? []).map((c: any) => ({
              value: c.idCliente,
              label: c.nombre,
            }))
          );
          const rawLeads =
            meta.teamleaders ??
            meta.teamLeaders ??
            meta.lideres ??
            meta.usuarios ??
            [];
          setLeadOptions(
            rawLeads.map((u: any) => ({
              value: u.idUsuario ?? u.id,
              label:
                [u.nombre, u.apellido].filter(Boolean).join(" ") ||
                u.nombreCompleto ||
                "Sin nombre",
            }))
          );
          setTecOptions(
            (meta.tecnologias ?? []).map((t: any) => ({
              value: t.idTecnologia,
              label: t.nombre,
            }))
          );
        }

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
          const opts = dias.map((d) => ({
            value: d.idDiaLaboral,
            label: d.nombreDia,
          }));
          setDiasOptions(opts);
          const m = new Map<
            number,
            { idUbicacion: number | null; nombreUbicacion: string | null }
          >();
          opts.forEach((d) =>
            m.set(d.value, { idUbicacion: null, nombreUbicacion: null })
          );
          setAsignaciones(m);
        }

        // Libres
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

        // Usuarios
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
          const usersArr = Array.isArray(usersData?.content)
            ? usersData.content
            : Array.isArray(usersData)
              ? usersData
              : [];
          setMemberOptions(
            usersArr.map((u: any) => ({
              value: u.id ?? u.idUsuario,
              label:
                [u.nombre, u.apellido].filter(Boolean).join(" ") ||
                u.nombreCompleto ||
                "Sin nombre",
              idCargo: u.idCargo ?? 0,
              dispRestante: Math.max(
                0,
                Math.min(
                  100,
                  Number(
                    u.disponibilidadRestante ??
                    u.disponibilidad ??
                    u.dispRestante ??
                    100
                  )
                )
              ),
            }))
          );
        }
      } catch (err: any) {
        if (err.name !== "AbortError") console.error(err);
      }
    })();
    return () => ac.abort();
  }, [token]);

  // ====== TECNOLOGÍAS ======
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

  // ====== DÍAS / UBICACIONES ======
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
      key: "estado",
      label: "Activo",
      render: (s: IMiembrosEquipo) => (
        <input
          type="checkbox"
          checked={s.estado === "A"} // строго проверяем на "A"
          onChange={(e) =>
            setMiembros((prev) =>
              prev.map((m) =>
                m.id === s.id ? { ...m, estado: e.target.checked ? "A" : "I" } : m
              )
            )
          }
          className="w-4 h-4"
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
  const setUbicacionForDay = (
    dayId: number,
    opt: { value: number; label: string } | null
  ) => {
    setAsignaciones((prev) => {
      const next = new Map(prev);
      next.set(dayId, {
        idUbicacion: opt ? opt.value : null,
        nombreUbicacion: opt ? opt.label : null,
      });
      return next;
    });
  };

  const rowsDU = useMemo(() => {
    return diasOptions.map((d) => {
      const curr =
        asignaciones.get(d.value) ?? {
          idUbicacion: null,
          nombreUbicacion: null,
        };
      return {
        idDiaLaboral: d.value,
        nombreDia: d.label,
        idUbicacion: curr.idUbicacion,
        nombreUbicacion: curr.nombreUbicacion,
      };
    });
  }, [diasOptions, asignaciones]);

  // ====== MIEMBROS ======
  useEffect(() => {
    if (!selectedMember) {
      setNewMemberMax(100);
      setNewMemberDisp(100);
      return;
    }
    const max = Math.max(0, Math.min(100, selectedMember.dispRestante));
    setNewMemberMax(max);
    setNewMemberDisp(Math.max(1, Math.min(max, newMemberDisp || 1)));
  }, [selectedMember]);

  const handleAddMember = () => {
    if (!selectedMember) return;

    const maxPermitido = newMemberMax; // уже вычислен
    if (newMemberDisp > maxPermitido) {
      setError(`El porcentaje asignado (${newMemberDisp}%) supera el máximo permitido (${maxPermitido}%)`);
      return;
    }

    const nuevo: IMiembrosEquipo = {
      id: selectedMember.value,
      nombre: selectedMember.label,
      idCargo: selectedMember.idCargo,
      disponibilidad: newMemberDisp,
      fechaEntrada: newMemberStart || formData.fechaInicio,
      fechaFin: newMemberEnd || formData.fechaFin,
      estado: "A",
    };

    setMiembros((prev) => [...prev, nuevo]);
    setMemberOptions((opts) =>
      opts.filter((o) => o.value !== selectedMember.value)
    );
    setSelectedMember(null);
    setNewMemberDisp(0);
  };

  const removeMember = (id: number) => {
    const m = miembros.find((x) => x.id === id);
    if (m) {
      setMemberOptions((opts) => [
        ...opts,
        {
          value: m.id,
          label: m.nombre,
          idCargo: m.idCargo,
          dispRestante: 100,
        },
      ]);
    }
    setMiembros((ms) => ms.filter((x) => x.id !== id));
  };

  // ====== SUBMIT ======
  const handleSubmit = async (data: Record<string, any>) => {
    const duPayload = rowsDU.map((a) => ({
      idDiaLaboral: a.idDiaLaboral,
      idUbicacion: a.idUbicacion ?? null,
    }));

    const payload = {
      nombre: data.nombre,
      fechaInicio: data.fechaInicio,
      idCliente: clienteSel ? Number(clienteSel.value) : null, // cliente opcional
      estado: data.estado ? "A" : "I",
      idTecnologias: tecSel.map((t) => Number(t.value)),
      usuarios: miembros.map((m) => ({
        idUsuario: m.id,
        idCargo: m.idCargo,
        porcentajeTrabajo: Math.max(1, Math.min(100, Number(m.disponibilidad) || 1)), // <- renombrado
        estado: m.estado || "A",
        fechaEntrada: m.fechaEntrada || formData.fechaInicio,
        fechaFin: m.fechaFin || null,
      })),
      equipoDiaUbicacion: duPayload,
      ...(leadSel ? { idLider: Number(leadSel.value) } : {}),
      ...(data.fechaFin ? { fechaLimite: data.fechaFin } : {}),
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
    console.log("✅ Equipo: ", payload);
    console.log(JSON.stringify(payload, null, 2));
    if (!r.ok) {
      const msg = await r.text();
      throw new Error(`${r.status} ${r.statusText} — ${msg}`);
    }
    navigate("/operations");
  };

  // secciones base
  const getSections = () => [
    {
      title: "Crear Equipo",
      icon: "📅",
      fields: [
        { name: "nombre", label: "Nombre del equipo", type: "text" as const, required: true },
        { name: "fechaInicio", label: "Fecha de inicio", type: "date" as const, required: true },
        { name: "fechaFin", label: "Fecha límite", type: "date" as const, required: false },
        { name: "estado", label: "Activo", type: "checkbox" as const, required: true },
      ],
    },
  ];

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
              onChange={(u) => setFormData((prev) => ({ ...prev, ...u }))}
              onSubmit={handleSubmit}
            />

            {/* Tabla Día–Ubicación */}
            <div>
              <h3 className="text-sm font-semibold mb-2">Días y ubicaciones</h3>
              <div className="w-full overflow-x-auto rounded-xl border border-gray-200/40 dark:border-gray-700/60">
                <table className="min-w-full text-sm">
                  <thead className="bg-indigo-900/70 text-white">
                    <tr>
                      <th className="px-4 py-2 text-left">Día</th>
                      <th className="px-4 py-2 text-left">Ubicación</th>
                      <th className="px-4 py-2 text-left">Acciones</th>
                    </tr>
                  </thead>
                  <tbody className="bg-transparent">
                    {rowsDU.map((r) => (
                      <tr
                        key={r.idDiaLaboral}
                        className="border-t border-gray-200/30 dark:border-gray-700/50"
                      >
                        <td className="px-4 py-2">{r.nombreDia}</td>
                        <td className="px-4 py-2">
                          <Select
                            options={optionsForDay(
                              r.idDiaLaboral,
                              r.idUbicacion ?? null,
                              r.nombreUbicacion ?? null
                            )}
                            value={
                              r.idUbicacion != null
                                ? {
                                  value: r.idUbicacion,
                                  label: r.nombreUbicacion ?? "—",
                                }
                                : null
                            }
                            onChange={(opt) =>
                              setUbicacionForDay(r.idDiaLaboral, opt as any)
                            }
                            isClearable
                            placeholder="Elegir ubicación…"
                            theme={selectTheme}
                            styles={selectStyles}
                            menuPortalTarget={document.body}
                            menuPosition="fixed"
                          />
                        </td>
                        <td className="px-4 py-2">
                          <button
                            type="button"
                            onClick={() => setUbicacionForDay(r.idDiaLaboral, null)}
                            className="px-3 py-1 text-xs text-white bg-blue-600 rounded hover:bg-blue-700"
                          >
                            Limpiar
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
              <p className="mt-2 text-xs text-gray-600 dark:text-gray-300">
                * Se enviarán todos los días, aunque no tengan ubicación asignada.
              </p>
            </div>

            {/* Cliente y Team Lead */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm mb-2">Cliente (opcional)</label>
                <Select
                  options={clienteOptions}
                  value={clienteSel}
                  onChange={(opt) => setClienteSel(opt as any)}
                  isClearable
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

            {/* Tecnologías */}
            <div>
              <label className="block text-sm mb-2">Tecnologías</label>
              <div className="flex gap-2">
                <select
                  className="w-full h-10 rounded border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100 px-3"
                  value={tecPick}
                  onChange={(e) =>
                    setTecPick(e.target.value === "" ? "" : Number(e.target.value))
                  }
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

            {/* Agregar miembro */}
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
            {/* Errores en tabla usuarios */}
            {error && (
              <div className="p-4 rounded-lg text-sm border bg-red-50 text-red-700 border-red-200 dark:bg-red-900/30 dark:text-red-300 dark:border-red-700 mb-4">
                <div className="flex items-center gap-2">
                  <span>❌</span>
                  <span>
                    {(() => {
                      try {
                        const jsonPart = error.split('—')[1]?.trim(); // берём часть после "—"
                        console.log(jsonPart);
                        try {
                          const obj = JSON.parse(jsonPart || '{}');
                          return obj.message || error;
                        } catch {
                          const match = jsonPart.match(/"message"\s*:\s*"([^"]*)"/);
                          if (match) return match[1]; // вернём найденное сообщение
                          return error;
                        }
                      } catch {
                        return error;
                      }
                    })()}
                  </span>
                </div>
              </div>
            )}
            <DataTable
              data={miembros}
              columns={columns}
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
