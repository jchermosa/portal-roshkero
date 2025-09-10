import { Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import ProtectedRoute from "../components/ProtectedRoute";
import DashboardLayout from "../layouts/DashboardLayout";

// Pages
import LoginPage from "../pages/LoginPage";

import HomePage from "../pages/HomePage";
import ProfilePage from "../pages/ProfilePage";

import UsuariosPage from "../pages/UsuariosPage";
// import PerfilPage from "../pages/PerfilPage";
// // si vas creando estas páginas
import VacacionesPage from "../pages/VacacionesPage";
// import VacacionesPage from "../pages/VacacionesPage";
import RequestPage from "../pages/RequestPage";
// import ReportesPage from "../pages/ReportesPage";
// import ConfiguracionPage from "../pages/ConfiguracionPage";

export default function AppRoutes() {
  const { user } = useAuth();

  return (
    <Routes>
      {/* Rutas públicas */}
      <Route path="/login" element={<LoginPage />} />

      {/* Rutas privadas dentro del DashboardLayout */}
      <Route element={<ProtectedRoute />}>
        <Route element={<DashboardLayout />}>

              <Route path="/requests" element={<RequestPage />} />
              <Route index element={<HomePage />} />
              <Route path="/profile" element={<ProfilePage />} /> 
              <Route path="/usuarios" element={<UsuariosPage />} />
              <Route path="/vacaciones" element={<VacacionesPage />} /> 
          {/*<Route path="/usuarios" element={<UsuariosPage />} />
          <Route path="/reportes" element={<ReportesPage />} />
          <Route path="/configuracion" element={<ConfiguracionPage />} /> */}
        </Route>
      </Route>

      {/* Fallback */}
      <Route path="*" element={user ? <Navigate to="/" /> : <LoginPage />} /> 
    </Routes>
  );
}
