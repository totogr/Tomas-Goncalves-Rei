import { useEffect } from "react";
import { QueryClientProvider } from "@tanstack/react-query";

import { Navigation } from "@/Navigation";
import { appQueryClient } from "@/config/app-query-client";
import { RoleProvider } from "@/services/RoleProvider";
import { TokenProvider } from "@/services/TokenContext";

export function App() {
  useEffect(() => {
    const link = document.createElement("link");
    link.rel = "stylesheet";
    link.href = "https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@latest/dist/tabler-icons.min.css";
    document.head.appendChild(link);
  }, []);

  return (
    <QueryClientProvider client={appQueryClient}>
      <TokenProvider>
        <RoleProvider>
          <Navigation />
        </RoleProvider>
      </TokenProvider>
    </QueryClientProvider>
  );
}