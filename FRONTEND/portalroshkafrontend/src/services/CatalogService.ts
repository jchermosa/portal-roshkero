// src/services/CatalogService.ts
import type { CatalogItem } from "../types";

export async function getRoles(
  token: string,
  _params: Record<string, string | number | undefined> = {}
): Promise<CatalogItem[]> {
  const res = await fetch(`/api/catalogos/roles`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

export async function getCargos(
  token: string,
  _params: Record<string, string | number | undefined> = {}
): Promise<CatalogItem[]> {
  const res = await fetch(`/api/catalogos/cargos`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

export async function getEquipos(
  token: string,
  _params: Record<string, string | number | undefined> = {}
): Promise<CatalogItem[]> {
  const res = await fetch(`/api/catalogos/equipos`, {
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