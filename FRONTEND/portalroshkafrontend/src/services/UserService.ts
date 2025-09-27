import type { UsuarioItem, FiltrosUsuarios } from "../types";

export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

const BASE_URL = "/api/v1/admin/th/users";

/**
 * GET usuarios paginados con filtro sortBy
 */
export async function getUsuarios(
  token: string,
  filtros: FiltrosUsuarios,
  page: number = 0,
  size: number = 10
): Promise<PaginatedResponse<UsuarioItem>> {
  const params = new URLSearchParams();

  if (filtros.sortBy) params.append("sortBy", filtros.sortBy);
  params.append("page", page.toString());
  params.append("size", size.toString());

  const res = await fetch(`${BASE_URL}?${params.toString()}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!res.ok) {
    const error = await res.text();
    throw new Error(error || "Error al obtener usuarios");
  }

  return res.json();
}

/**
 * GET usuario por ID
 * (usa UserByIdResponseDto en el backend)
 */
export async function getUsuarioById(
  token: string,
  id: number
): Promise<UsuarioItem> {
  const res = await fetch(`${BASE_URL}/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

/**
 * GET usuario por cédula
 * (usa UserDto en el backend)
 */
export async function getUsuarioByCedula(
  token: string,
  cedula: string
): Promise<UsuarioItem> {
  const res = await fetch(`${BASE_URL}/cedula/${cedula}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!res.ok) {
    if (res.status === 404) throw new Error("NOT_FOUND");
    throw new Error(await res.text());
  }

  return res.json();
}

/**
 * POST nuevo usuario
 * (usa UserInsertDto en el backend)
 */
export async function createUsuario(
  token: string,
  data: Partial<UsuarioItem>
) {
  const res = await fetch(BASE_URL, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json(); // DefaultResponseDto
}

/**
 * PUT editar usuario
 * (usa UserUpdateDto en el backend)
 */
export async function updateUsuario(
  token: string,
  id: number,
  data: Partial<UsuarioItem>
) {
  const res = await fetch(`${BASE_URL}/${id}`, {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json(); // DefaultResponseDto
}

/**
 * DELETE usuario
 */
export async function deleteUsuario(token: string, id: number) {
  const res = await fetch(`${BASE_URL}/${id}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json(); // DefaultResponseDto
}

/**
 * POST reset contraseña de usuario
 */
export async function resetUsuarioPassword(token: string, id: number) {
  const res = await fetch(`${BASE_URL}/${id}/resetpassword`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json(); // DefaultResponseDto
}
