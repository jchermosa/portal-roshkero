import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import DynamicForm, { type FormSection } from "../components/DynamicForm";

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
    <div className="min-h-screen flex flex-col overflow-hidden">
      {/* Fondo ilustrativo */}
      <div
        className="absolute inset-0 bg-brand-blue"
        style={{
          backgroundImage: "url('/src/assets/ilustracion-herov3.svg')",
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
        }}
      >
        <div className="absolute inset-0 bg-brand-blue/40"></div>
      </div>

      {/* Contenedor principal */}
      <div className="relative z-10 flex flex-col h-full p-4">
        <div className="max-w-3xl w-full mx-auto flex flex-col h-full">
          {/* Tarjeta translúcida con scroll interno */}
          <div className="bg-white/80 dark:bg-gray-900/80 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col w-full max-h-[96vh] overflow-hidden">
            <DynamicForm
              title="Solicitar Vacaciones"
              subtitle="Completá la información para tu solicitud"
              headerIcon="🌴"
              sections={sections}
              initialData={formData}
              onChange={handleChange}
              onSubmit={handleSubmit}
              onCancel={() => navigate("/vacaciones")}
              submitLabel="Enviar Solicitud"
              cancelLabel="Cancelar"
              className="flex-1 overflow-hidden"
            />
          </div>
        </div>
      </div>
    </div>
  );
}