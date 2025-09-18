import { useNavigate, useParams, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import DynamicForm from "../components/DynamicForm";
import type { FormSection } from "../components/DynamicForm";
import { useCatalogos } from "../hooks/catalogos/useCatalogos";
import { useUsuarioForm } from "../hooks/usuarios/useUsuarioForm.ts";
import FormLayout from "../layouts/FormLayout";
import { buildUsuarioSections } from "../config/forms/usuarioFormFields";
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
  const sections = buildUsuarioSections(equipos, roles, cargos);

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
