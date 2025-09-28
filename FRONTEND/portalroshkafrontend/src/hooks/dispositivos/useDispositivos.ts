import { useState, useEffect } from "react";
import { getDispositivos } from "../../services/DeviceService";
import type { DispositivoItem } from "../../types";

export function useDispositivos(
  token: string | null,
  filtros: Record<string, string | number | undefined> = {},
  page: number = 0,
  pageSize: number = 10
) {
  const [data, setData] = useState<DispositivoItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [totalPages, setTotalPages] = useState(1);
  const [totalElements, setTotalElements] = useState(0);

  useEffect(() => {
    if (!token) {
      setData([]);
      setLoading(false);
      return;
    }

    let isMounted = true;
    setLoading(true);
    setError(null);

    getDispositivos(token)
      .then((allDevices) => {
        if (!isMounted) return;

        // Asegurar que cada dispositivo tenga un ID único
        const devicesWithIds = allDevices.map((device, index) => ({
          ...device,
          idDispositivo: device.idDispositivo ?? index + 1, // Usar index como fallback
        }));

        // Aplicar filtros localmente
        let filteredDevices = devicesWithIds;
        
        if (filtros.categoria && filtros.categoria !== "") {
          filteredDevices = filteredDevices.filter(device => 
            device.categoria === filtros.categoria
          );
        }
        
        if (filtros.estado && filtros.estado !== "") {
          filteredDevices = filteredDevices.filter(device => 
            device.estado === filtros.estado
          );
        }
        
        if (filtros.encargado && filtros.encargado !== "") {
          filteredDevices = filteredDevices.filter(device => 
            device.encargado?.toString() === filtros.encargado
          );
        }

        // Calcular paginación
        const totalItems = filteredDevices.length;
        const calculatedTotalPages = Math.max(1, Math.ceil(totalItems / pageSize));
        const startIndex = page * pageSize;
        const endIndex = startIndex + pageSize;
        const paginatedData = filteredDevices.slice(startIndex, endIndex);

        setData(paginatedData);
        setTotalPages(calculatedTotalPages);
        setTotalElements(totalItems);
      })
      .catch((err) => {
        if (isMounted) {
          setError(err.message || "Error al cargar dispositivos");
        }
      })
      .finally(() => {
        if (isMounted) {
          setLoading(false);
        }
      });

    return () => {
      isMounted = false;
    };
  }, [token, page, pageSize, filtros.categoria, filtros.estado, filtros.encargado]);

  return { 
    data, 
    totalPages, 
    totalElements, 
    loading, 
    error 
  };
}
