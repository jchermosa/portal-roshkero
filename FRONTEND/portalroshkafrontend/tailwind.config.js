/** @type {import('tailwindcss').Config} */
export default {
  darkMode: "class", // se activa con la clase "dark" en <html>
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          blue: "#003366",
          lightblue: "#007BFF",
          yellow: "#FFC107",
        },
      },
    },
  },
  plugins: [],
}
