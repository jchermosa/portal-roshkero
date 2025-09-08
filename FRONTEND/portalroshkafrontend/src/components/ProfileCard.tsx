import React from "react";

interface UserRole {
  nombre: string;
}

interface User {
  rol?: UserRole;
}

interface ProfileCardProps {
  user?: User;
  setActiveSection: (section: string) => void;
}

const ProfileCard: React.FC<ProfileCardProps> = ({ user, setActiveSection }) => {
  return (
    <div className="bg-white p-6 rounded-xl shadow-md border-l-4 border-purple-500 hover:shadow-lg transition-shadow">
      <div className="flex items-center mb-3">
        <span className="text-2xl mr-3">👤</span>
        <h3 className="text-lg font-semibold text-gray-800">Mi Perfil</h3>
      </div>
      <p className="text-gray-600 text-sm mb-4">
        Rol: <span className="font-semibold">{user?.rol?.nombre}</span>
      </p>
      <button
        onClick={() => setActiveSection("perfil")}
        className="w-full bg-purple-500 text-white py-2 px-4 rounded-lg hover:bg-purple-600 transition-colors"
      >
        Ver perfil
      </button>
    </div>
  );
};

export default ProfileCard;
