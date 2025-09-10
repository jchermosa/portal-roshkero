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
      disabled={loading || props.disabled}
      className={`w-full bg-gradient-to-r from-yellow-400 to-yellow-500 text-blue-900 font-bold py-4 rounded-full hover:from-yellow-500 hover:to-yellow-600 transition-all duration-200 shadow-lg disabled:opacity-60 disabled:cursor-not-allowed text-lg ${
        props.className || ""
      }`}
    >
      {loading ? (
        <div className="flex items-center justify-center">
          <div className="animate-spin rounded-full h-5 w-5 border-2 border-blue-900 border-t-transparent mr-2"></div>
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
