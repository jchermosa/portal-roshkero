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
import BeneficiosPage from "../pages/solicitudes/BenefitsPage";
import UsuariosPage from "../pages/user/UserPage.tsx";
import Configuration from "../pages/Configuration";

// Solicitudes (genéricas)
import VacacionesPage from "../pages/solicitudes/VacacionesPage";
import RequestPage from "../pages/solicitudes/RequestPage";
import RequestFormPage from "../pages/solicitudes/RequestFormPage";
import BeneficioFormPage from "../pages/solicitudes/BenefitsFormPage.tsx";
import RequestManagementPage from "../pages/solicitudes/RequestManagementPage.tsx";
import RequestSearchPage from "../pages/solicitudes/RequestSearchPage.tsx";
import ChangePasswordPage from "../pages/ChangePasswordPage.tsx";
import DevicePage from "../pages/dispositivos/DevicePage.tsx";
import DeviceFormPage from "../pages/dispositivos/DeviceFormPage.tsx";
import DeviceAssignmentsPage from "../pages/dispositivos/DeviceAssignmentsPage.tsx";
import DeviceAssignmentsFormPage from "../pages/dispositivos/DeviceAssignmentFormPage.tsx";
import SolicitudDispositivoPage from "../pages/dispositivos/SolicitudDispositivoPage.tsx";
import SolicitudDispositivoFormPage from "../pages/dispositivos/SolicitudDispositivoFormPage.tsx";

import UserFormPage from "../pages/user/UserFormPage";
import UserSearchPage from "../pages/user/UserSearchPage";

import DeviceAssignmentFormPage from "../pages/dispositivos/DeviceAssignmentFormPage";
import GestionDispositivosPage from "../pages/dispositivos/GestionDispositivosPage";
import TipoDispositivoPage from "../pages/dispositivos/TipoDispositivoPage";
import UbicacionPage from "../pages/varios/UbicacionPage";
import ClientesPage from "../pages/varios/ClientesPage";
import CargosPage from "../pages/varios/CargosPage";
import RolesPage from "../pages/varios/RolesPage";
import GestionTHPage from "../pages/varios/CatalogoTHPage";
import CatalogoOperacionesPage from "../pages/varios/CatalogoSysPage";
import CatalogoOpPage from "../pages/varios/CatalogoOp";
import EquipoFormPage from "../pages/operations/EquipoFormPage";
import OperationsPage from "../pages/operations/OperationsPage";
import EditarEquipoPage from "../pages/operations/EditarEquipoPage";

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

          {/* Solicitudes */}
            {/*Solicitudes-Usuario*/}
              <Route path="/requests" element={<RequestPage />} />
              <Route path="/requests/beneficio" element={<RequestFormPage />} />
              <Route path="/requests/permiso" element={<RequestFormPage />} />
              <Route path="/request/vacaciones" element={<RequestFormPage />} />
              <Route path="/requests/:id" element={<RequestViewPageUsuario />} />
              
            {/*Solicitudes-Team Leader*/}
              <Route path="/solicitudesTL" element={<RequestTLPage/>}/> 
              <Route path="/solicitudesTL/:id/ver" element={<RequestViewTL/>}/>
              <Route path="/solicitudesTL/:id/evaluar" element={<RequestViewTL/>}/>

            {/*Solicitudes-Talento Humano*/}
              <Route path="/seleccion-solicitudesTH" element={<RequestSearchPage />} />
              <Route path="/solicitudesTH/permisos" element={<RequestManagementPage/>} />
              <Route path="/solicitudesTH/beneficios" element={<RequestManagementPage/>} />
              <Route path="/solicitudesTH/vacaciones" element={<RequestManagementPage/>} />
              <Route path="/solicitudesTH/:id/ver" element={<RequestViewPage/>}/>
              <Route path="/solicitudesTH/:id/evaluar" element={<RequestViewPage/>}/>


          {/* Usuarios */}
          <Route path="/usuarios" element={<UsuariosPage />} />
          <Route path="/usuarios/buscar" element={<UserSearchPage />} />
          <Route path="/usuarios/nuevo" element={<UserFormPage />} />
          <Route path="/usuarios/:id" element={<UserFormPage />} />


          {/* Operations */}
          <Route path="/operations" element={<OperationsPage />} />
          <Route path="/equipo/nuevo" element={<EquipoFormPage />} />
          <Route path="/equipo/:id/edit" element={<EditarEquipoPage />} />



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
