// src/pages/BeneficiosPage.tsx
import { useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import PaginationFooter from "../components/PaginationFooter";
import IconButton from "../components/IconButton";
// Carga masiva de imágenes desde src/assets/*.png
const images = import.meta.glob("/src/assets/*.png", {
  eager: true,
  import: "default",
}) as Record<string, string>;

const getImage = (fileName: string) => images[`/src/assets/${fileName}`] ?? "";

type Benefit = {
  id: number;
  nombre: string;
  descripcion: string;
  imagen: string; // solo el nombre del archivo .png
};

const DATA: Benefit[] = [
  { id: 1, nombre: "Horarios", descripcion: "Esquemas flexibes mientras no afecte tu productividad", imagen: "image1.png" },
  { id: 2, nombre: "Reposos", descripcion: "Roshka cubre el otro 50% que ips no cubre!", imagen: "image2.png" },
  { id: 3, nombre: "Cumpleaños", descripcion: "Dia libre por tu cumpleaños!", imagen: "image3.png" },
  { id: 4, nombre: "Examenes Finales o Tesis", descripcion: "Dias libres para concentrarte en tus estudios", imagen: "image4.png" },
  { id: 5, nombre: "Matrimonio", descripcion: "Dias libres y bonos!", imagen: "image5.png" },
  { id: 6, nombre: "Luto", descripcion: "Dias de licencia para apoyar al trabajador y ayuda social", imagen: "image6.png" },
  { id: 7, nombre: "Permiso por nacimiento", descripcion: "Dias  libres y obsequios!", imagen: "image7.png" },
  { id: 8, nombre: "Viernes 4 you", descripcion: "Un viernes de salida temprana al mes!", imagen: "image8.png" },
  { id: 9, nombre: "Prestamos", descripcion: "Prestamos sin intereses!", imagen: "image9.png" },
  { id: 10, nombre: "Capacitaciones", descripcion: "Financion y 50 horas libres al año", imagen: "image10.png" },
  { id: 11, nombre: "Nutricionista", descripcion: "Convenio con nutricionista y cuota automatica", imagen: "image11.png" },
  { id: 12, nombre: "Gym", descripcion: "Convenio con el gym FITTEST", imagen: "image12.png" },
  { id: 13, nombre: "Graduacion", descripcion: "Regalos para los nuevos egresados", imagen: "image13.png" },
  { id: 14, nombre: "Frutas y snacks", descripcion: "Snacks para merienda y desayuno saludable", imagen: "image14.png" },
];

const PAGE_SIZE = 14;

export default function BeneficiosPage() {
  const navigate = useNavigate();
  const [page, setPage] = useState(0); // 0-based
  const [seleccionado, setSeleccionado] = useState<Benefit | null>(null);

  const totalPages = Math.max(1, Math.ceil(DATA.length / PAGE_SIZE));
  const slice = useMemo(() => {
    const start = page * PAGE_SIZE;
    return DATA.slice(start, start + PAGE_SIZE);
  }, [page]);

  // Detalle
  if (seleccionado) {
    return (
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
          <div className="absolute inset-0 bg-brand-blue/40" />
        </div>

        <div className="relative z-10 flex flex-col h-full p-4">
          <div className="bg-white/80 dark:bg-gray-900/70 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col h-full overflow-hidden">
            <div className="p-6 border-b border-gray-200 dark:border-gray-700 flex-shrink-0">
              <h2 className="text-2xl font-bold text-brand-blue dark:text-white">
                {seleccionado.nombre}
              </h2>
              <p className="mt-2 text-gray-700 dark:text-gray-200">
                {seleccionado.descripcion}
              </p>
            </div>

            <div className="flex-1 overflow-auto p-6 flex items-center justify-center">
              <img
                src={getImage(seleccionado.imagen)}
                alt={seleccionado.nombre}
                className="w-full max-w-xl max-h-[800px] object-contain rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm"
              />
            </div>

            <div className="p-6 border-t border-gray-200 dark:border-gray-700 flex-shrink-0 flex justify-end">
              <button
                onClick={() => setSeleccionado(null)}
                className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
              >
                Volver a la lista
              </button>
            </div>
          </div>
        </div>
      </div>
    );
  }

  function handleSolicitudClick(): void {
    navigate("/beneficios/nuevo");
  }

  // Lista
  return (
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
        <div className="absolute inset-0 bg-brand-blue/40" />
      </div>

      <div className="relative z-10 flex flex-col h-full p-4">
        <div className="bg-white/45 dark:bg-gray-900/70 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col h-full overflow-hidden">
          <div className="p-6 border-b border-gray-200 dark:border-gray-700 flex-shrink-0 flex items-center justify-between">
                  <h2 className="text-2xl font-bold text-brand-blue dark:text-white">
                    Beneficios
                  </h2>
                  <IconButton
                    label="Solicitar Beneficio"
                    icon={<span>➕</span>}
                    variant="primary"
                    onClick={handleSolicitudClick}
                  />
                </div>


          <div className="flex-1 overflow-auto p-6">
            <div className="rounded-2xl overflow-hidden shadow bg-white dark:bg-gray-800">
              <table className="min-w-full border-collapse">
                <thead className="bg-blue-100 dark:bg-blue-900">
                  <tr>
                    <th className="px-4 py-3 text-left text-sm font-semibold text-blue-700 dark:text-blue-100">
                      Beneficio
                    </th>
                    <th className="px-4 py-3 text-left text-sm font-semibold text-blue-700 dark:text-blue-100">
                      Descripción
                    </th>
                  </tr>
                </thead>
                <tbody className="text-gray-800 dark:text-gray-200">
                  {slice.length === 0 ? (
                    <tr>
                      <td
                        colSpan={2}
                        className="px-4 py-6 text-center text-gray-400 dark:text-gray-500"
                      >
                        No hay resultados.
                      </td>
                    </tr>
                  ) : (
                    slice.map((b) => (
                      <tr
                        key={b.id}
                        className="hover:bg-gray-50 dark:hover:bg-gray-700/40 border-t border-gray-200 dark:border-gray-700"
                      >
                        <td className="px-4 py-2">
                          <button
                            className="text-blue-600 dark:text-blue-400 hover:underline"
                            onClick={() => setSeleccionado(b)}
                          >
                            {b.nombre}
                          </button>
                        </td>
                        <td className="px-4 py-2">{b.descripcion}</td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>
          </div>

          <div className="p-6 border-t border-gray-200 dark:border-gray-700 flex-shrink-0">
            <PaginationFooter
              currentPage={page}
              totalPages={totalPages}
              onPageChange={setPage}
              onCancel={() => navigate(-1)}
            />
          </div>
        </div>
      </div>
    </div>
  );
}
