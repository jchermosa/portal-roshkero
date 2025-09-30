import type { SolicitudDispositivoItem } from "../types";
import mockSolicitudesData from "../data/mockSolicitudesDispositivos.json";

const USE_MOCK = import.meta.env.VITE_USE_MOCK === "true";

// Copia mutable de mock
let mockSolicitudes: SolicitudDispositivoItem[] = [
  ...(mockSolicitudesData as SolicitudDispositivoItem[]),
];

// ================================
// Pageable estándar
// ================================
export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

// ================================
// API real
// ================================
async function getSolicitudesDispositivoApi(
  token: string,
  params: Record<string, string | number | undefined> = {}
): Promise<PaginatedResponse<SolicitudDispositivoItem>> {
  const query = new URLSearchParams();
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== "") query.append(k, String(v));
  });

  // ⚡ fuerza el filtro tipo_solicitud=Dispositivo
  query.append("tipo_solicitud", "Dispositivo");

  const res = await fetch(`/api/solicitudes?${query.toString()}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function getSolicitudDispositivoByIdApi(
  token: string,
  id: string
): Promise<SolicitudDispositivoItem> {
  const res = await fetch(`/api/solicitudes/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  const data = await res.json();

  // ⚡ validar que sea tipo Dispositivo
  if (data.tipo_solicitud !== "Dispositivo") {
    throw new Error("La solicitud no es de tipo Dispositivo");
  }
  return data;
}

async function createSolicitudDispositivoApi(
  token: string,
  data: Partial<SolicitudDispositivoItem>
) {
  const payload = { ...data, tipo_solicitud: "Dispositivo" }; // ⚡ fuerza tipo
  const res = await fetch(`/api/solicitudes`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function updateSolicitudDispositivoApi(
  token: string,
  id: string,
  data: Partial<SolicitudDispositivoItem>
) {
  const payload = { ...data, tipo_solicitud: "Dispositivo" }; // ⚡ nunca se pierde
  const res = await fetch(`/api/solicitudes/${id}`, {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// ================================
// Mocks
// ================================
async function getSolicitudesDispositivoMock(): Promise<
  PaginatedResponse<SolicitudDispositivoItem>
> {
  return {
    content: mockSolicitudes,
    totalPages: 1,
    totalElements: mockSolicitudes.length,
    size: mockSolicitudes.length,
    number: 0,
  };
}

async function getSolicitudDispositivoByIdMock(
  _token: string,
  id: string
): Promise<SolicitudDispositivoItem> {
  const s = mockSolicitudes.find((x) => x.id_solicitud === Number(id));
  if (!s) throw new Error("Solicitud no encontrada");
  return s;
}

async function createSolicitudDispositivoMock(
  _token: string,
  data: Partial<SolicitudDispositivoItem>
) {
  const newSolicitud: SolicitudDispositivoItem = {
    ...(data as SolicitudDispositivoItem),
    id_solicitud: mockSolicitudes.length + 1,
    tipo_solicitud: "Dispositivo",
    fecha_creacion: new Date().toISOString(),
  };
  mockSolicitudes.push(newSolicitud);
  return newSolicitud;
}

async function updateSolicitudDispositivoMock(
  _token: string,
  id: string,
  data: Partial<SolicitudDispositivoItem>
) {
  const index = mockSolicitudes.findIndex(
    (s) => s.id_solicitud === Number(id)
  );
  if (index === -1) throw new Error("Solicitud no encontrada");
  mockSolicitudes[index] = {
    ...mockSolicitudes[index],
    ...(data as SolicitudDispositivoItem),
    tipo_solicitud: "Dispositivo",
  };
  return mockSolicitudes[index];
}

// ================================
// Export condicional
// ================================
export const getSolicitudesDispositivo = USE_MOCK
  ? getSolicitudesDispositivoMock
  : getSolicitudesDispositivoApi;
export const getSolicitudDispositivoById = USE_MOCK
  ? getSolicitudDispositivoByIdMock
  : getSolicitudDispositivoByIdApi;
export const createSolicitudDispositivo = USE_MOCK
  ? createSolicitudDispositivoMock
  : createSolicitudDispositivoApi;
export const updateSolicitudDispositivo = USE_MOCK
  ? updateSolicitudDispositivoMock
  : updateSolicitudDispositivoApi;
