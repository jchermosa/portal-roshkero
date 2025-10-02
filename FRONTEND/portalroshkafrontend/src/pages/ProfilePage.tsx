import React, { useState } from "react";
import { useAuth } from "../context/AuthContext";
import EditableField from "../components/EditableField";
import UploadImageButton from "../components/UploadImageButton";

function formatDate(d?: string | Date) {
  if (!d) return "";
  const date = typeof d === "string" ? new Date(d) : d;
  return date.toLocaleDateString(undefined, {
    year: "numeric",
    month: "short",
    day: "2-digit",
  });
}

function namesFrom(input: any, key = "nombre"): string[] {
  if (!input) return [];
  if (Array.isArray(input)) {
    return input
      .map((x) =>
        typeof x === "string"
          ? x
          : x && typeof x === "object" && x[key]
          ? String(x[key])
          : ""
      )
      .filter(Boolean);
  }
  if (typeof input === "string") return [input];
  if (typeof input === "object" && input[key]) return [String(input[key])];
  return [];
}

// 🔧 función para convertir archivo a Base64
const toBase64 = (file: File): Promise<string> =>
  new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result as string);
    reader.onerror = (error) => reject(error);
  });

export default function ProfilePage() {
  const { user, token, refreshUser } = useAuth();


  const [isUploading, setIsUploading] = useState(false);
  const [uploadError, setUploadError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const [phoneLocal, setPhoneLocal] = useState<string | undefined>(user?.telefono);

  if (!user) {
    return (
      <div className="min-h-screen grid place-items-center">
        <div className="text-sm text-gray-500 dark:text-gray-400">
          Cargando perfil…
        </div>
      </div>
    );
  }

  const fullName = `${user?.nombre ?? ""} ${user?.apellido ?? ""}`.trim();
  const email = user?.correo;
  const joinedAt = (user as any)?.fechaIngreso ?? (user as any)?.fecha_ingreso;
  const diasVac =
    (user as any)?.diasVacaciones ?? (user as any)?.dias_vacaciones;
  const diasVacRest =
    (user as any)?.diasVacacionesRestante ??
    (user as any)?.dias_vacaciones_restante;
  const roles = namesFrom((user as any)?.rol ?? (user as any)?.roles);
  const equipos = namesFrom((user as any)?.equipo ?? (user as any)?.equipos);
  const cargos = namesFrom((user as any)?.cargo ?? (user as any)?.cargos);
  const avatarSeed = fullName || email || "usuario";

  const handleImageChange = async (file: File) => {
    try {
      setIsUploading(true);
      setUploadError(null);
      setSuccessMessage(null);

      const base64Image = await toBase64(file);
      const cleanBase64 = base64Image.split(",")[1]; // solo el puro contenido  

      if (!user) {
          throw new Error("Usuario no está cargado");
      }

      const res = await fetch(`/api/usuarios/${user.id}/foto`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ fotoBase64: cleanBase64 }),
      });

      
      if (!res.ok) {
        throw new Error("Error al actualizar la imagen");
      }

      refreshUser();
      console.log("Usuario desde refresUser:", user);

      setSuccessMessage("Imagen actualizada correctamente");
      setTimeout(() => setSuccessMessage(null), 3000);
    } catch (err) {
      setUploadError(
        err instanceof Error ? err.message : "Error al subir la imagen"
      );
      console.error("Error al subir la imagen:", err);
    } finally {
      setIsUploading(false);
    }
  };

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
        <div className="absolute inset-0 bg-brand-blue/40" />
      </div>

      {/* Contenedor principal */}
      <div className="relative z-10 flex flex-col h-full p-4">
        <div className="bg-white/45 dark:bg-gray-900/70 backdrop-blur-sm rounded-2xl shadow-lg flex flex-col h-full overflow-hidden">
          {/* Header */}
          <div className="p-4 md:p-6 border-b border-gray-200 dark:border-gray-700 flex-shrink-0">
            <h1 className="text-xl md:text-2xl font-bold text-brand-blue dark:text-white">
              Perfil
            </h1>
          </div>

          {/* Contenido */}
          <div className="flex-1 overflow-auto p-4 md:p-6">
            <div className="overflow-hidden rounded-2xl border border-white/40 dark:border-gray-700 bg-white/50 dark:bg-gray-800/70 backdrop-blur-sm shadow-sm">
              {/* Encabezado responsive */}
              <div className="p-4 md:p-6 flex flex-col md:flex-row md:items-start md:justify-between gap-4">
                <div className="flex items-center gap-4 min-w-0">
                  {/* {user.fotoBase64 } */}
                  
                    <img
                      src={`data:image/png;base64,${user.fotoBase64}`}
                      alt={fullName}
                      className="h-16 w-16 md:h-20 md:w-20 rounded-2xl object-cover shrink-0 shadow"
                    />
                 
                  <div className="min-w-0">
                    <div className="text-xl md:text-2xl font-semibold leading-tight text-gray-900 dark:text-gray-100 truncate">
                      {fullName || "Usuario"}
                    </div>

                    {/* Pills: rol + email */}
                    <div className="mt-1 flex flex-wrap items-center gap-2 text-xs md:text-sm text-gray-600 dark:text-gray-400">
                      {roles.map((r) => (
                        <span
                          key={r}
                          className="inline-flex items-center rounded-full border border-gray-300 dark:border-gray-600 bg-white/60 dark:bg-gray-700/70 px-2 py-0.5 text-gray-700 dark:text-gray-200"
                        >
                          {r}
                        </span>
                      ))}
                      {email && (
                        <a
                          href={`mailto:${email}`}
                          className="inline-flex items-center rounded-full border border-gray-300 dark:border-gray-600 bg-white/60 dark:bg-gray-700/70 px-2 py-0.5 text-gray-700 dark:text-gray-200 hover:underline max-w-full md:max-w-[360px] truncate"
                          title={email}
                        >
                          {email}
                        </a>
                      )}
                    </div>
                  </div>
                </div>

                {/* Botón arriba a la derecha */}
                <div className="flex justify-end">
                  <UploadImageButton
                    onImageSelect={handleImageChange}
                    isUploading={isUploading}
                    className="w-auto"
                  />
                </div>
              </div>

              {/* Línea divisoria */}
              <div className="h-px w-full bg-gray-200 dark:bg-gray-700" />

              {/* Grids responsivas */}
              <div className="grid gap-4 md:gap-6 p-4 md:p-6 md:grid-cols-3">
                {/* Columna izquierda */}
                <div className="md:col-span-2">
                  <h2 className="mb-3 text-sm font-medium text-gray-600 dark:text-gray-400">
                    Contacto
                  </h2>
                  <div className="grid gap-3 sm:grid-cols-1">
                    {/* Cargos */}
                    {cargos.length > 0 && (
                      <div className="flex items-center justify-between rounded-xl border border-gray-300 dark:border-gray-600 bg-white/60 dark:bg-gray-700/70 backdrop-blur-sm p-3 min-w-0">
                        <div className="flex items-center gap-3 shrink-0">
                          <span className="text-gray-600 dark:text-gray-400">
                            🧩
                          </span>
                          <span className="text-sm font-medium text-gray-800 dark:text-gray-200">
                            Cargos
                          </span>
                        </div>
                        <div className="flex flex-wrap gap-2 justify-end min-w-0">
                          {cargos.map((c) => (
                            <span
                              key={c}
                              className="inline-flex items-center rounded-full border border-gray-300 dark:border-gray-600 bg-white/70 dark:bg-gray-800/80 px-2 py-0.5 text-xs text-gray-700 dark:text-gray-200"
                            >
                              {c}
                            </span>
                          ))}
                        </div>
                      </div>
                    )}

                    {/* Equipos */}
                    {equipos.length > 0 && (
                      <div className="flex items-center justify-between rounded-xl border border-gray-300 dark:border-gray-600 bg-white/60 dark:bg-gray-700/70 backdrop-blur-sm p-3 min-w-0">
                        <div className="flex items-center gap-3 shrink-0">
                          <span className="text-gray-600 dark:text-gray-400">
                            👥
                          </span>
                          <span className="text-sm font-medium text-gray-800 dark:text-gray-200">
                            Equipos
                          </span>
                        </div>
                        <div className="flex flex-wrap gap-2 justify-end min-w-0">
                          {equipos.map((e) => (
                            <span
                              key={e}
                              className="inline-flex items-center rounded-full border border-gray-300 dark:border-gray-600 bg-white/70 dark:bg-gray-800/80 px-2 py-0.5 text-xs text-gray-700 dark:text-gray-200"
                            >
                              {e}
                            </span>
                          ))}
                        </div>
                      </div>
                    )}

                    {/* Teléfono */}
                    <EditableField
                      label="📞 Teléfono"
                      value={phoneLocal}
                      placeholder="No definido"
                      onSave={async (newPhone) => {
                        try {
                          const res = await fetch(`/api/usuarios/${user.id}`, {
                            method: "PUT",
                            headers: {
                              "Content-Type": "application/json",
                              Authorization: `Bearer ${localStorage.getItem(
                                "auth_token"
                              )}`,
                            },
                            body: JSON.stringify({ telefono: newPhone }),
                          });
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
                    />

                    {/* Fecha de ingreso */}
                    {joinedAt && (
                      <div className="flex items-center justify-between rounded-xl border border-gray-300 dark:border-gray-600 bg-white/60 dark:bg-gray-700/70 backdrop-blur-sm p-3">
                        <div className="flex items-center gap-3">
                          <span className="text-gray-600 dark:text-gray-400">
                            📅
                          </span>
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

                {/* Columna derecha */}
                <div>
                  <h2 className="mb-3 text-sm font-medium text-gray-600 dark:text-gray-400">
                    Resumen
                  </h2>
                  <div className="grid grid-cols-2 gap-3">
                    {typeof diasVac !== "undefined" && (
                      <div className="rounded-2xl border border-gray-300 dark:border-gray-600 bg-white/60 dark:bg-gray-800/70 backdrop-blur-sm shadow-sm overflow-hidden w-full">
                        <div className="p-4 text-center bg-white/80 dark:bg-gray-800">
                          <div className="text-2xl font-bold text-gray-900 dark:text-gray-100 tabular-nums">
                            {diasVac}
                          </div>
                        </div>
                        <div className="h-px bg-gray-400/60 dark:bg-gray-600 w-full" />
                        <div className="p-3 text-center bg-white/80 dark:bg-gray-800">
                          <div className="text-sm font-semibold text-gray-800 dark:text-gray-200">
                            Días vacaciones
                          </div>
                        </div>
                      </div>
                    )}
                    {typeof diasVacRest !== "undefined" && (
                      <div className="rounded-2xl border border-gray-300 dark:border-gray-600 bg-white/60 dark:bg-gray-700/70 backdrop-blur-sm shadow-sm overflow-hidden w-full">
                        <div className="p-4 text-center bg-white/80 dark:bg-gray-800">
                          <div className="text-2xl font-bold text-gray-900 dark:text-gray-100 tabular-nums">
                            {diasVacRest}
                          </div>
                        </div>
                        <div className="h-px bg-gray-400/60 dark:bg-gray-600 w-full" />
                        <div className="p-3 text-center bg-white/80 dark:bg-gray-800">
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
          <div className="p-4 md:p-6 border-t border-gray-200 dark:border-gray-700 flex-shrink-0 flex justify-end">
            <p className="text-sm text-gray-600 dark:text-gray-400">
              Última actualización del perfil visible aquí.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
// function setUser(arg0: (prev: any) => any) {
//   throw new Error("Function not implemented.");
// }

