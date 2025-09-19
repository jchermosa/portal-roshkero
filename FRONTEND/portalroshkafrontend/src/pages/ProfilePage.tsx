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
        <div className="text-sm text-gray-500 dark:text-gray-400">Cargando perfil…</div>
      </div>
    );
  }

  const fullName = `${user?.nombre ?? ""} ${user?.apellido ?? ""}`.trim();
  const email = user?.correo;
  const phone = user?.telefono;
  const [phoneLocal, setPhoneLocal] = useState<string | undefined>(phone);
  const joinedAt = user?.fecha_ingreso ?? user?.fecha_ingreso;
  const diasVac = user?.dias_vacaciones ?? user?.dias_vacaciones;
  const diasVacRest = user?.dias_vacaciones_restante ?? user?.dias_vacaciones_restante;
  const roleName = user?.rol?.nombre || "";
  const teamName = user?.equipo?.nombre || "";
  const jobName = user?.cargo?.nombre || "";
  const avatarSeed = fullName || email || "usuario";

  return (
    <div className="h-full flex flex-col overflow-hidden">
      {/* Fondo */}
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
        <div className="bg-white/45 dark:bg-gray-900/70 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col h-full overflow-hidden">
          {/* Header */}
          <div className="p-6 border-b border-gray-200 dark:border-gray-700 flex-shrink-0">
            <h1 className="text-2xl font-bold text-brand-blue dark:text-white">Perfil</h1>
          </div>

          {/* Contenido */}
          <div className="flex-1 overflow-auto p-6">
            <div className="overflow-hidden rounded-2xl border border-white/40 dark:border-gray-700 bg-white/50 dark:bg-gray-800/70 backdrop-blur-sm shadow-sm">
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
                    <div className="text-2xl font-semibold leading-tight text-gray-900 dark:text-gray-100">
                      {fullName || "Usuario"}
                    </div>
                    <div className="mt-1 flex flex-wrap items-center gap-3 text-sm text-gray-600 dark:text-gray-400">
                      {roleName && <span>{roleName}</span>}
                    </div>
                    <div className="mt-2 flex flex-wrap gap-2">
                      {teamName && (
                        <span className="inline-flex items-center rounded-full border border-gray-300 dark:border-gray-600 bg-white/60 dark:bg-gray-700/70 px-2 py-0.5 text-xs text-gray-700 dark:text-gray-200">
                          Equipo: {teamName}
                        </span>
                      )}
                      {jobName && (
                        <span className="inline-flex items-center rounded-full border border-gray-300 dark:border-gray-600 bg-white/60 dark:bg-gray-700/70 px-2 py-0.5 text-xs text-gray-700 dark:text-gray-200">
                          Cargo: {jobName}
                        </span>
                      )}
                    </div>
                  </div>
                </div>
              </div>

              <div className="h-px w-full bg-gray-200 dark:bg-gray-700" />

              {/* Grids */}
              <div className="grid gap-6 p-6 md:grid-cols-3">
                <div className="md:col-span-2">
                  <h2 className="mb-3 text-sm font-medium text-gray-600 dark:text-gray-400">
                    Contacto
                  </h2>
                  <div className="grid gap-3">
                    {email && (
                      <a
                        href={`mailto:${email}`}
                        className="group flex items-center justify-between rounded-xl border border-gray-300 dark:border-gray-600 bg-white/60 dark:bg-gray-700/70 backdrop-blur-sm p-3 hover:shadow-sm"
                      >
                        <div className="flex items-center gap-3">
                          <span className="text-gray-600 dark:text-gray-400">✉️</span>
                          <span className="text-sm font-medium text-gray-800 dark:text-gray-200">Email</span>
                        </div>
                        <span className="truncate text-sm text-gray-600 dark:text-gray-300 group-hover:underline">
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
                                Authorization: `Bearer ${localStorage.getItem("auth_token")}`,
                              },
                              body: JSON.stringify({ telefono: newPhone }),
                            }
                          );
                          if (!res.ok) {
                            console.error("Error al actualizar teléfono", res.status, await res.text());
                            return;
                          }
                          setPhoneLocal(newPhone);
                        } catch (err) {
                          console.error("Fallo al guardar teléfono:", err);
                        }
                      }}
                    />

                    {joinedAt && (
                      <div className="flex items-center justify-between rounded-xl border border-gray-300 dark:border-gray-600 bg-white/60 dark:bg-gray-700/70 backdrop-blur-sm p-3">
                        <div className="flex items-center gap-3">
                          <span className="text-gray-600 dark:text-gray-400">📅</span>
                          <span className="text-sm font-medium text-gray-800 dark:text-gray-200">
                            Fecha de ingreso
                          </span>
                        </div>
                        <span className="truncate text-sm text-gray-600 dark:text-gray-300">
                          {formatDate(joinedAt)}
                        </span>
                      </div>
                    )}
                  </div>
                </div>

                <div>
                  <h2 className="mb-3 text-sm font-medium text-gray-600 dark:text-gray-400">
                    Resumen
                  </h2>
                  <div className="grid grid-cols-2 gap-3 md:grid-cols-3">
                    {typeof diasVac !== "undefined" && (
                      <div className="rounded-2xl border border-gray-300 dark:border-gray-600 bg-white/60 dark:bg-gray-700/70 backdrop-blur-sm shadow-sm overflow-hidden w-full">
                        <div className="p-4 text-center bg-white/80 dark:bg-gray-800">
                          <div className="text-2xl font-bold text-gray-900 dark:text-gray-100 tabular-nums">
                            {diasVac}
                          </div>
                        </div>
                        <div className="h-px bg-gray-400/60 dark:bg-gray-600 w-full"></div>
                        <div className="p-3 text-center bg-white/80 dark:bg-gray-800">
                          <div className="text-sm font-semibold text-gray-800 dark:text-gray-200">
                            Días vacaciones
                          </div>
                        </div>
                      </div>
                    )}
                    {typeof diasVacRest !== "undefined" && (
                      <div className="rounded-2xl border border-gray-300 dark:border-gray-600 bg-white/60 dark:bg-gray-700/70 backdrop-blur-sm shadow-sm overflow-hidden w-full h-32 flex flex-col">
                        <div className="flex-1 flex items-center justify-center bg-white/80 dark:bg-gray-800">
                          <div className="text-2xl font-bold text-gray-900 dark:text-gray-100 tabular-nums">
                            {diasVacRest}
                          </div>
                        </div>
                        <div className="h-px bg-gray-400/60 dark:bg-gray-600 w-full"></div>
                        <div className="flex-1 flex items-center justify-center bg-white/80 dark:bg-gray-800">
                          <div className="text-sm font-semibold text-gray-800 dark:text-gray-200">
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
          <div className="p-6 border-t border-gray-200 dark:border-gray-700 flex-shrink-0 flex justify-end">
            <p className="text-sm text-gray-600 dark:text-gray-400">
              Última actualización del perfil visible aquí.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
