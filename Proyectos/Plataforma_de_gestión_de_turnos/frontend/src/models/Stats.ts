import { z } from "zod";

export const DayStatSchema = z.object({
  day: z.string(),
  count: z.number(),
});

export const ServiceStatSchema = z.object({
  name: z.string(),
  count: z.number(),
  percentage: z.number(),
});

export const ClientStatSchema = z.object({
  name: z.string(),
  visits: z.number(),
  cancellations: z.number(),
  status: z.enum(["frequent", "occasional"]),
});

export const StatsSchema = z.object({
  total_appointments: z.number(),
  cancellation_rate: z.number(),
  average_rating: z.number().nullable(),
  estimated_revenue: z.number(),
  appointments_by_day: z.array(DayStatSchema),
  top_services: z.array(ServiceStatSchema),
  frequent_clients: z.array(ClientStatSchema),
});

export type Stats = z.infer<typeof StatsSchema>;
export type DayStat = z.infer<typeof DayStatSchema>;
export type ServiceStat = z.infer<typeof ServiceStatSchema>;
export type ClientStat = z.infer<typeof ClientStatSchema>;

export type StatsPeriod = "7d" | "30d" | "3m";

export const PERIOD_LABELS: Record<StatsPeriod, string> = {
  "7d":  "Últimos 7 días",
  "30d": "Últimos 30 días",
  "3m":  "Últimos 3 meses",
};