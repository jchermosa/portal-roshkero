// src/pages/RequestManagementPage.tsx
import { useEffect, useMemo, useState } from "react";
import { useAuth } from "../context/AuthContext";
import DataTable from "../components/DataTable";
import IconButton from "../components/IconButton";
import DynamicForm, { type FormSection } from "../components/DynamicForm";
import PaginationFooter from "../components/PaginationFooter";

type Estado = "P" | "A" | "R";

interface Solicitud {
  id: number;
  tipo: string;
  usuarioNombre: string;
  fechaInicio?: string;
  fechaFin?: string;
  fecha?: string;
  comentario?: string;
  estado: Estado;
  creadaEl: string;
}

export default function RequestManagementPage() {
  const { token } = useAuth();

  const [items, setItems] = useState<Solicitud[]>([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(false);
  const [selected, setSelected] = useState<Solicitud | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!token) return;
    setLoading(true);
    setError(null);

    const params = new URLSearchParams();
    params.set("page", String(page));
    params.set("size", "10");

    fetch(`/api/solicitudes?${params.toString()}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(async (r) => {
        if (!r.ok) throw new Error(await r.text());
        return r.json();
      })
      .then((data) => {
        setItems(data.content ?? data.items ?? []);
        setTotalPages(data.totalPages ?? 1);
      })
      .catch((e: any) => setError(e?.message ?? "Error al cargar solicitudes"))
      .finally(() => setLoading(false));
  }, [token, page]);

  const columns = useMemo(
    () => [
      { key: "id", label: "ID" },
      { key: "usuarioNombre", label: "Solicitante" },
      {
        key: "tipo",
        label: "Tipo",
        render: (s: Solicitud) => s.tipo?.charAt(0).toUpperCase() + s.tipo?.slice(1),
      },
      {
        key: "fechas",
        label: "Fechas",
        render: (s: Solicitud) =>
          s.fecha ? s.fecha : s.fechaInicio && s.fechaFin ? `${s.fechaInicio} → ${s.fechaFin}` : "-",
      },
      {
        key: "estado",
        label: "Estado",
        render: (s: Solicitud) => {
          const map: Record<Estado, { label: string; cls: string }> = {
            P: { label: "Pendiente", cls: "bg-yellow-100 text-yellow-700 dark:bg-yellow-900 dark:text-yellow-300" },
            A: { label: "Aprobada", cls: "bg-green-100 text-green-700 dark:bg-green-900 dark:text-green-300" },
            R: { label: "Rechazada", cls: "bg-red-100 text-red-700 dark:bg-red-900 dark:text-red-300" },
          };
          const v = map[s.estado] ?? map.P;
          return <span className={`px-2 py-1 text-xs font-medium rounded-full ${v.cls}`}>{v.label}</span>;
        },
      },
      { key: "creadaEl", label: "Creada" },
    ],
    []
  );

  const detailSections = (s: Solicitud): FormSection[] => {
    const base = [
      { name: "id", label: "ID", type: "text" as const, disabled: true, value: String(s.id) },
      { name: "solicitante", label: "Solicitante", type: "text" as const, disabled: true, value: s.usuarioNombre },
      { name: "tipo", label: "Tipo", type: "text" as const, disabled: true, value: s.tipo },
      { name: "estado", label: "Estado", type: "text" as const, disabled: true, value: s.estado },
      { name: "creadaEl", label: "Creada el", type: "text" as const, disabled: true, value: s.creadaEl },
    ];

    const rango =
      s.fechaInicio && s.fechaFin
        ? [
            { name: "fecha_inicio", label: "Fecha inicio", type: "text" as const, disabled: true, value: s.fechaInicio },
            { name: "fecha_fin", label: "Fecha fin", type: "text" as const, disabled: true, value: s.fechaFin },
          ]
        : s.fecha
        ? [{ name: "fecha", label: "Fecha", type: "text" as const, disabled: true, value: s.fecha }]
        : [];

    const extra = s.comentario
      ? [{ name: "comentario", label: "Comentario", type: "textarea" as const, disabled: true, value: s.comentario }]
      : [];

    return [
      {
        title: "Detalle de Solicitud",
        icon: "📄",
        className: "grid-cols-1 md:grid-cols-2",
        fields: [...base, ...rango, ...extra],
      },
    ];
  };

  const aprobar = async (id: number) => {
    if (!token) return;
    setLoading(true);
    try {
      const r = await fetch(`/api/solicitudes/${id}/aprobar`, {
        method: "POST",
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!r.ok) throw new Error(await r.text());
      setItems((p) => p.map((x) => (x.id === id ? { ...x, estado: "A" } : x)));
      setSelected((s) => (s && s.id === id ? { ...s, estado: "A" } : s));
    } catch (e: any) {
      alert(e?.message ?? "Error al aprobar");
    } finally {
      setLoading(false);
    }
  };

  const rechazar = async (id: number) => {
    if (!token) return;
    setLoading(true);
    try {
      const r = await fetch(`/api/solicitudes/${id}/rechazar`, {
        method: "POST",
        headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
        body: JSON.stringify({ motivo: "Rechazado por el revisor" }),
      });
      if (!r.ok) throw new Error(await r.text());
      setItems((p) => p.map((x) => (x.id === id ? { ...x, estado: "R" } : x)));
      setSelected((s) => (s && s.id === id ? { ...s, estado: "R" } : s));
    } catch (e: any) {
      alert(e?.message ?? "Error al rechazar");
    } finally {
      setLoading(false);
    }
  };

  // Detalle
  if (selected) {
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
            <div className="p-6 border-b border-gray-200 dark:border-gray-700 flex items-center justify-between">
              <h2 className="text-2xl font-bold text-brand-blue dark:text-white">
                Solicitud #{selected.id}
              </h2>
              <div className="flex gap-2">
                <IconButton label="Volver" variant="secondary" onClick={() => setSelected(null)} />
                <IconButton label="Rechazar" variant="danger" onClick={() => rechazar(selected.id)} />
                <IconButton label="Aprobar" variant="primary" onClick={() => aprobar(selected.id)} />
              </div>
            </div>

            <div className="flex-1 overflow-auto p-6">
              <div className="w-full max-w-3xl mx-auto">
                <div className="bg-white/80 dark:bg-gray-900/80 backdrop-blur-sm rounded-2xl shadow p-4 sm:p-6">
                  <DynamicForm
                    title="Revisión"
                    subtitle="Verificá los datos antes de aprobar o rechazar"
                    headerIcon="🔎"
                    sections={detailSections(selected)}
                    initialData={{}}
                    onSubmit={async () => {}}
                    onCancel={() => setSelected(null)}
                    loading={loading}
                    submitLabel="Guardar"
                    className="flex-1"
                  />
                </div>
              </div>
            </div>

            <div className="p-6 border-t border-gray-200 dark:border-gray-700 flex items-center justify-end gap-2">
              <IconButton label="Rechazar" variant="danger" onClick={() => rechazar(selected.id)} />
              <IconButton label="Aprobar" variant="primary" onClick={() => aprobar(selected.id)} />
            </div>
          </div>
        </div>
      </div>
    );
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
          <div className="p-6 border-b border-gray-200 dark:border-gray-700 flex items-center justify-between">
            <h2 className="text-2xl font-bold text-brand-blue dark:text-white">
              Gestión de Solicitudes
            </h2>
          </div>

          <div className="flex-1 overflow-auto p-6">
            <div className="rounded-xl shadow bg-white dark:bg-gray-800">
              {error && <div className="p-3 text-sm text-red-600 dark:text-red-400">{error}</div>}
              {loading ? (
                <div className="p-6 text-sm text-gray-600 dark:text-gray-300">Cargando…</div>
              ) : (
                <DataTable<Solicitud>
                  data={items}
                  columns={columns}
                  rowKey={(s) => s.id}
                  onRowClick={(s) => setSelected(s)}
                  scrollable={false}
                  enableSearch={true}
                />
              )}
            </div>
          </div>

          <div className="p-6 border-t border-gray-200 dark:border-gray-700 flex-shrink-0">
            <PaginationFooter currentPage={page} totalPages={totalPages} onPageChange={setPage} />
          </div>
        </div>
      </div>
    </div>
  );
}
