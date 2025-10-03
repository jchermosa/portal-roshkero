import { useParams, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { useRequestViewUsuario } from "../../hooks/solicitudes/useRequestViewUsuario";
import PageLayout from "../../layouts/PageLayout";

export default function RequestViewPageUsuario() {
  const { token } = useAuth();
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const { solicitud, loading, error } = useRequestViewUsuario(token, id ?? null);

  if (loading) return <p className="text-center mt-10">Cargando...</p>;
  if (error) return <p className="text-red-500 text-center mt-10">{error}</p>;
  if (!solicitud) return <p className="text-center mt-10">No se encontró la solicitud.</p>;

  const estados: Record<string, string> = {
    P: "Pendiente",
    A: "Aprobado",
    R: "Rechazado",
    RC: "Recalendarizado",
  };

  return (
    <PageLayout title={`Detalle de ${solicitud.tipoSolicitud}`}>
      <div className="max-w-2xl mx-auto bg-white shadow-lg rounded-lg p-6 space-y-4">
        <div>
          <strong>Usuario:</strong> {solicitud.usuario}
        </div>
        {solicitud.nombreLider && (
          <div>
            <strong>Líder:</strong> {solicitud.nombreLider}
          </div>
        )}
        <div>
          <strong>Tipo:</strong> {solicitud.tipoSolicitud}
        </div>
        <div>
          <strong>Subtipo:</strong> {solicitud.subTipo || "—"}
        </div>
        <div>
          <strong>Estado:</strong> {estados[solicitud.estado] ?? solicitud.estado}
        </div>
        <div>
          <strong>Fecha Inicio:</strong> {solicitud.fechaInicio}
        </div>
        {(solicitud.tipoSolicitud === "PERMISO" || solicitud.tipoSolicitud === "VACACIONES") && (
          <div>
            <strong>Días:</strong> {solicitud.cantDias ?? "—"}
          </div>
        )}
        {solicitud.tipoSolicitud === "BENEFICIO" && (
          <div>
            <strong>Monto:</strong> {solicitud.monto ?? "0"}
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
