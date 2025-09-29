// src/routes/AppRoutes.tsx
import { Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import ProtectedRoute from "../components/ProtectedRoute";
import DashboardLayout from "../layouts/DashboardLayout";

// Pages
import LoginPage from "../pages/LoginPage";
import HomePage from "../pages/HomePage";
import ProfilePage from "../pages/ProfilePage";
import BeneficiosPage from "../pages/solicitudes/BenefitsPage";
import UsuariosPage from "../pages/user/UserPage.tsx";
import VacacionesPage from "../pages/solicitudes/VacacionesPage";
import RequestPage from "../pages/solicitudes/RequestPage";
import Configuration from "../pages/Configuration.tsx";
import UserFormPage from "../pages/user/UserFormPage.tsx";
import UserSearchPage from "../pages/user/UserSearchPage.tsx";
import RequestFormPage from "../pages/solicitudes/RequestFormPage";
import SolicitudVacacionesPage from "../pages/solicitudes/SolicitudVacacionesPage";
import BeneficioFormPage from "../pages/solicitudes/BenefitsFormPage.tsx";
import RequestManagementPage from "../pages/solicitudes/RequestManagementPage.tsx";
import RequestSearchPage from "../pages/solicitudes/RequestSearchPage.tsx";
import ChangePasswordPage from "../pages/ChangePasswordPage.tsx";

// Dispositivos
import DevicePage from "../pages/dispositivos/DevicePage.tsx";
import DeviceFormPage from "../pages/dispositivos/DeviceFormPage.tsx";
import DeviceAssignmentFormPage from "../pages/dispositivos/DeviceAssignmentFormPage.tsx";
import SolicitudDispositivoPage from "../pages/dispositivos/SolicitudDispositivoPage.tsx";
import TipoDispositivoPage from "../pages/dispositivos/TipoDispositivoPage.tsx";
import GestionDispositivosPage from "../pages/dispositivos/GestionDispositivosPage.tsx";
import UbicacionPage from "../pages/varios/UbicacionPage.tsx";

export default function AppRoutes() {
  const { user } = useAuth();

  return (
    <Routes>
      {/* Rutas públicas */}
      <Route path="/login" element={<LoginPage />} />
      {/* <Route path="/cambiar-contraseña" element={<ChangePasswordPage />} /> */}

      {/* Rutas privadas */}
      <Route element={<ProtectedRoute />}>
        <Route element={<DashboardLayout />}>
          <Route index element={<HomePage />} />

          {/* Solicitudes generales */}
          <Route path="/requests" element={<RequestPage />} />
          <Route path="/requests/nuevo" element={<RequestFormPage />} />
          <Route path="/requests/:id" element={<RequestFormPage />} />
          <Route path="/seleccion-solicitudesTH" element={<RequestSearchPage />} />
          <Route
            path="/solicitudesTH/permisos"
            element={<RequestManagementPage tipoVista="permisos" />}
          />
          <Route
            path="/solicitudesTH/beneficios"
            element={<RequestManagementPage tipoVista="beneficios" />}
          />

          {/* Usuarios */}
          <Route path="/usuarios" element={<UsuariosPage />} />
          <Route path="/usuarios/buscar" element={<UserSearchPage />} />
          <Route path="/usuarios/nuevo" element={<UserFormPage />} />
          <Route path="/usuarios/:id" element={<UserFormPage />} />

          {/* Vacaciones */}
          <Route path="/vacaciones" element={<VacacionesPage />} />
          <Route path="/solicitud-vacaciones" element={<SolicitudVacacionesPage />} />

          {/* Beneficios */}
          <Route path="/benefits" element={<BeneficiosPage />} />
          <Route path="/beneficios/nuevo" element={<BeneficioFormPage />} />

          {/* Dispositivos */}
          <Route path="/dispositivos" element={<DevicePage />} />
          <Route path="/dispositivos/nuevo" element={<DeviceFormPage />} />
          <Route path="/dispositivos/:id" element={<DeviceFormPage />} />

          {/* Solicitud de Dispositivos (usuarios normales) */}
          <Route path="/solicitud-dispositivo" element={<SolicitudDispositivoPage />} />

          {/* Gestión de Dispositivos (SYSADMIN → Tabs: Solicitudes + Asignaciones) */}
          <Route path="/gestion-dispositivos" element={<GestionDispositivosPage />} />

          {/* Asignaciones (formularios) */}
          <Route path="/dispositivos-asignados/nuevo" element={<DeviceAssignmentFormPage />} />
          <Route path="/dispositivos-asignados/:id" element={<DeviceAssignmentFormPage />} />

          {/* Tipos de dispositivo */}
          <Route path="/tipo-dispositivo" element={<TipoDispositivoPage />} />

          {/* Ubicación */}
          <Route path="/ubicacion" element={<UbicacionPage />} />

          {/* Configuración */}
          <Route path="/configuracion" element={<Configuration />} />
          <Route path="/gestion-solicitudes" element={<RequestManagementPage />} />

          {/* Perfil */}
          <Route path="/profile" element={<ProfilePage />} />
        </Route>
      </Route>

      {/* Fallback */}
      <Route
        path="*"
        element={user ? <Navigate to="/" replace /> : <LoginPage />}
      />
    </Routes>
  );
}
