import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { z } from "zod";
import { BASE_API_URL } from "@/config/app-query-client";
import { useAccessTokenGetter } from "@/services/TokenContext";
import { WaitListEntrySchema, WaitListEntry } from "@/models/WaitList";

export type WaitListParams = {
    professionalId: number;
    serviceId: number;
    slotStart: string;
};

export function useMyWaitListPosition(params: WaitListParams) {
    const getAccessToken = useAccessTokenGetter();

    return useQuery({
        queryKey: ["waitlist-position", params.professionalId, params.serviceId, params.slotStart],
        retry: false,
        queryFn: async (): Promise<WaitListEntry | null> => {
            const searchParams = new URLSearchParams({
                professionalId: String(params.professionalId),
                serviceId: String(params.serviceId),
                slotStart: params.slotStart,
            });

            const response = await fetch(`${BASE_API_URL}/waitlist/me?${searchParams}`, {
                headers: {
                    Accept: "application/json",
                    Authorization: `Bearer ${await getAccessToken()}`,
                },
            });

            if (response.status === 404) return null;
            if (!response.ok) throw new Error(`Error ${response.status}`);

            const json = await response.json();
            return WaitListEntrySchema.parse(json);
        },
    });
}

export function useJoinWaitList() {
    const getAccessToken = useAccessTokenGetter();
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: async ({ professionalId, serviceId, slotStart }: WaitListParams) => {
            const params = new URLSearchParams({
                professionalId: String(professionalId),
                serviceId: String(serviceId),
                slotStart,
            });

            const response = await fetch(`${BASE_API_URL}/waitlist?${params}`, {
                method: "POST",
                headers: {
                    Accept: "application/json",
                    Authorization: `Bearer ${await getAccessToken()}`,
                },
            });

            if (!response.ok) {
                const body = await response.json().catch(() => ({}));
                throw new Error(body.message ?? `Error ${response.status}`);
            }

            return WaitListEntrySchema.parse(await response.json());
        },
        onSuccess: (data, variables) => {
            queryClient.setQueryData(
                ["waitlist-position", variables.professionalId, variables.serviceId, variables.slotStart],
                data
            );
            queryClient.invalidateQueries({ queryKey: ["waitlist-me"] });
        },
    });
}

export function useLeaveWaitList() {
    const getAccessToken = useAccessTokenGetter();
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: async ({ professionalId, serviceId, slotStart }: WaitListParams) => {
            const params = new URLSearchParams({
                professionalId: String(professionalId),
                serviceId: String(serviceId),
                slotStart,
            });

            const response = await fetch(`${BASE_API_URL}/waitlist?${params}`, {
                method: "DELETE",
                headers: {
                    Accept: "application/json",
                    Authorization: `Bearer ${await getAccessToken()}`,
                },
            });

            if (!response.ok) throw new Error(`Error ${response.status}`);
        },
        onSuccess: (_data, variables) => {
            queryClient.setQueryData(
                ["waitlist-position", variables.professionalId, variables.serviceId, variables.slotStart],
                null
            );
            queryClient.invalidateQueries({ queryKey: ["waitlist-me"] });
        },
    });
}

export function useMyWaitListEntries(enabled: boolean) {
    const getAccessToken = useAccessTokenGetter();

    return useQuery({
        queryKey: ["waitlist-me"],
        enabled,
        queryFn: async (): Promise<WaitListEntry[]> => {
            const response = await fetch(`${BASE_API_URL}/waitlist/me/all`, {
                headers: {
                    Accept: "application/json",
                    Authorization: `Bearer ${await getAccessToken()}`,
                },
            });

            if (response.status === 404) return [];
            if (!response.ok) throw new Error(`Error ${response.status}`);

            return z.array(WaitListEntrySchema).parse(await response.json());
        },
    });
}

export function useConfirmPromotion() {
    const getAccessToken = useAccessTokenGetter();
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: async ({ professionalId, serviceId, slotStart }: WaitListParams) => {
            const params = new URLSearchParams({
                professionalId: String(professionalId),
                serviceId: String(serviceId),
                slotStart,
            });

            const response = await fetch(`${BASE_API_URL}/waitlist/confirm?${params}`, {
                method: "POST",
                headers: {
                    Accept: "application/json",
                    Authorization: `Bearer ${await getAccessToken()}`,
                },
            });

            if (!response.ok) {
                const body = await response.json().catch(() => ({}));
                throw new Error(body.message ?? `Error ${response.status}`);
            }
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["waitlist-me"] });
            queryClient.invalidateQueries({ queryKey: ["bookings"] });
        },
    });
}