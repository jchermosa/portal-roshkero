import type { UsuarioItem } from "../types";
import mockUsuariosData from "../data/mockUsuarios.json";

const USE_MOCK = import.meta.env.VITE_USE_MOCK === "true";

// Hacemos una copia mutable del mock
let mockUsuarios: UsuarioItem[] = [...(mockUsuariosData as UsuarioItem[])];

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
async function getUsuariosApi(
  token: string,
  params: Record<string, string | number | undefined> = {}
): Promise<PaginatedResponse<UsuarioItem>> {
  const query = new URLSearchParams();
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== "") query.append(k, String(v));
  });

  const res = await fetch(`/api/usuarios?${query.toString()}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function getUsuarioByIdApi(token: string, id: string): Promise<UsuarioItem> {
  const res = await fetch(`/api/usuarios/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function getUsuarioByCedulaApi(token: string, cedula: string): Promise<UsuarioItem | null> {
  const res = await fetch(`/api/usuarios/cedula/${cedula}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (res.status === 404) return null;
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function createUsuarioApi(token: string, data: Partial<UsuarioItem>) {
  const res = await fetch(`/api/usuarios`, {
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

async function updateUsuarioApi(token: string, id: string, data: Partial<UsuarioItem>) {
  const res = await fetch(`/api/usuarios/${id}`, {
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
async function getUsuariosMock(): Promise<PaginatedResponse<UsuarioItem>> {
  return {
    content: mockUsuarios,
    totalPages: 1,
    totalElements: mockUsuarios.length,
    size: mockUsuarios.length,
    number: 0,
  };
}

async function getUsuarioByIdMock(_token: string, id: string): Promise<UsuarioItem> {
  const user = mockUsuarios.find((u) => u.id === Number(id));
  if (!user) throw new Error("Usuario no encontrado");
  return user;
}

async function getUsuarioByCedulaMock(_token: string, cedula: string): Promise<UsuarioItem | null> {
  return mockUsuarios.find((u) => u.nroCedula === Number(cedula)) || null;
}

async function createUsuarioMock(_token: string, data: Partial<UsuarioItem>) {
  const newUser: UsuarioItem = {
    ...(data as UsuarioItem),
    id: mockUsuarios.length + 1,
  };
  mockUsuarios.push(newUser);
  return newUser;
}

async function updateUsuarioMock(_token: string, id: string, data: Partial<UsuarioItem>) {
  const index = mockUsuarios.findIndex((u) => u.id === Number(id));
  if (index === -1) throw new Error("Usuario no encontrado");
  mockUsuarios[index] = { ...mockUsuarios[index], ...(data as UsuarioItem) };
  return mockUsuarios[index];
}

// ================================
// Export condicional
// ================================
export const getUsuarios = USE_MOCK ? getUsuariosMock : getUsuariosApi;
export const getUsuarioById = USE_MOCK ? getUsuarioByIdMock : getUsuarioByIdApi;
export const getUsuarioByCedula = USE_MOCK ? getUsuarioByCedulaMock : getUsuarioByCedulaApi;
export const createUsuario = USE_MOCK ? createUsuarioMock : createUsuarioApi;
export const updateUsuario = USE_MOCK ? updateUsuarioMock : updateUsuarioApi;
