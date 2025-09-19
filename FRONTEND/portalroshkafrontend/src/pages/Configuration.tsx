// src/pages/Configuration.tsx
import { useEffect, useState } from "react";

export default function Configuration() {
  // Initial state: check <html> or localStorage
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

  return (
    <div className="h-full flex flex-col overflow-hidden">
      {/* Background illustration */}
      <div
        className="absolute inset-0 bg-brand-blue"
        style={{
          backgroundImage: "url('/src/assets/ilustracion-herov3.svg')",
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
        }}
      >
        <div className="absolute inset-0 bg-brand-blue/40" />
      </div>

      {/* Main content */}
      <div className="relative z-10 flex flex-col h-full p-4">
        <div className="bg-white/45 dark:bg-gray-900/70 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col h-full overflow-hidden">
          {/* Header */}
          <div className="p-6 border-b border-gray-200 dark:border-gray-700 flex-shrink-0">
            <h2 className="text-2xl font-bold text-brand-blue dark:text-white">
              Configuration
            </h2>
          </div>

          {/* Content */}
          <div className="flex-1 overflow-auto p-6">
            <div className="flex items-center gap-4">
              <label
                htmlFor="darkModeToggle"
                className="text-gray-800 dark:text-gray-100 font-medium"
              >
                Dark mode
              </label>
              <button
                id="darkModeToggle"
                onClick={() => setDarkMode((prev) => !prev)}
                className={`px-4 py-2 rounded-lg transition ${
                  darkMode
                    ? "bg-blue-600 text-white hover:bg-blue-700"
                    : "bg-gray-200 text-gray-900 hover:bg-gray-300"
                }`}
              >
                {darkMode ? "On" : "Off"}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}