import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import DynamicForm, { type FormSection } from "../components/DynamicForm";
import FormLayout from "../layouts/FormLayout";

const DIAS_TOTALES = 30;

export default function SolicitudVacacionesPage() {
  const navigate = useNavigate();
  const { user } = useAuth();
  
  if (!user) {
    return <div className="p-10 text-center">Cargando usuario...</div>;
  }

  // Simulación de días disponibles (puedes traerlo de tu API/contexto)
  const [diasDisponibles, setDiasDisponibles] = useState<number>(15);

  // Estado para calcular cantidad de días y destinatario
  const [formData, setFormData] = useState({
    fechaInicio: "",
    fechaFin: "",
    cantidadDias: 0,
    destinatario: "",
  });

  // Calcula la cantidad de días automáticamente
  const handleChange = (data: any) => {
    let cantidadDias = 0;
    if (data.fechaInicio && data.fechaFin) {
      const inicio = new Date(data.fechaInicio);
      const fin = new Date(data.fechaFin);
      cantidadDias =
        Math.max(
          0,
          Math.ceil((fin.getTime() - inicio.getTime()) / (1000 * 60 * 60 * 24)) + 1
        );
    }
    setFormData({
      ...data,
      cantidadDias,
    });
  };

  // Secciones del formulario
  const sections: FormSection[] = [
    {
      title: "Datos de la solicitud",
      icon: "📅",
      fields: [
        {
          name: "fechaInicio",
          label: "Fecha de inicio",
          type: "date",
          required: true,
        },
        {
          name: "fechaFin",
          label: "Fecha de fin",
          type: "date",
          required: true,
        },
        {
          name: "cantidadDias",
          label: "Cantidad de días",
          type: "number",
          required: true,
          disabled: true,
          value: formData.cantidadDias,
          helperText: "Se calcula automáticamente",
        },
        {
          name: "diasDisponibles",
          label: "Días disponibles",
          type: "number",
          disabled: true,
          value: diasDisponibles,
        },
      ],
    },
    {
      title: "Destino de la solicitud",
      icon: "📤",
      fields: [
        {
          name: "destinatario",
          label: "Enviar solicitud a",
          type: "select",
          required: true,
          options: [
            { value: "LIDER", label: "Líder" },
            { value: "TH", label: "TH" },
          ],
          placeholder: "Selecciona destinatario",
          value: formData.destinatario,
        },
      ],
    },
  ];

  // Lógica de envío
  const handleSubmit = async (data: any) => {
    if (data.cantidadDias > diasDisponibles) {
      throw new Error("No tienes suficientes días disponibles.");
    }
    // Aquí iría la llamada a tu API para guardar la solicitud
    setDiasDisponibles((prev) => prev - data.cantidadDias);
    navigate("/vacaciones");
  };

  return (
    <FormLayout
      title="Solicitar Vacaciones"
      subtitle="Completá la información para tu solicitud"
      icon="🌴"
      onCancel={() => navigate("/vacaciones")}
      onSubmitLabel="Enviar Solicitud"
      onCancelLabel="Cancelar"
    >
      <DynamicForm
        id="dynamic-form"
        sections={sections}
        initialData={formData}
        onChange={handleChange}
        onSubmit={handleSubmit}
        className="flex-1 overflow-hidden"
      />
    </FormLayout>
  );
}