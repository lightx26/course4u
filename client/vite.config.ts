import {defineConfig, loadEnv} from "vite";
import react from "@vitejs/plugin-react";
import {UserConfigExport, ConfigEnv} from "vite";

// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-expect-error
export default defineConfig(({mode}: ConfigEnv): UserConfigExport => {
  const env = loadEnv(mode, process.cwd());

  return{
    plugins: [react()],
    preview: {
      port: 5173,
      strictPort: true,
    },
    server: {
      port: 5173,
      strictPort: true,
      host: true,
    },
    base: env.VITE_BASE_URL,

  }
});
