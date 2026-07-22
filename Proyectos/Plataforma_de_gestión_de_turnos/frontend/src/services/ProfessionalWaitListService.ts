import { useQuery } from "@tanstack/react-query";
import { z } from "zod";
import { BASE_API_URL } from "@/config/app-query-client";
import { useAccessTokenGetter } from "@/services/TokenContext";

export const ActivePromotionSchema = z.object({
    id: z.number(),
    clientId: z.number(),
    clientName: z.string(),
    clientEmail: z.string(),
    serviceId: z.number(),
    serviceName: z.string(),
    slotStart: z.string(),
    expiresAt: z.string(),
    durationMinutes: z.number(),
});

export type ActivePromotion = z.infer<typeof ActivePromotionSchema>;

export function useActivePromotions() {
    const getAccessToken = useAccessTokenGetter();

    return useQuery({
        queryKey: ["pro-promotions"],
        queryFn: async (): Promise<ActivePromotion[]> => {
            const response = await fetch(`${BASE_API_URL}/waitlist/promotions/professional`, {
                headers: {
                    Accept: "application/json",
                    Authorization: `Bearer ${await getAccessToken()}`,
                },
            });

            if (!response.ok) throw new Error(`Error ${response.status}`);
            return z.array(ActivePromotionSchema).parse(await response.json());
        },
        refetchInterval: 30_000,
    });
}