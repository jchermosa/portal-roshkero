import { useParams, useNavigate } from "react-router-dom";
import { useRequestView } from "../../hooks/solicitudes/useRequestView";
import { useAuth } from "../../context/AuthContext";
import PageLayout from "../../layouts/PageLayout";

export interface SolicitudItem {
  idSolicitud: number;
  usuario: string; // nombre completo
  tipoSolicitud: "PERMISO" | "BENEFICIO";
  fechaInicio: string;
  cantidadDias: number | null;
  fechaCreacion: string;
  estado: "P" | "A" | "R";
}

export default function RequestViewPage() {
  const { token } = useAuth();
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const { solicitud, loading, error, aprobar, rechazar } = useRequestView(token, id ?? null);

  if (loading) return <p className="text-center mt-10">Cargando...</p>;
  if (error) return <p className="text-red-500 text-center mt-10">{error}</p>;
  if (!solicitud) return <p className="text-center mt-10">No se encontró la solicitud.</p>;

  // Separar nombre y apellido
  const nombreCompleto = solicitud.usuario ?? "";
  const [nombre, ...apellidoArr] = nombreCompleto.split(" ");
  const apellido = apellidoArr.join(" ");

  const handleAprobar = async () => {
    try {
      await aprobar?.();
      navigate(-1);
    } catch (err) {
      console.error(err);
    }
  };

  const handleRechazar = async () => {
    try {
      await rechazar?.();
      navigate(-1);
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <PageLayout title={solicitud.tipoSolicitud === "PERMISO" ? "Detalle Permiso" : "Detalle Beneficio"}>
      <div className="max-w-2xl mx-auto bg-white shadow-lg rounded-lg p-6">
        <div className="space-y-4">
          <div><strong>Usuario:</strong> {nombre} {apellido}</div>
          <div><strong>Tipo:</strong> {solicitud.tipoSolicitud}</div>
          <div><strong>Estado:</strong> {solicitud.estado}</div>
          <div><strong>Fecha Inicio:</strong> {solicitud.fechaInicio}</div>
          {solicitud.tipoSolicitud === "PERMISO" && (
            <div><strong>Días:</strong> {solicitud.cantidadDias ?? 0}</div>
          )}
        </div>

        {/* Botones solo si está pendiente */}
        {solicitud.estado === "P" && (
          <div className="flex justify-center gap-4 mt-8">
            <button
              onClick={handleRechazar}
              className="px-6 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-colors"
            >
              Rechazar
            </button>
            <button
              onClick={handleAprobar}
              className="px-6 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-colors"
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
