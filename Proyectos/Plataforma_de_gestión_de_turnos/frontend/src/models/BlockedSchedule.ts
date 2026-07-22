import { z } from "zod";

export const BlockedScheduleSchema = z.object({
    id: z.coerce.number().optional(),
    blockDate: z.string().regex(/^\d{4}-\d{2}-\d{2}$/, "Formato inválido"),
    startTime: z.string().regex(/^\d{2}:\d{2}$/, "Formato inválido"),
    endTime: z.string().regex(/^\d{2}:\d{2}$/, "Formato inválido"),
});

export const BlockedScheduleListSchema = z.array(BlockedScheduleSchema);

export const BlockedScheduleCreateResultSchema = z.object({
    block: BlockedScheduleSchema,
    cancelledAppointments: z.number().int().min(0),
});

export type BlockedSchedule = z.infer<typeof BlockedScheduleSchema>;
export type BlockedScheduleCreateResult = z.infer<typeof BlockedScheduleCreateResultSchema>;
