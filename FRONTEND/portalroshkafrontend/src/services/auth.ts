export type AuthRequest = {
  correo: string;
  contrasena: string;
};

export type AuthResponse = {
  token: string;
};

export type RegisterRequest = {
  nombre: string;
  apellido: string;
  correo: string;
  contrasena: string;
  telefono?: string;
}

const BASE = import.meta.env.VITE_API_BASE ?? "http://localhost:8080";

export async function login(req: AuthRequest): Promise<string> {
  const res = await fetch(`${BASE}/api/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(req),
    credentials: "include"
  });
  if (!res.ok) {
    const txt = await res.text();
    throw new Error(txt || "Login falló");
  }
  const data: AuthResponse = await res.json();
  return data.token;
}

export async function register(req: RegisterRequest): Promise<string> {
  const res = await fetch(`${BASE}/api/auth/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(req),
  });
  if (!res.ok) {
    const txt = await res.text();
    throw new Error(txt || "Registro falló");
  }
  const data: AuthResponse = await res.json();
  return data.token; 
}