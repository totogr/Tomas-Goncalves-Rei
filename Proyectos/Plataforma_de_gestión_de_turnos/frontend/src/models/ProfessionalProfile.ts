import { z } from "zod";

export const ProfessionalProfileFormSchema = z.object({
    specialty: z.string().min(1, "La especialidad es obligatoria"),
    address: z.string().min(1, "La dirección es obligatoria"),
    neighborhood: z.string().min(1, "El barrio es obligatorio"),
    city: z.string().min(1, "La ciudad es obligatoria"),
});

export type ProfessionalProfileForm = z.infer<typeof ProfessionalProfileFormSchema>;