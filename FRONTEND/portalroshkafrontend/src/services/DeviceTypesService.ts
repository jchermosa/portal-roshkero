import type { TipoDispositivoItem } from "../types";

// Obtener todos los tipos de dispositivos desde la nueva API
async function getDeviceTypes(token: string): Promise<TipoDispositivoItem[]> {
  const res = await fetch(
    `http://localhost:8080/api/v1/admin/sysadmin/devices/getDeviceTypes?page=0&size=100`,
    {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    }
  );

  if (!res.ok) {
    throw new Error(`Error ${res.status}: ${await res.text()}`);
  }

  const data = await res.json();

  // ⚡ Devolver solo el array de tipos
  return data.content ?? [];
}

export { getDeviceTypes };
