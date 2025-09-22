// src/pages/DeviceAssignmentFormPage.tsx
import { useNavigate, useParams, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import DynamicForm from "../components/DynamicForm";
import { useDispositivoAsignadoForm } from "../hooks/dispositivosAsignados/useDispositivoAsignadoForm";
import FormLayout from "../layouts/FormLayout";
import { buildDispositivoAsignadoSections } from "../config/forms/dispositivoAsignadoFormField";

export default function DeviceAssignmentFormPage() {
  const { token } = useAuth();
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();

  // ✅ Hook de formulario de dispositivo asignado
  const {
    data,
    loading,
    error,
    handleSubmit,
    isEditing,
  } = useDispositivoAsignadoForm(token, id);

  // ✅ Configuración de secciones (puede recibir catálogos en el futuro)
  const sections = buildDispositivoAsignadoSections();

  // 🚀 Render
  const readonly = new URLSearchParams(location.search).get("readonly") === "true";

  return (
    <FormLayout
      title={
        isEditing
          ? readonly
            ? "Detalle de asignación"
            : "Editar asignación"
          : "Nueva asignación"
      }
      subtitle={
        readonly
          ? "Vista de solo lectura"
          : isEditing
          ? "Modifica los datos de la asignación"
          : "Completá los datos para asignar un dispositivo"
      }
      icon={isEditing ? (readonly ? "👀" : "✏️") : "📦"}
      onCancel={() => navigate("/dispositivos-asignados")}
      onSubmitLabel={
        readonly
          ? undefined
          : isEditing
          ? "Guardar cambios"
          : "Asignar dispositivo"
      }
      onCancelLabel={readonly ? "Volver" : "Cancelar"}
    >
      <DynamicForm
        id="device-assignment-form"
        sections={sections}
        initialData={data}
        onSubmit={async (formData) => {
          if (!readonly) {
            await handleSubmit(formData);
            navigate("/dispositivos-asignados");
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
