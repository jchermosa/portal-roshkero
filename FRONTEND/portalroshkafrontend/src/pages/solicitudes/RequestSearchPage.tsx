import { useNavigate } from "react-router-dom";
import heroBg from "../../assets/ilustracion-herov3.svg";

export default function RequestSearchPage() {
  const navigate = useNavigate();

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
        <div className="absolute inset-0 bg-brand-blue/40"></div>
      </div>

      {/* Card central */}
      <div className="relative z-10 w-full max-w-md bg-white/90 dark:bg-gray-900/90 backdrop-blur-sm shadow-2xl rounded-2xl p-8">
        <h1 className="text-xl font-semibold mb-6 text-center text-gray-800 dark:text-white">
          Seleccione el tipo de solicitudes
        </h1>

        <div className="flex flex-col gap-4">
          <button
            onClick={() => navigate("/solicitudesTH/permisos")}
            className="w-full bg-blue-700 text-white py-3 rounded-lg hover:bg-blue-800 transition"
          >
            Ver Permisos
          </button>

          <button
            onClick={() => navigate("/solicitudesTH/beneficios")}
            className="w-full bg-sky-500 text-white py-3 rounded-lg hover:bg-sky-600 transition"
          >
            Ver Beneficios
          </button>

          <button
            onClick={() => navigate("/solicitudesTH/vacaciones")}
            className="w-full bg-amber-500 text-white py-3 rounded-lg hover:bg-amber-600 transition"
          >
            Ver Vacaciones
          </button>
        </div>
      </div>
    </div>
  );
}
