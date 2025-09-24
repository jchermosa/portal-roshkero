// src/pages/DeviceFormPage.tsx
import { useNavigate, useParams, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import DynamicForm from "../../components/DynamicForm";
import FormLayout from "../../layouts/FormLayout";
import { buildDispositivoSections } from "../../config/forms/dispositivoFormFields";
import { useDispositivoForm } from "../../hooks/dispositivos/useDispositivoForm";

export default function DeviceFormPage() {
  const { token } = useAuth();
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();

  // ✅ Hook de formulario de dispositivo
  const {
    data,
    loading,
    error,
    handleSubmit,
    isEditing,
  } = useDispositivoForm(token, id);

  // 🚀 Readonly
  const readonly = new URLSearchParams(location.search).get("readonly") === "true";

  // ✅ Configuración de secciones
  const sections = buildDispositivoSections();

  return (
    <FormLayout
      title={isEditing ? (readonly ? "Detalle dispositivo" : "Editar dispositivo") : "Crear dispositivo"}
      subtitle={
        readonly
          ? "Vista de solo lectura"
          : isEditing
          ? "Modifica los campos necesarios"
          : "Completá la información del nuevo dispositivo"
      }
      icon={isEditing ? (readonly ? "👀" : "✏️") : "💻"}
      onCancel={() => navigate("/dispositivos")}
      onSubmitLabel={readonly ? undefined : (isEditing ? "Guardar cambios" : "Crear dispositivo")}
      onCancelLabel={readonly ? "Volver" : "Cancelar"}
    >
      <DynamicForm
        id="dynamic-form-device"
        sections={sections}
        initialData={data}
        onSubmit={async (formData) => {
          if (!readonly) {
            await handleSubmit(formData);
            navigate("/dispositivos");
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
