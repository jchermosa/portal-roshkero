import type { DispositivoAsignadoItem } from "../types";
import mockDispositivosAsignadosData from "../data/mockDispositivosAsignados.json";

const USE_MOCK = import.meta.env.VITE_USE_MOCK === "true";

// Hacemos copia mutable del mock
let mockDispositivosAsignados: DispositivoAsignadoItem[] = [
  ...(mockDispositivosAsignadosData as DispositivoAsignadoItem[]),
];

// ================================
// Respuesta pageable estándar
// ================================
export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number; // página actual
}

// ================================
// Métodos reales
// ================================
async function getDispositivosAsignadosApi(
  token: string,
  params: Record<string, string | number | undefined> = {}
): Promise<PaginatedResponse<DispositivoAsignadoItem>> {
  const query = new URLSearchParams();
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== "") query.append(k, String(v));
  });

  const res = await fetch(`/api/dispositivos-asignados?${query.toString()}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function getDispositivoAsignadoByIdApi(
  token: string,
  id: string
): Promise<DispositivoAsignadoItem> {
  const res = await fetch(`/api/dispositivos-asignados/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function createDispositivoAsignadoApi(
  token: string,
  data: Partial<DispositivoAsignadoItem>
) {
  const res = await fetch(`/api/dispositivos-asignados`, {
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

async function updateDispositivoAsignadoApi(
  token: string,
  id: string,
  data: Partial<DispositivoAsignadoItem>
) {
  const res = await fetch(`/api/dispositivos-asignados/${id}`, {
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

// ================================
// Mocks
// ================================
async function getDispositivosAsignadosMock(): Promise<
  PaginatedResponse<DispositivoAsignadoItem>
> {
  return {
    content: mockDispositivosAsignados,
    totalPages: 1,
    totalElements: mockDispositivosAsignados.length,
    size: mockDispositivosAsignados.length,
    number: 0,
  };
}

async function getDispositivoAsignadoByIdMock(
  _token: string,
  id: string
): Promise<DispositivoAsignadoItem> {
  const found = mockDispositivosAsignados.find(
    (d) => d.id_dispositivo_asignado === Number(id)
  );
  if (!found) throw new Error("Dispositivo asignado no encontrado");
  return found;
}

async function createDispositivoAsignadoMock(
  _token: string,
  data: Partial<DispositivoAsignadoItem>
) {
  const newItem: DispositivoAsignadoItem = {
    ...(data as DispositivoAsignadoItem),
    id_dispositivo_asignado: mockDispositivosAsignados.length + 1,
  };
  mockDispositivosAsignados.push(newItem);
  return newItem;
}

async function updateDispositivoAsignadoMock(
  _token: string,
  id: string,
  data: Partial<DispositivoAsignadoItem>
) {
  const index = mockDispositivosAsignados.findIndex(
    (d) => d.id_dispositivo_asignado === Number(id)
  );
  if (index === -1) throw new Error("Dispositivo asignado no encontrado");
  mockDispositivosAsignados[index] = {
    ...mockDispositivosAsignados[index],
    ...(data as DispositivoAsignadoItem),
  };
  return mockDispositivosAsignados[index];
}

// ================================
// Export condicional
// ================================
export const getDispositivosAsignados = USE_MOCK
  ? getDispositivosAsignadosMock
  : getDispositivosAsignadosApi;
export const getDispositivoAsignadoById = USE_MOCK
  ? getDispositivoAsignadoByIdMock
  : getDispositivoAsignadoByIdApi;
export const createDispositivoAsignado = USE_MOCK
  ? createDispositivoAsignadoMock
  : createDispositivoAsignadoApi;
export const updateDispositivoAsignado = USE_MOCK
  ? updateDispositivoAsignadoMock
  : updateDispositivoAsignadoApi;
