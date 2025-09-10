import { Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import ProtectedRoute from "../components/ProtectedRoute";
import DashboardLayout from "../layouts/DashboardLayout";

// Pages
import LoginPage from "../pages/LoginPage";
import HomePage from "../pages/HomePage";
import ProfilePage from "../pages/ProfilePage";
import BeneficiosPage from "../pages/BenefitsPage";

import UsuariosPage from "../pages/UsuariosPage";
import VacacionesPage from "../pages/VacacionesPage";
// import ConfiguracionPage from "../pages/ConfiguracionPage";
import UserFormPage from "../pages/UserFormPage";

export default function AppRoutes() {
  const { user } = useAuth();

  return (
    <Routes>
      {/* Rutas públicas */}
      <Route path="/login" element={<LoginPage />} />

      {/* Rutas privadas dentro del DashboardLayout */}
      <Route element={<ProtectedRoute />}>
        <Route element={<DashboardLayout />}>
              <Route index element={<HomePage />} />
              <Route path="/profile" element={<ProfilePage />} /> 
              <Route path="/usuarios" element={<UsuariosPage />} />
              <Route path="/vacaciones" element={<VacacionesPage />} /> 
              <Route path="/benefits" element={<BeneficiosPage />} />
              <Route path="/usuarios/nuevo" element={<UserFormPage/>}></Route>
          {/*
          <Route path="/reportes" element={<ReportesPage />} />
          <Route path="/configuracion" element={<ConfiguracionPage />} /> */}
        </Route>
      </Route>

      {/* Fallback */}
      <Route path="*" element={user ? <Navigate to="/" /> : <LoginPage />} /> 
    </Routes>
  );
}
