import type { RolItem, CargoItem } from "../types";

export const EstadoActivoInactivoOptions = [
  { label: "Activo", value: "A" },
  { label: "Inactivo", value: "I" },
];


export async function getRoles(token: string): Promise<RolItem[]> {
  const res = await fetch(`/api/v1/admin/th/roles?page=0&size=100`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());

  const json = await res.json();
  return json.content;
}

export async function getCargos(token: string): Promise<CargoItem[]> {
  const res = await fetch(`/api/v1/admin/th/cargos?page=0&size=100`, {
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

export async function getTiposBeneficio(token: string) {
  const res = await fetch(`/api/catalogos/tipos-beneficio`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}
