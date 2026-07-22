import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { z } from "zod";
import { BASE_API_URL } from "@/config/app-query-client";
import { useAccessTokenGetter, useHandleResponse } from "@/services/TokenContext";

export const ProfessionalBookingSchema = z.object({
  id: z.number(),
  status: z.enum(["CONFIRMED", "CANCELLED", "COMPLETED"]),
  cancelled_by: z.string().nullable(),
  service_name: z.string(),
  client_name: z.string(),
  client_email: z.string(),
  client_id: z.number().optional(),
  date: z.string().transform((d) => d.slice(0, 10)),
  time: z.string(),
  end: z.string(),
  duration_minutes: z.number(),
  marked_absent_at: z.string().nullable().optional(),
});

export type ProfessionalBooking = z.infer<typeof ProfessionalBookingSchema>;

export type AgendaStatus = "upcoming" | "past" | "cancelled" | "all";

export function useProfessionalBookings(status: AgendaStatus = "all") {
  const getAccessToken = useAccessTokenGetter();
  const handleResponse = useHandleResponse();

  return useQuery({
    queryKey: ["pro-bookings", status],
    queryFn: async (): Promise<ProfessionalBooking[]> => {
      const response = await fetch(`${BASE_API_URL}/pro/bookings?status=${status}`, {
        headers: {
          Accept: "application/json",
          Authorization: `Bearer ${await getAccessToken()}`,
        },
      });
      return handleResponse(response, (json) => z.array(ProfessionalBookingSchema).parse(json));
    },
  });
}

export function useCancelProfessionalBooking() {
  const getAccessToken = useAccessTokenGetter();
  const handleResponse = useHandleResponse();
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (id: number) => {
      const response = await fetch(`${BASE_API_URL}/pro/bookings/${id}/cancel`, {
        method: "PATCH",
        headers: {
          Accept: "application/json",
          Authorization: `Bearer ${await getAccessToken()}`,
        },
      });
      return handleResponse(response, (json) => json);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["pro-bookings"] });
    },
  });
}
export function useMarkAbsent() {
  const getAccessToken = useAccessTokenGetter();
  const handleResponse = useHandleResponse();
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (id: number) => {
      const response = await fetch(`${BASE_API_URL}/pro/bookings/${id}/absent`, {
        method: "PATCH",
        headers: {
          Accept: "application/json",
          Authorization: `Bearer ${await getAccessToken()}`,
        },
      });
      return handleResponse(response, (json) => json);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["pro-bookings"] });
    },
  });
}
