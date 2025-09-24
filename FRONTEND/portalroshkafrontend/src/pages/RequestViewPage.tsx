import { useParams, useNavigate } from "react-router-dom";
import { useSolicitudDetail } from "../hooks/solicitudes/useRequestView";

interface SolicitudDetailPageProps {
  token: string | null;
}

export default function RequestViewPage({ token }: SolicitudDetailPageProps) {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { solicitud, loading, error, aprobar, rechazar } = useSolicitudDetail(token, id ? Number(id) : null);

  if (loading) return <p>Cargando...</p>;
  if (error) return <p className="text-red-500">{error}</p>;
  if (!solicitud) return <p>No se encontró la solicitud.</p>;

  const handleAprobar = async () => {
    await aprobar();
    navigate(-1); // volver
  };

  const handleRechazar = async () => {
    await rechazar();
    navigate(-1);
  };

  return (
    <div className="max-w-2xl mx-auto bg-white shadow-lg p-6 rounded-lg">
      <h1 className="text-xl font-bold mb-4">
        {solicitud.tipo_solicitud === "PERMISO" ? "Detalle Permiso" : "Detalle Beneficio"}
      </h1>

      <div className="space-y-3">
        <div><strong>Usuario:</strong> {solicitud.nombre} {solicitud.apellido}</div>
        <div><strong>Tipo:</strong> {solicitud.tipo_solicitud}</div>
        <div><strong>Subtipo:</strong> {solicitud.subtipo?.nombre}</div>
        <div><strong>Estado:</strong> {solicitud.estado}</div>
        <div><strong>Comentario:</strong> {solicitud.comentario || "-"}</div>

        {solicitud.tipo_solicitud === "PERMISO" && (
          <>
            <div><strong>Líder:</strong> {solicitud.lider?.nombre} {solicitud.lider?.apellido}</div>
            <div><strong>Días:</strong> {solicitud.cantidad_dias}</div>
            <div><strong>Fecha Inicio:</strong> {solicitud.fecha_inicio}</div>
            <div><strong>Fecha Fin:</strong> {solicitud.fecha_fin}</div>
          </>
        )}
      </div>

      <div className="flex justify-between mt-6">
        <button
          onClick={() => navigate(-1)}
          className="px-4 py-2 bg-gray-400 text-white rounded-lg hover:bg-gray-500"
        >
          Volver
        </button>
        <div className="space-x-2">
          <button
            onClick={handleRechazar}
            className="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600"
          >
            Rechazar
          </button>
          <button
            onClick={handleAprobar}
            className="px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600"
          >
            Aprobar
          </button>
        </div>
      </div>
    </div>
  );
}