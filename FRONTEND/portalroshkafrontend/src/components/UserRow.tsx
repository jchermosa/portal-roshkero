import React from "react";

interface User {
  id: string | number;
  nombre: string;
  apellido: string;
  correo: string;
  rol: string;
  estado: string;
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
      {onEdit && (
        <td className="px-4 py-2 text-blue-600" onClick={(e) => { e.stopPropagation(); onEdit(user.id); }}>
          Editar
        </td>
      )}
    </tr>
  );
};

export default UserRow;
