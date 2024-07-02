import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  preview: {
    // port: 8080,
    port: 5173,
    strictPort: true,
  },
  server: {
    // port: 8080,
    port: 5173,
    strictPort: true,
    host: true,
    // origin: "http://0.0.0.0:8080",
  },
});
