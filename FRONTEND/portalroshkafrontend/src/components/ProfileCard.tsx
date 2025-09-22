import React from "react";

interface CardProps {
  title: string;
  icon: string;
  color: string;
  description?: React.ReactNode;
  extraInfo?: React.ReactNode;
  onClick: () => void;
  borderClass: string; // ej: "border-blue-600"
  buttonClass: string; // ej: "bg-blue-600 hover:bg-blue-700"
}

const ProfileCard: React.FC<CardProps> = ({
  title,
  icon,
  description,
  extraInfo,
  onClick,
  borderClass,
  buttonClass,
}) => {
  return (
    <div
      className={`bg-white dark:bg-gray-800 p-6 rounded-xl shadow-md border-l-4 ${borderClass} hover:shadow-lg transition-shadow`}
    >
      <div className="flex items-center mb-3">
        <span className="text-2xl mr-3">{icon}</span>
        <h3 className="text-lg font-semibold text-gray-800 dark:text-gray-100">
          {title}
        </h3>
      </div>

      {description && (
        <p className="text-gray-600 dark:text-gray-300 text-sm mb-2">
          {description}
        </p>
      )}

      {extraInfo && (
        <div className="text-gray-600 dark:text-gray-300 text-sm mb-4">
          {extraInfo}
        </div>
      )}

      <button
        onClick={onClick}
        className={`w-full ${buttonClass} text-white py-2 px-4 rounded-lg transition-colors`}
      >
        Ver {title.toLowerCase()}
      </button>
    </div>
  );
};

export default ProfileCard;
