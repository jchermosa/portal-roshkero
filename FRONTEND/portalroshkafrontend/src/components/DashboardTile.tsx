import React from "react";

interface DashboardTileProps {
  icon: React.ReactNode;
  title: string;
  subtitle?: string;
  buttonText?: string;
  onClick: () => void;
  color?: string; // color de fondo base (ej: bg-purple-500)
}

const DashboardTile: React.FC<DashboardTileProps> = ({
  icon,
  title,
  subtitle,
  buttonText,
  onClick,
  color = "bg-purple-500",
}) => {
  return (
    <div
      className={`p-6 rounded-xl shadow-md ${color} text-white flex flex-col items-center 
                  dark:bg-gray-800 dark:text-gray-100 transition-colors`}
    >
      <div className="text-3xl mb-2">{icon}</div>
      <h3 className="text-xl font-bold">{title}</h3>
      {subtitle && <p className="text-sm opacity-90">{subtitle}</p>}
      {buttonText && (
        <button
          onClick={onClick}
          className="mt-4 px-4 py-2 rounded-lg shadow transition-colors
                     bg-white text-purple-600 hover:opacity-90
                     dark:bg-gray-700 dark:text-gray-100 dark:hover:bg-gray-600"
        >
          {buttonText}
        </button>
      )}
    </div>
  );
};

export default DashboardTile;
