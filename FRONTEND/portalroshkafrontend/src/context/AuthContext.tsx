import { createContext, useContext, useState, useEffect } from "react";
import type { ReactNode } from "react";

type Rol = { id?: number; nombre: string };
type Cargo = { id?: number; nombre: string };
type Equipo = { id?: number; nombre: string };

export type User = {
  idUsuario: number;
  nombre: string;
  apellido: string;
  nroCedula: string;
  correo: string;
  roles: Roles; // definir Roles como enum o type aparte
  fechaIngreso: string; // LocalDate → string (ej: "2025-09-25")
  antiguedad: string;
  diasVacaciones: number;
  estado: "ACTIVO" | "INACTIVO"; // si usas enum EstadoActivoInactivo mejor tiparlo
  contrasena: string;
  telefono: string;
  cargos: Cargos; // idem, definirlo aparte
  fechaNacimiento: string; // LocalDate → string
  diasVacacionesRestante: number;
  requiereCambioContrasena: boolean;
  url: string;
  disponibilidad: number | null;
};


type AuthContextType = {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  login: (token: string) => void;
  logout: () => void;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

// decode JWT payload con soporte UTF-8
function parseJwt(token: string): any {
  try {
    const base = token.split(".")[1];
    const json = decodeURIComponent(
      atob(base)
        .split("")
        .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
        .join("")
    );
    return JSON.parse(json);
  } catch {
    return {};
  }
}

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(null);

  useEffect(() => {
    const storedToken = localStorage.getItem("auth_token");
    if (storedToken) {
      setToken(storedToken);
      decodeAndSetUser(storedToken);
    }
  }, []);

  const decodeAndSetUser = (jwtToken: string) => {
try {
const base = jwtToken.split(".")[1];
const json = decodeURIComponent(
atob(base)
.split("")
.map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
.join("")
);
const payload = JSON.parse(json);

      const basicUser: User = {
        id: payload.id ?? payload.userId ?? payload.usuarioId ?? undefined,
        nombre: payload.nombre ?? "",
        apellido: payload.apellido ?? "",
        correo: payload.email ?? payload.sub ?? "",
        // El JWT solo contiene el ID del rol, no el nombre ni el cargo
        rol: payload.rol ? { id: payload.rol, nombre: "" } : { id: undefined, nombre: "" },
        cargo: { id: undefined, nombre: "" }, // El cargo no viene en el JWT
        equipo: payload.equipo ? { nombre: payload.equipo } : undefined,
        telefono: payload.telefono ?? undefined,
        fechaIngreso: payload.fechaIngreso ?? payload.fecha_ingreso ?? undefined,
        diasVacaciones: payload.diasVacaciones ?? payload.dias_vacaciones,
        diasVacacionesRestante:
          payload.diasVacacionesRestante ?? payload.dias_vacaciones_restante,
        requiereCambioContrasena: payload.requiereCambioContrasena ?? false,

      };


      
      setUser(basicUser);

      const res = await fetch("/api/v1/usuarios/me", {
        headers: { Authorization: `Bearer ${jwtToken}` },
      });

      if (!res.ok) throw new Error("No se pudo obtener datos completos del usuario");

      const fullUser: User = await res.json();

      setUser(fullUser);

    } catch (e) {
      console.error("Error al decodificar el token:", e);
    }
  };

  const login = (jwtToken: string) => {
    localStorage.setItem("auth_token", jwtToken);
    setToken(jwtToken);
    decodeAndSetUser(jwtToken);
  };

  const logout = () => {
    localStorage.removeItem("auth_token");
    setUser(null);
    setToken(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        isAuthenticated: !!token,
        login,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth debe usarse dentro de un AuthProvider");
  return context;
};
