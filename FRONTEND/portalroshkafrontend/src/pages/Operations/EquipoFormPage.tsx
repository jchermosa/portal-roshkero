import { useState } from "react";
import FormLayout from "../../layouts/FormLayout";
import { useNavigate } from "react-router-dom";
import DynamicForm from "../../components/DynamicForm";
import type { ICreateTeam } from "./interfaces/ICreateTeam";
import { useAuth } from "../../context/AuthContext";
import DataTable from "../../components/DataTable";
import type { IMiembrosEquipo } from "./interfaces/IMiembrosEquipo";



export default function EquipoFromPage(){

    const { user } = useAuth();

    // ======> Definicion de states <======= //
    const [isEditing, setIsEditing] = useState(true);
    const navigate = useNavigate();

    const [formData, setFormData] = useState<ICreateTeam>({
        nombre: "",
        fechaInicio: "",
        fechaFin: "",
        cliente: 0,
        estado: true
    });


    // ======> Definicion de funciones <======= //
    const getSections = (): any[] => [
    {
      title: isEditing ? "Crear Equipo" : "Nuevo Equipo",
      icon: "📅",
      fields: [
        {
          name: "Nombre",
          label: "Ingrese el nombre del equipo",
          type: "textarea" as const,
          value: formData.nombre ?? "", // Convertir null a string vacío para el input
          required: true,  
        },
        {
          name: "Cliente",
          label: "ID del cliente",
          type: "textarea" as const,
          required: true,
          value: formData.cliente || "",
        },
        {
          name: "fecha_inicio",
          label: "Fecha de inicio",
          type: "date" as const,
          required: true,
        //   disabled: !editable,
          value: formData.fechaInicio || "",
        },
        {
          name: "fecha_fin",
          label: "Fecha de fin",
          type: "date" as const,
          required: true,
        //   disabled: !editable,
          value: formData.fechaFin || "",
        },
        {
          name: "estado",
          label: "Estado",
          type: "checkbox" as const,
          required: true,
          value: formData.estado || true,
        },
      ],
    },
  ];

  const handleFormChange = (updatedData: any) => {
    setFormData((prevData) => ({
      ...prevData,
      ...updatedData,
    }));
  }

    const handleSubmit = async (e: React.FormEvent) => {
      e.preventDefault();
      console.log("Form data submitted:", formData);
      return;
    }

    // ======= > Dinamic Table < ======= //
    const columns = [
        { key: "id", label: "ID" },
        { key: "nombre", label: "Nombre" },
        { key: "idCargo", label: "Cargo" },
        {key: "acciones",
          label: "Acciones",
          render: (s: IMiembrosEquipo) => (
            <button
              onClick={() => navigate(`/removeFromTeam/${s.idMiembro}`)}
              className="px-3 py-1 text-sm text-white bg-blue-600 rounded hover:bg-blue-700"
            >
              Eliminar
            </button>
          ),
        }
    ];

    const equipo = {
        miembros: [
            {
                id: 1,
                nombre: "Miembro 1",
                idCargo: 1
            },
            {
                id: 2,
                nombre: "Miembro 2",
                idCargo: 2
            }
        ]
    };

  return (<>

    <FormLayout
        title={isEditing ? "Crear Equipo" : "Editar Equipo"}
        subtitle={
            isEditing
            ? "Modifica los campos necesarios"
            : "Completá la información de la nueva solicitud"
        }
        icon={isEditing ? "✏️" : "🧑‍💻"}
        onCancel={() => navigate("/operations")}
        onSubmitLabel={isEditing ? "Guardar cambios" : "Enviar solicitud"}
        >
        <DynamicForm
            // Using a stable key that won't change with form input
            key="create-team-form"
            id="equipo-form"
            sections={getSections()}
            initialData={formData}
            onSubmit={handleSubmit}
            onChange={handleFormChange}
        />


        {isEditing &&(
            <DataTable
              data={equipo.miembros}
              columns={columns}
              rowKey={(s) => s.id}
              scrollable={false}
            />
        )}


        </FormLayout>
  
    </>);
};
