import React, { useState } from "react";
import { useAuth } from "../context/AuthContext";
import EditableField from "../components/EditableField";

function formatDate(d?: string | Date) {
  if (!d) return "";
  const date = typeof d === "string" ? new Date(d) : d;
  return date.toLocaleDateString(undefined, {
    year: "numeric",
    month: "short",
    day: "2-digit",
  });
}

export default function ProfilePage() {
  const { user } = useAuth();

  if (!user) {
    return (
      <div className="min-h-screen grid place-items-center">
        <div className="text-sm text-gray-500">Cargando perfil…</div>
      </div>
    );
  }

  const fullName = `${user?.nombre ?? ""} ${user?.apellido ?? ""}`.trim();
  const email = user?.correo;
  const phone = user?.telefono;
  const [phoneLocal, setPhoneLocal] = useState<string | undefined>(phone);
  const joinedAt = user?.fecha_ingreso ?? user?.fechaIngreso;
  const diasVac = user?.dias_vacaciones ?? user?.diasVacaciones;
  const diasVacRest = user?.dias_vacaciones_restante ?? user?.diasVacacionesRestante;
  const roleName = user?.rol?.nombre || "";
  const teamName = user?.equipo?.nombre || "";
  const jobName = user?.cargo?.nombre || "";
  const avatarSeed = fullName || email || "usuario";

  return (
    <div className="h-full flex flex-col overflow-hidden">
      {/* Fondo con imagen */}
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

      {/* Contenedor principal */}
      <div className="relative z-10 flex flex-col h-full p-4">
        <div className="bg-white/45 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col h-full overflow-hidden">
          {/* Header fijo */}
          <div className="p-6 border-b border-gray-200 flex-shrink-0">
            <h1 className="text-2xl font-bold text-brand-blue">Perfil</h1>
          </div>

          {/* Contenido */}
          <div className="flex-1 overflow-auto p-6">
            <div className="overflow-hidden rounded-2xl border border-white/40 bg-white/50 backdrop-blur-sm shadow-sm">
              {/* Encabezado */}
              <div className="flex flex-col gap-6 p-6 md:flex-row md:items-center md:justify-between">
                <div className="flex items-center gap-4">
                  <img
                    src={`https://api.dicebear.com/9.x/initials/svg?seed=${encodeURIComponent(
                      avatarSeed
                    )}`}
                    alt={fullName}
                    className="h-20 w-20 rounded-2xl object-cover shadow"
                  />
                  <div>
                    <div className="text-2xl font-semibold leading-tight">
                      {fullName || "Usuario"}
                    </div>
                    <div className="mt-1 flex flex-wrap items-center gap-3 text-sm text-gray-600">
                      {roleName && <span>{roleName}</span>}
                    </div>
                    <div className="mt-2 flex flex-wrap gap-2">
                      {teamName && (
                        <span className="inline-flex items-center rounded-full border border-white/60 bg-white/60 px-2 py-0.5 text-xs text-gray-700">
                          Equipo: {teamName}
                        </span>
                      )}
                      {jobName && (
                        <span className="inline-flex items-center rounded-full border border-white/60 bg-white/60 px-2 py-0.5 text-xs text-gray-700">
                          Cargo: {jobName}
                        </span>
                      )}
                    </div>
                  </div>
                </div>
              </div>

              <div className="h-px w-full bg-gray-200/70" />

              {/* Grids */}
              <div className="grid gap-6 p-6 md:grid-cols-3">
                <div className="md:col-span-2">
                  <h2 className="mb-3 text-sm font-medium text-gray-600">
                    Contacto
                  </h2>
                  <div className="grid gap-3">
                    {email && (
                      <a
                        href={`mailto:${email}`}
                        className="group flex items-center justify-between rounded-xl border border-white/60 bg-white/60 backdrop-blur-sm p-3 hover:shadow-sm"
                      >
                        <div className="flex items-center gap-3">
                          <span className="text-gray-600">✉️</span>
                          <span className="text-sm font-medium">Email</span>
                        </div>
                        <span className="truncate text-sm text-gray-600 group-hover:underline">
                          {email}
                        </span>
                      </a>
                    )}

                    <EditableField
                      label="📞 Teléfono"
                      value={phoneLocal}
                      placeholder="No definido"
                      onSave={async (newPhone) => {
                        try {
                          const res = await fetch(
                            `/api/usuarios/${user.id}/telefono`,
                            {
                              method: "PUT",
                              headers: {
                                "Content-Type": "application/json",
                                Authorization: `Bearer ${localStorage.getItem(
                                  "auth_token"
                                )}`,
                              },
                              body: JSON.stringify({ telefono: newPhone }),
                            }
                          );
                          if (!res.ok) {
                            console.error(
                              "Error al actualizar teléfono",
                              res.status,
                              await res.text()
                            );
                            return;
                          }
                          setPhoneLocal(newPhone);
                        } catch (err) {
                          console.error("Fallo al guardar teléfono:", err);
                        }
                      }}
                      className="rounded-xl border border-white/60 bg-white/60 backdrop-blur-sm p-0"
                      inputClassName="bg-transparent"
                    />

                    {joinedAt && (
                      <div className="flex items-center justify-between rounded-xl border border-white/60 bg-white/60 backdrop-blur-sm p-3">
                        <div className="flex items-center gap-3">
                          <span className="text-gray-600">📅</span>
                          <span className="text-sm font-medium">
                            Fecha de ingreso
                          </span>
                        </div>
                        <span className="truncate text-sm text-gray-600">
                          {formatDate(joinedAt)}
                        </span>
                      </div>
                    )}
                  </div>
                </div>

                <div>
                  <h2 className="mb-3 text-sm font-medium text-gray-600">
                    Resumen
                  </h2>
                  <div className="grid grid-cols-2 gap-3 md:grid-cols-3">
                    {typeof diasVac !== "undefined" && (
                      <div className="rounded-2xl border border-white/60 bg-white/60 backdrop-blur-sm shadow-sm overflow-hidden w-full">
                        {/* Número con fondo claro */}
                        <div className="p-4 text-center bg-white/80">
                          <div className="text-2xl font-bold text-gray-900 tabular-nums">
                            {diasVac}
                          </div>
                        </div>

                        {/* Línea divisoria */}
                        <div className="h-px bg-gray-400/60 w-full"></div>

                        {/* Texto con fondo un poco más oscuro */}
                        <div className="p-3 text-center bg-white/80">
                          <div className="text-sm font-semibold text-gray-800">
                            Días vacaciones
                          </div>
                        </div>
                      </div>
                    )}
                    {typeof diasVacRest !== "undefined" && (
                      <div className="rounded-2xl border border-white/60 bg-white/60 backdrop-blur-sm shadow-sm overflow-hidden w-full h-32 flex flex-col">
                          {/* Número */}
                          <div className="flex-1 flex items-center justify-center bg-white/80">
                            <div className="text-2xl font-bold text-gray-900 tabular-nums">
                              {diasVacRest}
                            </div>
                          </div>

                          {/* Línea divisoria */}
                          <div className="h-px bg-gray-400/60 w-full"></div>

                          {/* Texto */}
                          <div className="flex-1 flex items-center justify-center bg-white/80">
                            <div className="text-sm font-semibold text-gray-800">
                              Días restantes
                            </div>
                          </div>
                        </div>

                    )}
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* Footer */}
          <div className="p-6 border-t border-gray-200 flex-shrink-0 flex justify-end">
            <p className="text-sm text-gray-600">
              Última actualización del perfil visible aquí.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
