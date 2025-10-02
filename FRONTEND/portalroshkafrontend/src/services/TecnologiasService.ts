// src/service/tecnologiasService.ts
import type {
  TecnologiasPageResponse,
  TecnologiaResponse,
  TecnologiaRequest,
} from "../types";
import { throwIfNotOk } from "../utils/http";

const BASE_URL = "http://localhost:8080/api/v1/admin/operations/tecnologias";

/**
 * GET /tecnologias (paginado)
 */
export async function getTecnologias(
  token: string,
  options: { page?: number; size?: number; sort?: string } = {}
): Promise<TecnologiasPageResponse> {
  const { page = 0, size = 10, sort } = options;

  const params = new URLSearchParams();
  params.append("page", String(page));
  params.append("size", String(size));
  if (sort) params.append("sort", sort);

  const res = await fetch(`${BASE_URL}?${params.toString()}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  await throwIfNotOk(res);
  return res.json();
}

/** GET /tecnologias/{id} */
export async function getTecnologiaById(
  token: string,
  id: number
): Promise<TecnologiaResponse> {
  const res = await fetch(`${BASE_URL}/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  await throwIfNotOk(res);
  return res.json();
}

/** POST /tecnologias */
export async function createTecnologia(
  token: string,
  data: TecnologiaRequest
): Promise<TecnologiaResponse> {
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

/** PUT /tecnologias/{id} */
export async function updateTecnologia(
  token: string,
  id: number,
  data: TecnologiaRequest
): Promise<TecnologiaResponse> {
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

/** DELETE /tecnologias/{id} */
export async function deleteTecnologia(
  token: string,
  id: number
): Promise<void> {
  const res = await fetch(`${BASE_URL}/${id}`, {
    method: "DELETE",
    headers: { Authorization: `Bearer ${token}` },
  });

  await throwIfNotOk(res);
}
