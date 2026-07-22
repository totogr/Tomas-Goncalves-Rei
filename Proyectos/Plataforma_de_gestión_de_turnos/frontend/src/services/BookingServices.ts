import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { z } from "zod";
import { BASE_API_URL } from "@/config/app-query-client";
import { useAccessTokenGetter, useHandleResponse } from "@/services/TokenContext";
import { ClientBooking, ClientBookingSchema, BookingDetail, BookingDetailSchema } from "@/models/Booking";

export type TabStatus = "upcoming" | "past" | "cancelled";

export function useClientBookings(status: "upcoming" | "past" | "cancelled" | "waitlist") {
  const getAccessToken = useAccessTokenGetter();
  const handleResponse = useHandleResponse();

  return useQuery({
    queryKey: ["client-bookings", status],
    queryFn: async (): Promise<ClientBooking[]> => {
      const response = await fetch(`${BASE_API_URL}/bookings/me?status=${status}`, {
        headers: {
          Accept: "application/json",
          Authorization: `Bearer ${await getAccessToken()}`,
        },
      });
      return handleResponse(response, (json) => z.array(ClientBookingSchema).parse(json));
    },
  });
}

export function useBookingDetail(id: string) {
  const getAccessToken = useAccessTokenGetter();
  const handleResponse = useHandleResponse();

  return useQuery({
    queryKey: ["booking-detail", id],
    queryFn: async (): Promise<BookingDetail> => {
      const response = await fetch(`${BASE_API_URL}/bookings/${id}`, {
        headers: {
          Accept: "application/json",
          Authorization: `Bearer ${await getAccessToken()}`,
        },
      });
      return handleResponse(response, (json) => BookingDetailSchema.parse(json));
    },
  });
}

export function useCancelBooking() {
  const getAccessToken = useAccessTokenGetter();
  const handleResponse = useHandleResponse();
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (id: string) => {
      const response = await fetch(`${BASE_API_URL}/bookings/${id}/cancel`, {
        method: "PATCH",
        headers: {
          Accept: "application/json",
          Authorization: `Bearer ${await getAccessToken()}`,
        },
      });
      return handleResponse(response, (json) => json);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["client-bookings"] });
      queryClient.invalidateQueries({ queryKey: ["booking-detail"] });
    },
  });
}


export function useRescheduleBooking() {
  const getAccessToken = useAccessTokenGetter();
  const handleResponse = useHandleResponse();
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (id: string) => {
      const response = await fetch(`${BASE_API_URL}/bookings/${id}/cancel`, {
        method: "PATCH",
        headers: {
          Accept: "application/json",
          Authorization: `Bearer ${await getAccessToken()}`,
        },
      });
      return handleResponse(response, (json) => json);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["client-bookings"] });
      queryClient.invalidateQueries({ queryKey: ["booking-detail"] });
    },
  });
}

export function useSubmitReview() {
  const getAccessToken = useAccessTokenGetter();
  const handleResponse = useHandleResponse();
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({ appointmentId, score }: { appointmentId: string; score: number }) => {
      const response = await fetch(`${BASE_API_URL}/bookings/${appointmentId}/review`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          Authorization: `Bearer ${await getAccessToken()}`,
        },
        body: JSON.stringify({ score }),
      });
      return handleResponse(response, (json) => json);
    },
    onSuccess: (_data, variables) => {
      queryClient.invalidateQueries({ queryKey: ["booking-detail", variables.appointmentId] });
      queryClient.invalidateQueries({ queryKey: ["client-bookings"] });
    },
  });
}

export function useUpdateClientPreferences() {
  const getAccessToken = useAccessTokenGetter();
  const handleResponse = useHandleResponse();

  return useMutation({
    mutationFn: async (receivesReminders: boolean) => {
      const response = await fetch(`${BASE_API_URL}/clients/me/preferences`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          Authorization: `Bearer ${await getAccessToken()}`,
        },
        body: JSON.stringify({ receives_reminders: receivesReminders }),
      });
      return handleResponse(response, (json) => json);
    },
    onSuccess: () => {
      console.log("Preferencias actualizadas en el servidor");
    },
  });
}