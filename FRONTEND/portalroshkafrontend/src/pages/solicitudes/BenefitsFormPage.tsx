// pages/BeneficioFormPage.tsx (conexión correcta)
import { useNavigate } from "react-router-dom";
import DynamicForm, { type FormSection } from "../../components/DynamicForm";
import FormLayout from "../../layouts/FormLayout";
import { useAuth } from "../../context/AuthContext";
import { useState } from "react";

export default function BeneficioFormPage() {
  const { token, user } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  // estado persistente del formulario
  const [formData, setFormData] = useState<Record<string, any>>({ tipo: "" });
  const tipo = formData.tipo;

  const buildSections = (): FormSection[] => {
    const fields: FormSection["fields"] = [
      {
        name: "tipo",
        label: "Tipo de beneficio",
        type: "select",
        required: true,
        options: [
          { value: "prestamo", label: "Préstamo" },
          { value: "capacitacion", label: "Capacitación" },
          { value: "nutricionista", label: "Nutricionista" },
          { value: "gimnasio", label: "Gimnasio" },
        ],
        placeholder: "Seleccionar...",
        fullWidth: true,
      },
    ];

    switch (tipo) {
      case "prestamo":
        fields.push(
          { name: "monto", label: "Monto solicitado", type: "number", required: true },
          { name: "comentario", label: "Motivo", type: "textarea", required: true }
        );
        break;
      case "capacitacion":
        fields.push(
          { name: "fecha", label: "Fecha", type: "date", required: true },
          { name: "comentario", label: "Detalle", type: "textarea", required: true }
        );
        break;
      case "nutricionista":
        fields.push(
          { name: "fecha", label: "Fecha", type: "date", required: true },
          { name: "comentario", label: "Comentario", type: "textarea" }
        );
        break;
      case "gimnasio":
        fields.push(
          { name: "fecha", label: "Fecha de inicio", type: "date", required: true },
          { name: "comentario", label: "Comentario", type: "textarea" }
        );
        break;
      default:
        if (tipo) {
          fields.push(
            { name: "comentario", label: "Comentario", type: "textarea", required: true },
            { name: "fecha", label: "Fecha solicitada", type: "date", required: true }
          );
        }
        break;
    }

    return [
      {
        title: "Solicitud de Beneficio",
        icon: "🎁",
        fields,
      },
    ];
  };

  const handleChange = (data: Record<string, any>) => setFormData(data);

  const handleSubmit = async (data: Record<string, any>) => {
    const payload = { ...data, userId: user?.id };
    if (!token) {
      alert("No estás autenticado");
      return;
    }
    try {
      setLoading(true);
      const res = await fetch("http://localhost:8080/api/beneficios", {
        method: "POST",
        headers: { "Content-Type": "application/json", Authorization: `Bearer ${token}` },
        body: JSON.stringify(payload),
      });
      if (!res.ok) throw new Error(await res.text());
      alert("Solicitud de beneficio enviada ✅");
      navigate("/beneficios");
    } catch (err: any) {
      alert("Error al enviar: " + err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <FormLayout
      title="Crear solicitud"
      subtitle="Completá los campos para enviar tu solicitud"
      icon={<span role="img" aria-label="form">📝</span>}
      onCancel={() => navigate("/beneficios")}
      onSubmitLabel="Enviar solicitud"
      onCancelLabel="Cancelar"
      loading={loading}
    >
      <DynamicForm
        sections={buildSections()}
        initialData={formData}
        onChange={handleChange}
        onSubmit={handleSubmit}
        loading={loading}
        className="flex-1"
      />
    </FormLayout>
  );
}
