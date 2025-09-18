// ProtectedRoute.tsx
import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function ProtectedRoute() {
  const { isAuthenticated, user } = useAuth();
  const location = useLocation();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  // 🚨 Forzar cambio de contraseña si aplica
  if (user?.requiereCambioContrasena && location.pathname !== "/cambiar-contraseña") {
    return <Navigate to="/cambiar-contraseña" replace />;
  }

  return <Outlet />;
}
