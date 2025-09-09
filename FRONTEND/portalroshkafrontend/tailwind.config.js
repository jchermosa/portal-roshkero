/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          blue: "#003366",      // Color principal azul oscuro
          lightblue: "#007BFF", // Azul claro para acentos
          yellow: "#FFC107",    // Amarillo corporativo
        },
      },
    },
  },
  plugins: [],
}