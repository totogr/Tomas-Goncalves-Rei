import { z } from "zod";
import { useQuery } from "@tanstack/react-query";

import { BASE_API_URL } from "@/config/app-query-client";
import { ProfessionalSummary, ProfessionalSummarySchema } from "@/models/Professional";
import { useAccessTokenGetter, useHandleResponse } from "./TokenContext";

export function useProfessionals() {
  const getAccessToken = useAccessTokenGetter();
  const handleResponse = useHandleResponse();

  return useQuery({
    queryKey: ["professionals"],
    queryFn: async (): Promise<ProfessionalSummary[]> => {
      const response = await fetch(`${BASE_API_URL}/professionals`, {
        method: "GET",
        headers: {
          Accept: "application/json",
          Authorization: `Bearer ${await getAccessToken()}`,
        },
      });

      return handleResponse(response, (json) =>
        z.array(ProfessionalSummarySchema).parse(json)
      );
    },
  });
}
