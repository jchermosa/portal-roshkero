// src/pages/dispositivos/DeviceAssignmentFormPage.tsx
import { useNavigate, useParams, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import DynamicForm from "../../components/DynamicForm";
import { useDispositivoAsignadoForm } from "../../hooks/dispositivosAsignados/useDispositivoAsignadoForm";
import FormLayout from "../../layouts/FormLayout";
import { buildDispositivoAsignadoSections } from "../../config/forms/dispositivoAsignadoFormField";

export default function DeviceAssignmentFormPage() {
  const { token } = useAuth();
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();

  // ✅ Extraer solicitudId del query param (cuando se aprueba una solicitud)
  const solicitudIdStr = new URLSearchParams(location.search).get("solicitudId");
  const solicitudId = solicitudIdStr ? Number(solicitudIdStr) : undefined;

  // ✅ Hook de formulario de dispositivo asignado
  const { data, setData, loading, error, handleSubmit, isEditing } =
    useDispositivoAsignadoForm(
      token,
      id ? Number(id) : undefined, // 👈 casteo a number
      solicitudId
    );

  // ✅ Configuración de secciones (puede recibir catálogos en el futuro)
  const sections = buildDispositivoAsignadoSections();

  // 🚀 Flags de contexto
  const readonly =
    new URLSearchParams(location.search).get("readonly") === "true";

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
          ? "Modificá los datos de la asignación"
          : "Completá los datos para asignar un dispositivo"
      }
      icon={isEditing ? (readonly ? "👀" : "✏️") : "📦"}
      onCancel={() => navigate("/gestion-dispositivos?tab=asignaciones")}
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
        onChange={setData}
        onSubmit={async (formData) => {
          if (!readonly) {
            await handleSubmit(formData);
            navigate("/gestion-dispositivos?tab=asignaciones");
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
