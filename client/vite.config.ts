import { defineConfig } from "vite";
import viteReact from "@vitejs/plugin-react";
// import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [viteReact()],
  preview: {
    port: 5173,
    strictPort: true,
  },
  server: {
    port: 5173,
    strictPort: true,
    host: true,
  },
});