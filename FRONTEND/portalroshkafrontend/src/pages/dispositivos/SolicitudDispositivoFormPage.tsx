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

  // ✅ Hook del formulario
  const { data, loading, error, handleSubmit, isEditing } =
    useSolicitudDispositivoForm(token, user?.id, id);

  // ✅ Configuración de secciones
  const tipoDispositivoOptions = [
    { value: 1, label: "Laptop" },
    { value: 2, label: "Impresora" },
    { value: 3, label: "Monitor" },
    { value: 4, label: "Tablet" },
    { value: 5, label: "Smartphone" },
    { value: 6, label: "Cables" },
  ];
  const sections = buildSolicitudDispositivoSections(tipoDispositivoOptions);

  // 🚀 Flags de contexto
  const readonly =
    new URLSearchParams(location.search).get("readonly") === "true";
  const gestion =
    new URLSearchParams(location.search).get("gestion") === "true";

  // 🚀 Handlers de aprobar/rechazar
  const handleAprobar = async () => {
    if (!id) return;
    await handleSubmit({ ...data, estado: "Aprobada" });
    navigate(`/dispositivos-asignados/nuevo?solicitudId=${id}`);
  };

  const handleRechazar = async () => {
    if (!id) return;
    await handleSubmit({ ...data, estado: "Rechazada" });
    navigate("/gestion-dispositivos?tab=solicitudes");
  };

  return (
    <FormLayout
      title={
        isEditing
          ? readonly
            ? "Detalle de solicitud de dispositivo"
            : gestion
            ? "Gestionar solicitud de dispositivo"
            : "Editar solicitud de dispositivo"
          : "Nueva solicitud de dispositivo"
      }
      subtitle={
        readonly
          ? "Vista de solo lectura"
          : gestion
          ? "Aprobá o rechazá la solicitud"
          : isEditing
          ? "Modifica los campos necesarios"
          : "Completá la información para solicitar un dispositivo"
      }
      icon={isEditing ? (readonly ? "👀" : "✏️") : "📱"}
      onCancel={() =>
        navigate(gestion ? "/gestion-dispositivos?tab=solicitudes" : "/solicitudes-dispositivos")
      }
      // En gestión, los botones de aprobar/rechazar se manejan abajo
      onSubmitLabel={
        readonly || gestion
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
          if (!readonly && !gestion) {
            await handleSubmit(formData);
            navigate("/solicitudes-dispositivos");
          }
        }}
        loading={loading}
        readonly={readonly || gestion} // 👈 en gestión el form es solo lectura
        className="flex-1 overflow-hidden"
      />

      {/* Botones extra de gestión */}
      {gestion && !readonly && (
        <div className="flex justify-end gap-2 mt-4">
          <button
            onClick={handleRechazar}
            className="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700"
          >
            Rechazar
          </button>
          <button
            onClick={handleAprobar}
            className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700"
          >
            Aprobar
          </button>
        </div>
      )}

      {error && <p className="text-red-500 text-sm mt-2">{error}</p>}
    </FormLayout>
  );
}
