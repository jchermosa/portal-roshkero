import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import DynamicForm from "../components/DynamicForm";
import type { FormSection } from "../components/DynamicForm";
import mockTipos from "../data/mockTipoSolicitudes.json";


interface CatalogItem {
  id: number;
  nombre: string;
}

export default function SolicitudFormPage() {
  const { token, user } = useAuth();
  const navigate = useNavigate();

  const [tipos, setTipos] = useState<CatalogItem[]>([]);
  const [lideres, setLideres] = useState<CatalogItem[]>([]);
  const [liderSeleccionado, setLiderSeleccionado] = useState<string>("");
  const [lideresAsignados, setLideresAsignados] = useState<CatalogItem[]>([]);
  const [tipoSeleccionado, setTipoSeleccionado] = useState<number | null>(null);
  const [cantidadDias, setCantidadDias] = useState<number | null>(null);
  const [cantidadEditable, setCantidadEditable] = useState<boolean>(false);
  const [loading, setLoading] = useState(false);
  const initialData: Record<string, any> = {
    id_solicitud_tipo: tipoSeleccionado ?? "",
    cantidad_dias: cantidadEditable ? undefined : cantidadDias ?? null,
    id_lideres: [],
  };

  const modoDesarrollo = true;

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


  useEffect(() => {
    const tipo = tipos.find((t) => t.id === tipoSeleccionado);
    if (!tipo) {
      setCantidadDias(null);
      setCantidadEditable(false);
      return;
    }

    const nombre = tipo.nombre;

    if (nombre.includes("Inventario")) {
      setCantidadDias(null);
      setCantidadEditable(false);
    } else if (nombre.includes("Paternidad")) {
      setCantidadDias(14);
      setCantidadEditable(false);
    } else if (nombre.includes("Maternidad")) {
      setCantidadDias(150);
      setCantidadEditable(false);
    } else if (nombre.includes("Cumpleanos")) {
      setCantidadDias(1);
      setCantidadEditable(false);  
    } else if (nombre.includes("Matrimonio")) {
      setCantidadDias(5);
      setCantidadEditable(false);    
    } else if (nombre.includes("Luto(1er grado)")) {
      setCantidadDias(5);
      setCantidadEditable(false);
    } else if (nombre.includes("Luto(2do grado)")) {
      setCantidadDias(3);
      setCantidadEditable(false);      
    } else if (["Capacitacion", "Examenes", "Reposo", "Otros"].some((k) => nombre.includes(k))) {
      setCantidadEditable(true);
      setCantidadDias(null);
    } else {
      setCantidadDias(null);
      setCantidadEditable(false);
    }
  }, [tipoSeleccionado, tipos]);

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
  };

  if (modoDesarrollo) {
    console.log("Solicitud simulada:", payload);
    alert("Solicitud simulada enviada correctamente.");
    navigate("/solicitudes");
    return;
  }

  const res = await fetch("/api/solicitudes", {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });

  if (!res.ok) throw new Error(await res.text());

  const solicitudCreada = await res.json(); // 👈 capturás el ID aquí
  const solicitudId = solicitudCreada.id;

    navigate("/requests");
  };


  const getSections = (): FormSection[] => [
    {
      title: "Nueva Solicitud",
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
          required: true,
        },
      ],
    },
  ];

  return (
    <div className="min-h-screen flex flex-col overflow-hidden">
      <div
        className="absolute inset-0 bg-brand-blue"
        style={{
          backgroundImage: "url('/src/assets/ilustracion-herov3.svg')",
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
        }}
      >
        <div className="absolute inset-0 bg-brand-blue/40"></div>
      </div>

      <div className="relative z-10 flex flex-col h-full p-4">
        <div className="max-w-2xl w-full mx-auto flex flex-col h-full">
          <div className="bg-white/80 dark:bg-gray-900/80 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col w-full max-h-[96vh] overflow-hidden">
            <DynamicForm
                title="Crear solicitud "
                subtitle="Completá los campos para enviar tu solicitud"
                headerIcon="📝"
                sections={getSections()}
                initialData={initialData}
                onChange={(data) => {
                    const id = data.id_solicitud_tipo;
                    setTipoSeleccionado(id ? Number(id) : null);
                }}
                onSubmit={handleSubmit}
                onCancel={() => navigate("/requests")}
                loading={loading}
                submitLabel="Enviar solicitud"
                className="flex-1 overflow-hidden"
            />
          </div>
        </div>
      </div>
    </div>
  );
}
