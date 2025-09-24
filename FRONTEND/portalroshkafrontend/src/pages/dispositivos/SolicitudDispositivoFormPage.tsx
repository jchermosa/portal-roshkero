// src/pages/SolicitudDispositivoFormPage.tsx
import { useNavigate, useParams, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import DynamicForm from "../../components/DynamicForm";
import FormLayout from "../../layouts/FormLayout";
import { useSolicitudDispositivoForm } from "../../hooks/deviceRequest/useSolicitudDispositivoForm";
import { buildSolicitudDispositivoSections } from "../../config/forms/solicitudDispositivoFormFields";

export default function SolicitudDispositivoFormPage() {
  const { token, user } = useAuth();
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();

  // ✅ Hook del formulario (pasamos el id_usuario implícito)
  const {
    data,
    loading,
    error,
    handleSubmit,
    isEditing,
  } = useSolicitudDispositivoForm(token, user?.id, id);

  // ✅ Configuración de secciones (ejemplo con tipos de dispositivo mockeados)
  const tipoDispositivoOptions = [
    { value: 1, label: "Laptop" },
    { value: 2, label: "Impresora" },
    { value: 3, label: "Monitor" },
  ];
  const sections = buildSolicitudDispositivoSections(tipoDispositivoOptions);

  // 🚀 Render
  const readonly = new URLSearchParams(location.search).get("readonly") === "true";

  return (
    <FormLayout
      title={
        isEditing
          ? readonly
            ? "Detalle de solicitud de dispositivo"
            : "Editar solicitud de dispositivo"
          : "Nueva solicitud de dispositivo"
      }
      subtitle={
        readonly
          ? "Vista de solo lectura"
          : isEditing
          ? "Modifica los campos necesarios"
          : "Completá la información para solicitar un dispositivo"
      }
      icon={isEditing ? (readonly ? "👀" : "✏️") : "📱"}
      onCancel={() => navigate("/solicitudes-dispositivos")}
      onSubmitLabel={
        readonly
          ? undefined
          : isEditing
          ? "Guardar cambios"
          : "Crear solicitud"
      }
      onCancelLabel={readonly ? "Volver" : "Cancelar"}
    >
      <DynamicForm
        id="dynamic-form"
        sections={sections}
        initialData={data}
        onSubmit={async (formData) => {
          if (!readonly) {
            await handleSubmit(formData);
            navigate("/solicitudes-dispositivos");
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
