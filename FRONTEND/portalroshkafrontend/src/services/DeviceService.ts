import type { DispositivoItem } from "../types";
import mockDispositivos from "../data/mockDispositivos.json";

const USE_MOCK = import.meta.env.VITE_USE_MOCK === "true";

// Copia mutable del mock
let mockData: DispositivoItem[] = [...(mockDispositivos as DispositivoItem[])];

// ========================
// API real (cuando exista)
// ========================
async function getDispositivosApi(
  token: string,
  params: Record<string, string | number | undefined> = {}
): Promise<{ content: DispositivoItem[]; totalPages: number; totalElements: number; size: number; number: number }> {
  const query = new URLSearchParams();
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== "") query.append(k, String(v));
  });

  const res = await fetch(`/api/dispositivos?${query.toString()}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function getDispositivoByIdApi(token: string, id: string | number): Promise<DispositivoItem> {
  const res = await fetch(`/api/dispositivos/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function createDispositivoApi(token: string, data: Partial<DispositivoItem>) {
  const res = await fetch(`/api/dispositivos`, {
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

async function updateDispositivoApi(token: string, id: string | number, data: Partial<DispositivoItem>) {
  const res = await fetch(`/api/dispositivos/${id}`, {
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

// ========================
// Mocks
// ========================
async function getDispositivosMock(): Promise<{ content: DispositivoItem[]; totalPages: number; totalElements: number; size: number; number: number }> {
  return {
    content: mockData,
    totalPages: 1,
    totalElements: mockData.length,
    size: mockData.length,
    number: 0,
  };
}

async function getDispositivoByIdMock(_token: string, id: string | number): Promise<DispositivoItem> {
  const item = mockData.find((d) => d.id_dispositivo === Number(id));
  if (!item) throw new Error("Dispositivo no encontrado");
  return item;
}

async function createDispositivoMock(_token: string, data: Partial<DispositivoItem>) {
  const newItem: DispositivoItem = {
    ...(data as DispositivoItem),
    id_dispositivo: mockData.length + 1,
  };
  mockData.push(newItem);
  return newItem;
}

async function updateDispositivoMock(_token: string, id: string | number, data: Partial<DispositivoItem>) {
  const index = mockData.findIndex((d) => d.id_dispositivo === Number(id));
  if (index === -1) throw new Error("Dispositivo no encontrado");
  mockData[index] = { ...mockData[index], ...(data as DispositivoItem) };
  return mockData[index];
}

// ========================
// Export condicional
// ========================
export const getDispositivos = USE_MOCK ? getDispositivosMock : getDispositivosApi;
export const getDispositivoById = USE_MOCK ? getDispositivoByIdMock : getDispositivoByIdApi;
export const createDispositivo = USE_MOCK ? createDispositivoMock : createDispositivoApi;
export const updateDispositivo = USE_MOCK ? updateDispositivoMock : updateDispositivoApi;
