import { z } from "zod";
export const ClientSignupFormSchema = z
    .object({
        email: z.string().email("Email inválido"),
        password: z.string().min(8, "Debe tener al menos 8 caracteres"),
        confirmPassword: z.string().min(1, "Confirmá tu contraseña"),
        firstName: z.string().min(1, "El nombre no puede estar vacío"),
        lastName: z.string().min(1, "El apellido no puede estar vacío"),
    })
    .refine((data) => data.password === data.confirmPassword, {
        message: "Las contraseñas no coinciden",
        path: ["confirmPassword"],
    });

export type ClientSignupFormValues = z.infer<typeof ClientSignupFormSchema>;

export const ClientSignupRequestSchema = z.object({
    email: z.string().email(),
    password: z.string(),
    firstName: z.string(),
    lastName: z.string(),
});
export type ClientSignupRequest = z.infer<typeof ClientSignupRequestSchema>;

export const ProfessionalSignupFormSchema = z
    .object({
        email: z.string().email("Email inválido"),
        password: z.string().min(8, "Debe tener al menos 8 caracteres"),
        confirmPassword: z.string().min(1, "Confirmá tu contraseña"),
        firstName: z.string().min(1, "El nombre no puede estar vacío"),
        lastName: z.string().min(1, "El apellido no puede estar vacío"),
    })
    .refine((data) => data.password === data.confirmPassword, {
        message: "Las contraseñas no coinciden",
        path: ["confirmPassword"],
    });

export type ProfessionalSignupFormValues = z.infer<typeof ProfessionalSignupFormSchema>;

export const ProfessionalSignupRequestSchema = z.object({
    email: z.string().email(),
    password: z.string(),
    firstName: z.string(),
    lastName: z.string(),
});
export type ProfessionalSignupRequest = z.infer<typeof ProfessionalSignupRequestSchema>;


export const LoginRequestSchema = z.object({
    email: z.string().email("Email inválido"),
    password: z.string().min(1, "La contraseña no puede estar vacía"),
});
export type LoginRequest = z.infer<typeof LoginRequestSchema>;

export const AuthResponseSchema = z.object({
    accessToken: z.string().min(1),
    refreshToken: z.string().min(1),
    role: z.enum(["CLIENT", "PROFESSIONAL"]),
    id: z.number(),
    firstName: z.string(),
    lastName: z.string(),
});
export type AuthResponse = z.infer<typeof AuthResponseSchema>;