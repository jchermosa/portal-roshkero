import type { RolItem, CargoItem } from "../types";

export const EstadoActivoInactivoOptions = [
  { label: "Activo", value: "A" },
  { label: "Inactivo", value: "I" },
];


export async function getRoles(token: string): Promise<RolItem[]> {
  const res = await fetch(`http://localhost:8080/api/v1/admin/th/roles?page=0&size=100`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

export async function getCargos(token: string): Promise<CargoItem[]> {
  const res = await fetch(`http://localhost:8080/api/v1/admin/th/cargos?page=0&size=100`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());

  const json = await res.json();
  return json.content;
}
export async function getTiposPermiso(token: string) {
  const res = await fetch(`/api/catalogos/tipos-permiso`, {
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
