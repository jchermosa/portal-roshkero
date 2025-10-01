
export interface IMiembrosEquipo {
    idMiembro: number,
    nombre: string,
    idCargo: number,
    // cargo: string,
    estado: "ACTIVO" | "INACTIVO",
}