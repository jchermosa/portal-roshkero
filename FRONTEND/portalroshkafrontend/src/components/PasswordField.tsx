import React, { useState } from "react";
import toast from "react-hot-toast";

type PasswordFieldProps = {
    label: string;
    placeholder?: string;
    onSave: (newValue: string) => Promise<void> | void;
};

export default function PasswordField({
    label,
    placeholder = "Ingrese nueva contraseña",
    onSave,
}: PasswordFieldProps) {
    const [editing, setEditing] = useState(false);
    const [password, setPassword] = useState("");
    const [confirm, setConfirm] = useState("");

    const handleSave = async () => {
        if (password !== confirm) {
            toast.error("Las contraseñas no coinciden");
            return;
        }
        try {
            await onSave(password);
            toast.success("Contraseña actualizada con éxito 🔐");
            setEditing(false);
            setPassword("");
            setConfirm("");
        } catch (err) {
            toast.error("Error al actualizar la contraseña");
            console.error(err);
        }
    };

    return (
        <div className="flex items-center justify-between rounded-xl border border-gray-200 dark:border-gray-600 bg-white dark:bg-gray-700/70 p-3">
            <div className="flex items-center gap-3">
                <span className="text-sm font-medium text-gray-800 dark:text-gray-200">
                    {label}
                </span>
            </div>

            {editing ? (
                <div className="flex flex-col gap-2 w-24">
                    <input
                        type="password"
                        className="w-40 rounded border px-2 py-1 text-sm"
                    />

                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder={placeholder}
                        className="rounded border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-800 px-2 py-1 text-sm text-gray-800 dark:text-gray-200"
                    />
                    <input
                        type="password"
                        value={confirm}
                        onChange={(e) => setConfirm(e.target.value)}
                        placeholder="Confirmar contraseña"
                        className="rounded border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-800 px-2 py-1 text-sm text-gray-800 dark:text-gray-200"
                    />

                    <div className="flex gap-2">
                        <button
                            onClick={handleSave}
                            className="rounded bg-green-600 px-2 py-1 text-xs text-white hover:bg-green-700"
                        >
                            Guardar
                        </button>
                        <button
                            onClick={() => {
                                setEditing(false);
                                setPassword("");
                                setConfirm("");
                            }}
                            className="rounded bg-gray-300 dark:bg-gray-600 px-2 py-1 text-xs text-gray-700 dark:text-gray-200 hover:bg-gray-400 dark:hover:bg-gray-500"
                        >
                            Cancelar
                        </button>
                    </div>
                </div>
            ) : (
                <div className="flex items-center gap-2">
                    <span className="truncate text-sm text-gray-500 dark:text-gray-300">
                        ********
                    </span>
                    <button
                        onClick={() => setEditing(true)}
                        className="text-xs text-blue-600 dark:text-blue-400 hover:underline"
                    >
                        Editar
                    </button>
                </div>
            )}
        </div>
    );
}
