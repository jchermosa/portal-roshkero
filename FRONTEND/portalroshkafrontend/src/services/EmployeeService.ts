import type { UsuarioItem } from "../types";

export type SortBy = "active" | "inactive" | "rol" | "cargo";

export interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  number: number; // página actual
}

/**
 * Trae lista de empleados con opción de filtro por sortBy y paginación
 */
export async function getEmployees(
  token: string,
  sortBy?: SortBy,
  page: number = 0,
  size: number = 10
): Promise<PageResponse<UsuarioItem>> {
  const url = sortBy
    ? `/api/v1/admin/th/users?sortBy=${sortBy}&page=${page}&size=${size}`
    : `/api/v1/admin/th/users?page=${page}&size=${size}`;

  const res = await fetch(url, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

export async function getEmployeeById(token: string, id: number) {
  const res = await fetch(`/api/v1/admin/th/users/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

export async function getEmployeeByCedula(token: string, cedula: string) {
  const res = await fetch(`/api/v1/admin/th/users/cedula/${cedula}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}
