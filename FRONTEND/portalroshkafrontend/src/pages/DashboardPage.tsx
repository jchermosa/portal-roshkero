import { useAuth } from "../context/AuthContext";

export default function Dashboard() {
  const { user } = useAuth();

  if (!user) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-100">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-700 mx-auto mb-4"></div>
          <p className="text-lg text-gray-700">Cargando información del usuario...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="bg-gradient-to-r from-blue-600 to-blue-800 text-white p-8 rounded-2xl">
        <h2 className="text-3xl font-bold mb-2">¡Bienvenido de nuevo! 👋</h2>
        <p className="text-blue-100 text-lg">
          {user?.nombre} {user?.apellido}
        </p>
        <p className="text-blue-200 text-sm mt-1">{user?.correo}</p>
      </div>
    </div>
  );
}
