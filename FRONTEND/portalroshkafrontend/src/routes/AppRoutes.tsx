import { Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import ProtectedRoute from "../components/ProtectedRoute";
import DashboardLayout from "../layouts/DashboardLayout";

// Pages
import LoginPage from "../pages/LoginPage";
import HomePage from "../pages/HomePage";
import ProfilePage from "../pages/ProfilePage";
import BeneficiosPage from "../pages/BenefitsPage";
import UsuariosPage from "../pages/UserPage.tsx";
import VacacionesPage from "../pages/VacacionesPage";
import RequestPage from "../pages/RequestPage";
import Configuration from "../pages/Configuration.tsx";
import UserFormPage from "../pages/UserFormPage";
import UserSearchPage from "../pages/UserSearchPage.tsx";
import RequestFormPage from "../pages/RequestFormPage"
import SolicitudVacacionesPage from "../pages/SolicitudVacacionesPage";
import BeneficioFormPage from "../pages/BenefitsFormPage.tsx";
import RequestManagementPage from "../pages/RequestManagementPage.tsx";
import RequestSearchPage from "../pages/RequestSearchPage.tsx";


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
              <Route path="/usuarios/buscar" element={<UserSearchPage />} />
              <Route path="/usuarios/nuevo" element={<UserFormPage />} />
              <Route path="/usuarios/:id" element={<UserFormPage />} />
              <Route path="/vacaciones" element={<VacacionesPage />} />
              <Route path="/solicitud-vacaciones" element={<SolicitudVacacionesPage />} />
              <Route path="/benefits" element={<BeneficiosPage />} />
              <Route path="/configuracion" element={<Configuration />} />
              <Route path="/requests/nuevo" element={<RequestFormPage />} />
              <Route path="/requests/:id" element={<RequestFormPage />} />
              <Route path="/beneficios/nuevo" element={<BeneficioFormPage />} />
              <Route path="/seleccion-solicitudesTH" element={<RequestSearchPage/>} />
              <Route path="/solicitudesTH/permisos" element={<RequestManagementPage tipoVista="permisos"/>} />
              <Route path="/solicitudesTH/beneficios" element={<RequestManagementPage tipoVista="beneficios"/>} />
          {/*<Route path="/usuarios" element={<UsuariosPage />} />

          < */}
        </Route>
      </Route>

      {/* Fallback */}
      <Route path="*" element={user ? <Navigate to="/" /> : <LoginPage />} /> 
    </Routes>
  );
}
