import type { TipoDispositivoItem } from "../types";

async function getDeviceTypes(token: string): Promise<TipoDispositivoItem[]> {
  const endpoints = [
    "http://localhost:8080/api/v1/usuarios/tiposdispositivos", 
    "/api/v1/admin/sysadmin/devices/getDeviceTypes?page=0&size=100", 
  ];

  for (const url of endpoints) {
    try {
      const res = await fetch(url, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

      if (!res.ok) throw new Error(`Error ${res.status}: ${await res.text()}`);

      const data = await res.json();

      // En sysadmin puede venir paginado -> content
      return Array.isArray(data) ? data : data.content ?? [];
    } catch (err) {
      console.warn(`[DeviceTypes] Fallback: ${url} falló`, err);
    }
  }

  throw new Error("No se pudo obtener los tipos de dispositivos");
}

export { getDeviceTypes };
