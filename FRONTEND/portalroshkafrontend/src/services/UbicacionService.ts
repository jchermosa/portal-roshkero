import type { UbicacionItem } from "../types";

const USE_MOCK = import.meta.env.VITE_USE_MOCK === "true";

// ================================
// API real
// ================================

// Listar todos
async function getUbicacionesApi(token: string): Promise<UbicacionItem[]> {
  const res = await fetch(`/api/ubicaciones`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// Obtener por id
async function getUbicacionByIdApi(token: string, id: number): Promise<UbicacionItem> {
  const res = await fetch(`/api/ubicaciones/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// Crear
async function createUbicacionApi(token: string, data: Partial<UbicacionItem>) {
  const res = await fetch(`/api/ubicaciones`, {
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
async function updateUbicacionApi(token: string, id: number, data: Partial<UbicacionItem>) {
  const res = await fetch(`/api/ubicaciones/${id}`, {
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

// Eliminar
async function deleteUbicacionApi(token: string, id: number) {
  const res = await fetch(`/api/ubicaciones/${id}`, {
    method: "DELETE",
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return true;
}

// ================================
// MOCK
// ================================
let mockUbicaciones: UbicacionItem[] = [
  { id_ubicacion: 1, nombre: "Depósito Central", estado: "ACTIVO" },
  { id_ubicacion: 2, nombre: "Sucursal Asunción", estado: "ACTIVO" },
  { id_ubicacion: 3, nombre: "Sucursal Encarnación", estado: "INACTIVO" },
];

async function getUbicacionesMock(): Promise<UbicacionItem[]> {
  return mockUbicaciones;
}

async function getUbicacionByIdMock(_token: string, id: number): Promise<UbicacionItem> {
  const ubi = mockUbicaciones.find((u) => u.id_ubicacion === id);
  if (!ubi) throw new Error("Ubicación no encontrada");
  return ubi;
}

async function createUbicacionMock(_token: string, data: Partial<UbicacionItem>) {
  const newUbi: UbicacionItem = {
    ...(data as UbicacionItem),
    id_ubicacion: mockUbicaciones.length + 1,
  };
  mockUbicaciones.push(newUbi);
  return newUbi;
}

async function updateUbicacionMock(_token: string, id: number, data: Partial<UbicacionItem>) {
  const index = mockUbicaciones.findIndex((u) => u.id_ubicacion === id);
  if (index === -1) throw new Error("Ubicación no encontrada");
  mockUbicaciones[index] = { ...mockUbicaciones[index], ...(data as UbicacionItem) };
  return mockUbicaciones[index];
}

async function deleteUbicacionMock(_token: string, id: number) {
  mockUbicaciones = mockUbicaciones.filter((u) => u.id_ubicacion !== id);
  return true;
}

// ================================
// Export condicional
// ================================
export const getUbicaciones = USE_MOCK ? getUbicacionesMock : getUbicacionesApi;
export const getUbicacionById = USE_MOCK ? getUbicacionByIdMock : getUbicacionByIdApi;
export const createUbicacion = USE_MOCK ? createUbicacionMock : createUbicacionApi;
export const updateUbicacion = USE_MOCK ? updateUbicacionMock : updateUbicacionApi;
export const deleteUbicacion = USE_MOCK ? deleteUbicacionMock : deleteUbicacionApi;
