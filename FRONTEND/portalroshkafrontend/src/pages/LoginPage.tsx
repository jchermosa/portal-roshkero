// src/pages/Login.tsx
import { useState, useEffect } from "react";
import type { FormEvent } from "react";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import logo from "../assets/logo.jpg";
import heroBg from "../assets/ilustracion-herov3.svg";
import LoadingButton from "../components/LoadingButton";

export default function Login() {
  const { login, user } = useAuth();
  const nav = useNavigate();

  const [correo, setCorreo] = useState("");
  const [contrasena, setContrasena] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  // Dark mode toggle
  const [darkMode, setDarkMode] = useState(
    () =>
      document.documentElement.classList.contains("dark") ||
      localStorage.getItem("theme") === "dark"
  );

  useEffect(() => {
    if (darkMode) {
      document.documentElement.classList.add("dark");
      localStorage.setItem("theme", "dark");
    } else {
      document.documentElement.classList.remove("dark");
      localStorage.setItem("theme", "light");
    }
  }, [darkMode]);

  useEffect(() => {
    if (user) {
      nav("/");
    }
  }, [user]);

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const res = await fetch(`${import.meta.env.VITE_API_URL}/api/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ correo, contrasena }),
      });

      if (!res.ok) {
        const errorText = await res.text();
        throw new Error(errorText);
      }

      const { token } = await res.json();
      login(token);
    } catch (err: any) {
      setError(err?.message ?? "Credenciales inválidas");
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

      {/* Toggle dark mode con íconos */}
        <button
          onClick={() => setDarkMode((prev) => !prev)}
          type="button"
          className="absolute top-4 right-4 z-20 text-2xl transition-transform transform hover:scale-110"
        >
          {darkMode ? "🌙" : "🌞"}
        </button>


      {/* Contenedor */}
      <div className="relative z-10 w-full max-w-md bg-white/95 dark:bg-gray-900/90 backdrop-blur-sm shadow-2xl rounded-2xl p-10">
       

        {/* Logo + título */}
        <div className="text-center mb-8">
          <img
            src={logo}
            alt="Logo Roshka"
            className="h-32 w-auto mx-auto mb-6 object-cover rounded-full shadow-md"
          />
          <h1 className="text-2xl font-semibold text-brand-blue dark:text-white">
            INICIAR SESIÓN
          </h1>
        </div>

        {/* Formulario */}
        <form onSubmit={onSubmit} className="space-y-6">
          {/* Input correo */}
          <div className="relative">
            <div className="absolute left-5 top-1/2 -translate-y-1/2 text-blue-400 dark:text-gray-400">
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
              className="w-full border-0 rounded-full px-14 py-4 text-blue-900 dark:text-gray-100 placeholder-blue-400 dark:placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500 shadow-inner text-lg
                         bg-gradient-to-r from-blue-50 to-blue-100 dark:from-gray-700 dark:to-gray-800"
            />
          </div>

          {/* Input contraseña */}
          <div className="relative">
            <div className="absolute left-5 top-1/2 -translate-y-1/2 text-blue-400 dark:text-gray-400">
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
              className="w-full border-0 rounded-full px-14 py-4 text-blue-900 dark:text-gray-100 placeholder-blue-400 dark:placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500 shadow-inner text-lg
                         bg-gradient-to-r from-blue-50 to-blue-100 dark:from-gray-700 dark:to-gray-800"
            />
          </div>

          {/* Link olvidar contraseña */}
          <div className="text-center">
            <button
              type="button"
              className="text-sm text-blue-600 dark:text-blue-400 hover:text-blue-800 dark:hover:text-blue-300 font-medium"
            >
              ¿Olvidaste tu contraseña?
            </button>
          </div>

          {/* Error */}
          {error && (
            <div className="text-sm text-red-600 dark:text-red-400 bg-red-50 dark:bg-red-900/40 p-3 rounded-xl text-center border border-red-100 dark:border-red-700">
              {error}
            </div>
          )}

          {/* Botón ingresar */}
          <LoadingButton
            type="submit"
            loading={loading}
            text="INGRESAR"
            className="bg-blue-600 hover:bg-blue-700 text-white dark:bg-blue-600 dark:hover:bg-blue-700"
          />
        </form>

        {/* Footer */}
        <div className="mt-8 text-center">
          <div className="flex items-center justify-center space-x-2 text-xs text-gray-500 dark:text-gray-400">
            <div className="w-2 h-2 bg-blue-500 rounded-full"></div>
            <div className="w-2 h-2 bg-yellow-400 rounded-full"></div>
          </div>
        </div>
      </div>
    </div>
  );
}
