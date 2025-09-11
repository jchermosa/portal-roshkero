import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import DynamicForm from "../components/DynamicForm";
import type { FormSection } from "../components/DynamicForm";

interface CatalogItem {
  id: number;
  nombre: string;
}

export default function UserFormPage() {
  const { token } = useAuth();
  const { id } = useParams();
  const navigate = useNavigate();

  const [roles, setRoles] = useState<CatalogItem[]>([]);
  const [cargos, setCargos] = useState<CatalogItem[]>([]);
  const [equipos, setEquipos] = useState<CatalogItem[]>([]);
  const [initialData, setInitialData] = useState<Record<string, any>>({});
  const [loading, setLoading] = useState(false); 

  const isEditing = !!id;

  useEffect(() => {
    if (!token) return;

    
    setLoading(true);
    Promise.all([
      fetch("/api/catalogos/roles").then((r) => r.json()),
      fetch("/api/catalogos/cargos").then((r) => r.json()),
      fetch("/api/catalogos/equipos").then((r) => r.json()),
    ])
    .then(([rolesData, cargosData, equiposData]) => {
      setRoles(rolesData);
      setCargos(cargosData);
      setEquipos(equiposData);
    })
    .catch((error) => {
      console.error('Error loading catalogs:', error);
    })
    .finally(() => {
      if (!isEditing) {
        setLoading(false);
      }
    });
  }, [token, isEditing]);

  useEffect(() => {
    if (!token || !isEditing) {
      setLoading(false);
      return;
    }

    setLoading(true);
    fetch(`/api/usuarios/${id}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then((data) => {
        const processedData = {
          ...data,
          estado: data.estado ?? true,
          requiereCambioContrasena: data.requiereCambioContrasena ?? false,
        };
        setInitialData(processedData);
      })
      .catch((error) => {
        console.error('Error loading user data:', error);
      })
      .finally(() => setLoading(false));
  }, [id, token, isEditing]);

  const handleSubmit = async (formData: Record<string, any>) => {
    const method = isEditing ? "PUT" : "POST";
    const url = isEditing ? `/api/usuarios/${id}` : "/api/usuarios";

    const res = await fetch(url, {
      method,
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    });

    if (!res.ok) {
      throw new Error(await res.text());
    }

    navigate("/usuarios");
  };

  const sections: FormSection[] = [
    {
      title: "Información básica",
      icon: "👤",
      fields: [
        { name: "nombre", label: "Nombre", type: "text", required: true },
        { name: "apellido", label: "Apellido", type: "text", required: true },
        { name: "nroCedula", label: "Número de cédula", type: "text", required: true },
        { name: "correo", label: "Correo electrónico", type: "email", required: true },
        { name: "telefono", label: "Teléfono", type: "text" },
        { name: "fechaIngreso", label: "Fecha de ingreso", type: "date" },
        { name: "fechaNacimiento", label: "Fecha de nacimiento", type: "date" },
      ],
    },
    {
      title: "Rol y asignación",
      icon: "🛠️",
      fields: [
        {
          name: "equipoId",
          label: "Equipo",
          type: "select",
          required: true,
          options: equipos.map((e) => ({ value: e.id, label: e.nombre })),
        },
        {
          name: "rolId",
          label: "Rol",
          type: "select",
          required: true,
          options: roles.map((r) => ({ value: r.id, label: r.nombre })),
        },
        {
          name: "cargoId",
          label: "Cargo",
          type: "select",
          required: true,
          options: cargos.map((c) => ({ value: c.id, label: c.nombre })),
        },
      ],
    },
    {
      title: "Configuración avanzada",
      icon: "⚙️",
      fields: [
        { name: "estado", label: "Usuario activo", type: "checkbox" },
        { name: "requiereCambioContrasena", label: "Requiere cambio de contraseña", type: "checkbox" },
      ],
    },
  ];

  return (
  <div className="min-h-screen flex flex-col overflow-hidden">
    {/* Fondo ilustrativo */}
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

    {/* Contenedor principal */}
    <div className="relative z-10 flex flex-col h-full p-4">
      <div className="max-w-3xl w-full mx-auto flex flex-col h-full">
        {/* Tarjeta translúcida con scroll interno */}
        <div className="bg-white/80 dark:bg-gray-900/80 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col w-full max-h-[96vh] overflow-hidden">
          <DynamicForm
            title={isEditing ? "Editar usuario" : "Crear usuario"}
            subtitle={
              isEditing
                ? "Modifica los campos necesarios"
                : "Completá la información del nuevo usuario"
            }
            headerIcon={isEditing ? "✏️" : "🧑‍💻"}
            sections={sections}
            initialData={initialData}
            onSubmit={handleSubmit}
            onCancel={() => navigate("/usuarios")}
            loading={loading}
            submitLabel={isEditing ? "Guardar cambios" : "Crear usuario"}
            className="flex-1 overflow-hidden"   
          />
        </div>
      </div>
    </div>
  </div>
);
}