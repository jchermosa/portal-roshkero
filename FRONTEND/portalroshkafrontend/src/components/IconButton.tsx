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

  const baseStyles =
    "px-4 py-2 rounded-lg font-medium transition flex items-center gap-2";

  const variants: Record<NonNullable<IconButtonProps["variant"]>, string> = {
    primary: "bg-green-600 text-white hover:bg-green-700 dark:bg-green-500 dark:hover:bg-green-600",
    secondary: "bg-gray-200 text-black hover:bg-gray-300 dark:bg-gray-700 dark:text-gray-100 dark:hover:bg-gray-600",
    danger: "bg-red-600 text-white hover:bg-red-700 dark:bg-red-500 dark:hover:bg-red-600",
    ghost: ""
  };

  return (
    <button
      type={type}
      onClick={onClick}
      className={`${baseStyles} ${variants[variant]} ${className}`}
    ><link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200&icon_names=add" />
      {icon && <span className="material-symbols-outlined">
add
</span>}
      <span>{label}</span>
    </button>
  );
}
