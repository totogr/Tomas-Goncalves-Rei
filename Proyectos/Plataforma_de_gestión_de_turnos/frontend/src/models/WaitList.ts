import { z } from "zod";

export const WaitListEntrySchema = z.object({
        id: z.union([z.string(), z.number()]).transform(Number),
        professional_id: z.number(),
        professional_name: z.string().optional(), // <- Lo agregás acá para recibirlo
        service_id: z.number(),
        service_name: z.string().optional(),      // <- Lo agregás acá para recibirlo
        slot_start: z.string(),
        creation_time: z.string().optional(),
        position: z.number(),
}).transform(d => ({
        id: d.id,
        professionalId: d.professional_id,
        professionalName: d.professional_name || `Profesional #${d.professional_id}`, // Fallback por las dudas
        serviceId: d.service_id,
        serviceName: d.service_name || `Servicio #${d.service_id}`,
        slotStart: d.slot_start,
        creationTime: d.creation_time,
        position: d.position,
}));

export type WaitListEntry = z.infer<typeof WaitListEntrySchema>;