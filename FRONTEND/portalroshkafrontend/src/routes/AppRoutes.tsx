// src/routes/AppRoutes.tsx
import { Routes, Route, Navigate } from "react-router-dom";
import ProtectedRoute from "../components/ProtectedRoute";
import DashboardLayout from "../layouts/DashboardLayout";

// Pages públicas
import LoginPage from "../pages/LoginPage";
// import ChangePasswordPage from "../pages/ChangePasswordPage";

// Pages privadas (home, perfil, etc.)
import HomePage from "../pages/HomePage";
import ProfilePage from "../pages/ProfilePage";
import Configuration from "../pages/Configuration";

// Solicitudes (genéricas)
import RequestPage from "../pages/solicitudes/RequestPage";
import RequestFormPage from "../pages/solicitudes/RequestFormPage";
import RequestManagementPage from "../pages/solicitudes/RequestManagementPage";
import RequestSearchPage from "../pages/solicitudes/RequestSearchPage";

// Usuarios
import UsuariosPage from "../pages/user/UserPage";
import UserFormPage from "../pages/user/UserFormPage";
import UserSearchPage from "../pages/user/UserSearchPage";

// Vacaciones
import VacacionesPage from "../pages/solicitudes/VacacionesPage";
import SolicitudVacacionesPage from "../pages/solicitudes/SolicitudVacacionesPage";

// Beneficios
import BeneficiosPage from "../pages/solicitudes/BenefitsPage";
import BeneficioFormPage from "../pages/solicitudes/BenefitsFormPage"; 

// Dispositivos
import DevicePage from "../pages/dispositivos/DevicePage";
import DeviceFormPage from "../pages/dispositivos/DeviceFormPage";
import DeviceAssignmentFormPage from "../pages/dispositivos/DeviceAssignmentFormPage";
import SolicitudDispositivoPage from "../pages/dispositivos/SolicitudDispositivoPage";
import GestionDispositivosPage from "../pages/dispositivos/GestionDispositivosPage";
import TipoDispositivoPage from "../pages/dispositivos/TipoDispositivoPage";
import UbicacionPage from "../pages/varios/UbicacionPage";
import ClientesPage from "../pages/varios/ClientesPage";
import CargosPage from "../pages/varios/CargosPage";
import RolesPage from "../pages/varios/RolesPage";
import GestionTHPage from "../pages/varios/CatalogoTHPage";
import CatalogoOperacionesPage from "../pages/varios/CatalogoSysPage";
import CatalogoOpPage from "../pages/varios/CatalogoOp";

export default function AppRoutes() {
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

          {/* Solicitud de Dispositivos (usuarios) */}
          <Route path="/solicitud-dispositivo" element={<SolicitudDispositivoPage />} />

          {/* Gestión de Dispositivos (SYSADMIN) */}
          <Route path="/gestion-dispositivos" element={<GestionDispositivosPage />} />

          {/* Asignaciones de Dispositivos */}
          <Route path="/dispositivos-asignados/nuevo" element={<DeviceAssignmentFormPage />} />
          <Route path="/dispositivos-asignados/:id" element={<DeviceAssignmentFormPage />} />

          {/* Tipos de Dispositivo */}
          <Route path="/tipo-dispositivo" element={<TipoDispositivoPage />} />

          {/* Clientes */}
          <Route path="/clientes" element={<ClientesPage />} />

          {/* Cargos */}
          <Route path="/cargos" element={<CargosPage />} />

          {/* Roles */}
          <Route path="/roles" element={<RolesPage />} />
          
          {/* GestionTH */}
          <Route path="/catalogo-th" element={<GestionTHPage />} />

          {/* Ubicación */}
          <Route path="/ubicacion" element={<UbicacionPage />} />

          {/* CatalogoSysAdmin */}
          <Route path="/catalogo-sys" element={< CatalogoOperacionesPage />} />

          {/* CatalogoOperaciones */}
           <Route path="/catalogo-op" element={< CatalogoOpPage />} />

          {/* Configuración */}
          <Route path="/configuracion" element={<Configuration />} />

          {/* Perfil */}
          <Route path="/profile" element={<ProfilePage />} />
        </Route>
      </Route>

      {/* Fallback simple para evitar loops */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
