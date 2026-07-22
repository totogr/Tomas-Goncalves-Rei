import { z } from "zod";

export const AvailabilitySlotSchema = z.object({
  time: z.string().regex(/^\d{2}:\d{2}$/),
  available: z.boolean(),
  reason: z.string().nullable().optional(),
});
export type AvailabilitySlot = z.infer<typeof AvailabilitySlotSchema>;