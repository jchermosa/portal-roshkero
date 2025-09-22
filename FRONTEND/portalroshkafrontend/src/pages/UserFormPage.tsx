import { useNavigate, useParams, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import DynamicForm from "../components/DynamicForm";
import { useCatalogosUsuarios } from "../hooks/catalogos/useCatalogosUsuarios.ts";
import { useUsuarioForm } from "../hooks/usuarios/useUsuarioForm.ts";
import FormLayout from "../layouts/FormLayout";
import { buildUsuarioSections } from "../config/forms/usuarioFormFields";
export default function UserFormPage() {
  const { token } = useAuth();
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const cedulaParamStr = new URLSearchParams(location.search).get("cedula");
  const cedulaParam = cedulaParamStr ? Number(cedulaParamStr) : undefined;


  // ✅ Catálogos
  const { roles, cargos, equipos, loading: loadingCatalogos } = useCatalogosUsuarios(token);

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

  // 🚀 Render
  const readonly = new URLSearchParams(location.search).get("readonly") === "true";


  return (
    <FormLayout
  title={isEditing ? (readonly ? "Detalle usuario" : "Editar usuario") : "Crear usuario"}
  subtitle={
    readonly
      ? "Vista de solo lectura"
      : isEditing
        ? "Modifica los campos necesarios"
        : "Completá la información del nuevo usuario"
  }
  icon={isEditing ? (readonly ? "👀" : "✏️") : "🧑‍💻"}
  onCancel={() => navigate("/usuarios")}
  onSubmitLabel={readonly ? undefined : (isEditing ? "Guardar cambios" : "Crear usuario")}
  onCancelLabel={readonly ? "Volver" : "Cancelar"}   
>
      <DynamicForm
        id="dynamic-form"
        sections={sections}
        initialData={data}
        onSubmit={async (formData) => {
          if (!readonly) {
            await handleSubmit(formData);
            navigate("/usuarios");
          }
        }}
        loading={loading}
        readonly={readonly}
        className="flex-1 overflow-hidden"
      />
      {error && <p className="text-red-500 text-sm mt-2">{error}</p>}
    </FormLayout>
  );
}
