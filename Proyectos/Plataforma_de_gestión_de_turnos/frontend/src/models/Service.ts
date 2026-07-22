import { z } from "zod";

export const ServiceSchema = z.object({
    id: z.coerce.string().optional(),
    name: z.string().min(1, "El nombre es obligatorio"),
    category: z.string().optional(),
    duration_minutes: z.number().min(5, "Mínimo 5 minutos"),
    price: z.number().min(0, "El precio no puede ser negativo"),
    max_capacity: z.number().min(1, "Mínimo 1 persona"),
    active: z.boolean(),
});

export const ServicesResponseSchema = z.object({
    services: z.array(ServiceSchema),
});

export type Service = z.infer<typeof ServiceSchema>;
export type ServicesResponse = z.infer<typeof ServicesResponseSchema>;

export const DEFAULT_SERVICE = (): Service => ({
    id: undefined,
    name: "",
    category: "",
    duration_minutes: 30,
    price: 0,
    max_capacity: 1,
    active: true,
});
