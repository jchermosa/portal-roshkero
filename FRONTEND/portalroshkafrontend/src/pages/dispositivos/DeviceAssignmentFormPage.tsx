import { useNavigate, useParams, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import DynamicForm from "../../components/DynamicForm";
import { useDispositivoAsignadoForm } from "../../hooks/dispositivosAsignados/useDispositivoAsignadoForm";
import FormLayout from "../../layouts/FormLayout";
import { buildDispositivoAsignadoSections } from "../../config/forms/dispositivoAsignadoFormFields";
import { useAvailableDevicesOptions } from "../../hooks/dispositivosAsignados/useAvailableDevicesOptions";
import { useSolicitudOptions } from "../../hooks/deviceRequest/useSolicitudOptions";

export default function DeviceAssignmentFormPage() {
  const { token } = useAuth();
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();

  const solicitudIdStr = new URLSearchParams(location.search).get("solicitudId");
  const solicitudId = solicitudIdStr ? Number(solicitudIdStr) : undefined;

  const { data, setData, loading, error, handleSubmit, isEditing } =
    useDispositivoAsignadoForm(token, id ? Number(id) : undefined, solicitudId);

  const { options: deviceOptions } = useAvailableDevicesOptions(token);
  const { options: solicitudOptions } = useSolicitudOptions(token, solicitudId);

  const sections = buildDispositivoAsignadoSections(
    deviceOptions,
    solicitudOptions,
    solicitudId
  );

  const readonly = new URLSearchParams(location.search).get("readonly") === "true";

  return (
    <FormLayout
      title={
        isEditing ? (readonly ? "Detalle de asignación" : "Editar asignación") : "Nueva asignación"
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
      onSubmitLabel={readonly ? undefined : isEditing ? "Guardar cambios" : "Asignar dispositivo"}
      onCancelLabel={readonly ? "Volver" : "Cancelar"}
    >
      <DynamicForm
        id="dynamic-form"
        sections={sections}
        initialData={{ ...data, ...(solicitudId ? { idSolicitud: solicitudId } : {}) }}
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
