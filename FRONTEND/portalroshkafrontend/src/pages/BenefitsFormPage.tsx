import { useNavigate } from "react-router-dom";
import DynamicForm, { type FormSection } from "../components/DynamicForm";
import { useAuth } from "../context/AuthContext";
import { useState } from "react";

export default function BeneficioFormPage() {
  const { token, user } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  // 👉 Ahora formData es persistente
  const [formData, setFormData] = useState<Record<string, any>>({
    tipo: "",
  });

  const tipo = formData.tipo;

  const buildSections = (): FormSection[] => {
    let fields: FormSection["fields"] = [
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

  const handleChange = (data: Record<string, any>) => {
    setFormData(data); // ✅ ahora guarda todo el formulario sin resetear
  };

  const handleSubmit = async (data: Record<string, any>) => {
    const payload = { ...data, userId: user?.id };
    if (!token) {
      alert("No estás autenticado");
      return;
    }

    try {
      setLoading(true);
      const res = await fetch("/api/beneficios", {
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
    <div className="relative min-h-screen overflow-hidden">
      {/* Fondo */}
      <div
        className="absolute inset-0 bg-brand-blue"
        style={{
          backgroundImage: "url('/src/assets/ilustracion-herov3.svg')",
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
        }}
      >
        <div className="absolute inset-0 bg-brand-blue/40" />
      </div>

      {/* Contenido */}
      <div className="relative z-10 min-h-screen flex items-center justify-center p-4">
        <div className="w-full max-w-2xl">
          <div className="bg-white/80 dark:bg-gray-900/80 backdrop-blur-sm rounded-2xl shadow-xl overflow-hidden">
            <DynamicForm
              title="Crear solicitud"
              subtitle="Completá los campos para enviar tu solicitud"
              headerIcon="📝"
              sections={buildSections()}
              initialData={formData} // ✅ usamos el formData persistente
              onChange={handleChange}
              onSubmit={handleSubmit}
              onCancel={() => navigate("/beneficios")}
              loading={loading}
              submitLabel="Enviar solicitud"
              className="flex-1"
            />
          </div>
        </div>
      </div>
    </div>
  );
}
