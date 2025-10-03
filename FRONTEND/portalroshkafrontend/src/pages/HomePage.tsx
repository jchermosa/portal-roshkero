import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import ProfileCard from "../components/ProfileCard";

export default function HomePage() {
  const { user } = useAuth();
  const navigate = useNavigate();

  if (!user) return <p>Cargando...</p>; 

  return (
    <div className="h-full flex flex-col overflow-hidden">
      {/* Fondo */}
      <div
        className="absolute inset-0 bg-brand-blue"
        style={{
          backgroundImage: "url('/src/assets/ilustracion-herov3.svg')",
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
        }}
      >
        <div className="absolute inset-0 bg-brand-blue/40"></div>
      </div>

      {/* Contenedor */}
      <div className="relative z-10 flex flex-col h-full p-4">
        <div className="bg-white/60 dark:bg-gray-900/70 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col h-full overflow-hidden">
          {/* Header */}
          <div className="p-6 border-b border-gray-200 dark:border-gray-700 flex-shrink-0">
            <h2 className="text-2xl font-bold text-brand-blue dark:text-white mb-1">
              ¡Bienvenido de nuevo! 👋
            </h2>
            <p className="text-gray-700 dark:text-gray-200 text-lg">
              {user?.nombre} {user?.apellido}
            </p>
            <p className="text-gray-500 dark:text-gray-400 text-sm mt-1">
              {user?.correo}
            </p>
          </div>

          {/* Cards */}
          <div className="flex-1 overflow-auto p-6">
            <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
              <ProfileCard
                title="Mi Perfil"
                icon="👤"
                description="Visualiza tu información personal"
                onClick={() => navigate("/profile")}
                borderClass="border-blue-600"
                buttonClass="bg-blue-600 hover:bg-blue-700"
                color=""
              />
             

              <ProfileCard
                title="Solicitudes"
                icon="📝"
                description="Crea y revisa tus solicitudes"
                onClick={() => navigate("/requests")}
                borderClass="border-blue-600"
                buttonClass="bg-blue-600 hover:bg-blue-700"
                color=""
              />

              <ProfileCard
                title="Solicitar Dispositivos"
                icon="📱"
                description="Crea y revisa tus peticiones de dispositivos"
                onClick={() => navigate("/requests")}
                borderClass="border-blue-600"
                buttonClass="bg-blue-600 hover:bg-blue-700"
                color=""
              />
               <ProfileCard
                title="Beneficios"
                icon="🎁"
                description="Explora y accede a tus beneficios"
                onClick={() => navigate("/benefits")}
                borderClass="border-blue-600"
                buttonClass="bg-blue-600 hover:bg-blue-700"
                color=""
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
