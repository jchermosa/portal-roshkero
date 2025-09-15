// src/hooks/useFormResource.ts
import { useEffect, useState } from "react";

interface Options<T> {
  initialData?: Partial<T>; // datos por defecto si es creación
  transformResponse?: (data: any) => T;
}

export function useFormResource<T>(
  endpoint: string,          // ej: "usuarios"
  token: string | null,
  id?: string,               // si existe, es edición
  options?: Options<T>
) {
  const [data, setData] = useState<Partial<T>>(options?.initialData ?? {});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const isEditing = !!id;

  // Cargar recurso si es edición
  useEffect(() => {
    if (!token || !isEditing) return;
    setLoading(true);

    fetch(`/api/${endpoint}/${id}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(async (res) => {
        if (!res.ok) throw new Error(await res.text());
        return res.json();
      })
      .then((raw) => {
        const processed = options?.transformResponse
          ? options.transformResponse(raw)
          : raw;
        setData(processed);
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [id, token]);

  // Submit
  const handleSubmit = async (formData: Partial<T>) => {
    const method = isEditing ? "PUT" : "POST";
    const url = isEditing ? `/api/${endpoint}/${id}` : `/api/${endpoint}`;

    const res = await fetch(url, {
      method,
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    });

    if (!res.ok) throw new Error(await res.text());
    return await res.json();
  };

  return { data, setData, loading, error, handleSubmit, isEditing };
}
