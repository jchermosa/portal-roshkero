// src/pages/ChangePasswordPage.tsx
import { useState } from "react";
import { useAuth } from "../context/AuthContext";
import heroBg from "../assets/ilustracion-herov3.svg";
import { updatePassword } from "../services/AuthService"; 
import { updatePasswordMock } from "../services/AuthService";

const useMock = import.meta.env.VITE_USE_MOCK === "true";

export default function ChangePasswordPage() {
  const { token, logout } = useAuth();
  const [password, setPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  const handleSubmit = async () => {
    if (!password || !confirm) {
      setError("Debe completar ambos campos");
      return;
    }
    if (password !== confirm) {
      setError("Las contraseñas no coinciden");
      return;
    }

    setError(null);
    setLoading(true);

    try {
      if (!token) throw new Error("Token inválido");
      await updatePassword(token, password);

      setSuccess(true);
      setTimeout(() => {
        logout(); // 👈 obligamos a re-loguearse con la nueva clave
      }, 2000);
    } catch (err: any) {
      setError(err.message || "Error al actualizar contraseña");
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
        <div className="absolute inset-0 bg-brand-blue/40"></div>
      </div>

      {/* Card central */}
      <div className="relative z-10 w-full max-w-md bg-white/90 dark:bg-gray-900/90 backdrop-blur-sm shadow-2xl rounded-2xl p-8">
        <h1 className="text-xl font-semibold mb-6 text-center text-gray-800 dark:text-white">
          Cambio de contraseña
        </h1>

        {success ? (
          <p className="text-green-600 text-center font-medium">
            ✅ Contraseña cambiada con éxito. Redirigiendo...
          </p>
        ) : (
          <>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Nueva contraseña"
              className="w-full px-4 py-3 border rounded-lg mb-3 focus:ring focus:ring-blue-500 dark:bg-gray-800 dark:text-white"
            />

            <input
              type="password"
              value={confirm}
              onChange={(e) => setConfirm(e.target.value)}
              placeholder="Confirmar contraseña"
              className="w-full px-4 py-3 border rounded-lg mb-3 focus:ring focus:ring-blue-500 dark:bg-gray-800 dark:text-white"
            />

            {error && (
              <p className="text-sm text-red-500 mt-2 text-center">{error}</p>
            )}

            <button
              onClick={handleSubmit}
              disabled={loading}
              className="mt-4 w-full bg-blue-600 text-white py-3 rounded-lg hover:bg-blue-700 transition disabled:opacity-50"
            >
              {loading ? "Guardando..." : "Guardar contraseña"}
            </button>
          </>
        )}
      </div>
    </div>
  );
}
