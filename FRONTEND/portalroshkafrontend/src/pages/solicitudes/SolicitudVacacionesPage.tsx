import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import DynamicForm, { type FormSection } from "../../components/DynamicForm";
import FormLayout from "../../layouts/FormLayout";

const DIAS_TOTALES = 30;

export default function SolicitudVacacionesPage() {
  const navigate = useNavigate();
  const { user } = useAuth();
  
  if (!user) {
    return <div className="p-10 text-center">Cargando usuario...</div>;
  }

  const [diasDisponibles, setDiasDisponibles] = useState<number>(15);

  const [formData, setFormData] = useState({
    fechaInicio: "",
    fechaFin: "",
    cantidadDias: 0,
    destinatario: "",
  });

  // Función auxiliar para verificar si un día es fin de semana
  const esFinDeSemana = (fecha: Date): boolean => {
    const dia = fecha.getDay();
    return dia === 0 || dia === 6; // 0 es domingo, 6 es sábado
  };

  // Calcula la cantidad de días hábiles entre dos fechas
  const calcularDiasHabiles = (inicio: Date, fin: Date): number => {
    let cantidadDias = 0;
    const actual = new Date(inicio);
    
    while (actual <= fin) {
      if (!esFinDeSemana(actual)) {
        cantidadDias++;
      }
      actual.setDate(actual.getDate() + 1);
    }
    
    return cantidadDias;
  };

  const handleChange = (data: any) => {
    let cantidadDias = 0;
    if (data.fechaInicio && data.fechaFin) {
      const inicio = new Date(data.fechaInicio);
      const fin = new Date(data.fechaFin);
      
      if (inicio <= fin) {
        cantidadDias = calcularDiasHabiles(inicio, fin);
      }
    }
    
    setFormData({
      ...data,
      cantidadDias,
    });
  };

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
          label: "Cantidad de días hábiles",
          type: "number",
          required: true,
          disabled: true,
          value: formData.cantidadDias,
          helperText: "Se calcula automáticamente (excluye sábados y domingos)",
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

  const handleSubmit = async (data: any) => {
    if (data.cantidadDias > diasDisponibles) {
      throw new Error("No tienes suficientes días disponibles.");
    }
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