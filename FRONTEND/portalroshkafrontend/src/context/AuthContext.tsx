import { createContext, useContext, useState, useEffect } from "react";
import type { ReactNode } from "react";

type Rol = { id?: number; nombre: string };
type Cargo = { id?: number; nombre: string };
type Equipo = { id?: number; nombre: string };

export type User = {
  id?: number;
  nombre: string;
  apellido: string;
  correo: string;
  rol: Rol;
  cargo?: Cargo;
  equipo?: Equipo;
  dias_vacaciones?: number;
  dias_vacaciones_restante?: number;
};

type AuthContextType = {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  login: (token: string) => void;
  logout: () => void;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

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
      const payload = JSON.parse(atob(jwtToken.split(".")[1]));
      const usuario: User = {
        nombre: payload.nombre ?? "",
        apellido: payload.apellido ?? "",
        correo: payload.sub,
        rol: { nombre: payload.rol },
        cargo: payload.cargo ? { nombre: payload.cargo } : undefined,
        equipo: payload.equipo ? { nombre: payload.equipo } : undefined,
        dias_vacaciones: payload.diasVacaciones,
        dias_vacaciones_restante: payload.diasVacacionesRestante,
      };
      setUser(usuario);
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
  if (!context) {
    throw new Error("useAuth debe usarse dentro de un AuthProvider");
  }
  return context;
};
