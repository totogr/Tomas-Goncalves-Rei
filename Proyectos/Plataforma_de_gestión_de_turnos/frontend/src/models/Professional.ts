import { z } from "zod";

import { ServiceSchema } from "./Service";

export const ProfessionalSummarySchema = z
  .object({
    id: z.number(),
    first_name: z.string(),
    last_name: z.string(),
    specialty: z.string().nullable().optional(),
    rating: z.number().nullable().optional(),
  })
  .transform((p) => ({
    id: p.id,
    firstName: p.first_name,
    lastName: p.last_name,
    specialty: p.specialty ?? null,
    rating: p.rating ?? null,
  }));

export type ProfessionalSummary = z.infer<typeof ProfessionalSummarySchema>;

const LocationSchema = z.object({
  neighborhood: z.string().nullable().optional(),
  city: z.string().nullable().optional(),
  address: z.string().nullable().optional(),
});

export const ProfessionalSchema = z
  .object({
    id: z.number(),
    first_name: z.string(),
    last_name: z.string(),
    specialty: z.string().nullable().optional(),
    location: LocationSchema.nullable().optional(),
    rating: z.number().nullable().optional(),
    review_count: z.number().int().nullable().optional(),
    services: z.array(ServiceSchema).default([]),
  })
  .transform((p) => ({
    id: p.id,
    firstName: p.first_name,
    lastName: p.last_name,
    specialty: p.specialty ?? null,
    location: p.location ?? null,
    rating: p.rating ?? null,
    reviewCount: p.review_count ?? null,
    services: p.services,
  }));

export type Professional = z.infer<typeof ProfessionalSchema>;