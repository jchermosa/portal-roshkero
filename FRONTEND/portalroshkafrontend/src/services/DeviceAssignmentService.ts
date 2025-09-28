import type { DispositivoAsignadoItem } from "../types";

// Listar todas las asignaciones de dispositivos
async function getDispositivosAsignados(
  token: string
): Promise<DispositivoAsignadoItem[]> {
  const res = await fetch(`/api/v1/admin/sysadmin/deviceAssignments/listAssignments`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// Crear nueva asignación
async function createDispositivoAsignado(
  token: string,
  data: Partial<DispositivoAsignadoItem>
): Promise<DispositivoAsignadoItem> {
  const res = await fetch(`/api/v1/admin/sysadmin/deviceAssignments/createAssignment`, {
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

// Actualizar asignación
async function updateDispositivoAsignado(
  token: string,
  id: number,
  data: Partial<DispositivoAsignadoItem>
): Promise<DispositivoAsignadoItem> {
  const res = await fetch(`/api/v1/admin/sysadmin/deviceAssignments/updateAssignment/${id}`, {
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

// Eliminar asignación
async function deleteDispositivoAsignado(token: string, id: number): Promise<void> {
  const res = await fetch(`/api/v1/admin/sysadmin/deviceAssignments/deleteAssignment/${id}`, {
    method: "DELETE",
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
}

export {
  getDispositivosAsignados,
  createDispositivoAsignado,
  updateDispositivoAsignado,
  deleteDispositivoAsignado,
};
