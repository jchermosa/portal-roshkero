import { createContext, useContext, useState, useEffect} from "react";
import type { ReactNode } from "react";
import { useNavigate } from "react-router-dom";

type Rol = {
  id: number;
  nombre: string;
};

type Cargo = {
  id: number;
  nombre: string;
};

type Equipo = {
  id: number;
  nombre: string;
};

export type User = {
  id: number;
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
  login: (userData: User) => void;
  logout: () => void;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<User | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    // 🔹 Recuperar usuario guardado en localStorage (si existe)
    const storedUser = localStorage.getItem("user");
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  }, []);

  const login = (userData: User) => {
    setUser(userData);
    localStorage.setItem("user", JSON.stringify(userData));
    navigate("/"); // Redirige al dashboard después de login
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem("user");
    navigate("/login");
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

// Hook para acceder fácilmente al contexto
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth debe usarse dentro de un AuthProvider");
  }
  return context;
};
