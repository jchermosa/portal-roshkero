interface IconButtonProps {
  label: string;
  icon?: React.ReactNode;
  onClick?: () => void;
  className?: string;
  type?: "button" | "submit" | "reset";
  variant?: "primary" | "secondary" | "danger" | "ghost";
  disabled?: boolean;
}

export default function IconButton({
  label,
  icon,
  onClick,
  className = "",
  type = "button",
  variant = "primary",
}: IconButtonProps) {
  const baseStyles = "px-4 py-2 rounded-lg font-medium transition flex items-center gap-2";
  const variants: Record<typeof variant, string> = {
    primary: "bg-green-600 text-white hover:bg-green-700",
    secondary: "bg-gray-200 text-black hover:bg-gray-300",
    danger: "bg-red-600 text-white hover:bg-red-700",
    ghost: "bg-transparent text-gray-700 hover:bg-gray-100",
  };

  return (
    <button
      type={type}
      onClick={onClick}
      className={`${baseStyles} ${variants[variant]} ${className}`}
    >
      {icon && <span>{icon}</span>}
      <span>{label}</span>
    </button>
  );
}


// 🎯 ICONOS DISPONIBLES (pueden usarse como <span>icon</span>)

// ➕   Crear, agregar nuevo
// 🖊️   Editar
// 🗑️   Eliminar
// 🧹   Limpiar filtros o campos
// 🔍   Buscar
// 📤   Enviar o exportar
// 📥   Importar o recibir
// ✅   Confirmar
// ❌   Cancelar o cerrar
// 🔄   Recargar o refrescar
// ⏳   Cargando, procesando
// 👁️   Ver detalles
// 🧾   Ver comprobante, factura
// 🧑‍🤝‍🧑   Usuarios
// 📄   Documentos
// 📅   Fecha / calendario
// 💾   Guardar
// ⚙️   Configurar o ajustar
// 🔒   Bloquear
// 🔓   Desbloquear
// 📋   Copiar
// 📊   Reportes o estadísticas
// utilizacion del boton en Pages
{/* <IconButton
  label="Limpiar filtros"
  icon={<span>🧹</span>}
  variant="secondary"
  onClick={limpiarFiltros}
/> */}
