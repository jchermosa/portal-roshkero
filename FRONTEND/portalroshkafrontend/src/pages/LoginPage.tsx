import { useState } from "react";
import type { FormEvent } from "react";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import logo from "../assets/logo.jpg";
import heroBg from "../assets/ilustracion-herov3.svg";


export default function Login() {
  const { login } = useAuth();
  const nav = useNavigate();

  const [correo, setCorreo] = useState("");
  const [contrasena, setContrasena] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      await login({ correo, contrasena });
      nav("/");
    } catch (err: any) {
      setError(err?.response?.data?.message ?? "Credenciales inválidas");
    } finally {
      setLoading(false);
    }
  };

    return (
    <div className="relative min-h-screen flex items-center justify-center overflow-hidden">
      {/* Fondo azul con imagen */}
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

      {/* Contenedor centrado */}
      <div className="relative z-10 w-full max-w-md bg-white/95 backdrop-blur-sm shadow-2xl rounded-2xl p-10">
        <div className="text-center mb-8">
          {/* <div className="inline-block bg-brand-blue text-white px-6 py-2 rounded-full text-sm font-medium mb-6">
            Bienvenido al Sistema
          </div> */}

          <img
            src={logo}
            alt="Logo Roshka"
            className="h-32 w-auto mx-auto mb-6 object-cover rounded-full shadow-md"
            />

          <h1 className="text-2xl font-semibold text-brand-blue">
            INICIAR SESIÓN
          </h1>
        </div>

        <form onSubmit={onSubmit} className="space-y-6">
          <div className="relative">
            <div className="absolute left-5 top-1/2 -translate-y-1/2 text-blue-400">
              <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 20 20">
                <path d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" />
              </svg>
            </div>
            <input
              type="email"
              value={correo}
              onChange={(e) => setCorreo(e.target.value)}
              required
              placeholder="Correo electrónico"
              className="w-full bg-gradient-to-r from-blue-50 to-blue-100 border-0 rounded-full px-14 py-4 text-blue-900 placeholder-blue-400 focus:outline-none focus:ring-2 focus:ring-blue-500 shadow-inner text-lg"
            />
          </div>

          <div className="relative">
            <div className="absolute left-5 top-1/2 -translate-y-1/2 text-blue-400">
              <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 20 20">
                <path
                  fillRule="evenodd"
                  d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3 3 0 016 0z"
                  clipRule="evenodd"
                />
              </svg>
            </div>
            <input
              type="password"
              value={contrasena}
              onChange={(e) => setContrasena(e.target.value)}
              required
              placeholder="Contraseña"
              className="w-full bg-gradient-to-r from-blue-50 to-blue-100 border-0 rounded-full px-14 py-4 text-blue-900 placeholder-blue-400 focus:outline-none focus:ring-2 focus:ring-blue-500 shadow-inner text-lg"
            />
          </div>

          <div className="text-center">
            <button
              type="button"
              className="text-sm text-blue-600 hover:text-blue-800 font-medium"
            >
              ¿Olvidaste tu contraseña?
            </button>
          </div>

          {error && (
            <div className="text-sm text-red-600 bg-red-50 p-3 rounded-xl text-center border border-red-100">
              {error}
            </div>
          )}

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-gradient-to-r from-yellow-400 to-yellow-500 text-blue-900 font-bold py-4 rounded-full hover:from-yellow-500 hover:to-yellow-600 transition-all duration-200 shadow-lg disabled:opacity-60 disabled:cursor-not-allowed text-lg"
          >
            {loading ? (
              <div className="flex items-center justify-center">
                <div className="animate-spin rounded-full h-5 w-5 border-2 border-blue-900 border-t-transparent mr-2"></div>
                Ingresando...
              </div>
            ) : (
              "INGRESAR"
            )}
          </button>
        </form>

        <div className="mt-8 text-center">
          <p className="text-sm text-gray-600">
            ¿No tienes cuenta?{" "}
            <button
              className="font-medium text-blue-700 hover:text-blue-900"
              onClick={() => nav("/register")}
            >
              Crear cuenta
            </button>
          </p>
        </div>

        <div className="mt-8 text-center">
          <div className="flex items-center justify-center space-x-2 text-xs text-gray-500">
            <div className="w-2 h-2 bg-blue-500 rounded-full"></div>
            <div className="w-2 h-2 bg-yellow-400 rounded-full"></div>
          </div>
        </div>
      </div>
    </div>
  );
}