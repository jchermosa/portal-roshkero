import type { CatalogItem } from "../types";
import type { PaginatedResponse } from "../hooks/common/usePaginatedResource";

export async function getRoles(
  token: string,
  params: Record<string, string | number | undefined> = {}
): Promise<PaginatedResponse<CatalogItem>> {
  const query = new URLSearchParams();
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== "") query.append(k, String(v));
  });

  const res = await fetch(`/api/catalogos/roles?${query.toString()}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

export async function getCargos(
  token: string,
  params: Record<string, string | number | undefined> = {}
): Promise<PaginatedResponse<CatalogItem>> {
  const query = new URLSearchParams();
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== "") query.append(k, String(v));
  });

  const res = await fetch(`/api/catalogos/cargos?${query.toString()}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

export async function getEquipos(
  token: string,
  params: Record<string, string | number | undefined> = {}
): Promise<PaginatedResponse<CatalogItem>> {
  const query = new URLSearchParams();
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== "") query.append(k, String(v));
  });

  const res = await fetch(`/api/catalogos/equipos?${query.toString()}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}
