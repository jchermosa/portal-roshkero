import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { getUsuarioByCedula } from "../../services/UserService";
import heroBg from "../../assets/ilustracion-herov3.svg";

export default function UserSearchPage() {
  const { token } = useAuth();
  const navigate = useNavigate();
  const [cedula, setCedula] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSearch = async () => {
    const trimmedCedula = cedula.trim();

    if (!trimmedCedula || !token) return;
    setError(null);
    setLoading(true);

    try {
      const data = await getUsuarioByCedula(token, trimmedCedula);

      // Si encuentra → ir a editar
      navigate(`/usuarios/${data.idUsuario}`); // <-- Ajustado a idUsuario
    } catch (err: any) {
      if (err.message === "NOT_FOUND") {
        // Si no encuentra → ir a crear con la cédula precargada
        navigate(`/usuarios/nuevo?cedula=${trimmedCedula}`);
      } else {
        setError(err.message || "Error al buscar usuario");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="relative min-h-screen flex items-center justify-center overflow-hidden">
      {/* Fondo */}
      <div
        className="absolute inset-0 bg-brand-blue"
        style={{
          backgroundImage: `url(${heroBg})`,
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
        }}
      >
        <div className="absolute inset-0 bg-brand-blue/40" />
      </div>

      {/* Card central */}
      <div className="relative z-10 w-full max-w-md bg-white/90 dark:bg-gray-900/90 backdrop-blur-sm shadow-2xl rounded-2xl p-8">
        <h1 className="text-xl font-semibold mb-6 text-center text-gray-800 dark:text-white">
          Buscar o crear usuario
        </h1>

        <input
          type="text"
          value={cedula}
          onChange={(e) => setCedula(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && handleSearch()} // ✅ ahora en el input
          placeholder="Ingrese número de cédula"
          className="w-full px-4 py-3 border rounded-lg focus:ring focus:ring-blue-500 dark:bg-gray-800 dark:text-white"
        />

        {error && (
          <p className="text-sm text-red-500 mt-2 text-center">{error}</p>
        )}

        <button
          onClick={handleSearch}
          disabled={loading}
          className="mt-4 w-full bg-blue-600 text-white py-3 rounded-lg hover:bg-blue-700 transition disabled:opacity-50"
        >
          {loading ? "Buscando..." : "Continuar"}
        </button>
      </div>
    </div>
  );
}
