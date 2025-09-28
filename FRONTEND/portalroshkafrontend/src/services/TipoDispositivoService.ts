export interface TipoDispositivoItem {
  idTipoDispositivo: number;
  nombre: string;
  detalle: string;
}


// Listar todos los tipos de dispositivos
async function getTiposDispositivo(token: string): Promise<TipoDispositivoItem[]> {
  const res = await fetch(`/api/v1/admin/sysadmin/devices/getDeviceTypes`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}


export { getTiposDispositivo };
