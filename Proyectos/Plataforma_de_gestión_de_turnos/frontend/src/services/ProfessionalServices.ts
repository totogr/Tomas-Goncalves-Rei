import { useQuery } from "@tanstack/react-query";

import { BASE_API_URL } from "@/config/app-query-client";
import { Professional, ProfessionalSchema } from "@/models/Professional";

import { useAccessTokenGetter, useHandleResponse } from "./TokenContext";

export function useProfessional(id: string) {
  const getAccessToken = useAccessTokenGetter();
  const handleResponse = useHandleResponse();

  return useQuery({
    queryKey: ["professional", id],
    enabled: id.trim().length > 0,
    queryFn: async (): Promise<Professional> => {
      const response = await fetch(`${BASE_API_URL}/professionals/${id}`, {
        method: "GET",
        headers: {
          Accept: "application/json",
          Authorization: `Bearer ${await getAccessToken()}`,
        },
      });

      return handleResponse(response, (json) => ProfessionalSchema.parse(json));
    },
  });
}