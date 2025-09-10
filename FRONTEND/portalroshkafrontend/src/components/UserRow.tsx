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
  return (
    <tr
      className="hover:bg-gray-100 cursor-pointer"
      onClick={() => onClick && onClick(user.id)}
    >
      <td className="px-4 py-2">{user.nombre}</td>
      <td className="px-4 py-2">{user.apellido}</td>
      <td className="px-4 py-2">{user.correo}</td>
      <td className="px-4 py-2">{user.rol}</td>
      <td className="px-4 py-2">{user.estado}</td>
      <td className="px-4 py-2">{user.antiguedadPretty ?? "-"}</td>
      <td className="px-4 py-2">
        {user.estado ? (
          <span className="px-2 py-1 text-xs font-medium bg-green-100 text-green-700 rounded-full">
            Activo
          </span>
        ) : (
          <span className="px-2 py-1 text-xs font-medium bg-red-100 text-red-700 rounded-full">
            Inactivo
          </span>
        )}
      </td>
      {onEdit && (
        <td className="px-4 py-2 text-blue-600" onClick={(e) => { e.stopPropagation(); onEdit(user.id); }}>
          Editar
        </td>
      )}
    </tr>
  );
};

export default UserRow;
