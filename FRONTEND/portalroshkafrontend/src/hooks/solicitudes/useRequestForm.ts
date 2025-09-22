import { useEffect, useState } from "react";
import mockTipos from "../../data/mockTiposPermiso.json";
import mockLideres from "../../data/mockLideres.json";
import mockSolicitudes from "../../data/mockSolicitudes.json";
import type { CatalogItem, TipoPermisoItem } from "../../types";

export interface LiderItem extends CatalogItem {
  aprobado: boolean;
}

export interface SolicitudItem {
  id: number;
  id_usuario: number;
  id_solicitud_tipo: number | null;
  tipo?: CatalogItem;
  cantidad_dias: number | null;
  fecha_inicio: string;
  fecha_fin: string;
  comentario: string;
  estado: "P" | "A" | "R";
  numero_aprobaciones: number;
  lideres: LiderItem[];
}

export function useSolicitudForm(user: any, id?: string) {
  const isEditing = !!id;
  const modoDesarrollo = true; // cambiar a false cuando haya backend

  const [tipos, setTipos] = useState<TipoPermisoItem[]>([]);
  const [lideres, setLideres] = useState<LiderItem[]>(mockLideres as LiderItem[]);
  const [lideresAsignados, setLideresAsignados] = useState<LiderItem[]>([]);
  const [solicitudes, setSolicitudes] = useState<SolicitudItem[]>([]);
  const [dataState, setDataState] = useState<Partial<SolicitudItem>>({
    id_solicitud_tipo: null,
    cantidad_dias: null,
    fecha_inicio: "",
    fecha_fin: "",
    comentario: "",
  });

  const [editable, setEditable] = useState(true); // permiso para editar TODO (según reglas)
  const [cantidadDiasEditable, setCantidadDiasEditable] = useState(true); // permiso solo para el campo cantidad_dias
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Cargar tipos mock
  useEffect(() => {
    setTipos(mockTipos as TipoPermisoItem[]);
  }, []);

  // Inicializar solicitudes mock
  useEffect(() => {
    setSolicitudes(
      mockSolicitudes.map((s) => ({
        ...s,
        estado: s.estado as "P" | "A" | "R",
        tipo: s.tipo ?? mockTipos.find((t) => t.id === s.id_solicitud_tipo),
      }))
    );
  }, []);

  // Wrapper para setData: fusiona cambios y aplica lógica inmediatamente si cambia id_solicitud_tipo
const setData = (
  valueOrUpdater:
    | Partial<SolicitudItem>
    | ((prev: Partial<SolicitudItem>) => Partial<SolicitudItem>)
) => {
  setDataState((prev) =>
    typeof valueOrUpdater === "function"
      ? valueOrUpdater(prev)
      : { ...prev, ...valueOrUpdater }
  );
};

  // Si es edición, cargar datos iniciales y aplicar reglas
  useEffect(() => {
    if (!isEditing || !id || tipos.length === 0) return;

    const s = solicitudes.find((sol) => sol.id === Number(id));
    if (!s) return;

    const tipo = tipos.find((t) => t.id === s.id_solicitud_tipo);
    const cantidadDiasTipo = tipo?.cantidadDias ?? null;
    const bloqueadaPorAprobaciones = s.numero_aprobaciones > 0 || s.estado !== "P";

    setCantidadDiasEditable(cantidadDiasTipo === null);
    // regla solicitada: solo permitir editar si cantidadDias === null (y no bloqueada por aprobaciones)
    setEditable(!bloqueadaPorAprobaciones && cantidadDiasTipo === null);

    // usamos el wrapper para que aplique la lógica consistente (merge + flags)
    setData({
      ...s,
      cantidad_dias: cantidadDiasTipo ?? s.cantidad_dias,
    });

    setLideresAsignados(s.lideres);
  }, [id, solicitudes, tipos]); // tipos debe estar cargado

  useEffect(() => {
  const tipo = tipos.find((t) => t.id === dataState.id_solicitud_tipo);

  if (!tipo) {
    setCantidadDiasEditable(true);
    setEditable(true);
    return;
  }

  if (tipo.cantidadDias != null) {
    setDataState((prev) => ({
      ...prev,
      cantidad_dias: tipo.cantidadDias,
    }));
    setCantidadDiasEditable(false);

    if (isEditing && id) {
      const s = solicitudes.find((sol) => sol.id === Number(id));
      const bloqueada = s ? (s.numero_aprobaciones > 0 || s.estado !== "P") : false;
      setEditable(!bloqueada); // ✅ solo bloquea si está aprobada o cerrada
    } else {
      setEditable(true); // ✅ en creación, todo editable
    }
  } else {
    setCantidadDiasEditable(true);

    if (isEditing && id) {
      const s = solicitudes.find((sol) => sol.id === Number(id));
      const bloqueada = s ? (s.numero_aprobaciones > 0 || s.estado !== "P") : false;
      setEditable(!bloqueada); // ✅ igual que arriba
    } else {
      setEditable(true);
    }
}


}, [dataState.id_solicitud_tipo]);

  // Guardar solicitud
  const handleSubmit = async (formData: Partial<SolicitudItem>) => {
    if (!editable) {
      alert("No se puede editar esta solicitud.");
      return;
    }

    // Forzar cantidad si tipo tiene cantidad fija
    const tipoSeleccionado = tipos.find((t) => t.id === formData.id_solicitud_tipo);
    const cantidadFija = tipoSeleccionado?.cantidadDias ?? null;

    const payload: SolicitudItem = {
      ...formData,
      id_usuario: user?.id ?? 0,
      estado: "P",
      numero_aprobaciones: isEditing ? formData.numero_aprobaciones ?? 0 : 0,
      lideres: lideresAsignados,
      id: isEditing ? Number(id) : Date.now(),
      tipo: tipoSeleccionado ?? undefined,
      cantidad_dias: cantidadFija ?? (formData.cantidad_dias ?? null),
    } as SolicitudItem;

    try {
      if (modoDesarrollo) {
        if (isEditing) {
          setSolicitudes((prev) => prev.map((s) => (s.id === Number(id) ? payload : s)));
          alert("✅ Solicitud editada correctamente (mock).");
        } else {
          setSolicitudes((prev) => [...prev, payload]);
          alert("✅ Solicitud creada correctamente (mock).");
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
    lideresAsignados,
    setLideresAsignados,
    editable,
    cantidadDiasEditable,
  };
}
