import { useEffect, useState } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import DynamicForm, { type FormSection } from "../../components/DynamicForm";
import FormLayout from "../../layouts/FormLayout";
import { useCatalogosSolicitudes } from "../../hooks/catalogos/useCatalogosSolicitudes";
import { useRequestForm } from "../../hooks/solicitudes/useRequestForm";
import type { SolicitudFormData } from "../../types";

export default function RequestFormPage() {
  const { id } = useParams();
  const location = useLocation();
  const navigate = useNavigate();
  const { token } = useAuth();

  const getTipo = (): "PERMISO" | "BENEFICIO" | "VACACIONES" => {
    if (location.pathname.includes("permiso")) return "PERMISO";
    if (location.pathname.includes("beneficio")) return "BENEFICIO";
    return "VACACIONES";
  };

  const tipo = getTipo();

  const { 
    tiposPermiso, 
    tiposBeneficio, 
    loading: loadingCatalogos 
  } = useCatalogosSolicitudes(token);

  const { 
    data, 
    setData, 
    handleSubmit, 
    isEditing,
    loading: loadingForm,
    error,
    editable
  } = useRequestForm(tipo, id);

  const [beneficioError, setBeneficioError] = useState<string | null>(null);

  const loading = loadingCatalogos || loadingForm;

  // Auto-calcular días para permisos
  useEffect(() => {
    if (tipo === "PERMISO" && data.idSubtipo != null) {
      const tipoPermiso = tiposPermiso.find((t) => t.idTipoPermiso === data.idSubtipo);
      if (tipoPermiso) {
        setData((prev) => ({
          ...prev,
          cantDias: tipoPermiso.cantDias ?? 0 // asigna siempre el valor del tipo, incluso 0
        }));
      }
    }
  }, [data.idSubtipo, tipo, tiposPermiso, setData]);




  // Si es beneficio, asegurar monto en 0 por defecto
  useEffect(() => {
    if (tipo === "BENEFICIO" && (data.monto == null || isNaN(Number(data.monto)))) {
      setData((prev) => ({ ...prev, monto: 0 }));
    }
  }, [tipo, data.monto, setData]);

  // Validar monto contra monto máximo del beneficio
  useEffect(() => {
    if (tipo === "BENEFICIO" && data.idSubtipo) {
      const beneficio = tiposBeneficio.find((b) => b.idTipoBeneficio === data.idSubtipo);
      if (beneficio && data.monto != null) {
        if (data.monto > beneficio.montoMaximo) {
          setBeneficioError(
            `El monto ingresado (${data.monto}) excede el máximo permitido (${beneficio.montoMaximo}) para este beneficio.`
          );
        } else {
          setBeneficioError(null);
        }
      }
    } else {
      setBeneficioError(null);
    }
  }, [tipo, data.idSubtipo, data.monto, tiposBeneficio]);

  const getSections = (): FormSection[] => {
      const fields: any[] = [];

      if (tipo === "PERMISO") {
        fields.push({
          name: "idSubtipo",
          label: "Tipo de permiso",
          type: "select",
          required: true,
          options: tiposPermiso.map(t => ({ value: t.idTipoPermiso, label: t.nombre })),
          value: data.idSubtipo,
          disabled: !editable,
        });
      }

      if (tipo === "BENEFICIO") {
        fields.push({
          name: "idSubtipo",
          label: "Tipo de beneficio",
          type: "select",
          required: true,
          options: tiposBeneficio.map((t) => ({
            value: t.idTipoBeneficio,
            label: t.nombre,
          })),
          value: data.idSubtipo,
          disabled: !editable,
        });

        const beneficioSeleccionado = tiposBeneficio.find(
          (b) => b.idTipoBeneficio === data.idSubtipo
        );
        const montoMax = beneficioSeleccionado?.montoMaximo;

        fields.push({
          name: "monto",
          label: `Monto ${montoMax ? `(máx. ${montoMax})` : ""}`,
          type: "number",
          required: true,
          value: data.monto ?? 0,
          disabled: !editable,
          error: beneficioError || undefined,
        });
      }

      // Campos para todas las solicitudes
      fields.push({
        name: "fechaInicio",
        label: "Fecha de inicio",
        type: "date",
        required: true,
        value: data.fechaInicio,
        disabled: !editable,
      });

      // Solo VACACIONES tiene fechaFin
      if (tipo === "VACACIONES") {
        fields.push({
          name: "fechaFin",
          label: "Fecha de fin",
          type: "date",
          required: true,
          value: data.fechaFin,
          disabled: !editable,
        });
      }

      // Campos solo para PERMISO
      if (tipo === "PERMISO") {
        fields.push({
          name: "cantidadDias",
          label: "Cantidad de días",
          type: "number",
          required: true,
          value: data.cantDias ?? 0,
          disabled: !editable,
        });
      }

      // Comentario solo para PERMISO y BENEFICIO
      if (tipo !== "VACACIONES") {
        fields.push({
          name: "comentario",
          label: "Comentario",
          type: "textarea",
          required: tipo === "BENEFICIO",
          value: data.comentario,
          disabled: !editable,
        });
      }

      const iconMap = {
        PERMISO: "📋",
        BENEFICIO: "🎁",
        VACACIONES: "🌴",
      };

      return [
        {
          title: `Información de ${tipo === "PERMISO" ? "Permiso" : tipo === "BENEFICIO" ? "Beneficio" : "Vacaciones"}`,
          icon: iconMap[tipo],
          fields,
        },
      ];
    };


  if (loading) return <p>Cargando...</p>;

  const titleMap = {
    PERMISO: "Permiso",
    BENEFICIO: "Beneficio",
    VACACIONES: "Vacaciones"
  };

  const handleFormSubmit = async () => {
    if (beneficioError) return; 

    const payload = { ...data, ...(tipo === "BENEFICIO" ? { monto: data.monto ?? 0 } : {}) };

    const success = await handleSubmit(payload);
    if (success) {
      navigate(-1);
    }
  };

  return (
    <FormLayout 
      title={`${isEditing ? "Editar" : "Nueva"} Solicitud de ${titleMap[tipo]}`}
      subtitle={`Completa la información de tu solicitud de ${titleMap[tipo].toLowerCase()}`}
      icon={tipo === "PERMISO" ? "📋" : tipo === "BENEFICIO" ? "🎁" : "🌴"}
      onCancel={() => navigate(-1)}
      onSubmitLabel={isEditing ? "Guardar cambios" : "Enviar solicitud"}
    >
      <DynamicForm
        id="dynamic-form"
        sections={getSections()}
        initialData={data}
        onChange={(newData) => setData(prev => ({ ...prev, ...newData }))}
        onSubmit={handleFormSubmit}
        loading={loading}
      />
      {beneficioError && <p className="text-red-500 text-sm mt-2">{beneficioError}</p>}
      {error && <p className="text-red-500 text-sm mt-2">{error}</p>}
    </FormLayout>
  );
}
