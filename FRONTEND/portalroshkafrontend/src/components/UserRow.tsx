import React from "react";

interface User {
  id: string | number;
  nombre: string;
  apellido: string;
  correo: string;
  rol: string;
  estado: string;
  antiguedadPretty?: string;
}

interface UserRowProps {
  user: User;
  onEdit?: (id: string | number) => void;
  onClick?: (id: string | number) => void;
}

const UserRow: React.FC<UserRowProps> = ({ user, onEdit, onClick }) => {
  const isActive = user.estado === "activo";

  return (
    <tr
      className="hover:bg-gray-100 dark:hover:bg-gray-700 cursor-pointer border-b border-gray-200 dark:border-gray-700"
      onClick={() => onClick && onClick(user.id)}
    >
      <td className="px-4 py-2 text-gray-800 dark:text-gray-200">{user.nombre}</td>
      <td className="px-4 py-2 text-gray-800 dark:text-gray-200">{user.apellido}</td>
      <td className="px-4 py-2 text-gray-800 dark:text-gray-200">{user.correo}</td>
      <td className="px-4 py-2 text-gray-800 dark:text-gray-200">{user.rol}</td>
      <td className="px-4 py-2 text-gray-800 dark:text-gray-200">{user.estado}</td>
      <td className="px-4 py-2 text-gray-800 dark:text-gray-200">
        {user.antiguedadPretty ?? "-"}
      </td>
      <td className="px-4 py-2">
        {isActive ? (
          <span className="px-2 py-1 text-xs font-medium bg-green-100 text-green-700 dark:bg-green-900 dark:text-green-300 rounded-full">
            Activo
          </span>
        ) : (
          <span className="px-2 py-1 text-xs font-medium bg-red-100 text-red-700 dark:bg-red-900 dark:text-red-300 rounded-full">
            Inactivo
          </span>
        )}
      </td>
      {onEdit && (
        <td
          className="px-4 py-2 text-blue-600 dark:text-blue-400 hover:underline"
          onClick={(e) => {
            e.stopPropagation();
            onEdit(user.id);
          }}
        >
          Editar
        </td>
      )}
    </tr>
  );
};

export default UserRow;
