import type { EquipoItem, RolItem, CargoItem } from "../types";
import mockEquipos from "../data/mockEquipos.json";
import mockRoles from "../data/mockRoles.json";
import mockCargos from "../data/mockCargos.json";

const USE_MOCK = import.meta.env.VITE_USE_MOCK === "true";

// ================================
// API real
// ================================
async function getEquiposApi(token: string): Promise<EquipoItem[]> {
  const res = await fetch(`/api/catalogos/equipos`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function getRolesApi(token: string): Promise<RolItem[]> {
  const res = await fetch(`/api/catalogos/roles`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function getCargosApi(token: string): Promise<CargoItem[]> {
  const res = await fetch(`/api/catalogos/cargos`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}
//Catalgos nuevos para solicitud
export async function getPermisos(
  token: string,
  _params: Record<string, string | number | undefined> = {}
): Promise<CatalogItem[]> {
  const res = await fetch(`/api/catalogos/tipos-permiso`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

export async function getBeneficio(
  token: string,
  _params: Record<string, string | number | undefined> = {}
): Promise<CatalogItem[]> {
  const res = await fetch(`/api/catalogos/tipos-beneficio`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}
// ================================
// Mocks
// ================================
async function getEquiposMock(): Promise<EquipoItem[]> {
  return mockEquipos as EquipoItem[];
}

async function getRolesMock(): Promise<RolItem[]> {
  return mockRoles as RolItem[];
}

async function getCargosMock(): Promise<CargoItem[]> {
  return mockCargos as CargoItem[];
}

// ================================
// Export condicional
// ================================
export const getEquipos = USE_MOCK ? getEquiposMock : getEquiposApi;
export const getRoles = USE_MOCK ? getRolesMock : getRolesApi;
export const getCargos = USE_MOCK ? getCargosMock : getCargosApi;
