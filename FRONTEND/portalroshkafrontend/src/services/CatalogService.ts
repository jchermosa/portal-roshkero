import type {RolItem, CargoItem } from "../types";

export const EstadoActivoInactivoOptions = [
  { label: "Activo", value: "A" },
  { label: "Inactivo", value: "I" },
];



export async function getRoles(token: string): Promise<RolItem[]> {
  const res = await fetch(`http://localhost:8080/api/v1/admin/th/roles?page=0&size=100`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());

  const json = await res.json();
  return json.content;
}

export async function getCargos(token: string): Promise<CargoItem[]> {
  const res = await fetch(`http://localhost:8080/api/v1/admin/th/cargos?page=0&size=100`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());

  const json = await res.json();
  return json.content;
}

async function getTiposPermisoApi(token: string) {
  const res = await fetch(`http://localhost:8080/api/v1/usuarios/tipospermisos`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function getTiposBeneficioApi(token: string) {
  const res = await fetch(`http://localhost:8080/api/v1/usuarios/tiposbeneficios`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}




export const getTiposPermiso =  getTiposPermisoApi;
export const getTiposBeneficio = getTiposBeneficioApi;

