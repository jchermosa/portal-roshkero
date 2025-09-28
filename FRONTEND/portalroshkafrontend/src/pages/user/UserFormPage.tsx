import { useNavigate, useParams, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext.tsx";
import DynamicForm from "../../components/DynamicForm.tsx";
import { useCatalogosUsuarios } from "../../hooks/catalogos/useCatalogosUsuarios.ts";
import { useUsuarioForm } from "../../hooks/usuarios/useUsuarioForm.ts";
import FormLayout from "../../layouts/FormLayout.tsx";
import { buildUsuarioSections } from "../../config/forms/usuarioFormFields.ts";
import { EstadoActivoInactivo } from "../../types";

export default function UserFormPage() {
  const { token } = useAuth();
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const cedulaParamStr = new URLSearchParams(location.search).get("cedula");
  const cedulaParam = cedulaParamStr ? Number(cedulaParamStr) : undefined;

  // Catálogos (roles y cargos)
  const { roles, cargos, loading: loadingCatalogos } = useCatalogosUsuarios(token);

  // Hook de formulario de usuario
  const {
    data,
    setData,
    loading: loadingUsuario,
    error,
    handleSubmit,
    isEditing,
  } = useUsuarioForm(token, id, cedulaParam);

  // Loading combinado
  const loading = loadingCatalogos || loadingUsuario;

  // Configuración de secciones (roles y cargos actuales)
  const sections = buildUsuarioSections(roles, cargos);

  // Render
  const readonly = new URLSearchParams(location.search).get("readonly") === "true";

  const normalizeData = (formData: Record<string, any>) => {
    return {
      ...formData,
      nroCedula: formData.nroCedula ?? "",
      rol: { idRol: Number(formData.idRol) },
      cargo: { idCargo: Number(formData.idCargo) },
      estado: formData.estado ?? EstadoActivoInactivo.A, 
      fechaIngreso: formData.fechaIngreso || null,
      fechaNacimiento: formData.fechaNacimiento || null,
      requiereCambioContrasena: formData.requiereCambioContrasena ?? true,
      urlPerfil: formData.urlPerfil || null,
      disponibilidad: formData.disponibilidad ?? 0,
    };
  };

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
      onSubmitLabel={readonly ? undefined : isEditing ? "Guardar cambios" : "Crear usuario"}
      onCancelLabel={readonly ? "Volver" : "Cancelar"}
    >
      <DynamicForm
        id="dynamic-form"
        sections={sections}
        initialData={data}
        onChange={setData}
        onSubmit={async (formData) => {
          if (!readonly) {
            const normalized = normalizeData(formData);
            const ok = await handleSubmit(normalized);
            if (ok) {
              navigate(`/usuarios?success=${isEditing ? "updated" : "created"}`);
            }
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
