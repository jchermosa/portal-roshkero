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
import RequestPage from "../pages/solicitudes/RequestPage";
import Configuration from "../pages/Configuration.tsx";
import UserFormPage from "../pages/user/UserFormPage.tsx";
import UserSearchPage from "../pages/user/UserSearchPage.tsx";
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
import RequestViewPage from "../pages/solicitudes/RequestViewPage.tsx";
import RequestViewPageUsuario from "../pages/solicitudes/RequestViewPageUsuario.tsx";
import RequestTLPage from "../pages/solicitudes/RequestTLPage.tsx";
import RequestViewTL from "../pages/solicitudes/RequestViewTL.tsx";

export default function AppRoutes() {
  const { user } = useAuth();

  return (
    <Routes>
      {/* Rutas públicas */}
      <Route path="/login" element={<LoginPage />} />
      <Route path="/cambiar-contraseña" element={<ChangePasswordPage />} />

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


          {/* Beneficios */}
          <Route path="/benefits" element={<BeneficiosPage />} />
          <Route path="/beneficios/nuevo" element={<BeneficioFormPage />} />

          {/* Dispositivos */}
           <Route path="/dispositivos" element={<DevicePage />} />
           <Route path="/dispositivos/nuevo" element={<DeviceFormPage />} />

           {/* Dispositivos Asignados */}
           <Route path="/dispositivos-asignados" element={<DeviceAssignmentsPage />} />
           <Route path="/dispositivos-asignados/nuevo" element={<DeviceAssignmentsFormPage />} />
           
           {/* Solicitud Dispositivos */}
           <Route path="/solicitud-dispositivo" element={<SolicitudDispositivoPage />} />
           <Route path="/solicitud-dispositivo/nuevo" element={<SolicitudDispositivoFormPage />} />

          {/* Configuración */}
          <Route path="/configuracion" element={<Configuration />} />
          <Route path="/gestion-solicitudes" element={<RequestManagementPage />} />

          {/* Perfil */}
          <Route path="/profile" element={<ProfilePage />} />
        </Route>
      </Route>

      {/* Fallback */}
      <Route path="*" element={user ? <Navigate to="/" replace /> : <LoginPage />} />
    </Routes>
  );
}
