import { defineConfig, loadEnv } from "vite";
import react from "@vitejs/plugin-react";
import { UserConfigExport, ConfigEnv } from "vite";
import path from 'path';
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-expect-error
export default defineConfig(({ mode }: ConfigEnv): UserConfigExport => {
    const env = loadEnv(mode, process.cwd());

    return {
        plugins: [react()],
        resolve: {
            alias: {
                '@': path.resolve(__dirname, 'src'),
                '@css-file': path.resolve(__dirname, 'src/assets/css'),
                '@service': path.resolve(__dirname, 'src/service'),'@utils': path.resolve(__dirname, 'src/utils'),
                '@constant': path.resolve(__dirname, 'src/constant'),
                '@store': path.resolve(__dirname, 'src/redux/store'),
                '@hooks': path.resolve(__dirname, 'src/hooks'),
                '@project-types': path.resolve(__dirname, 'src/types'),
                '@schemas': path.resolve(__dirname, 'src/schemas'),
                '@components': path.resolve(__dirname, 'src/components'),
                '@screen': path.resolve(__dirname, 'src/screens'),
            },
          },
        preview: {
            port: 5173,
            strictPort: true,
        },
        server: {
            port: 5173,
            strictPort: true,
            host: true,
        },
        base: env.VITE_BASE_URL || "/",
    };
});
