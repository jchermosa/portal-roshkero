import { useNavigate, useParams, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import DynamicForm from "../components/DynamicForm";
import type { FormSection } from "../components/DynamicForm";
import { useCatalogos } from "../hooks/catalogos/useCatalogos";
import { useUsuarioForm } from "../hooks/usuarios/useUsuarioForm.ts";
import FormLayout from "../layouts/FormLayout";

export default function UserFormPage() {
  const { token } = useAuth();
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const cedulaParam = new URLSearchParams(location.search).get("cedula") || undefined;

  // ✅ Catálogos
  const { roles, cargos, equipos, loading: loadingCatalogos } = useCatalogos(token);

  // ✅ Hook de formulario de usuario
  const {
    data,
    loading: loadingUsuario,
    error,
    handleSubmit,
    isEditing,
  } = useUsuarioForm(token, id, cedulaParam);

  // 🔄 Loading combinado
  const loading = loadingCatalogos || loadingUsuario;

  // ✅ Configuración de secciones
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
    <FormLayout
      title={isEditing ? "Editar usuario" : "Crear usuario"}
      subtitle={
        isEditing
          ? "Modifica los campos necesarios"
          : "Completá la información del nuevo usuario"
      }
      icon={isEditing ? "✏️" : "🧑‍💻"}
      onCancel={() => navigate("/usuarios")}
      onSubmitLabel={isEditing ? "Guardar cambios" : "Crear usuario"}
    >
      <DynamicForm
        id="dynamic-form"
        sections={sections}
        initialData={data}
        onSubmit={async (formData) => {
          await handleSubmit(formData);
          navigate("/usuarios");
        }}
        loading={loading}
        className="flex-1 overflow-hidden"
      />
      {error && <p className="text-red-500 text-sm mt-2">{error}</p>}
    </FormLayout>
  );
}
