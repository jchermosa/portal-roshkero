export const Roles = {
  TALENTO_HUMANO: "TALENTO_HUMANO",
  DESARROLLO: "DESARROLLO",
  OPERACIONES: "OPERACIONES",
  ADMINISTRADOR_DEL_SISTEMA: "ADMINISTRADOR_DEL_SISTEMA",
  DIRECTIVO: "DIRECTIVO",
  ADMINISTRATIVO: "ADMINISTRATIVO",
} as const;

export type Roles = typeof Roles[keyof typeof Roles];


// Conversión entre DB ↔ Frontend
export const RolesMap: Record<string, Roles> = {
  "TALENTO HUMANO": Roles.TALENTO_HUMANO,
  "DESARROLLO": Roles.DESARROLLO,
  "OPERACIONES": Roles.OPERACIONES,
  "ADMINISTRADOR DEL SISTEMA": Roles.ADMINISTRADOR_DEL_SISTEMA,
  "DIRECTIVO": Roles.DIRECTIVO,
  "ADMINISTRATIVO": Roles.ADMINISTRATIVO,
};

// Opcional: para mostrar labels legibles en la UI
export const RolesLabels: Record<Roles, string> = {
  [Roles.TALENTO_HUMANO]: "Talento Humano",
  [Roles.DESARROLLO]: "Desarrollo",
  [Roles.OPERACIONES]: "Operaciones",
  [Roles.ADMINISTRADOR_DEL_SISTEMA]: "Administrador del Sistema",
  [Roles.DIRECTIVO]: "Directivo",
  [Roles.ADMINISTRATIVO]: "Administrativo",
};
