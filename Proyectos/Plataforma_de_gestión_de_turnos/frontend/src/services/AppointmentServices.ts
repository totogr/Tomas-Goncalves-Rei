import { useMutation, useQueryClient } from "@tanstack/react-query";

import { BASE_API_URL } from "@/config/app-query-client";
import { Appointment, AppointmentSchema, CreateAppointmentRequest } from "@/models/Appointment";

import { useAccessTokenGetter, useHandleResponse } from "./TokenContext";

export function useCreateAppointment() {
  const getAccessToken = useAccessTokenGetter();
  const handleResponse = useHandleResponse();
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (data: CreateAppointmentRequest): Promise<Appointment> => {
      const response = await fetch(`${BASE_API_URL}/appointments`, {
        method: "POST",
        headers: {
          Accept: "application/json",
          Authorization: `Bearer ${await getAccessToken()}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          professional_id: data.professionalId,
          service_id: Number(data.serviceId),
          date: data.date,
          time: data.time,
        }),
      });

      return handleResponse(response, (json) => AppointmentSchema.parse(json));
    },
    onSuccess: (_appointment, variables) => {
      queryClient.invalidateQueries({
        queryKey: ["availability", variables.professionalId, variables.serviceId, variables.date],
      });
    },
  });
}
