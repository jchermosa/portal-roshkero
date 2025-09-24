import { useEffect, useState } from "react";
import mockTipos from "../../data/mockTiposPermiso.json";
import mockLideres from "../../data/mockLideres.json";
import mockPermisos from "../../data/mockSolicitudPermiso.json";
import type { TipoPermisoItem, SolicitudPermiso } from "../../types";

export function useSolicitudForm(user: any, id?: string) {
  const isEditing = !!id;
  const modoDesarrollo = true; // cambiar a false cuando haya backend

  const [tipos, setTipos] = useState<TipoPermisoItem[]>([]);
  const [lideres] = useState(mockLideres); // lista de todos los líderes
  const [solicitudes, setSolicitudes] = useState<SolicitudPermiso[]>([]);
  const [dataState, setDataState] = useState<Partial<SolicitudPermiso>>({
    subtipo: undefined,
    cantidad_dias: undefined,
    fecha_inicio: "",
    fecha_fin: "",
    comentario: "",
    lider: undefined,
  });

  const [editable, setEditable] = useState(true);
  const [cantidadDiasEditable, setCantidadDiasEditable] = useState(true);
  const [loading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Cargar tipos mock
  useEffect(() => {
    setTipos(mockTipos as TipoPermisoItem[]);
  }, []);

  // Inicializar solicitudes mock
  useEffect(() => {
    setSolicitudes(
      (mockPermisos as SolicitudPermiso[]).map((s) => ({
        ...s,
        estado: s.estado as "P" | "A" | "R",
      }))
    );
  }, []);

  const setData = (
    valueOrUpdater:
      | Partial<SolicitudPermiso>
      | ((prev: Partial<SolicitudPermiso>) => Partial<SolicitudPermiso>)
  ) => {
    setDataState((prev) =>
      typeof valueOrUpdater === "function"
        ? valueOrUpdater(prev)
        : { ...prev, ...valueOrUpdater }
    );
  };

  // Si es edición, cargar datos iniciales
  useEffect(() => {
    if (!isEditing || !id || tipos.length === 0) return;

    const s = solicitudes.find((sol) => sol.id === Number(id));
    if (!s) return;

    const tipo = tipos.find((t) => t.id === s.subtipo.id);
    const cantidadDiasTipo = tipo?.cantidadDias ?? null;
    const bloqueada = s.estado !== "P";

    setCantidadDiasEditable(cantidadDiasTipo === null);
    setEditable(!bloqueada);

    setData({
      ...s,
      cantidad_dias: cantidadDiasTipo ?? s.cantidad_dias,
    });
  }, [id, solicitudes, tipos]);

  // Cuando cambia subtipo → fijar cantidad_dias si el tipo la tiene definida
  useEffect(() => {
    if (!dataState.subtipo) return;

    const tipo = tipos.find((t) => t.id === dataState.subtipo?.id);

    if (!tipo) {
      setCantidadDiasEditable(true);
      setEditable(true);
      return;
    }

    if (tipo.cantidadDias != null) {
      setDataState((prev) => ({
        ...prev,
        cantidad_dias: tipo.cantidadDias ?? undefined,
      }));

      setCantidadDiasEditable(false);

      if (isEditing && id) {
        const s = solicitudes.find((sol) => sol.id === Number(id));
        const bloqueada = s ? s.estado !== "P" : false;
        setEditable(!bloqueada);
      } else {
        setEditable(true);
      }
    } else {
      setCantidadDiasEditable(true);

      if (isEditing && id) {
        const s = solicitudes.find((sol) => sol.id === Number(id));
        const bloqueada = s ? s.estado !== "P" : false;
        setEditable(!bloqueada);
      } else {
        setEditable(true);
      }
    }
  }, [dataState.subtipo]);

  // Guardar solicitud
  const handleSubmit = async (formData: Partial<SolicitudPermiso>) => {
    if (!editable) {
      alert("No se puede editar esta solicitud.");
      return;
    }

    const tipoSeleccionado = tipos.find((t) => t.id === formData.subtipo?.id);
    const cantidadFija = tipoSeleccionado?.cantidadDias ?? null;

    const payload: SolicitudPermiso = {
      ...formData,
      id_usuario: user?.id ?? 0,
      nombre: user?.nombre ?? "",
      apellido: user?.apellido ?? "",
      estado: isEditing ? formData.estado ?? "P" : "P",
      id: isEditing ? Number(id) : Date.now(),
      subtipo: tipoSeleccionado
        ? { id: tipoSeleccionado.id, nombre: tipoSeleccionado.nombre }
        : (formData.subtipo as any),
      cantidad_dias: cantidadFija ?? formData.cantidad_dias ?? 0,
      fecha_inicio: formData.fecha_inicio ?? "",
      fecha_fin: formData.fecha_fin ?? "",
      comentario: formData.comentario ?? "",
      lider: formData.lider!,
      tipo_solicitud: "PERMISO",
    };

    try {
      if (modoDesarrollo) {
        if (isEditing) {
          setSolicitudes((prev) =>
            prev.map((s) => (s.id === Number(id) ? payload : s))
          );
          alert("Solicitud editada correctamente (mock).");
        } else {
          setSolicitudes((prev) => [...prev, payload]);
          alert("Solicitud creada correctamente (mock).");
        }
      } else {
        // fetch a la API...
      }
    } catch (err: any) {
      setError(err.message || "Error al guardar la solicitud");
      throw err;
    }
  };

  return {
    data: dataState,
    setData,
    loading,
    error,
    handleSubmit,
    isEditing,
    tipos,
    lideres,
    editable,
    cantidadDiasEditable,
  };
}
