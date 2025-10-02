import { createContext, useContext, useState, useEffect } from "react";
import type { ReactNode } from "react";

export type User = {
<<<<<<< HEAD
  idUsuario?: number;
  nombre: string;
  apellido: string;
  correo: string;
  rol?: Rol;
  cargo?: Cargo;
  equipos: Equipo[];
  fechaIngreso?: string;
  antiguedad?: string;
=======
  id: number;
  nombre: string;
  apellido: string;
  correo: string;
  rol: {
    idRol: number;
    nombre: string;
  } | null;
  cargo?: { idCargo: number; nombre: string };
  equipos?: { idEquipo: number; nombre: string }[];
>>>>>>> origin/merge-estable
  diasVacaciones?: number;
  diasVacacionesRestante?: number;
  telefono?: string;
  nroCedula?: string;
<<<<<<< HEAD
  fechaNacimiento?: string;
  estado?: string;
  requiereCambioContrasena?: boolean;
  seniority?: string;
  foco?: string;
  urlPerfil?: string;
  disponibilidad?: number;
=======
  estado?: string;
  requiereCambioContrasena?: boolean;
  fotoBase64?: string | null;
>>>>>>> origin/merge-estable
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

// decode JWT payload 
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

  const decodeAndSetUser = async (jwtToken: string) => {
    try {
      const payload = parseJwt(jwtToken);

      const basicUser: User = {
<<<<<<< HEAD
        idUsuario: payload.idUsuario ?? payload.id ?? payload.userId ?? undefined,
        nombre: payload.nombre ?? "",
        apellido: payload.apellido ?? "",
        correo: payload.email ?? payload.sub ?? "",
<<<<<<< HEAD
        rol: payload.rol ?? payload.nombreRol ?? "",
        cargo: payload.idCargo,
        equipos: payload.equipos ?? [],
=======
        // El JWT solo contiene el ID del rol, no el nombre ni el cargo
        rol: payload.rol ? { id: payload.rol, nombre: "" } : { id: undefined, nombre: "" },
        cargo: { id: undefined, nombre: "" }, // El cargo no viene en el JWT
        equipo: payload.equipo ? { nombre: payload.equipo } : undefined,
>>>>>>> origin/merge-estable
        telefono: payload.telefono ?? undefined,
        fechaIngreso: payload.fechaIngreso ?? undefined,
        diasVacaciones: payload.diasVacaciones,
        diasVacacionesRestante: payload.diasVacacionesRestante,
        requiereCambioContrasena: payload.requiereCambioContrasena ?? false,
      };

=======
        id: payload.id ?? 0,
        nombre: payload.nombre ?? "",
        apellido: payload.apellido ?? "",
        correo: payload.email ?? payload.sub ?? "",
        rol: payload.rol
          ? {
              idRol: payload.rol.idRol ?? 0,
              nombre: payload.rol.nombre ?? "",
            }
          : null, // 👈 si no viene, null
      };
>>>>>>> origin/merge-estable
      setUser(basicUser);

      // Ahora pedimos los datos completos al backend
      const res = await fetch("/api/v1/usuarios/me", {
        headers: { Authorization: `Bearer ${jwtToken}` },
      });

      if (!res.ok) throw new Error("No se pudo obtener datos completos del usuario");

      const fullUser: User = await res.json();
      setUser(fullUser);
<<<<<<< HEAD

      console.log("Full user recibido del backend:", fullUser);


=======
>>>>>>> origin/merge-estable
    } catch (e) {
      console.error("Error al decodificar el token:", e);
      setUser(null);
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