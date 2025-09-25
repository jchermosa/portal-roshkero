// src/pages/EquipoFormPage.tsx
import { useState, useMemo, useEffect } from "react";
import Select from "react-select";
import { useNavigate } from "react-router-dom";
import DynamicForm from "../../components/DynamicForm";
import { useAuth } from "../../context/AuthContext";

const METADATAS_PATH = "/api/v1/admin/operations/metadatas";
const CREATE_TEAM_PATH = "/api/v1/admin/operations/team";

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

export default function EquipoFormPage() {
  const { token } = useAuth();
  const navigate = useNavigate();
  const isDark = useIsDark();

  // Form base
  const [formData, setFormData] = useState({
    nombre: "",
    fechaInicio: "",
    fechaFin: "",
    estado: true,
  });

  // Metadatas
  const [clienteOptions, setClienteOptions] = useState<Array<{ value: number; label: string }>>([]);
  const [leadOptions, setLeadOptions] = useState<Array<{ value: number; label: string }>>([]);
  const [tecOptions, setTecOptions] = useState<Array<{ value: number; label: string }>>([]);

  // Selecciones
  const [clienteSel, setClienteSel] = useState<{ value: number; label: string } | null>(null);
  const [leadSel, setLeadSel] = useState<{ value: number; label: string } | null>(null); // opcional
  const [tecSel, setTecSel] = useState<Array<{ value: number; label: string }>>([]);
  const [tecPick, setTecPick] = useState<number | "">("");

  // cargar metadatas
  useEffect(() => {
    const ac = new AbortController();
    (async () => {
      const r = await fetch(METADATAS_PATH, {
        headers: { Accept: "application/json", ...(token ? { Authorization: `Bearer ${token}` } : {}) },
        credentials: "include",
        signal: ac.signal,
      });
      if (!r.ok) throw new Error(`Metadatas ${r.status}`);
      const data = await r.json();

      setClienteOptions(
        (data.clientes ?? []).map((c: any) => ({ value: c.idCliente, label: c.nombre }))
      );

      setLeadOptions(
        (data.teamleaders ?? data.lideres ?? data.usuarios ?? []).map((u: any) => ({
          value: u.idUsuario ?? u.id,
          label: [u.nombre, u.apellido].filter(Boolean).join(" "),
        }))
      );

      setTecOptions(
        (data.tecnologias ?? []).map((t: any) => ({ value: t.idTecnologia, label: t.nombre }))
      );
    })().catch(console.error);
    return () => ac.abort();
  }, [token]);

  // secciones base
  const getSections = () => [
    {
      title: "Crear Equipo",
      icon: "📅",
      fields: [
        { name: "nombre", label: "Nombre del equipo", type: "text" as const, required: true },
        { name: "fechaInicio", label: "Fecha de inicio", type: "date" as const, required: true },
        { name: "fechaFin", label: "Fecha límite", type: "date" as const, required: true },
        { name: "estado", label: "Activo", type: "checkbox" as const, required: true },
      ],
    },
  ];

  const handleFormChange = (updatedData: any) =>
    setFormData((prev) => ({ ...prev, ...updatedData }));

  // agregar/quitar tecnologías
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

  // submit (team lead opcional)
  const handleSubmit = async (data: Record<string, any>) => {
  if (!clienteSel) throw new Error("Seleccioná un cliente.");

  const payload = {
    nombre: data.nombre,
    idLider: leadSel ? Number(leadSel.value) : null, // opcional
    fechaInicio: data.fechaInicio,
    fechaLimite: data.fechaFin,
    idCliente: Number(clienteSel.value),
    estado: data.estado ? "A" : "I",
    idTecnologias: tecSel.map(t => Number(t.value)), // <-- SOLO IDs
    usuarios: null, // <-- según tu API
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
    throw new Error(`${r.status} ${r.statusText} — ${msg}`);
  }
  navigate("/operations");
};

  // theme
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
        minHeight: 40,
      }),
      menuPortal: (b: any) => ({ ...b, zIndex: 9999 }),
    }),
    [isDark]
  );

  return (
    <div className="h-full flex flex-col overflow-hidden">
      <div className="absolute inset-0 bg-brand-blue">
        <div className="absolute inset-0 bg-brand-blue/40" />
      </div>

      <div className="relative z-10 h-full p-4">
        <div className="mx-auto w-full max-w-4xl bg-white/45 dark:bg-gray-900/80 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col max-h-[calc(100vh-2rem)] text-gray-900 dark:text-gray-100">
          
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
