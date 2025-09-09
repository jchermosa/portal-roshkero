// src/pages/HomePage.tsx
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import ProfileCard from "../components/ProfileCard";

export default function HomePage() {
  const { user } = useAuth();
  const navigate = useNavigate();

  if (!user) return <p>Cargando...</p>;

  const isThOrGth = user?.rol?.nombre === "TH" || user?.rol?.nombre === "GTH";
  const isFuncionario = [
    "FUNCIONARIO_FABRICA",
    "FUNCIONARIO_TERCERIZADO",
    "LIDER",
    "OPERACIONES",
    "DIRECTORIO",
  ].includes(user?.rol?.nombre || "");

  return (
    <div className="space-y-6">
      {/* Header de bienvenida */}
      <div className="bg-gradient-to-r from-blue-600 to-blue-800 text-white p-8 rounded-2xl">
        <h2 className="text-3xl font-bold mb-2">¡Bienvenido de nuevo! 👋</h2>
        <p className="text-blue-100 text-lg">
          {user?.nombre} {user?.apellido}
        </p>
        <p className="text-blue-200 text-sm mt-1">{user?.correo}</p>
      </div>

      {/* Cards */}
      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
        <ProfileCard
                  title="Mi Perfil"
                  icon="👤"
                  description={`Rol: ${user?.rol?.nombre}`}
                  onClick={() => navigate("/perfil")}
                  borderClass="border-blue-600"
                  buttonClass="bg-blue-600 hover:bg-blue-700" color={""}        />

        <ProfileCard
                  title="Vacaciones"
                  icon="🏖️"
                  description="Consulta tus vacaciones"
                  onClick={() => navigate("/vacaciones")}
                  borderClass="border-blue-600"
                  buttonClass="bg-blue-600 hover:bg-blue-700" color={""}        />

        {isFuncionario && (
          <ProfileCard
                      title="Mis Vacaciones"
                      icon="🏖️"
                      description="Consulta tus vacaciones"
                      extraInfo={<>
                          Disponibles:{" "}
                          <span className="font-semibold text-yellow-600">
                              {user?.dias_vacaciones_restante ?? 0} días
                          </span>
                          <br />
                          Total asignados: {user?.dias_vacaciones ?? 0} días
                      </>}
                      onClick={() => navigate("/vacaciones")}
                      borderClass="border-blue-600"
                      buttonClass="bg-blue-600 hover:bg-blue-700" color={""}         />
        )}

        {isThOrGth && (
          <ProfileCard
                      title="Gestión de Usuarios"
                      icon="👥"
                      description="Administra perfiles y usuarios del sistema"
                      onClick={() => navigate("/usuarios")}
                      borderClass="border-blue-600"
                      buttonClass="bg-blue-600 hover:bg-blue-700" color={""}          />
        )}

        <ProfileCard
                  title="Solicitudes"
                  icon="📝"
                  description="Crea y revisa tus solicitudes"
                  onClick={() => navigate("/solicitudes")}
                  borderClass="border-blue-600"
                  buttonClass="bg-blue-600 hover:bg-blue-700" color={""}        />
        <ProfileCard
                  title="Gestion de Solcitudes"
                  icon="📤"
                  description="Administra las solicitudes de los usuarios"
                  onClick={() => navigate("/usuarios")}
                  borderClass="border-blue-600"
                  buttonClass="bg-blue-600 hover:bg-blue-700" color={""}        />
                  
      </div>
    </div>
  );
}
