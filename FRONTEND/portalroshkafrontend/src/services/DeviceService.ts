import type { DispositivoItem, PageResponse } from "../types";

const BASE = "http://localhost:8080/api/v1/admin/sysadmin/devices";

// Helpers
async function ensureOk(res: Response) {
  if (!res.ok) {
    const text = await res.text().catch(() => "");
    throw new Error(text || `HTTP ${res.status}`);
  }
}

// === GET by ID (para edición) ===
export async function getDispositivoById(
  token: string,
  id: number | string
): Promise<DispositivoItem> {
  const res = await fetch(`${BASE}/getDevice/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  await ensureOk(res);
  return res.json();
}

// === LISTADO paginado de dispositivos ===
export async function getDispositivos(
  token: string,
  page: number = 0,
  size: number = 10
): Promise<PageResponse<DispositivoItem>> {
  const res = await fetch(`${BASE}/allDevices?page=${page}&size=${size}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  await ensureOk(res);
  return res.json();
}

// === LISTADO de dispositivos sin dueño (compat: sin paginar) ===
// Mantengo tu función anterior (que devolvía array) para no romper código existente.
export async function getDispositivosWithoutOwner(
  token: string,
  sortBy: string = "default",
  filterValue?: string
): Promise<DispositivoItem[]> {
  const qs = new URLSearchParams();
  if (sortBy) qs.append("sortBy", sortBy);
  if (filterValue) qs.append("filterValue", filterValue);

  const res = await fetch(`${BASE}/allDevicesWithoutOwner?${qs}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  await ensureOk(res);
  return res.json();
}

// === LISTADO de dispositivos sin dueño (paginado real) ===
export async function getDispositivosWithoutOwnerPaged(
  token: string,
  page: number = 0,
  size: number = 10,
  sortBy: string = "default",
  filterValue?: string
): Promise<PageResponse<DispositivoItem>> {
  const qs = new URLSearchParams({
    page: String(page),
    size: String(size),
    sortBy,
  });
  if (filterValue) qs.append("filterValue", filterValue);

  const res = await fetch(`${BASE}/allDevicesWithoutOwner?${qs}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  await ensureOk(res);
  return res.json();
}

// === Tipos de dispositivo (paginado) ===
export async function getDeviceTypesPaged<T = any>(
  token: string,
  page: number = 0,
  size: number = 10
): Promise<PageResponse<T>> {
  const res = await fetch(`${BASE}/getDeviceTypes?page=${page}&size=${size}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  await ensureOk(res);
  return res.json();
}

// === CREATE ===
export async function createDispositivo(
  token: string,
  data: Partial<DispositivoItem>
) {
  const res = await fetch(`${BASE}/create`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });
  await ensureOk(res);
  return res.json();
}

// === UPDATE ===
export async function updateDispositivo(
  token: string,
  id: number | string,
  data: Partial<DispositivoItem>
) {
  const res = await fetch(`${BASE}/update/${id}`, {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });
  await ensureOk(res);
  return res.json();
}

// === DELETE ===
export async function deleteDispositivo(token: string, id: number | string) {
  const res = await fetch(`${BASE}/delete/${id}`, {
    method: "DELETE",
    headers: { Authorization: `Bearer ${token}` },
  });
  await ensureOk(res);
  return true;
}
