export function Alert({
  kind = "error",
  children,
}: { kind?: "error" | "info" | "success" | "warning"; children: React.ReactNode }) {
  const styles: Record<string, string> = {
    error:   "bg-red-50 text-red-700 border-red-200 dark:bg-red-900/20 dark:text-red-300 dark:border-red-700",
    info:    "bg-blue-50 text-blue-700 border-blue-200 dark:bg-blue-900/20 dark:text-blue-300 dark:border-blue-700",
    success: "bg-green-50 text-green-700 border-green-200 dark:bg-green-900/20 dark:text-green-300 dark:border-green-700",
    warning: "bg-yellow-50 text-yellow-700 border-yellow-200 dark:bg-yellow-900/20 dark:text-yellow-300 dark:border-yellow-700",
  };
  const icon: Record<string, string> = { error:"❌", info:"ℹ️", success:"✅", warning:"⚠️" };
  return (
    <div className={`mt-3 p-3 rounded-lg border text-sm ${styles[kind]}`}>
      <span className="mr-2">{icon[kind]}</span>
      {children}
    </div>
  );
}
