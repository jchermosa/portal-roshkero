import type { PageResponse } from "../types";

export interface TipoDispositivoItem {
  idTipoDispositivo: number;
  nombre: string;
  detalle: string;
}


// Listar todos los tipos de dispositivos
async function getTiposDispositivo(token: string): Promise<TipoDispositivoItem[]> {
  const res = await fetch(`http://localhost:8080/api/v1/admin/sysadmin/deviceTypes/allTypes`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  
  const pageResponse: PageResponse<TipoDispositivoItem> = await res.json();
  return pageResponse.content; // Extraer solo el array content
}

// Obtener tipo de dispositivo por ID
async function getTipoDispositivoById(token: string, id: number): Promise<TipoDispositivoItem> {
  const res = await fetch(`http://localhost:8080/api/v1/admin/sysadmin/deviceTypes/getTypeDevice/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// Crear nuevo tipo de dispositivo
async function createTipoDispositivo(token: string, data: Partial<TipoDispositivoItem>): Promise<TipoDispositivoItem> {
  const res = await fetch(`http://localhost:8080/api/v1/admin/sysadmin/deviceTypes/createTypeDevice`, {
    method: 'POST',
    headers: { 
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// Actualizar tipo de dispositivo
async function updateTipoDispositivo(token: string, id: number, data: Partial<TipoDispositivoItem>): Promise<TipoDispositivoItem> {
  const res = await fetch(`http://localhost:8080/api/v1/admin/sysadmin/deviceTypes/updateTypeDevice/${id}`, {
    method: 'PUT',
    headers: { 
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// Eliminar tipo de dispositivo
async function deleteTipoDispositivo(token: string, id: number): Promise<void> {
  const res = await fetch(`http://localhost:8080/api/v1/admin/sysadmin/deviceTypes/deleteTypeDevice/${id}`, {
    method: 'DELETE',
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
}

export { 
  getTiposDispositivo,
  getTipoDispositivoById,
  createTipoDispositivo,
  updateTipoDispositivo,
  deleteTipoDispositivo
};
