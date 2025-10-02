export async function throwIfNotOk(res: Response) {
  if (res.ok) return;
  let msg = `HTTP ${res.status}`;
  try {
    const text = await res.text();
    try {
      const json = JSON.parse(text);
      msg = json.message || text || msg;
    } catch {
      msg = text || msg;
    }
  } catch {}
  throw new Error(msg);
}
