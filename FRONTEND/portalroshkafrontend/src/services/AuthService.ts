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
  });
  if (!res.ok) {
    const txt = await res.text();
    throw new Error(txt || "Login falló");
  }
  const token = await res.text(); // ✅ plain token
  return token;
}

// 🔄 Cambio de contraseña (real)
export async function updatePassword(token: string, newPassword: string): Promise<string> {
  const res = await fetch(`${BASE}/api/v1/usuarios/cambiar_contrasena`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({ nuevaContrasena: newPassword}),
  });
  if (!res.ok) {
    const txt = await res.text();
    throw new Error(txt || "Error al actualizar contraseña");
  }
  const data: AuthResponse = await res.json();
  return data.token; // 📌 nuevo token sin `requiereCambioContrasena`
}

