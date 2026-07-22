import { z } from "zod";

export const ClientBookingSchema = z.object({
  id: z.union([z.string(), z.number()]).transform(String),
  status: z.string().transform((s) => s.toLowerCase()),
  cancelled_by: z.string().nullable().optional(),
  service_name: z.string(),
  date: z.string(),
  time: z.string(),
  duration_minutes: z.number(),
  professional_name: z.string(),
  location: z.string().optional().nullable(),
});

export type ClientBooking = z.infer<typeof ClientBookingSchema>;

export const BookingDetailSchema = z.object({
  id: z.union([z.string(), z.number()]).transform(String),
  status: z.string().transform((s) => s.toLowerCase()),
  cancelled_by: z.string().nullable().optional(),
  service: z.object({
    id: z.union([z.string(), z.number()]).transform(String),
    name: z.string(),
    duration_minutes: z.number(),
    price: z.number(),
  }),
  professional: z.object({
    id: z.union([z.string(), z.number()]).transform(String),
    first_name: z.string(),
    last_name: z.string().optional(),
  }),
  date: z.string(),
  start: z.string(),
  end: z.string(),
  score: z.number().int().min(1).max(5).nullable().optional(),
});

export type BookingDetail = z.infer<typeof BookingDetailSchema>;