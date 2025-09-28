import type { UsuarioItem, FiltrosUsuarios } from "../types";
import type { PaginatedResponse } from "../types";
import { mapUserResponseToUsuarioItem } from "../mappers/userMapper";

const BASE_URL = "http://localhost:8080/api/v1/admin/th/users";

/**
 * GET usuarios paginados con filtros
 */
export async function getUsuarios(
  token: string,
  filtros: FiltrosUsuarios = {},
  page: number = 0,
  size: number = 10
): Promise<PaginatedResponse<UsuarioItem>> {
  const params = new URLSearchParams();

  if (filtros.sortBy) params.append("sortBy", filtros.sortBy);
  if (filtros.idRol) params.append("rol_id", filtros.idRol.toString());
  if (filtros.idCargo) params.append("cargo_id", filtros.idCargo.toString());
  if (filtros.estado) params.append("estado", filtros.estado);

  params.append("page", page.toString());
  params.append("size", size.toString());

  const res = await fetch(`${BASE_URL}?${params.toString()}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());

  const json = await res.json();
 
  console.log("getUsuarios response:", json);
  return {
    ...json,
    content: (json.content || []).map(mapUserResponseToUsuarioItem),
  };
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
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());

  const json = await res.json();
  return mapUserResponseToUsuarioItem(json);
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
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) {
    if (res.status === 404) throw new Error("NOT_FOUND");
    throw new Error(await res.text());
  }

  const json = await res.json();
  return mapUserResponseToUsuarioItem(json);
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
    headers: { Authorization: `Bearer ${token}` },
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
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json(); // DefaultResponseDto
}
