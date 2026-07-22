import { useQuery } from "@tanstack/react-query";

import { BASE_API_URL } from "@/config/app-query-client";
import { AvailabilitySlot, AvailabilitySlotSchema } from "@/models/AvailabilitySlot";

import { useAccessTokenGetter, useHandleResponse } from "./TokenContext";

export function useAvailability(professionalId: number, serviceId: string | undefined, date: string | null) {
  const getAccessToken = useAccessTokenGetter();
  const handleResponse = useHandleResponse();

  return useQuery({
    queryKey: ["availability", professionalId, serviceId, date],
    enabled: date !== null && serviceId !== undefined,
    queryFn: async (): Promise<AvailabilitySlot[]> => {
      const url = `${BASE_API_URL}/professionals/${professionalId}/services/${serviceId}/availability?date=${date}`;
      const response = await fetch(url, {
        method: "GET",
        headers: {
          Accept: "application/json",
          Authorization: `Bearer ${await getAccessToken()}`,
        },
      });

      return handleResponse(response, (json) => AvailabilitySlotSchema.array().parse(json));
    },
  });
}
