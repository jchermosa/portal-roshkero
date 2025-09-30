import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import DynamicForm, { type FormSection } from "../../components/DynamicForm";
import FormLayout from "../../layouts/FormLayout";
import { useCatalogosSolicitudes } from "../../hooks/catalogos/useCatalogosSolicitudes";
import { useRequestForm } from "../../hooks/solicitudes/useRequestForm";
import type { SolicitudItem } from "../../types";

export default function RequestFormPage() {
  const { token } = useAuth();
  const { id } = useParams();
  const navigate = useNavigate();

  // Catalogo de tipos de permiso
  const { tiposPermiso: tipos, loading: loadingCatalogos } = useCatalogosSolicitudes(token);

  // Hook del formulario
  const { data, setData, loading: loadingSolicitud, error, handleSubmit, isEditing, editable } = useRequestForm(id);

  const loading = loadingCatalogos || loadingSolicitud;

  // Manejar cambios en formulario
  const handleFormChange = (newData: Partial<SolicitudItem>) => {
    if (newData.id_subtipo && newData.id_subtipo !== data.id_subtipo) {
      const tipo = tipos.find(t => t.id === newData.id_subtipo);
      const dias = tipo?.cantidadDias ?? 0;
      setData({ ...data, ...newData, cant_dias: dias });
    } else {
      setData(prev => ({ ...prev, ...newData }));
    }
  };

  // Generar secciones del formulario
  const getSections = (): FormSection[] => [
    {
      title: "Información de la Solicitud",
      icon: "📅",
      fields: [
        {
          name: "id_subtipo",
          label: "Tipo de permiso",
          type: "select",
          required: true,
          options: tipos.map(t => ({ value: t.id, label: t.nombre })),
          value: data.id_subtipo,
          disabled: !editable,
        },
        {
          name: "cant_dias",
          label: "Cantidad de días",
          type: "number",
          required: true,
          value: data.cant_dias,
          disabled: true, // readonly
        },
        {
          name: "fecha_inicio",
          label: "Fecha de inicio",
          type: "date",
          required: true,
          value: data.fecha_inicio,
          disabled: !editable,
        },
        {
          name: "comentario",
          label: "Comentario",
          type: "textarea",
          required: true,
          value: data.comentario,
          disabled: !editable,
        },
      ],
    },
  ];

  if (loading) return <p>Cargando...</p>;

  return (
    <FormLayout
      title={isEditing ? "Editar Solicitud de Permiso" : "Nueva Solicitud de Permiso"}
      subtitle={isEditing ? "Modifica los campos necesarios" : "Completa la información de tu solicitud de permiso"}
      icon={isEditing ? "✏️" : "🧑‍💻"}
      onCancel={() => navigate("/requests")}
      onSubmitLabel={isEditing ? "Guardar cambios" : "Enviar solicitud"}
    >
      <DynamicForm
        id="solicitud-form"
        sections={getSections()}
        initialData={data}
        onChange={handleFormChange}
        onSubmit={async () => {
          const payload: SolicitudItem = {
            ...data,
            tipo_solicitud: "PERMISO",
            estado: "P",
          };
          await handleSubmit(payload);
          navigate("/requests");
        }}
        loading={loading}
        className="flex-1 overflow-hidden"
      />
      {error && <p className="text-red-500 text-sm mt-2">{error}</p>}
    </FormLayout>
  );
}
