import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import ProfileCard from "../components/ProfileCard";
import { useEffect, useState } from "react";

interface SolicitudItem {
  id: number;
  fecha_inicio: string;
  fecha_fin: string | null;
  cantidad_dias: number;
  numero_aprobaciones: number;
  estado: {
    id: number;
    nombre: string;
  };
}

export default function RequestPage() {


const { token, user } = useAuth();
const [solicitudes, setSolicitudes] = useState<SolicitudItem[]>([]);
const [page, setPage] = useState<number>(0);
const [totalPages, setTotalPages] = useState<number>(1);
const [error, setError] = useState<string | null>(null);
const [loading, setLoading] = useState(true);

useEffect(() => {
  if (!token) return;

  setLoading(true);
  setSolicitudes([]);

  const params = new URLSearchParams();
  params.append("usuarioId", user?.id.toString() ?? "");
  params.append("page", page.toString());
  params.append("size", "10");

  fetch(`/api/solicitudes?${params.toString()}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    .then(async (res) => {
      if (!res.ok) throw new Error(await res.text());
      return res.json();
    })
    .then((data) => {
      setSolicitudes(data.content);
      setTotalPages(data.totalPages);
    })
    .catch((err) => {
      setError("Error al cargar solicitudes: " + err.message);
    })
    .finally(() => setLoading(false));
}, [token, page]);

  return (
  <div className="space-y-6">
    <h2 className="text-2xl font-bold text-brand-blue mb-6">
    Mis Solicitudes
   </h2>

   {error && <p className="text-red-600 mb-4">{error}</p>}

   <div className="overflow-x-auto">
  <table className="w-full border-collapse rounded-lg overflow-hidden">
     <thead className="bg-blue-100 text-blue-800">
      <tr>
        <th className="px-4 py-2 text-left">ID</th>
        <th className="px-4 py-2 text-left">Inicio</th>
        <th className="px-4 py-2 text-left">Fin</th>
        <th className="px-4 py-2 text-left">Días</th>
        <th className="px-4 py-2 text-left">Aprobaciones</th>
        <th className="px-4 py-2 text-left">Estado</th>
      </tr>
     </thead>
     <tbody>
      {solicitudes.map((s) => (
        <tr key={s.id} className="border-b last:border-0">
          <td className="px-4 py-2">{s.id}</td>
          <td className="px-4 py-2">{s.fecha_inicio}</td>
          <td className="px-4 py-2">{s.fecha_fin ?? ""}</td>
          <td className="px-4 py-2">{s.cantidad_dias}</td>
          <td className="px-4 py-2">{s.numero_aprobaciones}</td>
          <td className="px-4 py-2">
            <span className="px-2 py-1 text-xs font-medium bg-blue-100 text-blue-700 rounded-full">
              {s.estado?.nombre ?? "Sin estado"}
            </span>
          </td>
        </tr>
      ))}
     </tbody>
    </table>
   </div>
  </div> 
  );
}
