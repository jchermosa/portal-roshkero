// src/pages/dispositivos/DeviceFormPage.tsx
import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import DynamicForm from "../../components/DynamicForm";
import FormLayout from "../../layouts/FormLayout";

import { buildDispositivoSections } from "../../config/forms/dispositivoFormFields";
import { useDispositivoForm } from "../../hooks/dispositivos/useDispositivoForm";
import { mapDeviceToForm } from "../../mappers/dispositivoMapper";

// servicios para catálogos
import { getDeviceTypesPaged } from "../../services/DeviceService";
import { getUbicaciones } from "../../services/UbicacionService";

export default function DeviceFormPage() {
  const { token } = useAuth();
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();

  const idParam = id ? parseInt(id, 10) : undefined;

  // ---------- Hook del formulario (detalle + create/update) ----------
  const {
    data,
    setData,
    loading,
    error,
    handleSubmit,
    isEditing,
  } = useDispositivoForm(token, idParam);

  // ---------- Catálogos (tipos de dispositivo / ubicaciones / usuarios?) ----------
  const [loadingCatalogs, setLoadingCatalogs] = useState(false);
  const [tipoOptions, setTipoOptions] = useState<{ value: number; label: string }[]>([]);
  const [ubicOptions, setUbicOptions] = useState<{ value: number; label: string }[]>([]);
  // si más adelante quieres “usuarios”, agrega aquí:
  const userOptions: { value: number; label: string }[] = [];

  useEffect(() => {
    if (!token) return;

    let cancelled = false;
    const loadCatalogs = async () => {
      try {
        setLoadingCatalogs(true);

        // Tipos de dispositivo (paginado)
        const tiposPage = await getDeviceTypesPaged<any>(token, 0, 200);
        const tipos = Array.isArray((tiposPage as any)?.content)
          ? (tiposPage as any).content
          : Array.isArray(tiposPage) ? tiposPage : [];

        // Ubicaciones (tu servicio puede devolver Page o Array; soportamos ambas)
        const ubic = await getUbicaciones(token);
        const ubicList = Array.isArray((ubic as any)?.content)
          ? (ubic as any).content
          : Array.isArray(ubic) ? ubic : [];

        if (cancelled) return;

        setTipoOptions(
          tipos.map((t: any) => ({
            value: t.idTipoDispositivo ?? t.id ?? 0,
            label: t.nombre ?? `Tipo #${t.idTipoDispositivo ?? t.id}`,
          }))
        );

        setUbicOptions(
          ubicList.map((u: any) => ({
            value: u.idUbicacion ?? u.id ?? 0,
            label: u.nombre ?? `Ubicación #${u.idUbicacion ?? u.id}`,
          }))
        );
      } finally {
        if (!cancelled) setLoadingCatalogs(false);
      }
    };

    loadCatalogs();
    return () => { cancelled = true; };
  }, [token]);

  // ---------- Secciones (como en User: se construyen con catálogos) ----------
  const sections = useMemo(
    () => buildDispositivoSections(tipoOptions, ubicOptions, userOptions),
    [tipoOptions, ubicOptions, userOptions]
  );

  // ---------- Readonly + normalización + loading combinado ----------
  const readonly = new URLSearchParams(location.search).get("readonly") === "true";

  // normalizamos lo que vino del back a lo que el form espera
  const initial = useMemo(() => mapDeviceToForm(data), [data]);

  // no bloquear inputs si ya hay datos
  const hasData = initial && Object.keys(initial).length > 0;
  const formLoading = !readonly && (loading || loadingCatalogs) && !hasData;

  // ---------- Render ----------
  return (
    <FormLayout
      title={isEditing ? (readonly ? "Detalle dispositivo" : "Editar dispositivo") : "Crear dispositivo"}
      subtitle={
        readonly
          ? "Vista de solo lectura"
          : isEditing
          ? "Modificá los campos necesarios"
          : "Completá la información del nuevo dispositivo"
      }
      icon={isEditing ? (readonly ? "👀" : "✏️") : "💻"}
      onCancel={() => navigate("/dispositivos")}
      onSubmitLabel={readonly ? undefined : (isEditing ? "Guardar cambios" : "Crear dispositivo")}
      onCancelLabel={readonly ? "Volver" : "Cancelar"}
    >
      <DynamicForm
        id="dynamic-form-device"
        // remount cuando el detalle está “loaded” para asegurar rehidratación
        key={isEditing ? `device-${idParam}-${data?.idDispositivo ? "loaded" : "loading"}` : "device-new"}
        sections={sections}
        initialData={initial}
        // igual que en User: 2-vías hacia el hook
        onChange={setData}
        onSubmit={async (formData) => {
          if (!readonly) {
            await handleSubmit(formData);
            navigate("/dispositivos");
          }
        }}
        loading={formLoading}
        readonly={readonly}
        className="flex-1 overflow-hidden"
      />
      {error && <p className="text-red-500 text-sm mt-2">{error}</p>}
    </FormLayout>
  );
}
