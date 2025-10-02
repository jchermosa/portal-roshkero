import { throwIfNotOk } from "../utils/http";
import type {
  PageSpring,
  RolListItem,
  RolDetail,
  RolInsert,
  RolActionResponse,
} from "../types";

const BASE_URL = "http://localhost:8080/api/v1/admin/th/roles";

export async function getRoles(
  token: string,
  options: { page?: number; size?: number; sort?: string } = {}
): Promise<PageSpring<RolListItem>> {
  const { page = 0, size = 10, sort } = options;

  const params = new URLSearchParams();
  params.append("page", String(page));
  params.append("size", String(size));
  if (sort) params.append("sort", sort); // ej: "nombre,ASC"

  const res = await fetch(`${BASE_URL}?${params.toString()}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  await throwIfNotOk(res);
  return res.json();
}

export async function getRolById(
  token: string,
  idRol: number
): Promise<RolDetail> {
  const res = await fetch(`${BASE_URL}/${idRol}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  await throwIfNotOk(res);
  return res.json();
}

export async function createRol(
  token: string,
  payload: RolInsert
): Promise<RolActionResponse> {
  const res = await fetch(BASE_URL, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });

  await throwIfNotOk(res);
  return res.json();
}

export async function updateRol(
  token: string,
  idRol: number,
  payload: RolInsert
): Promise<RolActionResponse> {
  const res = await fetch(`${BASE_URL}/${idRol}`, {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });

  await throwIfNotOk(res);
  return res.json();
}

export async function deleteRol(
  token: string,
  idRol: number
): Promise<RolActionResponse> {
  const res = await fetch(`${BASE_URL}/${idRol}`, {
    method: "DELETE",
    headers: { Authorization: `Bearer ${token}` },
  });

  await throwIfNotOk(res);
  return res.json(); // { idRol, message }
}
