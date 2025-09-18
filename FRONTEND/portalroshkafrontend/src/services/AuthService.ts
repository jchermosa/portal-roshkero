const BASE = import.meta.env.VITE_API_BASE ?? "http://localhost:8080";

// ✅ Tipos
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
};

// ✅ Login
export async function login(req: AuthRequest): Promise<string> {
  const res = await fetch(`${BASE}/api/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(req),
    credentials: "include", 
  });

  if (!res.ok) {
    const txt = await res.text();
    throw new Error(txt || "Login falló");
  }

  const data: AuthResponse = await res.json();
  return data.token;
}

// ✅ Registro
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

// ✅ Cambio de contraseña
export async function changePassword(
  token: string,
  oldPassword: string,
  newPassword: string
): Promise<void> {
  const res = await fetch(`${BASE}/api/auth/change-password`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({ oldPassword, newPassword }),
  });

  if (!res.ok) {
    const txt = await res.text();
    throw new Error(txt || "No se pudo cambiar la contraseña");
  }
}

export async function updatePassword(token: string, nuevaContrasena: string): Promise<void> {
  const res = await fetch(`${BASE}/api/auth/cambiar-contrasena`, {
    method: "POST", 
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({ contrasena: nuevaContrasena }),
  });

  if (!res.ok) {
    const txt = await res.text();
    throw new Error(txt || "Error al cambiar la contraseña");
  }
}

export async function updatePasswordMock(token: string, nuevaContrasena: string): Promise<void> {
  console.log("Mock updatePassword", { token, nuevaContrasena });
  return new Promise((resolve) => setTimeout(resolve, 500)); // simula delay
}