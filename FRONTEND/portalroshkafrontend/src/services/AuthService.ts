const BASE = import.meta.env.VITE_API_BASE ?? "http://localhost:8080";

export type AuthRequest = {
  correo: string;
  contrasena: string;
};

export type AuthResponse = {
  token: string;
};

// 🔑 Login normal
export async function login(req: AuthRequest): Promise<string> {
  const res = await fetch(`${BASE}/login`, {
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

// 🔑 Registro (opcional)
export async function register(req: any): Promise<string> {
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

// 🔄 Cambio de contraseña (real)
export async function updatePassword(token: string, newPassword: string): Promise<string> {
  const res = await fetch(`${BASE}/api/auth/update-password`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({ contrasena: newPassword }),
  });
  if (!res.ok) {
    const txt = await res.text();
    throw new Error(txt || "Error al actualizar contraseña");
  }
  const data: AuthResponse = await res.json();
  return data.token; // 📌 nuevo token sin `requiereCambioContrasena`
}

// 🔄 Cambio de contraseña (mock)
export async function updatePasswordMock(token: string, newPassword: string): Promise<string> {
  console.log("Mock updatePassword:", { token, newPassword });

  // Simula un nuevo token válido sin `requiereCambioContrasena`
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(
        "mock.jwt.token.sin_requiereCambioContrasena"
      );
    }, 1000);
  });
}
