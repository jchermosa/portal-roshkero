// src/pages/EquipoFormPage.tsx
import { useState, useMemo, useEffect } from "react";
import Select from "react-select";
import FormLayout from "../../layouts/FormLayout";
import { useNavigate } from "react-router-dom";
import DynamicForm from "../../components/DynamicForm";
import type { ICreateTeam } from "./interfaces/ICreateTeam";
import { useAuth } from "../../context/AuthContext";
import DataTable from "../../components/DataTable";
import type { IMiembrosEquipo } from "./interfaces/IMiembrosEquipo";

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

export default function EquipoFromPage() {
  const { user } = useAuth();
  const [isEditing] = useState(true);
  const navigate = useNavigate();
  const isDark = useIsDark();

  const [formData, setFormData] = useState<ICreateTeam>({
    nombre: "",
    fechaInicio: "",
    fechaFin: "",
    cliente: 0,
    estado: true,
  });

  const [miembros, setMiembros] = useState<IMiembrosEquipo[]>([
    { id: 1, nombre: "Miembro 1", idCargo: 1 },
    { id: 2, nombre: "Miembro 2", idCargo: 2 },
  ]);

  const memberOptions = [
    { value: 1, label: "Miembro 1", idCargo: 1 },
    { value: 2, label: "Miembro 2", idCargo: 2 },
    { value: 3, label: "Ana Gómez", idCargo: 1 },
    { value: 4, label: "Luis Pérez", idCargo: 2 },
    { value: 5, label: "Carla Díaz", idCargo: 3 },
  ];

  const [leadId, setLeadId] = useState<number | null>(null);
  const [leadOption, setLeadOption] = useState<{ value: number; label: string } | null>(null);
  const [selectedMember, setSelectedMember] = useState<{ value: number; label: string; idCargo: number } | null>(null);

  const getSections = () => [
    {
      title: isEditing ? "Crear Equipo" : "Nuevo Equipo",
      icon: "📅",
      fields: [
        { name: "Nombre", label: "Ingrese el nombre del equipo", type: "textarea" as const, value: formData.nombre ?? "", required: true },
        { name: "Cliente", label: "ID del cliente", type: "textarea" as const, required: true, value: formData.cliente || "" },
        { name: "fecha_inicio", label: "Fecha de inicio", type: "date" as const, required: true, value: formData.fechaInicio || "" },
        { name: "fecha_fin", label: "Fecha de fin", type: "date" as const, required: true, value: formData.fechaFin || "" },
        { name: "estado", label: "Estado", type: "checkbox" as const, required: true, value: formData.estado || true },
      ],
    },
  ];

  const handleFormChange = (updatedData: any) => setFormData((prev) => ({ ...prev, ...updatedData }));

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    console.log("Form data submitted:", formData, miembros, { leadId });
  };

  const columns = [
    { key: "id", label: "ID" },
    {
      key: "nombre",
      label: "Nombre",
      render: (s: IMiembrosEquipo) => (
        <div className="flex items-center gap-2">
          <span>{s.nombre}</span>
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
          onClick={() => setMiembros((ms) => ms.filter((m) => m.id !== s.id))}
          className="px-3 py-1 text-sm text-white bg-blue-600 rounded hover:bg-blue-700"
        >
          Eliminar
        </button>
      ),
    },
  ];

  const handleAddMember = () => {
    if (!selectedMember) return;
    if (miembros.some((m) => m.id === selectedMember.value)) return;
    setMiembros((ms) => [
      ...ms,
      { id: selectedMember.value, nombre: selectedMember.label, idCargo: selectedMember.idCargo },
    ]);
    setSelectedMember(null);
  };

  // react-select: tema y estilos sensibles a dark
  const selectTheme = useMemo(
    () => (base: any) => ({
      ...base,
      colors: {
        ...base.colors,
        neutral0: isDark ? "#1f2937" : "#ffffff",
        neutral5: isDark ? "#374151" : "#f3f4f6",
        neutral10: isDark ? "#374151" : "#e5e7eb",
        neutral20: isDark ? "#374151" : "#d1d5db",
        neutral30: isDark ? "#4b5563" : "#cbd5e1",
        neutral40: isDark ? "#9ca3af" : "#6b7280",
        neutral80: isDark ? "#f3f4f6" : "#111827",
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
        borderColor: s.isFocused ? "#3b82f6" : (isDark ? "#374151" : "#d1d5db"),
        boxShadow: s.isFocused ? "0 0 0 2px rgba(59,130,246,.2)" : "none",
        ":hover": { borderColor: "#3b82f6" },
        minHeight: 40,
      }),
      menuPortal: (base: any) => ({ ...base, zIndex: 9999 }),
      menu: (p: any) => ({ ...p, zIndex: 9999 }),
      input: (p: any) => ({ ...p, color: isDark ? "#f3f4f6" : "#111827" }),
      singleValue: (p: any) => ({ ...p, color: isDark ? "#f3f4f6" : "#111827" }),
      placeholder: (p: any) => ({ ...p, color: isDark ? "#9ca3af" : "#6b7280" }),
      option: (p: any, s: any) => ({
        ...p,
        backgroundColor: s.isSelected
          ? (isDark ? "#1f2937" : "#dbeafe")
          : s.isFocused
          ? (isDark ? "#374151" : "#e5e7eb")
          : "transparent",
        color: isDark ? "#f3f4f6" : "#111827",
      }),
    }),
    [isDark]
  );

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
          <div className="flex-1 overflow-auto p-6">
            <FormLayout
              title={isEditing ? "Crear Equipo" : "Editar Equipo"}
              subtitle={isEditing ? "Modifica los campos necesarios" : "Completá la información de la nueva solicitud"}
              icon={isEditing ? "✏️" : "🧑‍💻"}
              onCancel={() => navigate("/operations")}
              onSubmitLabel={isEditing ? "Guardar cambios" : "Enviar solicitud"}
            >
              <DynamicForm
                key="create-team-form"
                id="equipo-form"
                sections={getSections()}
                initialData={formData}
                onSubmit={handleSubmit}
                onChange={handleFormChange}
              />

              {/* 1) Team Lead */}
              <div className="mt-4 flex flex-col gap-3 md:flex-row md:items-center">
                <div className="flex-1 min-w-[260px]">
                  <Select
                    options={memberOptions}
                    value={leadOption}
                    onChange={(opt) => setLeadOption(opt as any)}
                    isClearable
                    isSearchable
                    placeholder="Elegir Team Lead…"
                    theme={selectTheme}
                    styles={selectStyles}
                    menuPortalTarget={document.body}
                    menuPosition="fixed"
                  />
                </div>
                <button
                  type="button"
                  onClick={() => {
                    if (!leadOption) return;
                    if (!miembros.some((m) => m.id === leadOption.value)) {
                      const found = memberOptions.find((o) => o.value === leadOption.value);
                      if (found) {
                        setMiembros((ms) => [
                          ...ms,
                          { id: found.value, nombre: found.label, idCargo: (found as any).idCargo },
                        ]);
                      }
                    }
                    setLeadId(leadOption.value);
                  }}
                  disabled={!leadOption}
                  className="px-4 py-2 rounded bg-green-600 text-white text-sm disabled:opacity-60"
                >
                  Cambiar
                </button>
              </div>

              {isEditing && (
                <>
                  {/* 2) Miembros (agregar) */}
                  <div className="mt-4 mb-4 flex flex-col gap-3 md:flex-row md:items-center">
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

                  {/* 3) Tabla */}
                  <DataTable
                    data={miembros}
                    columns={columns}
                    rowKey={(s) => s.id}
                    scrollable={false}
                    enableSearch={false}
                  />
                </>
              )}
            </FormLayout>
          </div>

          <div className="p-4 md:p-6 border-t border-gray-200 dark:border-gray-700 flex-shrink-0 flex justify-end">
            <p className="text-sm text-gray-600 dark:text-gray-400">
              Última actualización del formulario visible aquí.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
