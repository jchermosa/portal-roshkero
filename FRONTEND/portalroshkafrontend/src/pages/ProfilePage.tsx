
import React, { useState } from "react";
import { useAuth } from "../context/AuthContext";
import EditableField from "../components/EditableField";


function formatDate(d?: string | Date) {
  if (!d) return "";
  const date = typeof d === "string" ? new Date(d) : d;
  return date.toLocaleDateString(undefined, { year: "numeric", month: "short", day: "2-digit" });
}

export default function ProfilePage() {
  const { user, logout } = useAuth();

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
    <div className="min-h-screen bg-gray-50">
      <div className="mx-auto max-w-5xl p-4 md:p-8">
        <h1 className="mb-4 text-2xl font-semibold">Perfil</h1>

        <div className="overflow-hidden rounded-xl border border-gray-200 bg-white shadow-sm">
          <div className="flex flex-col gap-6 bg-gray-50 p-6 md:flex-row md:items-center md:justify-between">
            <div className="flex items-center gap-4">
              <img
                src={`https://api.dicebear.com/9.x/initials/svg?seed=${encodeURIComponent(avatarSeed)}`}
                alt={fullName}
                className="h-20 w-20 rounded-2xl object-cover shadow"
              />
              <div>
                <div className="text-2xl font-semibold leading-tight">
                  {fullName || "Usuario"}
                </div>
                <div className="mt-1 flex flex-wrap items-center gap-3 text-sm text-gray-500">
                  {roleName && <span> {roleName}</span>}
                </div>
                <div className="mt-2 flex flex-wrap gap-2">
                  {teamName && (
                    <span className="inline-flex items-center rounded-full border border-gray-200 bg-gray-100 px-2 py-0.5 text-xs text-gray-700">Equipo: {teamName}</span>
                  )}
                  {jobName && (
                    <span className="inline-flex items-center rounded-full border border-gray-200 bg-gray-100 px-2 py-0.5 text-xs text-gray-700">Cargo: {jobName}</span>
                  )}
                </div>
              </div>
            </div>
          </div>

          <div className="h-px w-full bg-gray-200" />

          <div className="grid gap-6 p-6 md:grid-cols-3">
            <div className="md:col-span-2">
              <h2 className="mb-3 text-sm font-medium text-gray-500">Contacto</h2>
              <div className="grid gap-3">
                {email && (
                  <a href={`mailto:${email}`} className="group flex items-center justify-between rounded-xl border border-gray-200 bg-white p-3 hover:shadow-sm">
                    <div className="flex items-center gap-3">
                      <span className="text-gray-500">✉️</span>
                      <span className="text-sm font-medium">Email</span>
                    </div>
                    <span className="truncate text-sm text-gray-500 group-hover:underline">{email}</span>
                  </a>
                )}

                {/* Teléfono editable */}
                <EditableField
                    label="📞 Teléfono"
                    value={phoneLocal}
                    placeholder="No definido"
                    onSave={async (newPhone) => {
                      try {
                        // usá el id que viene de decodeAndSetUser
                        const res = await fetch(`/api/usuarios/${user.id}/telefono`, {
                          method: "PUT",
                          headers: {
                            "Content-Type": "application/json",
                            Authorization: `Bearer ${localStorage.getItem("auth_token")}`,
                          },
                          body: JSON.stringify({ telefono: newPhone }),
                        });

                        if (!res.ok) {
                          console.error("Error al actualizar teléfono", res.status, await res.text());
                          return;
                        }

                        setPhoneLocal(newPhone); // actualiza la UI
                      } catch (err) {
                        console.error("Fallo al guardar teléfono:", err);
                      }
                    }}
                  />
                {joinedAt && (
                  <div className="flex items-center justify-between rounded-xl border border-gray-200 bg-white p-3">
                    <div className="flex items-center gap-3">
                      <span className="text-gray-500">📅</span>
                      <span className="text-sm font-medium">Fecha de ingreso</span>
                    </div>
                    <span className="truncate text-sm text-gray-500">{formatDate(joinedAt)}</span>
                  </div>
                )}
              </div>
            </div>

            <div>
              <h2 className="mb-3 text-sm font-medium text-gray-500">Resumen</h2>
              <div className="grid grid-cols-2 gap-3 md:grid-cols-3">
                {typeof diasVac !== "undefined" && (
                  <div className="rounded-2xl border border-gray-200 bg-white p-4 text-center shadow-sm">
                    <div className="text-2xl font-semibold tabular-nums">{diasVac}</div>
                    <div className="mt-1 text-xs text-gray-500">Días vacaciones</div>
                  </div>
                )}
                {typeof diasVacRest !== "undefined" && (
                  <div className="rounded-2xl border border-gray-200 bg-white p-4 text-center shadow-sm">
                    <div className="text-2xl font-semibold tabular-nums">{diasVacRest}</div>
                    <div className="mt-1 text-xs text-gray-500">Días restantes</div>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
