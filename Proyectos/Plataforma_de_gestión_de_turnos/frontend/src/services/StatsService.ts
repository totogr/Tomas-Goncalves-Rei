import { useQuery } from "@tanstack/react-query";
import { BASE_API_URL } from "@/config/app-query-client";
import { useAccessTokenGetter, useHandleResponse } from "@/services/TokenContext";
import { Stats, StatsSchema, StatsPeriod } from "@/models/Stats";

export function useProfessionalStats(period: StatsPeriod) {
  const getAccessToken = useAccessTokenGetter();
  const handleResponse = useHandleResponse();

  return useQuery({
    queryKey: ["professional-stats", period],
    queryFn: async (): Promise<Stats> => {
      const response = await fetch(`${BASE_API_URL}/pro/stats?period=${period}`, {
        headers: {
          Accept: "application/json",
          Authorization: `Bearer ${await getAccessToken()}`,
        },
      });
      return handleResponse(response, (json) => StatsSchema.parse(json));
    },
  });
}