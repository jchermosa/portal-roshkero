import { throwIfNotOk } from "../utils/http";
import type {
  PageSpring,
  CargoListItem,
  CargoDetail,
  CargoInsert,
  CargoActionResponse,
} from "../types";

const BASE_URL = "http://localhost:8080/api/v1/admin/th/cargos";

/** GET /th/cargos — listado paginado (Spring Page) */
export async function getCargos(
  token: string,
  options: { page?: number; size?: number; sort?: string } = {}
): Promise<PageSpring<CargoListItem>> {
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

/** GET /th/cargos/{idCargo} — detalle con empleados asignados */
export async function getCargoById(
  token: string,
  idCargo: number
): Promise<CargoDetail> {
  const res = await fetch(`${BASE_URL}/${idCargo}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  await throwIfNotOk(res);
  return res.json();
}

/** POST /th/cargos — crear cargo */
export async function createCargo(
  token: string,
  payload: CargoInsert
): Promise<CargoActionResponse> {
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

/** PUT /th/cargos/{idCargo} — actualizar cargo */
export async function updateCargo(
  token: string,
  idCargo: number,
  payload: CargoInsert
): Promise<CargoActionResponse> {
  const res = await fetch(`${BASE_URL}/${idCargo}`, {
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

/** DELETE /th/cargos/{idCargo} — eliminar cargo */
export async function deleteCargo(
  token: string,
  idCargo: number
): Promise<CargoActionResponse> {
  const res = await fetch(`${BASE_URL}/${idCargo}`, {
    method: "DELETE",
    headers: { Authorization: `Bearer ${token}` },
  });

  await throwIfNotOk(res);
  return res.json(); // tu back devuelve { idCargo, message }
}
