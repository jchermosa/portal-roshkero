import type { ButtonHTMLAttributes, ReactNode } from "react";

type LoadingButtonProps = {
  text: string;
  loading?: boolean;
  icon?: ReactNode;
} & ButtonHTMLAttributes<HTMLButtonElement>;

export default function LoadingButton({
  text,
  loading = false,
  icon,
  ...props
}: LoadingButtonProps) {
  return (
    <button
      {...props}
      aria-busy={loading}
      disabled={loading || props.disabled}
      className={`w-full font-bold py-4 rounded-full transition-all duration-200 shadow-lg disabled:opacity-60 disabled:cursor-not-allowed text-lg
      bg-gradient-to-r from-yellow-400 to-yellow-500 hover:from-yellow-500 hover:to-yellow-600 text-blue-900
      dark:from-blue-600 dark:to-blue-700 dark:hover:from-blue-700 dark:hover:to-blue-800 dark:text-white
      ${props.className || ""}`}
    >
      {loading ? (
        <div className="flex items-center justify-center">
          <div className="animate-spin rounded-full h-5 w-5 border-2 border-blue-900 dark:border-white border-t-transparent mr-2"></div>
          Ingresando...
        </div>
      ) : (
        <div className="flex items-center justify-center gap-2">
          {icon}
          {text}
        </div>
      )}
    </button>
  );
}
