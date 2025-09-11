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
    primary:
      "bg-green-600 text-white hover:bg-green-700 dark:bg-green-500 dark:hover:bg-green-600",
    secondary:
      "bg-gray-200 text-black hover:bg-gray-300 dark:bg-gray-700 dark:text-gray-100 dark:hover:bg-gray-600",
    danger:
      "bg-red-600 text-white hover:bg-red-700 dark:bg-red-500 dark:hover:bg-red-600",

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
