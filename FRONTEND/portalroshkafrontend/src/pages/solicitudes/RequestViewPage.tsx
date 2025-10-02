import { useParams, useNavigate } from "react-router-dom";
import { useRequestView } from "../../hooks/solicitudes/useRequestView";
import { useAuth } from "../../context/AuthContext";
import PageLayout from "../../layouts/PageLayout";

export default function RequestViewPage() {
  const { token } = useAuth();
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const { solicitud, loading, error, aprobar, rechazar, procesando } = useRequestView(
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