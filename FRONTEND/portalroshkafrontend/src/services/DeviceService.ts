import type { DispositivoItem } from "../types";


async function getDispositivos(
  token: string
): Promise<DispositivoItem[]> {
  const res = await fetch(`http://localhost:8080/api/v1/admin/sysadmin/devices/allDevices`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function getDispositivosWithoutOwner(
  token: string,
  sortBy: string = "default",
  filterValue?: string
): Promise<DispositivoItem[]> {
  const query = new URLSearchParams();
  if (sortBy) query.append("sortBy", sortBy);
  if (filterValue) query.append("filterValue", filterValue);

  const res = await fetch(`http://localhost:8080/api/v1/admin/sysadmin/devices/allDevicesWithoutOwner?${query.toString()}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function createDispositivo(token: string, data: Partial<DispositivoItem>) {
  const res = await fetch(`http://localhost:8080/api/v1/admin/sysadmin/devices/create`, {
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

async function updateDispositivo(token: string, id: string | number, data: Partial<DispositivoItem>) {
  const res = await fetch(`/api/v1/admin/sysadmin/devices/update/${id}`, {
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

async function deleteDispositivo(token: string, id: string | number) {
  const res = await fetch(`/api/v1/admin/sysadmin/devices/delete/${id}`, {
    method: "DELETE",
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
}

export {
  getDispositivos,
  getDispositivosWithoutOwner,
  createDispositivo,
  updateDispositivo,
  deleteDispositivo,
};
