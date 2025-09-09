import React from "react";

interface UserCardProps {
  id: string | number;
  nombre: string;
  apellido: string;
  correo: string;
  rol: string;
  estado: string;
  onEdit?: (id: string | number) => void;
  onClick?: (id: string | number) => void;
}

const UserCard: React.FC<UserCardProps> = ({
  id,
  nombre,
  apellido,
  correo,
  rol,
  estado,
  onEdit,
  onClick,
}) => {
  return (
    <div
      onClick={() => onClick && onClick(id)}
      className="p-4 border rounded-lg shadow-md hover:shadow-lg transition cursor-pointer"
    >
      <h3 className="text-lg font-bold">{nombre} {apellido}</h3>
      <p className="text-sm text-gray-600">{correo}</p>
      <p className="text-sm">Rol: {rol}</p>
      <p className={`text-sm ${estado === "activo" ? "text-green-600" : "text-red-600"}`}>
        Estado: {estado}
      </p>
      {onEdit && (
        <button
          onClick={(e) => { e.stopPropagation(); onEdit(id); }}
          className="mt-2 px-3 py-1 bg-purple-500 text-white rounded-lg"
        >
          Editar
        </button>
      )}
    </div>
  );
};

export default UserCard;
