import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import DynamicForm from "../components/DynamicForm";
import FormLayout from "../layouts/FormLayout";
import { AsignadorEntidad } from "../components/Assigner";
import mockTipos from "../data/mockTipoSolicitudes.json";
import mockLideres from "../data/mockLideres.json";
import mockSolicitudes from "../data/mockSolicitudes.json";
import type { CatalogItem } from "../types";

interface LiderItem extends CatalogItem {
  aprobado: boolean;
}

interface SolicitudItem {
  id: number;
  id_usuario: number;
  id_solicitud_tipo: number | null; // <-- ahora acepta null
  tipo?: CatalogItem;
  cantidad_dias: number | null;
  fecha_inicio: string;
  fecha_fin: string;
  comentario: string;
  estado: "P" | "A" | "R";
  numero_aprobaciones: number;
  lideres: LiderItem[];
}

export default function SolicitudFormPage() {
  const { user } = useAuth();
  const { id } = useParams();
  const navigate = useNavigate();
  const isEditing = !!id;

  const modoDesarrollo = true; // 👈 cambiar a false cuando haya backend

  const [solicitudes, setSolicitudes] = useState<SolicitudItem[]>(
      mockSolicitudes.map((s) => ({
        ...s,
        estado: s.estado as "P" | "A" | "R", // forzar tipo
        tipo: s.tipo ?? mockTipos.find((t) => t.id === s.id_solicitud_tipo), // opcional
    }))
  );
  const [tipos, setTipos] = useState<CatalogItem[]>(mockTipos);
  const [lideres, setLideres] = useState<LiderItem[]>(mockLideres as LiderItem[]);
  const [lideresAsignados, setLideresAsignados] = useState<LiderItem[]>([]);
  const [tipoSeleccionado, setTipoSeleccionado] = useState<number | null>(null);
  const [cantidadEditable, setCantidadEditable] = useState<boolean>(false);
  const [formData, setFormData] = useState<Partial<SolicitudItem>>({
    id_solicitud_tipo: null,
    cantidad_dias: null,
    fecha_inicio: "",
    fecha_fin: "",
    comentario: "",
  });

  // --- Cargar datos si es edición ---
  useEffect(() => {
    if (isEditing && id) {
      if (modoDesarrollo) {
        const s = solicitudes.find((sol) => sol.id === Number(id));
        if (s) {
          setFormData(s);
          setLideresAsignados(s.lideres);
          setTipoSeleccionado(s.id_solicitud_tipo);
        } else {
          alert("Solicitud no encontrada (mock).");
          navigate("/requests");
        }
      } else {
        // Aquí iría el fetch a la API cuando esté disponible
      }
    }
  }, [id]);

  // --- Calcular cantidad de días según tipo ---
  const calcularCantidadDias = (nombre: string): { dias: number | null; editable: boolean } => {
    let dias: number | null = null;
    let editable = false;
    if (nombre.includes("Paternidad")) dias = 14;
    else if (nombre.includes("Maternidad")) dias = 150;
    else if (nombre.includes("Cumpleanos")) dias = 1;
    else if (nombre.includes("Matrimonio")) dias = 5;
    else if (nombre.includes("Luto(1er grado)")) dias = 5;
    else if (nombre.includes("Luto(2do grado)")) dias = 3;
    else if (["Capacitacion", "Examenes", "Reposo", "Otros"].some((k) => nombre.includes(k)))
      editable = true;

    return { dias, editable };
  };

  // --- Actualizar cantidad y editable si cambió el tipo ---
  useEffect(() => {
    const tipo = tipos.find((t) => t.id === tipoSeleccionado);
    const nombre = tipo?.nombre ?? "";
    const { dias, editable } = calcularCantidadDias(nombre);
    setCantidadEditable(editable);
    setFormData((prev) => ({
      ...prev,
      id_solicitud_tipo: tipoSeleccionado ?? undefined, // ahora compatible
      cantidad_dias: editable ? prev.cantidad_dias : dias,
    }));
  }, [tipoSeleccionado]);

  // --- Generar secciones para DynamicForm ---
  const getSections = (): any[] => [
    {
      title: isEditing ? "Editar Solicitud" : "Nueva Solicitud",
      icon: "📅",
      fields: [
        {
          name: "id_solicitud_tipo",
          label: "Tipo de solicitud",
          type: "select" as const,
          required: true,
          options: tipos.map((t) => ({ value: t.id, label: t.nombre })),
          placeholder: "Seleccionar...",
        },
        {
          name: "cantidad_dias",
          label: "Cantidad de días",
          type: "number" as const,
          required: false,
          disabled: !cantidadEditable,
        },
        {
          name: "fecha_inicio",
          label: "Fecha de inicio",
          type: "date" as const,
          required: true,
        },
        {
          name: "fecha_fin",
          label: "Fecha de fin",
          type: "date" as const,
          required: true,
        },
        {
          name: "comentario",
          label: "Comentario",
          type: "textarea" as const,
          fullWidth: true,
          required: true,
        },
        {
          name: "lideres_custom",
          label: "Líderes asignados",
          type: "custom" as const,
          fullWidth: true,
          render: () => (
            <AsignadorEntidad
              disponibles={lideres}
              asignados={lideresAsignados}
              setAsignados={setLideresAsignados}
              label="Líderes asignados"
            />
          ),
        },
      ],
    },
  ];

  // --- Guardar la solicitud ---
  const handleSubmit = async (data: Partial<SolicitudItem>) => {
    const payload: SolicitudItem = {
      ...data,
      id_usuario: user?.id ?? 0,
      estado: "P",
      numero_aprobaciones: 0,
      lideres: lideresAsignados,
      id: isEditing ? Number(id) : Date.now(),
    } as SolicitudItem;

    if (modoDesarrollo) {
      if (isEditing) {
        setSolicitudes((prev) =>
          prev.map((s) => (s.id === Number(id) ? payload : s))
        );
        alert("✅ Solicitud editada correctamente (mock).");
      } else {
        setSolicitudes((prev) => [...prev, payload]);
        alert("✅ Solicitud creada correctamente (mock).");
      }
    } else {
      // Aquí iría el fetch a la API
    }

    navigate("/requests");
  };

  return (
    <FormLayout
      title={isEditing ? "Editar Solicitud" : "Crear Solicitud"}
      subtitle={
        isEditing
          ? "Modifica los campos necesarios"
          : "Completá la información de la nueva solicitud"
      }
      icon={isEditing ? "✏️" : "🧑‍💻"}
      onCancel={() => navigate("/requests")}
      onSubmitLabel={isEditing ? "Guardar cambios" : "Enviar solicitud"}
    >
      <DynamicForm
        key={isEditing ? `form-${id}` : "form-nuevo"}
        id="solicitud-form"
        sections={getSections()}
        initialData={formData}
        onSubmit={handleSubmit}
        onChange={(data) => setFormData(data)}
      />
    </FormLayout>
  );
}
