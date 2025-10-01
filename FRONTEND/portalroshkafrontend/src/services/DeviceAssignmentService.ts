// src/services/DeviceAssignmentService.ts
import type { DispositivoAsignadoItem, PaginatedResponse } from "../types";

// Listar todas las asignaciones de dispositivos (con paginación)
async function getDispositivosAsignados(
  token: string,
  page: number = 0,
  size: number = 10
): Promise<PaginatedResponse<DispositivoAsignadoItem>> {
  const res = await fetch(
    `http://localhost:8080/api/v1/admin/sysadmin/deviceAssignments/listAssignments?page=${page}&size=${size}`,
    {
      headers: { Authorization: `Bearer ${token}` },
    }
  );
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// Crear nueva asignación
async function createDispositivoAsignado(
  token: string,
  data: Partial<DispositivoAsignadoItem>
): Promise<DispositivoAsignadoItem> {
  const res = await fetch(
    `http://localhost:8080/api/v1/admin/sysadmin/deviceAssignments/createAssignment`,
    {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    }
  );
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// Actualizar asignación
async function updateDispositivoAsignado(
  token: string,
  id: number,
  data: Partial<DispositivoAsignadoItem>
): Promise<DispositivoAsignadoItem> {
  const res = await fetch(
    `http://localhost:8080/api/v1/admin/sysadmin/deviceAssignments/updateAssignment/${id}`,
    {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    }
  );
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// Eliminar asignación
async function deleteDispositivoAsignado(
  token: string,
  id: number
): Promise<void> {
  const res = await fetch(
    `http://localhost:8080/api/v1/admin/sysadmin/deviceAssignments/deleteAssignment/${id}`,
    {
      method: "DELETE",
      headers: { Authorization: `Bearer ${token}` },
    }
  );
  if (!res.ok) throw new Error(await res.text());
}

export {
  getDispositivosAsignados,
  createDispositivoAsignado,
  updateDispositivoAsignado,
  deleteDispositivoAsignado,
};
