import { createContext, useContext, useState, useEffect } from "react";
import type { ReactNode } from "react";

type Rol = { id?: number; nombre: string };
type Cargo = { id?: number; nombre: string };
type Equipo = { id?: number; nombre: string };

export type User = {
  idUsuario?: number;
  nombre: string;
  apellido: string;
  correo: string;
  idRol?: number;
  nombreRol?: string;
  idCargo?: number;
  nombreCargo?: string;
  fechaIngreso?: string;
  antiguedad?: string;
  diasVacaciones?: number;
  diasVacacionesRestante?: number;
  telefono?: string;
  nroCedula?: string;
  fechaNacimiento?: string;
  estado?: string;
  requiereCambioContrasena?: boolean;
  seniority?: string;
  foco?: string;
  urlPerfil?: string;
  disponibilidad?: number;
};

type AuthContextType = {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  login: (token: string) => void;
  logout: () => void;
  refreshUser: () => void;
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

  const decodeAndSetUser = async(jwtToken: string) => {
    try {
      const payload = parseJwt(jwtToken);

      const basicUser: User = {
        idUsuario: payload.idUsuario ?? payload.id ?? payload.userId ?? undefined,
        nombre: payload.nombre ?? "",
        apellido: payload.apellido ?? "",
        correo: payload.email ?? payload.sub ?? "",
        nombreRol: payload.rol ?? payload.nombreRol ?? "",
        idCargo: payload.idCargo,
        nombreCargo: payload.cargo ?? payload.nombreCargo,
        telefono: payload.telefono ?? undefined,
        fechaIngreso: payload.fechaIngreso ?? undefined,
        diasVacaciones: payload.diasVacaciones,
        diasVacacionesRestante: payload.diasVacacionesRestante,
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

  const refreshUser = () => {
    if (token) decodeAndSetUser(token);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        isAuthenticated: !!token,
        login,
        logout,
        refreshUser, 
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