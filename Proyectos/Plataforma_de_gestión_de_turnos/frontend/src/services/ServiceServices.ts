import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { BASE_API_URL } from "@/config/app-query-client";
import { Service, ServiceSchema, ServicesResponseSchema } from "@/models/Service";
import { useAccessTokenGetter, useHandleResponse } from "@/services/TokenContext";
import { appQueryClient } from "@/config/app-query-client";

type ServiceApiResponse = {
    id?: string | number;
    name?: string;
    duration_minutes?: number;
    price?: number;
    max_capacity?: number;
    active?: boolean;
};

const mapServiceFromApi = (svc: ServiceApiResponse): Service => ({
    id: String(svc.id ?? ""),
    name: String(svc.name ?? ""),
    duration_minutes: Number(svc.duration_minutes ?? 30),
    price: Number(svc.price ?? 0),
    max_capacity: Number(svc.max_capacity ?? 1),
    active: Boolean(svc.active ?? true),
});

export function useServices() {
    const getAccessToken = useAccessTokenGetter();
    const handleResponse = useHandleResponse();

    return useQuery({
        queryKey: ["pro", "services"],
        queryFn: async () => {
            const token = await getAccessToken();
            const response = await fetch(`${BASE_API_URL}/services`, {
                headers: {
                    Accept: "application/json",
                    Authorization: `Bearer ${token}`,
                },
            });
            return handleResponse(response, (json: unknown) => {
                const services = Array.isArray(json) ? (json as ServiceApiResponse[]) : [];
                const transformed = services.map(mapServiceFromApi);
                return ServicesResponseSchema.parse({ services: transformed });
            });
        },
    });
}

export function useCreateService() {
    const getAccessToken = useAccessTokenGetter();
    const handleResponse = useHandleResponse();

    return useMutation({
        mutationFn: async (data: Service) => {
            const token = await getAccessToken();
            const payload = {
                name: data.name,
                duration_minutes: data.duration_minutes,
                price: data.price,
                max_capacity: data.max_capacity,
            };
            const response = await fetch(`${BASE_API_URL}/services`, {
                method: "POST",
                headers: {
                    Accept: "application/json",
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(payload),
            });
            return handleResponse(response, (json: unknown) => {
                if (!json || typeof json !== "object") {
                    throw new Error("Respuesta inválida del servicio");
                }
                return ServiceSchema.parse(mapServiceFromApi(json as ServiceApiResponse));
            });
        },
        onSuccess: () => {
            appQueryClient.invalidateQueries({ queryKey: ["pro", "services"] });
        },
    });
}

export function useUpdateService() {
    const getAccessToken = useAccessTokenGetter();
    const handleResponse = useHandleResponse();

    return useMutation({
        mutationFn: async (data: Service) => {
            const token = await getAccessToken();
            const payload = {
                name: data.name,
                duration_minutes: data.duration_minutes,
                price: data.price,
                max_capacity: data.max_capacity,
                active: data.active,
            };
            const response = await fetch(`${BASE_API_URL}/services/${data.id}`, {
                method: "PUT",
                headers: {
                    Accept: "application/json",
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(payload),
            });
            return handleResponse(response, (json: unknown) => {
                if (!json || typeof json !== "object") {
                    throw new Error("Respuesta inválida del servicio");
                }
                return ServiceSchema.parse(mapServiceFromApi(json as ServiceApiResponse));
            });
        },
        onSuccess: () => {
            appQueryClient.invalidateQueries({ queryKey: ["pro", "services"] });
        },
    });
}

export function useDeleteService() {
    const queryClient = useQueryClient();
    const getAccessToken = useAccessTokenGetter();

    return useMutation({
        retry: 0,
        mutationFn: async (id: string) => {
            const token = await getAccessToken();
            const response = await fetch(`${BASE_API_URL}/services/${id}`, {
                method: "DELETE",
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            if (!response.ok) {
                const errorMsg = response.status === 409
                    ? "Este servicio tiene turnos asociados. Desactivalo en su lugar."
                    : "Error al eliminar el servicio";
                throw new Error(errorMsg);
            }
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["pro", "services"] });
        },
    });
}

