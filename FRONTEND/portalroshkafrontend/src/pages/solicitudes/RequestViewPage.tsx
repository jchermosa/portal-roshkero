import { useParams, useNavigate } from "react-router-dom";
import { useRequestView } from "../../hooks/solicitudes/useRequestView";
import { useAuth } from "../../context/AuthContext";
import PageLayout from "../../layouts/PageLayout";
import type { SolicitudItem } from "../../types";

export default function RequestViewPage() {
  const { token } = useAuth();
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const { solicitud, loading, error, aprobar, rechazar, confirmar, procesando } = useRequestView(
    token,
    id ?? null
  );

  if (loading) return <p className="text-center mt-10">Cargando...</p>;
  if (error) return <p className="text-red-500 text-center mt-10">{error}</p>;
  if (!solicitud)
    return <p className="text-center mt-10">No se encontró la solicitud.</p>;

  const handleAprobar = async () => {
    if (!aprobar) return;
    
    const confirmado = window.confirm("¿Estás seguro de aprobar esta solicitud?");
    if (!confirmado) return;
    
    try {
      const ok = await aprobar();
      if (ok) navigate(-1);
    } catch (err) {
      console.error(err);
      alert("Error al aprobar la solicitud");
    }
  };

  const handleRechazar = async () => {
    if (!rechazar) return;
    
    const confirmado = window.confirm("¿Estás seguro de rechazar esta solicitud?");
    if (!confirmado) return;
    
    try {
      const ok = await rechazar();
      if (ok) navigate(-1);
    } catch (err) {
      console.error(err);
      alert("Error al rechazar la solicitud");
    }
  };

  const handleConfirmar = async () => {
    if (!confirmar) return;
    
    const confirmado = window.confirm("¿Confirmar las vacaciones aprobadas?");
    if (!confirmado) return;
    
    try {
      const ok = await confirmar();
      if (ok) navigate(-1);
    } catch (err) {
      console.error(err);
      alert("Error al confirmar la solicitud");
    }
  };

  const estados: Record<string, string> = {
    P: "Pendiente",
    A: "Aprobado",
    R: "Rechazado",
    RC: "Recalendarizado",
  };

  const tituloPorTipo: Record<string, string> = {
    PERMISO: "Detalle Permiso",
    VACACIONES: "Detalle Vacaciones",
    BENEFICIO: "Detalle Beneficio",
  };

  const esVacacionesAprobadaSinConfirmar = 
    solicitud.tipoSolicitud === "VACACIONES" && 
    solicitud.estado === "A" && 
    !solicitud.confirmacionTh;

  return (
    <PageLayout title={tituloPorTipo[solicitud.tipoSolicitud] ?? "Detalle"}>
      <div className="max-w-2xl mx-auto bg-white shadow-lg rounded-lg p-6">
        <div className="space-y-4">
          <div>
            <strong>Usuario:</strong> {solicitud.usuario}
          </div>
          <div>
            <strong>Tipo:</strong> {solicitud.tipoSolicitud}
          </div>
          <div>
            <strong>Especificación:</strong> {solicitud.subTipo}
          </div>
          <div>
            <strong>Estado:</strong> {estados[solicitud.estado] ?? solicitud.estado}
          </div>
          <div>
            <strong>Fecha Inicio:</strong> {solicitud.fechaInicio}
          </div>

          {(solicitud.tipoSolicitud === "PERMISO" ||
            solicitud.tipoSolicitud === "VACACIONES") && (
            <div>
              <strong>Días:</strong> {solicitud.cantDias ?? 0}
            </div>
          )}

          {solicitud.tipoSolicitud === "VACACIONES" && solicitud.confirmacionTh && (
            <div>
              <span className="px-3 py-1 bg-green-100 text-green-700 rounded-full text-sm font-medium">
                ✓ Confirmado por TH
              </span>
            </div>
          )}
        </div>

        {solicitud.estado === "P" && (
          <div className="flex justify-center gap-4 mt-8">
            <button
              onClick={handleRechazar}
              disabled={procesando}
              className="px-6 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-colors disabled:bg-gray-400"
            >
              Rechazar
            </button>
            <button
              onClick={handleAprobar}
              disabled={procesando}
              className="px-6 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-colors disabled:bg-gray-400"
            >
              Aprobar
            </button>
          </div>
        )}

        {esVacacionesAprobadaSinConfirmar && (
          <div className="flex justify-center gap-4 mt-8">
            <button
              onClick={handleConfirmar}
              disabled={procesando}
              className="px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors disabled:bg-gray-400"
            >
              Confirmar Vacaciones
            </button>
          </div>
        )}

        <div className="flex justify-center mt-6">
          <button
            onClick={() => navigate(-1)}
            className="px-6 py-2 bg-gray-400 text-white rounded-lg hover:bg-gray-500 transition-colors"
          >
            Volver
          </button>
        </div>
      </div>
    </PageLayout>
  );
}