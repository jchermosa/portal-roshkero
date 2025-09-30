import { BrowserRouter } from "react-router-dom";
import AppRoutes from "./routes/AppRoutes";
import { Toaster } from "react-hot-toast";

function App() {
  return (
    <BrowserRouter>
      <AppRoutes />
      {/* Toaster para notificaciones globales */}
      <Toaster position="top-right" reverseOrder={false} />
    </BrowserRouter>
  );
}

export default App;
