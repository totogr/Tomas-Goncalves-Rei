import { useMutation, useQuery } from "@tanstack/react-query";

import { BASE_API_URL, appQueryClient } from "@/config/app-query-client";
import { useAccessTokenGetter, useHandleResponse } from "@/services/TokenContext";

export function useBlockedClientIds(professionalId: number) {
  const getAccessToken = useAccessTokenGetter();
  const handleResponse = useHandleResponse();

  return useQuery({
    queryKey: ["pro", "blocked-clients"],
    queryFn: async () => {
      const token = await getAccessToken();
      const response = await fetch(
        `${BASE_API_URL}/professionals/${professionalId}/blocked-clients`,
        {
          headers: {
            Accept: "application/json",
            Authorization: `Bearer ${token}`,
          },
        },
      );
      return handleResponse(response, (json) => json as number[]);
    },
  });
}

export function useBlockClient(professionalId: number) {
  const getAccessToken = useAccessTokenGetter();

  return useMutation({
    mutationFn: async (clientId: number) => {
      const token = await getAccessToken();
      const response = await fetch(
        `${BASE_API_URL}/professionals/${professionalId}/blocked-clients`,
        {
          method: "POST",
          headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({ clientId }),
        },
      );

      if (response.ok) {
        return;
      }

      if (response.status === 409) {
        throw new Error("El cliente ya está bloqueado.");
      }

      if (response.status === 403) {
        throw new Error("No tenés permisos para bloquear clientes.");
      }

      throw new Error("No se pudo bloquear al cliente. Probá nuevamente.");
    },
    onSuccess: () => {
      appQueryClient.invalidateQueries({ queryKey: ["pro", "blocked-clients"] });
    },
  });
}

export function useAllClients() {
  const getAccessToken = useAccessTokenGetter();
  const handleResponse = useHandleResponse();

  return useQuery({
    queryKey: ["all-clients"],
    queryFn: async (): Promise<
      { clientId: number; clientName: string; clientEmail: string }[]
    > => {
      const token = await getAccessToken();
      const response = await fetch(`${BASE_API_URL}/clients`, {
        headers: {
          Accept: "application/json",
          Authorization: `Bearer ${token}`,
        },
      });
      return handleResponse(response, (json) =>
        (json as { id: number; firstName: string; lastName: string; email: string }[]).map((c) => ({
          clientId: c.id,
          clientName: `${c.firstName} ${c.lastName}`,
          clientEmail: c.email,
        })),
      );
    },
  });
}

export function useUnblockClient(professionalId: number) {
  const getAccessToken = useAccessTokenGetter();

  return useMutation({
    retry: 0,
    mutationFn: async (clientId: number) => {
      const token = await getAccessToken();
      const response = await fetch(
        `${BASE_API_URL}/professionals/${professionalId}/blocked-clients/${clientId}`,
        {
          method: "DELETE",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );

      if (!response.ok) {
        const errorMsg =
          response.status === 403
            ? "No tenés permisos para desbloquear clientes."
            : response.status === 404
              ? "No se encontró el bloqueo a eliminar."
              : "Error al desbloquear al cliente";
        throw new Error(errorMsg);
      }
    },
    onSuccess: () => {
      appQueryClient.invalidateQueries({ queryKey: ["pro", "blocked-clients"] });
    },
  });
}