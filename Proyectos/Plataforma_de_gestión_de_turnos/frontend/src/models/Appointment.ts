import { z } from "zod";

export const AppointmentStatusSchema = z.enum(["CONFIRMED", "CANCELLED", "COMPLETED"]);
export type AppointmentStatus = z.infer<typeof AppointmentStatusSchema>;

export const AppointmentSchema = z
  .object({
    id: z.number(),
    status: AppointmentStatusSchema,
    start: z.string(),
    end: z.string(),
  })
  .transform((a) => ({
    id: a.id,
    status: a.status,
    start: a.start,
    end: a.end,
  }));

export type Appointment = z.infer<typeof AppointmentSchema>;

export type CreateAppointmentRequest = {
  professionalId: number;
  serviceId: string;
  date: string;
  time: string;
};
