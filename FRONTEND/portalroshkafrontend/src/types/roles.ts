export const Roles = {
  TH: "TH",
  GTH: "GTH",
  OPERACIONES: "OPERACIONES",
  DIRECTORIO: "DIRECTORIO",
  LIDER: "LIDER",
  FUNCIONARIO_FABRICA: "FUNCIONARIO_FABRICA",
  FUNCIONARIO_TERCERIZADO: "FUNCIONARIO_TERCERIZADO",
} as const;

export type Roles = typeof Roles[keyof typeof Roles];
