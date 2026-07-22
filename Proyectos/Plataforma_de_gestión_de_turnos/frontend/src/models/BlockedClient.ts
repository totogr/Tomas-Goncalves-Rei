import { z } from "zod";

export const BlockedClientSchema = z.object({
  id: z.number(),
  professionalId: z.number(),
  clientId: z.number(),
  blockedAt: z.string(),
});

export type BlockedClient = z.infer<typeof BlockedClientSchema>;

export const ClientInfoSchema = z.object({
  clientId: z.number(),
  clientName: z.string(),
  clientEmail: z.string(),
});

export type ClientInfo = z.infer<typeof ClientInfoSchema>;