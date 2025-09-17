// src/services/UserService.ts
import type { UsuarioItem } from "../types";

// Respuesta pageable estándar de Spring
export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number; // página actual
}

// ✅ Listado paginado de usuarios
export async function getUsuarios(
  token: string,
  params: Record<string, string | number | undefined> = {}
): Promise<PaginatedResponse<UsuarioItem>> {
  const query = new URLSearchParams();
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== "") query.append(k, String(v));
  });

  const res = await fetch(`/api/usuarios?${query.toString()}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// ✅ Obtener un usuario por ID
export async function getUsuarioById(token: string, id: string): Promise<UsuarioItem> {
  const res = await fetch(`/api/usuarios/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// ✅ Buscar usuario por cédula
export async function getUsuarioByCedula(token: string, cedula: string): Promise<UsuarioItem | null> {
  const res = await fetch(`/api/usuarios/cedula/${cedula}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (res.status === 404) return null;
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// ✅ Crear usuario
export async function createUsuario(token: string, data: Partial<UsuarioItem>) {
  const res = await fetch(`/api/usuarios`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// ✅ Actualizar usuario
export async function updateUsuario(token: string, id: string, data: Partial<UsuarioItem>) {
  const res = await fetch(`/api/usuarios/${id}`, {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}
