import { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { useSolicitudesTH } from "../../hooks/solicitudes/useSolicitudesTH";
import { useCatalogosSolicitudes } from "../../hooks/catalogos/useCatalogosSolicitudes";
import { confirmarSolicitudVacaciones } from "../../services/RequestTHService";
import { tieneRol } from "../../utils/permisos";
import { Roles } from "../../types/roles";

import DataTable from "../../components/DataTable";
import PaginationFooter from "../../components/PaginationFooter";
import SelectDropdown from "../../components/SelectDropdown";
import IconButton from "../../components/IconButton";
import PageLayout from "../../layouts/PageLayout";
import type { SolicitudItem } from "../../types/";

export default function SolicitudesTHPage() {
  const { token, user } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  let tipoSolicitud: "PERMISO" | "BENEFICIO" | "VACACIONES";

  if (location.pathname.includes("permisos")) tipoSolicitud = "PERMISO";
  else if (location.pathname.includes("beneficios")) tipoSolicitud = "BENEFICIO";
  else if (location.pathname.includes("vacaciones")) tipoSolicitud = "VACACIONES";
  else tipoSolicitud = "PERMISO";

  const [subTipo, setSubTipo] = useState("");
  const [estado, setEstado] = useState("");
  const [page, setPage] = useState(0);
  const [procesando, setProcesando] = useState<number | null>(null);

  const puedeVerSolicitudes = tieneRol(user, Roles.TH, Roles.GTH);

  const { tiposPermiso, tiposBeneficio, loading: loadingCatalogos } = useCatalogosSolicitudes(token);

  const opcionesTipoEspecifico =
    tipoSolicitud === "PERMISO"
      ? tiposPermiso.map((t) => ({ value: t.nombre, label: t.nombre }))
      : tipoSolicitud === "BENEFICIO"
      ? tiposBeneficio.map((t) => ({ value: t.nombre, label: t.nombre }))
      : [];

  const { data: solicitudes, totalPages, loading, error, refetch } = useSolicitudesTH(
    token,
    { tipoSolicitud, subTipo, estado },
    page
  );

  const handleConfirmarVacacion = async (idSolicitud: number, confirmado: boolean) => {
    if (!token || confirmado) return;

    const confirmacion = window.confirm("¿Confirmar esta solicitud de vacaciones?");
    if (!confirmacion) return;

    setProcesando(idSolicitud);
    try {
      await confirmarSolicitudVacaciones(token, idSolicitud.toString());
      // Recargar la lista
      if (refetch) refetch();
    } catch (err) {
      console.error("Error al confirmar:", err);
      alert("Error al confirmar la solicitud");
    } finally {
      setProcesando(null);
    }
  };

  const limpiarFiltros = () => {
    setSubTipo("");
    setEstado("");
    setPage(0);
  };

  const columns = [
    { key: "idSolicitud", label: "ID" },
    {
      key: "usuario",
      label: "Usuario",
      render: (s: SolicitudItem) => s.nombreUsuario || s.usuario || "-",
    },
    {
      key: "tipoSolicitud",
      label: "Tipo",
      render: (s: SolicitudItem) => s.tipoSolicitud,
    },
    ...(tipoSolicitud === "PERMISO" || tipoSolicitud === "BENEFICIO"
      ? [
          {
            key: "subTipo",
            label: "Subtipo",
            render: (s: SolicitudItem) => s.nombreSubTipoSolicitud || s.subTipo || "-",
          },
        ]
      : []),
    {
      key: "estado",
      label: "Estado",
      render: (s: SolicitudItem) => {
        const estados = { P: "Pendiente", A: "Aprobada", R: "Rechazada", RC: "Recalendarizada"};
        const colores = {
          P: "bg-yellow-100 text-yellow-700",
          A: "bg-green-100 text-green-700",
          R: "bg-red-100 text-red-700",
          RC: "bg-orange-100 text-red-700"
        };
        return (
          <span
            className={`px-2 py-1 text-xs font-medium rounded-full ${
              colores[s.estado] || "bg-gray-100 text-gray-700"
            }`}
          >
            {estados[s.estado] || s.estado}
          </span>
        );
      },
    },
    // Columna de confirmación TH solo para VACACIONES
    ...(tipoSolicitud === "VACACIONES"
      ? [
          {
            key: "confirmacionTh",
            label: "Confirmado TH",
            render: (s: SolicitudItem) => (
              <input
                type="checkbox"
                checked={s.confirmacionTh || false}
                onChange={() => handleConfirmarVacacion(s.idSolicitud, s.confirmacionTh || false)}
                disabled={s.estado !== "A" || s.confirmacionTh || procesando === s.idSolicitud}
                className="w-5 h-5 cursor-pointer disabled:cursor-not-allowed disabled:opacity-50"
              />
            ),
          },
        ]
      : []),
  ];

  const renderActions = (s: SolicitudItem) => {
    
    // Para solicitudes pendientes
    if (s.estado === "P") {
      return (
        <button
          onClick={() => navigate(`/solicitudesTH/${s.idSolicitud}/evaluar`)}
          className="w-16 px-3 py-1 bg-blue-600 text-white rounded-lg text-xs hover:bg-blue-700 transition-colors duration-200"
        >
          Evaluar
        </button>
      );
    } else {
      return (
        <button
          onClick={() => navigate(`/solicitudesTH/${s.idSolicitud}/ver`)}
          className="w-16 px-3 py-1 bg-gray-400 text-white rounded-lg text-xs hover:bg-gray-500 transition-colors duration-200"
        >
          Ver
        </button>
      );
    }
  };

  if (loading || loadingCatalogos) return <p>Cargando solicitudes...</p>;
  if (error) return <p>{error}</p>;

  const estadosOptions = [
    { value: "P", label: "Pendiente" },
    { value: "A", label: "Aprobada" },
    { value: "R", label: "Rechazada" },
    { value: "RC", label: "Recalendarizada"}
  ];

  return (
    <PageLayout title="Gestión de Solicitudes">
      <div className="flex items-center gap-4 mb-4">
        {tipoSolicitud && opcionesTipoEspecifico.length > 0 && (
          <SelectDropdown
            name="subTipo"
            label="Tipo"
            value={subTipo}
            onChange={(e) => {
              setSubTipo(e.target.value);
              setPage(0);
            }}
            options={opcionesTipoEspecifico}
            placeholder={`Filtrar ${tipoSolicitud.toLowerCase()}`}
          />
        )}

        <SelectDropdown
          name="estado"
          label="Estado"
          value={estado}
          onChange={(e) => {
            setEstado(e.target.value);
            setPage(0);
          }}
          options={estadosOptions}
          placeholder="Filtrar por Estado"
        />

        <IconButton
          label="Limpiar filtros"
          icon={<span>🧹</span>}
          variant="secondary"
          onClick={limpiarFiltros}
          className="h-10 text-sm px-4 flex items-center"
        />
      </div>

      <DataTable
        data={solicitudes}
        columns={columns}
        rowKey={(s) => s.idSolicitud}
        actions={renderActions}
        scrollable={false}
      />

      <PaginationFooter
        currentPage={page}
        totalPages={totalPages}
        onPageChange={setPage}
        onCancel={() => navigate(-1)}
      />
    </PageLayout>
  );
}