import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import DynamicForm from "../components/DynamicForm";
import type { FormSection } from "../components/DynamicForm";
import mockTipos from "../data/mockTipoSolicitudes.json";
import { AsignadorEntidad } from "../components/Assigner";
import FormLayout from "../layouts/FormLayout";

interface CatalogItem {
  id: number;
  nombre: string;
}

export default function SolicitudFormPage() {
  const { token, user } = useAuth();
  const navigate = useNavigate();
  const { id } = useParams();
  const isEditing = !!id;

  const [tipos, setTipos] = useState<CatalogItem[]>([]);
  const [lideres, setLideres] = useState<CatalogItem[]>([]);
  const [lideresAsignados, setLideresAsignados] = useState<CatalogItem[]>([]);
  const [tipoSeleccionado, setTipoSeleccionado] = useState<number | null>(null);
  const [cantidadDias, setCantidadDias] = useState<number | null>(null);
  const [cantidadEditable, setCantidadEditable] = useState<boolean>(false);
  const [loading, setLoading] = useState(false);
  const [initialData, setInitialData] = useState<Record<string, any>>({
    id_solicitud_tipo: "",
    cantidad_dias: null,
    fecha_inicio: "",
    fecha_fin: "",
    comentario: "",
    id_lideres: [],
  });

  const modoDesarrollo = true;

  // Cargar tipos de solicitud
  useEffect(() => {
    if (modoDesarrollo) {
      setTipos(mockTipos);
      return;
    }

    if (!token) return;

    setLoading(true);
    fetch("/api/tipo-solicitud", {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then((data) => setTipos(data))
      .catch((err) => console.error("Error al cargar tipos:", err))
      .finally(() => setLoading(false));
  }, [token]);

  // Cargar líderes disponibles
  useEffect(() => {
    if (modoDesarrollo) {
      setLideres([
        { id: 1, nombre: "Ana" },
        { id: 2, nombre: "Carlos" },
        { id: 3, nombre: "Lucía" },
      ]);
      return;
    }

    fetch("/api/lideres", {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then((data) => setLideres(data))
      .catch((err) => console.error("Error al cargar líderes:", err));
  }, [token]);

  // Cargar datos si estás editando
  useEffect(() => {
    if (!isEditing || modoDesarrollo || !token) return;

    setLoading(true);
    fetch(`/api/solicitudes/${id}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then((data) => {
        setInitialData({
          id_solicitud_tipo: data.id_solicitud_tipo,
          cantidad_dias: data.cantidad_dias,
          fecha_inicio: data.fecha_inicio,
          fecha_fin: data.fecha_fin,
          comentario: data.comentario,
          id_lideres: data.id_lideres ?? [],
        });
        setTipoSeleccionado(data.id_solicitud_tipo);
        setLideresAsignados(data.id_lideres ?? []);
      })
      .catch((err) => console.error("Error al cargar solicitud:", err))
      .finally(() => setLoading(false));
  }, [id, isEditing, token]);

  // Actualizar cantidad de días según tipo
  useEffect(() => {
    const tipo = tipos.find((t) => t.id === tipoSeleccionado);
    const nombre = tipo?.nombre ?? "";

    let nuevaCantidad: number | null = null;
    let editable = false;

    if (nombre.includes("Paternidad")) {
      nuevaCantidad = 14;
    } else if (nombre.includes("Maternidad")) {
      nuevaCantidad = 150;
    } else if (nombre.includes("Cumpleanos")) {
      nuevaCantidad = 1;
    } else if (nombre.includes("Matrimonio")) {
      nuevaCantidad = 5;
    } else if (nombre.includes("Luto(1er grado)")) {
      nuevaCantidad = 5;
    } else if (nombre.includes("Luto(2do grado)")) {
      nuevaCantidad = 3;
    } else if (["Capacitacion", "Examenes", "Reposo", "Otros"].some((k) => nombre.includes(k))) {
      editable = true;
    }

    setCantidadEditable(editable);
    setCantidadDias(nuevaCantidad);

    if (!isEditing || !initialData.cantidad_dias) {
      setInitialData((prev) => ({
        ...prev,
        id_solicitud_tipo: tipoSeleccionado ?? "",
        cantidad_dias: editable ? undefined : nuevaCantidad,
      }));
    }
  }, [tipoSeleccionado, tipos]);

  // Enviar o actualizar solicitud
  const handleSubmit = async (formData: Record<string, any>) => {
    const payload = {
      id_usuario: user?.id,
      id_solicitud_tipo: formData.id_solicitud_tipo,
      cantidad_dias: formData.cantidad_dias ?? cantidadDias,
      fecha_inicio: formData.fecha_inicio,
      fecha_fin: formData.fecha_fin,
      comentario: formData.comentario,
      estado: "P",
      numero_aprobaciones: 0,
      id_lideres: lideresAsignados.map((l) => l.id),
    };

    if (modoDesarrollo) {
      console.log(isEditing ? "Edición simulada:" : "Creación simulada:", payload);
      alert(isEditing ? "Solicitud simulada actualizada." : "Solicitud simulada enviada.");
      navigate("/requests");
      return;
    }

    const url = isEditing ? `/api/solicitudes/${id}` : "/api/solicitudes";
    const method = isEditing ? "PUT" : "POST";

    const res = await fetch(url, {
      method,
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload),
    });

    if (!res.ok) throw new Error(await res.text());

    alert(isEditing ? "Solicitud actualizada correctamente." : "Solicitud creada correctamente.");
    navigate("/requests");
  };

  // Secciones del formulario
  const getSections = (): FormSection[] => [
    {
      title: isEditing ? "Editar Solicitud" : "Nueva Solicitud",
      icon: "📅",
      fields: [
        {
          name: "id_solicitud_tipo",
          label: "Tipo de solicitud",
          type: "select",
          required: true,
          options: tipos
            .filter((t) => t.nombre.toLowerCase() !== "vacaciones")
            .map((t) => ({ value: t.id, label: t.nombre })),
          placeholder: "Seleccionar...",
        },
        {
          name: "cantidad_dias",
          label: "Cantidad de días",
          type: "number",
          required: false,
          disabled: !cantidadEditable,
        },
        {
          name: "fecha_inicio",
          label: "Fecha de inicio",
          type: "date",
          required: true,
        },
        {
          name: "fecha_fin",
          label: "Fecha de fin",
          type: "date",
          required: true,
        },
        {
          name: "comentario",
          label: "Comentario",
          type: "textarea",
          fullWidth: true,
          required: true,
        },
        {
          name: "lideres_custom",
          label: "Líderes asignados",
          type: "custom",
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

  return (
    <FormLayout
      title={isEditing ? "Editar Solicitud" : "Crear Solicitud"}
      subtitle={isEditing ? "Modifica los campos necesarios" : "Completá la información de la nueva solicitud"}
      icon={isEditing ? "✏️" : "🧑‍💻"}
      onCancel={() => navigate("/requests")}
      onSubmitLabel={isEditing ? "Guardar cambios" : "Enviar solicitud"}
    >
      <DynamicForm
        id="dynamic-form"
        sections={getSections()}
        initialData={initialData}
        onChange={(data) => {
          const id = data.id_solicitud_tipo;
          setTipoSeleccionado(id ? Number(id) : null);}}
         onSubmit={handleSubmit}
         onCancel={() => navigate("/requests")}
         loading={loading}
         submitLabel="Enviar solicitud"
         className="flex-1 overflow-hidden"
            />
      </FormLayout>
  );
}
