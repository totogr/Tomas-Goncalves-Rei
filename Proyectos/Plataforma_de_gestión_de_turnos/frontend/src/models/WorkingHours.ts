import { z } from "zod";

export const TimeRangeSchema = z.object({
    id: z.string().optional(),
    from: z.string().regex(/^\d{2}:\d{2}$/, "Formato inválido"),
    to: z.string().regex(/^\d{2}:\d{2}$/, "Formato inválido"),
}).refine((data) => data.from < data.to, {
    message: "El horario de cierre debe ser mayor al de apertura",
    path: ["to"],
});

export const WorkingHoursDaySchema = z.object({
    day: z.enum(["monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"]),
    enabled: z.boolean(),
    ranges: z.array(TimeRangeSchema),
});

export const WorkingHoursSchema = z.object({
    schedule: z.array(WorkingHoursDaySchema),
    slotIntervalMinutes: z.number().int().min(5).default(30),
});

export type TimeRange = z.infer<typeof TimeRangeSchema>;
export type WorkingHoursDay = z.infer<typeof WorkingHoursDaySchema>;
export type WorkingHours = z.infer<typeof WorkingHoursSchema>;

export const DAY_LABELS: Record<WorkingHoursDay["day"], string> = {
    monday: "Lunes",
    tuesday: "Martes",
    wednesday: "Miércoles",
    thursday: "Jueves",
    friday: "Viernes",
    saturday: "Sábado",
    sunday: "Domingo",
};

export const DEFAULT_WORKING_HOURS: WorkingHours = {
    slotIntervalMinutes: 30,
    schedule: [
        { day: "monday",    enabled: true,  ranges: [{ from: "09:00", to: "18:00" }] },
        { day: "tuesday",   enabled: true,  ranges: [{ from: "09:00", to: "18:00" }] },
        { day: "wednesday", enabled: true,  ranges: [{ from: "09:00", to: "18:00" }] },
        { day: "thursday",  enabled: true,  ranges: [{ from: "09:00", to: "18:00" }] },
        { day: "friday",    enabled: true,  ranges: [{ from: "09:00", to: "18:00" }] },
        { day: "saturday",  enabled: false, ranges: [] },
        { day: "sunday",    enabled: false, ranges: [] },
    ],
};
