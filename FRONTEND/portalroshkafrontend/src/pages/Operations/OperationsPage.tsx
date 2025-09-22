import { useState } from "react";
import DataTable from "../../components/DataTable";
import SelectDropdown from "../../components/SelectDropdown";
import type { IListTeams } from "./interfaces/IListTeams";
import { useNavigate } from "react-router-dom";
import IconButton from "../../components/IconButton";

const OperationsPage = () => {

    const navigate = useNavigate();

    const equipos = [
        { idTeam: 1, nombre: "Equipo A", teamLeader: "Juan Pérez", cliente: "Cliente X", estado: true },
        { idTeam: 2, nombre: "Equipo B", teamLeader: "María Gómez", cliente: "Cliente Y", estado: false },
        { idTeam: 3, nombre: "Equipo C", teamLeader: "Pedro López", cliente: "Cliente Z", estado: true },
      ];

  const tiposUnicos: { value: string; label: string }[] = [
    { value: "Cliente", label: "Cliente" },
    { value: "Estado", label: "Estado" },
    { value: "Team Leader", label: "Team Leader" },
  ];
  

  // const tiposUnicos: { value: string; label: string }[] =
  // solicitudes.length > 0
  //   ? Array.from(
  //       new Set(
  //         solicitudes
  //           .map((s) => s.tipo?.nombre)
  //           .filter((nombre): nombre is string => typeof nombre === "string")
  //       )
  //     ).map((nombre) => ({
  //       value: nombre,
  //       label: nombre,
  //     }))
  //   : [];

  const [tipo, setTipo] = useState<string>("");

//     const historialSolicitudes: IListTeams[] = [
//     { idTeam: 1, nombre: "Equipo A", teamLeader: "Juan Pérez", cliente: "Cliente X", estado: true },
//     { idTeam: 2, nombre: "Equipo B", teamLeader: "María Gómez", cliente: "Cliente Y", estado: false },
//     { idTeam: 3, nombre: "Equipo C", teamLeader: "Pedro López", cliente: "Cliente Z", estado: true },
//   ];

  const columns = [
    {key:"idTeam", label:"id"},
    {key:"nombre", label:"Nombre"},
    {key:"teamLeader", label:"Team Leader"},
    {key:"cliente", label:"Cliente"},
    {key:"estado", label:"Estado"},
    {
    key: "acciones",
    label: "Acciones",
    render: (s: IListTeams) => (
      <button
        onClick={() => navigate(`/equipo/${s.idTeam}`)}
        className="px-3 py-1 text-sm text-white bg-blue-600 rounded hover:bg-blue-700"
      >
        Editar
      </button>
    ),
  },
  ]

  const limpiarFiltros = () => {
    setTipo("");
  };

  return (
    <>
    
    <div className="h-full flex flex-col overflow-hidden">
      <div
        className="absolute inset-0 bg-brand-blue"
        style={{
          backgroundImage: "url('/src/assets/ilustracion-herov3.svg')",
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
        }}
      >
        <div className="absolute inset-0 bg-brand-blue/40"></div>
      </div>

      <div className="relative z-10 flex flex-col h-full p-4">
        <div className="bg-white/45 dark:bg-gray-900/70 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col h-full overflow-hidden">
          <div className="p-6 border-b border-gray-200 flex-shrink-0">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-2xl font-bold text-brand-blue">
                Equipos
              </h2>
              <IconButton
                label="Nuevo Equipo"
                icon={<span>➕</span>}
                variant="primary"
                onClick={() => navigate("/equipo/nuevo")}
                className="h-10 text-sm px-4 flex items-center"
              />
            </div>

            <div className="flex items-center gap-4">
              <SelectDropdown
                label="Tipo"
                name="tipo"
                value={tipo}
                onChange={(e) => setTipo(e.target.value)}
                options={tiposUnicos}
                placeholder="Filtrar por"
              />

              <div className="mb-4">
                <label className="block text-sm font-medium text-transparent mb-1 select-none">
                  &nbsp;
                </label>
                <IconButton
                  label="Limpiar filtros"
                  icon={<span>🧹</span>}
                  variant="secondary"
                  onClick={limpiarFiltros}
                  className="h-10 text-sm px-4 flex items-center"
                />
              </div>
            </div>
          </div>

          <div className="flex-1 overflow-auto p-6">
            <DataTable
              data={equipos}
              columns={columns}
              rowKey={(s) => s.idTeam}
              scrollable={false}
            />
          </div>

          {/* <div className="p-6 border-t border-gray-200 flex-shrink-0">
            <PaginationFooter
              currentPage={page}
              totalPages={totalPages}
              onPageChange={setPage}
              onCancel={() => navigate(-1)}
            />
          </div> */}
        </div>
      </div>
    </div>

    </>
  )
};

export default OperationsPage;