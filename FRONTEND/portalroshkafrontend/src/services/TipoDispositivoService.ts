// src/services/TipoDispositivoService.ts
export interface TipoDispositivoItem {
  id_tipo_dispositivo: number;
  nombre: string;
  detalle: string;
  fecha_creacion: string;
}

const USE_MOCK = import.meta.env.VITE_USE_MOCK === "true";

// ================================
// API real
// ================================

// Listar todos
async function getTiposDispositivoApi(token: string): Promise<TipoDispositivoItem[]> {
  const res = await fetch(`/api/tipo-dispositivo`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// Obtener por id
async function getTipoDispositivoByIdApi(token: string, id: number): Promise<TipoDispositivoItem> {
  const res = await fetch(`/api/tipo-dispositivo/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// Crear
async function createTipoDispositivoApi(token: string, data: Partial<TipoDispositivoItem>) {
  const res = await fetch(`/api/tipo-dispositivo`, {
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
async function updateTipoDispositivoApi(token: string, id: number, data: Partial<TipoDispositivoItem>) {
  const res = await fetch(`/api/tipo-dispositivo/${id}`, {
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
async function deleteTipoDispositivoApi(token: string, id: number) {
  const res = await fetch(`/api/tipo-dispositivo/${id}`, {
    method: "DELETE",
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return true;
}

// ================================
// MOCK
// ================================
let mockTipos: TipoDispositivoItem[] = [
  {
    id_tipo_dispositivo: 1,
    nombre: "Notebook",
    detalle: "Computadora portátil",
    fecha_creacion: new Date().toISOString(),
  },
  {
    id_tipo_dispositivo: 2,
    nombre: "Monitor",
    detalle: "Pantalla externa",
    fecha_creacion: new Date().toISOString(),
  },
  {
    id_tipo_dispositivo: 3,
    nombre: "Teléfono IP",
    detalle: "Dispositivo de telefonía para oficina",
    fecha_creacion: new Date().toISOString(),
  },
];

async function getTiposDispositivoMock(): Promise<TipoDispositivoItem[]> {
  return mockTipos;
}

async function getTipoDispositivoByIdMock(_token: string, id: number): Promise<TipoDispositivoItem> {
  const tipo = mockTipos.find((t) => t.id_tipo_dispositivo === id);
  if (!tipo) throw new Error("Tipo de dispositivo no encontrado");
  return tipo;
}

async function createTipoDispositivoMock(_token: string, data: Partial<TipoDispositivoItem>) {
  const newTipo: TipoDispositivoItem = {
    ...(data as TipoDispositivoItem),
    id_tipo_dispositivo: mockTipos.length + 1,
    fecha_creacion: new Date().toISOString(),
  };
  mockTipos.push(newTipo);
  return newTipo;
}

async function updateTipoDispositivoMock(_token: string, id: number, data: Partial<TipoDispositivoItem>) {
  const index = mockTipos.findIndex((t) => t.id_tipo_dispositivo === id);
  if (index === -1) throw new Error("Tipo de dispositivo no encontrado");
  mockTipos[index] = { ...mockTipos[index], ...(data as TipoDispositivoItem) };
  return mockTipos[index];
}

async function deleteTipoDispositivoMock(_token: string, id: number) {
  mockTipos = mockTipos.filter((t) => t.id_tipo_dispositivo !== id);
  return true;
}

// ================================
// Export condicional
// ================================
export const getTiposDispositivo = USE_MOCK ? getTiposDispositivoMock : getTiposDispositivoApi;
export const getTipoDispositivoById = USE_MOCK ? getTipoDispositivoByIdMock : getTipoDispositivoByIdApi;
export const createTipoDispositivo = USE_MOCK ? createTipoDispositivoMock : createTipoDispositivoApi;
export const updateTipoDispositivo = USE_MOCK ? updateTipoDispositivoMock : updateTipoDispositivoApi;
export const deleteTipoDispositivo = USE_MOCK ? deleteTipoDispositivoMock : deleteTipoDispositivoApi;
