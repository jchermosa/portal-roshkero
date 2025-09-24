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
  id_solicitud_tipo: number | null;
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
      estado: s.estado as "P" | "A" | "R",
      tipo: s.tipo ?? mockTipos.find((t) => t.id === s.id_solicitud_tipo),
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
  const [editable, setEditable] = useState<boolean>(true); // controla si se puede editar

  const calcularCantidadDias = (nombre: string): { dias: number | null; editable: boolean } => {
    let dias: number | null = null;
    let editableCampo = false;
    if (nombre.includes("Paternidad")) dias = 14;
    else if (nombre.includes("Maternidad")) dias = 150;
    else if (nombre.includes("Cumpleanos")) dias = 1;
    else if (nombre.includes("Matrimonio")) dias = 5;
    else if (nombre.includes("Luto(1er grado)")) dias = 5;
    else if (nombre.includes("Luto(2do grado)")) dias = 3;
    else if (["Capacitacion", "Examenes", "Reposo", "Otros"].some((k) => nombre.includes(k)))
      editableCampo = true;

    return { dias, editable: editableCampo };
  };

  useEffect(() => {
    if (isEditing && id) {
      const s = solicitudes.find((sol) => sol.id === Number(id));
      if (!s) return;

      const bloqueada = s.numero_aprobaciones > 0 || s.estado !== "P";
      setEditable(!bloqueada);

      // Calcular cantidad_dias según tipo
      const tipo = tipos.find((t) => t.id === s.id_solicitud_tipo);
      const nombre = tipo?.nombre ?? "";
      const { dias, editable: editableCampo } = calcularCantidadDias(nombre);

      setCantidadEditable(editableCampo);

      // IMPORTANTE: Determinar el valor correcto de cantidad_dias
      let cantidadDiasValue = s.cantidad_dias;
      
      // Si el campo es editable, usar el valor guardado
      // Si no es editable, usar el valor calculado o el guardado si existe
      if (!editableCampo && dias !== null) {
        cantidadDiasValue = dias;
      } else if (editableCampo && s.cantidad_dias === null) {
        cantidadDiasValue = null; // Mantener null si es editable y no hay valor
      }

      setFormData({
        ...s,
        cantidad_dias: cantidadDiasValue,
      });

      setTipoSeleccionado(s.id_solicitud_tipo);
      setLideresAsignados(s.lideres);
    }
  }, [id, solicitudes]); // Agregar solicitudes como dependencia

  // --- Actualizar cantidad y editable si cambió el tipo (solo para nuevos registros) ---
  useEffect(() => {
    // Solo actualizar si no estamos editando o si el tipo cambió manualmente
    if (!isEditing || (isEditing && tipoSeleccionado !== formData.id_solicitud_tipo)) {
      const tipo = tipos.find((t) => t.id === tipoSeleccionado);
      const nombre = tipo?.nombre ?? "";
      const { dias, editable: editableCampo } = calcularCantidadDias(nombre);
      setCantidadEditable(editableCampo);
      
      setFormData((prev) => ({
        ...prev,
        id_solicitud_tipo: tipoSeleccionado ?? undefined,
        cantidad_dias: editableCampo ? (isEditing ? prev.cantidad_dias : null) : dias,
      }));
    }
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
          disabled: !editable,
          value: formData.id_solicitud_tipo || "", // Asegurar que no sea null
        },
        {
          name: "cantidad_dias",
          label: "Cantidad de días",
          type: "number" as const,
          required: cantidadEditable, // Solo requerido si es editable
          disabled: !cantidadEditable || !editable,
          value: formData.cantidad_dias ?? "", // Convertir null a string vacío para el input
          min: 1, // Agregar validación mínima
        },
        {
          name: "fecha_inicio",
          label: "Fecha de inicio",
          type: "date" as const,
          required: true,
          disabled: !editable,
          value: formData.fecha_inicio || "",
        },
        {
          name: "comentario",
          label: "Comentario",
          type: "textarea" as const,
          fullWidth: true,
          required: true,
          disabled: !editable,
          value: formData.comentario || "",
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
              setAsignados={editable ? setLideresAsignados : () => {}} 
              label="Líderes asignados"
              disabled={!editable}
            />
          ),
        },
      ],
    },
  ];

  // --- Guardar la solicitud ---
  const handleSubmit = async (data: Partial<SolicitudItem>) => {
    if (!editable) {
      alert("No se puede editar esta solicitud.");
      return;
    }

    // Validar cantidad_dias si es editable y requerido
    if (cantidadEditable && (!data.cantidad_dias || data.cantidad_dias <= 0)) {
      alert("Por favor ingrese una cantidad de días válida.");
      return;
    }

    const payload: SolicitudItem = {
      ...data,
      id_usuario: user?.id ?? 0,
      estado: "P",
      numero_aprobaciones: 0,
      lideres: lideresAsignados,
      id: isEditing ? Number(id) : Date.now(),
      tipo: tipos.find((t) => t.id === data.id_solicitud_tipo) ?? undefined,
      cantidad_dias: data.cantidad_dias ? Number(data.cantidad_dias) : null, // Asegurar que sea número
      // NOTA: fecha_fin se elimina, el backend la calculará
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

  // --- Manejar cambios en el formulario ---
  const handleFormChange = (data: Partial<SolicitudItem>) => {
    setFormData(data);
    
    // Si el tipo de solicitud cambió, actualizar el tipo seleccionado
    if (data.id_solicitud_tipo !== undefined && data.id_solicitud_tipo !== tipoSeleccionado) {
      setTipoSeleccionado(data.id_solicitud_tipo);
    }
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
        key={isEditing ? `form-${id}-${tipoSeleccionado}` : "form-nuevo"} // Agregar tipoSeleccionado al key
        id="solicitud-form"
        sections={getSections()}
        initialData={formData}
        onSubmit={handleSubmit}
        onChange={handleFormChange}
      />
    </FormLayout>
  );
}