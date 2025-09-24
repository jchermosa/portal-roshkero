import { useNavigate, useParams } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import DynamicForm from "../components/DynamicForm";
import FormLayout from "../layouts/FormLayout";
import { AsignadorEntidad } from "../components/Assigner";
import { useSolicitudForm } from "../hooks/solicitudes/useRequestForm";
import type { FormSection } from "../components/DynamicForm";

export default function SolicitudFormPage() {
  const { user } = useAuth();
  const { id } = useParams();
  const navigate = useNavigate();

  // ✅ Hook con toda la lógica
  const {
    data,
    setData,
    loading,
    error,
    handleSubmit,
    isEditing,
    tipos,
    lideres,
    lideresAsignados,
    setLideresAsignados,
    editable,
    cantidadDiasEditable,
  } = useSolicitudForm(user, id);

  // ✅ Definir secciones directamente aquí
  const sections: FormSection[] = [
    {
      title: "Información de la solicitud",
      icon: "📅",
      fields: [
        {
          name: "id_solicitud_tipo",
          label: "Tipo de solicitud",
          type: "select",
          required: true,
          options: tipos.map((t) => ({ value: t.id, label: t.nombre })),
          disabled: !editable,
        },
        {
          name: "cantidad_dias",
          label: "Cantidad de días",
          type: "number",
          required: false,
          disabled: !editable || !cantidadDiasEditable,
        },
        {
          name: "fecha_inicio",
          label: "Fecha de inicio",
          type: "date",
          required: true,
          disabled: !editable,
        },
        {
          name: "comentario",
          label: "Comentario",
          type: "textarea",
          required: true,
          disabled: !editable,
        },
        {
          name: "lideres_custom",
          label: "Líderes asignados",
          type: "custom",
          fullWidth: true,
          render: () => (
            <AsignadorEntidad
              disponibles={lideres}
              asignados={lideresAsignados}
              setAsignados={editable ? setLideresAsignados : () => {}}
              label="Líderes asignados"
              disabled={!editable}
            />
          ),
        },
      ],
    },
  ];

  return (
    <FormLayout
      title={isEditing ? "Editar Solicitud" : "Crear Solicitud"}
      subtitle={
        isEditing
          ? "Modifica los campos necesarios"
          : "Completá la información de la nueva solicitud"
      }
      icon={isEditing ? "✏️" : "🧑‍💻"}
      onCancel={() => navigate("/requests")}
      onSubmitLabel={isEditing ? "Guardar cambios" : "Enviar solicitud"}
    >
      <DynamicForm
        id="solicitud-form"
        sections={sections}
        initialData={data}
        onChange={setData}
        onSubmit={async (formData) => {
          await handleSubmit(formData);
          navigate("/requests");
        }}
        loading={loading}
      />
      {error && <p className="text-red-500 text-sm mt-2">{error}</p>}
    </FormLayout>
  );
}
