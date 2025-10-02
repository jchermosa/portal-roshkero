import type { EquipoItem, RolItem, CargoItem, TipoBeneficioItem, TipoPermisoItem } from "../types";
import mockEquipos from "../data/mockEquipos.json";
import mockRoles from "../data/mockRoles.json";
import mockCargos from "../data/mockCargos.json";
import mockTiposPermiso from "../data/mockTiposPermiso.json";
import mockTiposBeneficio from "../data/mockTiposBeneficio.json";
import mockLideres from "../data/mockLideres.json"

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

async function getTiposPermisoApi(token: string) {
  const res = await fetch(`/api/v1/usuarios/tipospermisos`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function getTiposBeneficioApi(token: string) {
  const res = await fetch(`/api/v1/usuarios/tiposbeneficios`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}



// ================================
// Mocks
// ================================
async function getEquiposMock(_: string): Promise<EquipoItem[]> {
  return mockEquipos as EquipoItem[];
}

async function getRolesMock(_: string): Promise<RolItem[]> {
  return mockRoles as RolItem[];
}

async function getCargosMock(_: string): Promise<CargoItem[]> {
  return mockCargos as CargoItem[];
}



// ================================
// Export condicional
// ================================
export const getEquipos = USE_MOCK ? getEquiposMock : getEquiposApi;
export const getRoles = USE_MOCK ? getRolesMock : getRolesApi;
export const getCargos = USE_MOCK ? getCargosMock : getCargosApi;
export const getTiposPermiso =  getTiposPermisoApi;
export const getTiposBeneficio = getTiposBeneficioApi;

