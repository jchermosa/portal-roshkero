// services/UbicacionesService.ts
import type { UbicacionItem, PageResponse } from "../types";

// Listar paginado (Page<UbicacionDto>)
async function getUbicaciones(
  token: string,
  page: number = 0,
  size: number = 10
): Promise<PageResponse<UbicacionItem>> {
  const url = `/api/v1/admin/sysadmin/ubicaciones/getAll?page=${page}&size=${size}`;
  const res = await fetch(url, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json(); // { content, totalPages, totalElements, number, size, ... }
}

// Obtener por id
async function getUbicacionById(
  token: string,
  id: number
): Promise<UbicacionItem> {
  const res = await fetch(`/api/v1/admin/sysadmin/ubicaciones/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// Crear
async function createUbicacion(
  token: string,
  data: Partial<UbicacionItem>
): Promise<UbicacionItem> {
  const res = await fetch(`/api/v1/admin/sysadmin/ubicaciones/create`, {
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

// Actualizar
async function updateUbicacion(
  token: string,
  id: number,
  data: Partial<UbicacionItem>
): Promise<UbicacionItem> {
  const res = await fetch(`/api/v1/admin/sysadmin/ubicaciones/update/${id}`, {
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

// Toggle estado (A <-> I)
async function deleteUbicacion(token: string, id: number): Promise<true> {
  const res = await fetch(`/api/v1/admin/sysadmin/ubicaciones/delete/${id}`, {
    method: "DELETE",
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return true;
}

export {
  getUbicaciones,
  getUbicacionById,
  createUbicacion,
  updateUbicacion,
  deleteUbicacion,
};
