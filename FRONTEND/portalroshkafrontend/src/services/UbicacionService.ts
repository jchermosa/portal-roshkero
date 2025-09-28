import type { UbicacionItem } from "../types";


// Listar todas
async function getUbicaciones(token: string): Promise<UbicacionItem[]> {
  const res = await fetch(`/api/v1/admin/sysadmin/ubicaciones/getAll`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// Obtener por id
async function getUbicacionById(token: string, id: number): Promise<UbicacionItem> {
  const res = await fetch(`/api/v1/admin/sysadmin/ubicaciones/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// Crear
async function createUbicacion(token: string, data: Partial<UbicacionItem>) {
  const res = await fetch(`/api/v1/admin/sysadmin/ubicaciones/create`, {
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
async function updateUbicacion(token: string, id: number, data: Partial<UbicacionItem>) {
  const res = await fetch(`/api/v1/admin/sysadmin/ubicaciones/update/${id}`, {
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
async function deleteUbicacion(token: string, id: number) {
  const res = await fetch(`/api/v1/admin/sysadmin/ubicaciones/delete/${id}`, {
    method: "DELETE",
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return true;
}


export {
  getUbicaciones,
  getUbicacionById,
  createUbicacion,
  updateUbicacion,
  deleteUbicacion,
};
