import { Toaster } from "@components/ui/sonner";
import { store } from "@store/store";
import App from "App";
import React from "react";
import ReactDOM from "react-dom/client";
import { Provider } from "react-redux";

ReactDOM.createRoot(document.getElementById("root")!).render(
    <React.StrictMode>
        <Provider store={store}>
            <App />
            <Toaster position='top-right' closeButton />
        </Provider>
    </React.StrictMode>
);
