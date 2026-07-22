import { useMutation, useQuery } from "@tanstack/react-query";

import { BASE_API_URL, appQueryClient } from "@/config/app-query-client";
import {
    BlockedSchedule,
    BlockedScheduleCreateResultSchema,
    BlockedScheduleListSchema,
} from "@/models/BlockedSchedule";
import { useAccessTokenGetter, useHandleResponse } from "@/services/TokenContext";

export type CreateBlockedScheduleRequest = Omit<BlockedSchedule, "id">;

export function useBlockedSchedules() {
    const getAccessToken = useAccessTokenGetter();
    const handleResponse = useHandleResponse();

    return useQuery({
        queryKey: ["pro", "blocked-schedules"],
        queryFn: async () => {
            const token = await getAccessToken();
            const response = await fetch(`${BASE_API_URL}/schedule/blocks`, {
                headers: {
                    Accept: "application/json",
                    Authorization: `Bearer ${token}`,
                },
            });
            return handleResponse(response, (json) => BlockedScheduleListSchema.parse(json));
        },
    });
}

export function useCreateBlockedSchedule() {
    const getAccessToken = useAccessTokenGetter();

    return useMutation({
        mutationFn: async (data: CreateBlockedScheduleRequest) => {
            const token = await getAccessToken();
            const response = await fetch(`${BASE_API_URL}/schedule/blocks`, {
                method: "POST",
                headers: {
                    Accept: "application/json",
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                return BlockedScheduleCreateResultSchema.parse(await response.json());
            }

            if (response.status === 409) {
                throw new Error("Ya existe un bloqueo que se superpone con ese horario.");
            }

            if (response.status === 400) {
                throw new Error("El rango horario no es válido. Revisá inicio y fin.");
            }

            if (response.status === 403) {
                throw new Error("No tenés permisos para crear este bloqueo.");
            }

            throw new Error("No se pudo crear el bloqueo. Probá nuevamente.");
        },
        onSuccess: () => {
            appQueryClient.invalidateQueries({ queryKey: ["pro", "blocked-schedules"] });
        },
    });
}

export function useDeleteBlockedSchedule() {
    const getAccessToken = useAccessTokenGetter();

    return useMutation({
        retry: 0,
        mutationFn: async (id: number) => {
            const token = await getAccessToken();
            const response = await fetch(`${BASE_API_URL}/schedule/blocks/${id}`, {
                method: "DELETE",
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (!response.ok) {
                const errorMsg = response.status === 403
                    ? "No tenés permisos para borrar este bloqueo."
                    : response.status === 404
                        ? "No se encontró el bloqueo a eliminar."
                        : "Error al eliminar el bloqueo";
                throw new Error(errorMsg);
            }
        },
        onSuccess: () => {
            appQueryClient.invalidateQueries({ queryKey: ["pro", "blocked-schedules"] });
        },
    });
}