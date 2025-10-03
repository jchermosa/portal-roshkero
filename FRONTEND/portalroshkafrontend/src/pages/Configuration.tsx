import { useState, useEffect } from "react";

// Iconos SVG nativos
const EyeIcon = () => (
  <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
  </svg>
);

const EyeOffIcon = () => (
  <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21" />
  </svg>
);

const CheckIcon = () => (
  <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
  </svg>
);

const XIcon = () => (
  <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
  </svg>
);

const LockIcon = () => (
  <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
  </svg>
);

const MoonIcon = () => (
  <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z" />
  </svg>
);

interface PasswordFieldProps {
  label: string;
  onSave: (currentPassword: string, newPassword: string) => Promise<void>;
}

function PasswordField({ label, onSave }: PasswordFieldProps) {
  const [isEditing, setIsEditing] = useState(false);
  const [currentPassword, setCurrentPassword] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [showCurrent, setShowCurrent] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleEdit = () => {
    setIsEditing(true);
    setCurrentPassword("");
    setPassword("");
    setConfirmPassword("");
    setError("");
  };

  const handleCancel = () => {
    setIsEditing(false);
    setCurrentPassword("");
    setPassword("");
    setConfirmPassword("");
    setError("");
    setShowCurrent(false);
    setShowPassword(false);
    setShowConfirm(false);
  };

  const handleSave = async () => {
    if (!currentPassword) {
      setError("Ingresa tu contraseña actual");
      return;
    }

    if (password.length < 6) {
      setError("La nueva contraseña debe tener al menos 6 caracteres");
      return;
    }

    if (password !== confirmPassword) {
      setError("Las contraseñas no coinciden");
      return;
    }

    setLoading(true);
    setError("");

    try {
      await onSave(currentPassword, password);
      setIsEditing(false);
      setCurrentPassword("");
      setPassword("");
      setConfirmPassword("");
      setShowCurrent(false);
      setShowPassword(false);
      setShowConfirm(false);
    } catch (err) {
      setError((err as Error).message || "Error al actualizar contraseña");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-3">
      <label className="text-gray-800 dark:text-gray-100 font-medium flex items-center gap-2">
        <LockIcon />
        {label}
      </label>

      {!isEditing ? (
        <div className="flex items-center gap-3">
          <div className="flex-1 px-4 py-2.5 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-lg">
            <span className="text-gray-400 dark:text-gray-500">••••••••</span>
          </div>
          <button
            onClick={handleEdit}
            className="px-4 py-2.5 bg-blue-600 hover:bg-blue-700 text-white rounded-lg transition font-medium"
          >
            Editar
          </button>
        </div>
      ) : (
        <div className="space-y-3">
          <div className="relative">
            <input
              type={showCurrent ? "text" : "password"}
              value={currentPassword}
              onChange={(e) => setCurrentPassword(e.target.value)}
              placeholder="Contraseña actual"
              className="w-full px-4 py-2.5 pr-12 bg-white dark:bg-gray-800 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-600 focus:border-transparent transition text-gray-900 dark:text-white placeholder-gray-400"
            />
            <button
              type="button"
              onClick={() => setShowCurrent(!showCurrent)}
              className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200"
            >
              {showCurrent ? <EyeOffIcon /> : <EyeIcon />}
            </button>
          </div>

          <div className="relative">
            <input
              type={showPassword ? "text" : "password"}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Nueva contraseña"
              className="w-full px-4 py-2.5 pr-12 bg-white dark:bg-gray-800 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-600 focus:border-transparent transition text-gray-900 dark:text-white placeholder-gray-400"
            />
            <button
              type="button"
              onClick={() => setShowPassword(!showPassword)}
              className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200"
            >
              {showPassword ? <EyeOffIcon /> : <EyeIcon />}
            </button>
          </div>

          <div className="relative">
            <input
              type={showConfirm ? "text" : "password"}
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              placeholder="Confirmar contraseña"
              className="w-full px-4 py-2.5 pr-12 bg-white dark:bg-gray-800 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-600 focus:border-transparent transition text-gray-900 dark:text-white placeholder-gray-400"
            />
            <button
              type="button"
              onClick={() => setShowConfirm(!showConfirm)}
              className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200"
            >
              {showConfirm ? <EyeOffIcon /> : <EyeIcon />}
            </button>
          </div>

          {error && (
            <div className="text-red-500 dark:text-red-400 text-sm font-medium">
              {error}
            </div>
          )}

          <div className="flex gap-2">
            <button
              onClick={handleSave}
              disabled={loading}
              className="flex-1 px-4 py-2.5 bg-green-600 hover:bg-green-700 disabled:bg-gray-400 text-white rounded-lg transition font-medium flex items-center justify-center gap-2"
            >
              {loading ? (
                <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin" />
              ) : (
                <>
                  <CheckIcon />
                  Guardar
                </>
              )}
            </button>
            <button
              onClick={handleCancel}
              disabled={loading}
              className="flex-1 px-4 py-2.5 bg-gray-300 hover:bg-gray-400 dark:bg-gray-700 dark:hover:bg-gray-600 text-gray-800 dark:text-white rounded-lg transition font-medium flex items-center justify-center gap-2"
            >
              <XIcon />
              Cancelar
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default function Configuration() {
  const [darkMode, setDarkMode] = useState(true);

  useEffect(() => {
    if (darkMode) {
      document.documentElement.classList.add("dark");
    } else {
      document.documentElement.classList.remove("dark");
    }
  }, [darkMode]);

  // Función para cambiar contraseña con Spring Boot
  const handlePasswordChange = async (currentPassword: string, newPassword: string): Promise<void> => {
    const API_URL = "http://localhost:8080/api/v1/usuarios/cambiarcontrasena";

    try {
      const response = await fetch(API_URL, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
           //Si usas JWT, descomenta y ajusta esta línea:
           Authorization: `Bearer ${localStorage.getItem("auth_token")}`,
        },
        body: JSON.stringify({
          contrasenaActual: currentPassword,
          nuevaContrasena: newPassword,
        }),
      });

      const data = await response.json();


      if (!response.ok) {
        throw new Error(data.message || "Error al cambiar contraseña");
      }

      // Opcional: Mostrar mensaje de éxito
      console.log("Contraseña actualizada exitosamente");
    } catch (error) {
      console.error("Error en cambio de contraseña:", error);
      throw error;
    }
  };

  return (
    <div className="h-full flex flex-col overflow-hidden">
      {/* Fondo */}
      <div
        className="absolute inset-0 bg-blue-600"
        style={{
          backgroundImage: "url('/src/assets/ilustracion-herov3.svg')",
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
        }}
      >
        <div className="absolute inset-0 bg-blue-600/40"></div>
      </div>

      {/* Main content */}
      <div className="relative z-10 flex flex-col h-full p-6">
        <div className="bg-white/95 dark:bg-gray-900/95 backdrop-blur-md rounded-2xl shadow-2xl flex flex-col h-full overflow-hidden border border-gray-200 dark:border-gray-800">
          {/* Header */}
          <div className="p-6 border-b border-gray-200 dark:border-gray-700 flex-shrink-0">
            <h2 className="text-3xl font-bold text-gray-900 dark:text-white">
              Configuración
            </h2>
          </div>

          {/* Content */}
          <div className="flex-1 overflow-auto p-6 space-y-6">
            {/* Password Field */}
            <PasswordField
              label="Contraseña"
              onSave={handlePasswordChange}
            />

            {/* Divider */}
            <div className="border-t border-gray-200 dark:border-gray-700" />

            {/* Dark Mode */}
            <div className="space-y-3">
              <label className="text-gray-800 dark:text-gray-100 font-medium flex items-center gap-2">
                <MoonIcon />
                Modo oscuro
              </label>
              <div className="flex items-center gap-3">
                <button
                  onClick={() => setDarkMode(!darkMode)}
                  className={`relative w-16 h-8 rounded-full transition-colors duration-300 ${darkMode ? "bg-blue-600" : "bg-gray-300"
                    }`}
                >
                  <div
                    className={`absolute top-1 left-1 w-6 h-6 bg-white rounded-full shadow-md transform transition-transform duration-300 ${darkMode ? "translate-x-8" : "translate-x-0"
                      }`}
                  />
                </button>
                <span className="text-sm text-gray-600 dark:text-gray-400 font-medium">
                  {darkMode ? "Activado" : "Desactivado"}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}