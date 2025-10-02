// src/service/clientesService.ts
import type { ClientesPageResponse, ClienteResponse, ClienteRequest } from "../types";
// Ajustá esta ruta según dónde tengas tu helper:
import { throwIfNotOk } from "../utils/http";

const BASE_URL = "http://localhost:8080/api/v1/admin/operations/clientes";

/**
 * GET /clientes (paginado)
 */
export async function getClientes(
  token: string,
  options: { page?: number; size?: number; sortBy?: string; sort?: string } = {}
): Promise<ClientesPageResponse> {
  const { page = 0, size = 10, sortBy, sort } = options;

  const params = new URLSearchParams();
  params.append("page", String(page));
  params.append("size", String(size));
  if (sortBy) params.append("sortBy", sortBy);
  if (sort) params.append("sort", sort);

  const res = await fetch(`${BASE_URL}?${params.toString()}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  await throwIfNotOk(res);
  return res.json();
}

/** GET /clientes/{id} */
export async function getClienteById(
  token: string,
  id: number
): Promise<ClienteResponse> {
  const res = await fetch(`${BASE_URL}/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  await throwIfNotOk(res);
  return res.json();
}

/** POST /clientes */
export async function createCliente(
  token: string,
  data: ClienteRequest
): Promise<ClienteResponse> {
  const res = await fetch(BASE_URL, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  await throwIfNotOk(res);
  return res.json();
}

/** PUT /clientes/{id} */
export async function updateCliente(
  token: string,
  id: number,
  data: ClienteRequest
): Promise<ClienteResponse> {
  const res = await fetch(`${BASE_URL}/${id}`, {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  await throwIfNotOk(res);
  return res.json();
}

/** DELETE /clientes/{id} — backend responde 204 */
export async function deleteCliente(
  token: string,
  id: number
): Promise<void> {
  const res = await fetch(`${BASE_URL}/${id}`, {
    method: "DELETE",
    headers: { Authorization: `Bearer ${token}` },
  });

  await throwIfNotOk(res);
}
